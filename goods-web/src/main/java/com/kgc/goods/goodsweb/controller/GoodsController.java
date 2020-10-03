package com.kgc.goods.goodsweb.controller;

import com.kgc.goods.bean.Goods;
import com.kgc.goods.service.GoodsService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {
    @Reference
    GoodsService goodsService;

    @GetMapping("/goods/list")
    @ResponseBody
    public List<Goods> goodsList(@RequestParam(value = "district",required = false,defaultValue = "0") Integer district){
        List<Goods> goodsList=goodsService.queryGoodsBydistrict(district);
        return goodsList;
    }

    @PostMapping("/goods/by/id")
    @ResponseBody
    public Goods queryByid(Integer id){
        Goods goods = goodsService.queryGoodsByGoods(id);
        return goods;
    }

    @PostMapping("/save")
    @ResponseBody
    public int save(@RequestBody Goods goods){
        int i=goodsService.UpdateGoods(goods);
        return i;
    }
}
