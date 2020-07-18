package com.caffeine.demo.service.impl;

import com.caffeine.demo.service.GoodsService;
import com.caffeine.demo.service.StatService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentMap;

@Profile("cacheable")
@Service
public class StatServiceImpl implements StatService {
    @Resource
    private CacheManager cacheManager;

    //得到商品列表这个cache的统计    
    @Override
    public String goodslistStats() {
        CaffeineCache caffeine = (CaffeineCache)cacheManager.getCache("goodslist");
        Cache goods = caffeine.getNativeCache();
        String statsInfo="cache名字:goodslist<br/>";
        Long size = goods.estimatedSize();
        statsInfo += "size:"+size+"<br/>";
        ConcurrentMap map= goods.asMap();
        statsInfo += "map keys:<br/>";
        for(Object key : map.keySet()) {
            statsInfo += "key:"+key.toString()+";value:"+map.get(key)+"<br/>";
        }
        statsInfo += "统计信息:"+goods.stats().toString();
        return statsInfo;
    }
}
