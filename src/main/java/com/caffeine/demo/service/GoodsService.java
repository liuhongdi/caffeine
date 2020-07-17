package com.caffeine.demo.service;

import com.caffeine.demo.pojo.Goods;
import java.util.Map;

public interface GoodsService {
    public Goods getOneGoodsById(Long goodsId);
    public Map<String,Object> getAllGoodsByPage(int currentPage);
    public Map<String,Object> putAllGoodsByPage(int currentPage);
}