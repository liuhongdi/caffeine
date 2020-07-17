package com.caffeine.demo.service.impl;

import com.caffeine.demo.mapper.GoodsMapper;
import com.caffeine.demo.pojo.Goods;
import com.caffeine.demo.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    //得到一件商品的信息
    @Override
    public Goods getOneGoodsById(Long goodsId) {
        System.out.println("query database");
        Goods goodsOne = goodsMapper.selectOneGoods(goodsId);
        return goodsOne;
    }

    //获取商品列表，只更新缓存
    @CachePut(value = "goodslist", key="#currentPage")
    @Override
    public Map<String,Object> putAllGoodsByPage(int currentPage) {
        Map<String,Object> res = getAllGoodsByPageDdata(currentPage);
        return res;
    }

    //获取商品列表，加缓存
    //@Cacheable(key = "#page+'-'+#pageSize") 多个参数可以用字符串连接起来
    @Cacheable(value = "goodslist", key="#currentPage",sync = true)
    @Override
    public Map<String,Object> getAllGoodsByPage(int currentPage) {

        Map<String,Object> res = getAllGoodsByPageDdata(currentPage);
        return res;
    }

    //从数据库获取商品列表
    public Map<String,Object> getAllGoodsByPageDdata(int currentPage) {
        Map<String,Object> res = new HashMap<String,Object>();
        PageHelper.startPage(currentPage, 5);
        List<Goods> goodsList = goodsMapper.selectAllGoods();
        res.put("goodslist",goodsList);
        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);
        res.put("pageInfo",pageInfo);
        return res;
    }

}
