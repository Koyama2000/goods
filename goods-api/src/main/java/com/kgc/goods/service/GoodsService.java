package com.kgc.goods.service;

import com.kgc.goods.bean.Goods;

import java.util.List;

public interface GoodsService {
    //根据区域编号查询商品订单
    public List<Goods> queryGoodsBydistrict(Integer district);
    //根据订单编号查询
    public Goods queryGoodsByGoods(Integer id);
    //修改
    public int UpdateGoods(Goods goods);
}
