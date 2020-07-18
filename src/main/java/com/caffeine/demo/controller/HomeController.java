package com.caffeine.demo.controller;

import com.caffeine.demo.service.GoodsService;
import com.caffeine.demo.pojo.Goods;
import com.caffeine.demo.service.StatService;
import com.caffeine.demo.service.impl.StatServiceImpl;
import com.github.benmanes.caffeine.cache.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Profile;
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

    //商品详情 参数:商品id
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


}

