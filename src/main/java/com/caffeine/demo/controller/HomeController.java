package com.caffeine.demo.controller;

import com.caffeine.demo.service.GoodsService;
import com.caffeine.demo.pojo.Goods;
import com.github.benmanes.caffeine.cache.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private CacheManager cacheManager;

    //商品详情 参数:商品id
    @Cacheable(value = "goods", key="#goodsId",sync = true)
    @GetMapping("/goodsone")
    @ResponseBody
    public Goods goodsInfo(@RequestParam(value="goodsid",required = true,defaultValue = "0") Long goodsId) {
        Goods goods = goodsService.getOneGoodsById(goodsId);
        return goods;
    }


    //商品列表 参数:第几页
    @GetMapping("/goodslist")
    public String goodsList(Model model,
                            @RequestParam(value="p",required = false,defaultValue = "1") int currentPage) {

        Map<String,Object> res = goodsService.getAllGoodsByPage(currentPage);
        model.addAttribute("pageInfo", res.get("pageInfo"));
        model.addAttribute("goodslist", res.get("goodslist"));
        return "goods/goodslist";
    }

    //更新
    @ResponseBody
    @GetMapping("/goodslistput")
    public String goodsListPut(@RequestParam(value="p",required = false,defaultValue = "1") int currentPage) {
        Map<String,Object> res = goodsService.putAllGoodsByPage(currentPage);
        return "cache put succ";
    }

    //清除
    @CacheEvict(value="goodslist", allEntries=true)
    @ResponseBody
    @GetMapping("/goodslistevict")
    public String goodsListEvict() {
        return "cache evict succ";
    }

    //统计，如果是生产环境，需要加密才允许访问
    @GetMapping("/stats")
    @ResponseBody
    public Object stats() {

        CaffeineCache caffeine = (CaffeineCache)cacheManager.getCache("goodslist");
        Cache goods = caffeine.getNativeCache();
        String statsInfo="cache名字:goods<br/>";
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

