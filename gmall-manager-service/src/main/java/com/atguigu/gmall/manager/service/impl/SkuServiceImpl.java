package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.SkuService;
import com.atguigu.gmall.manager.constant.RedisCacheKeyConst;
import com.atguigu.gmall.manager.es.SkuBaseAttrEsVo;
import com.atguigu.gmall.manager.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuAttrValueMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuImageMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuInfoMapper;
import com.atguigu.gmall.manager.mapper.sku.SkuSaleAttrValueMapper;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttrMapper;
import com.atguigu.gmall.manager.sku.*;
import com.atguigu.gmall.manager.spu.SpuSaleAttr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author fanrongxiang
 * @email fanxiaoxiang3988@126.com
 * @date 2019/7/24 0024
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id) {
        return baseAttrInfoMapper.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrBySpuId(spuId);
    }

    @Transactional
    @Override
    public void saveBigSkuInfo(SkuInfo skuInfo) {
        //保存skuInfo的基本信息
        skuInfoMapper.insert(skuInfo);
        Integer id = skuInfo.getId();
        //保存skuImages
        List<SkuImage> skuImages = skuInfo.getSkuImages();
        for (SkuImage skuImage : skuImages) {
            skuImage.setSkuId(id);
            skuImageMapper.insert(skuImage);
        }
        //保存skuAttrValues
        List<SkuAttrValue> skuAttrValues = skuInfo.getSkuAttrValues();
        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueMapper.insert(skuAttrValue);
        }
        //保存skuSaleAttrValues
        List<SkuSaleAttrValue> skuSaleAttrValues = skuInfo.getSkuSaleAttrValues();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValues) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValueMapper.insert(skuSaleAttrValue);
        }
    }

    @Override
    public List<SkuInfo> getSkuInfoBySpuId(Integer spuId) {
        QueryWrapper<SkuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        return skuInfoMapper.selectList(wrapper);
    }

    @Transactional
    @Override
    public SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException {
        Jedis jedis = jedisPool.getResource();
        String key = RedisCacheKeyConst.SKU_INFO_PREFIX + skuId + RedisCacheKeyConst.SKU_INFO_SUFFIX;
        SkuInfo result = null;
        //先去redis中查询，看是否有，有的话就直接返回，没有则去MySQL查询返回，并加入redis中
        String s = jedis.get(key);
        if(s != null) {
            log.debug("缓存中找到数据了skuId为：{}的商品信息",skuId);
            //转换成我们需要的格式
            result = JSON.parseObject(s, SkuInfo.class);
            jedis.close();
            return result;
        }
        if("null".equals(s)) {
            //防止缓存穿透的
            //缓存中存了，只不过这是你数据库给我的
            //之前数据库查过，但是没有，所以给缓存中放了一个null串
            return null;
        }
        if(s == null){
            //缓存中没有，需要加锁
            //我们需要避免一种情况，就是我们再锁规定的RedisCacheKeyConst.LOCK_TIMEOUT这个超时时间结束的时候，业务逻辑还没有删除所
            //那么当我们业务逻辑执行完以后，就很有可能会删除掉别人新创建的锁，所以可以给锁的值设置为一个随机字符串，删除时判定一下，是否是自己当时加的那个锁
            String token = UUID.randomUUID().toString();
            String lock = jedis.set(RedisCacheKeyConst.LOCK_SKU_INFO+skuId, token, "NX", "EX", RedisCacheKeyConst.LOCK_TIMEOUT);
            if(lock == null) {
                //没有拿到锁
                log.debug("没有拿到锁，等待重试。。。");
                Thread.sleep(1000);//等待一秒重试
                //自旋锁
                getSkuInfoBySkuId(skuId);
            } else if("OK".equals(lock)) {
                //拿到锁了
                result = getFromDb(skuId);
                //将查询的数据转为json字符串放入redis中
                String skuInfoJson = JSON.toJSONString(result);
                //插入时，设置过期时间为一天，一天以后如果数据有更新，则会更新redis数据库
                //jedis.set(key, skuInfoJson);
                if("null".equals(skuInfoJson)) {
                    //空数据存放时间短
                    jedis.setex(key,RedisCacheKeyConst.SKU_INFO_NULL_TIMEOUT,skuInfoJson);
                } else {
                    //正常数据存放1天
                    jedis.setex(key,RedisCacheKeyConst.SKU_INFO_TIMEOUT,skuInfoJson);
                }
                //释放锁
                //....释放锁；解锁有问题吗？删锁的错误姿势 这是一种错误的解锁方式，极端情况：判断玩是否相等，锁过期了，然后删除的时候删除了别人的锁
//                String redisToken = jedis.get(RedisCacheKeyConst.LOCK_SKU_INFO);
//                if(token.equals(redisToken)){
//                    jedis.del(RedisCacheKeyConst.LOCK_SKU_INFO);
//                }else{
//                    //业务逻辑已经超出锁的时间了，别人已经持有锁了，我们不要把别人锁删了
//                }
                //脚本；正确的解锁；一定要是原子操作
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script,
                        Collections.singletonList(RedisCacheKeyConst.LOCK_SKU_INFO+skuId),
                        Collections.singletonList(token));
            }
        }
        jedis.close();
        return result;

    }

    @Override
    public List<SkuAttrValueMappingTo> getSkuAttrValueMapping(Integer spuId) {
        return skuInfoMapper.getSkuAttrValueMapping(spuId);
    }

    @Override
    public List<SkuBaseAttrEsVo> getSkuBaseAttrValueIds(Integer skuId) {
        QueryWrapper<SkuAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuId);
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.selectList(wrapper);
        List<SkuBaseAttrEsVo> list = new ArrayList<>();
        if(skuAttrValues!= null && skuAttrValues.size()>0) {
            for (SkuAttrValue skuAttrValue : skuAttrValues) {
                SkuBaseAttrEsVo skuBaseAttrEsVo = new SkuBaseAttrEsVo();
                skuBaseAttrEsVo.setValueId(skuAttrValue.getValueId());
                list.add(skuBaseAttrEsVo);
            }
        }
        return list;
    }

    private SkuInfo getFromDb(Integer skuId){
        log.debug("缓存中没找到，准备从数据库中查询并插入skuId是{}的商品信息", skuId);
        //查出这个sku的所有基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //即使没有查到数据，也返回，避免空指针异常以及缓存穿透
        if(skuInfo == null) {
            return null;
        }
        //查出这个sku的所有图片信息
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuInfo.getId());
        List<SkuImage> skuImages = skuImageMapper.selectList(wrapper);
        skuInfo.setSkuImages(skuImages);
        //查出sku所有的销售属性及其对应的所有值封装进属性：private List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos
        List<SkuAllSaveAttrAndValueTo> skuAllSaveAttrAndValueTos = skuImageMapper.getSkuAllSaveAttrAndValue(skuInfo.getId(),skuInfo.getSpuId());
        skuInfo.setSkuAllSaveAttrAndValueTos(skuAllSaveAttrAndValueTos);

        return skuInfo;
    }

}
