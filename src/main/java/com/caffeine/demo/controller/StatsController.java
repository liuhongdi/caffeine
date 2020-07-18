package com.caffeine.demo.controller;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentMap;

@Profile("cacheable")
@Controller
@RequestMapping("/stats")
public class StatsController {
    //统计，如果是生产环境，需要加密才允许访问
    @Resource
    private CacheManager cacheManager;

    @GetMapping("/stats")
    @ResponseBody
    public String stats() {
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
