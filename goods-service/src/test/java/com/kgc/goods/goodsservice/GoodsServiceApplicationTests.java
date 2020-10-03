package com.kgc.goods.goodsservice;

import com.kgc.goods.bean.Goods;
import com.kgc.goods.goodsservice.mapper.GoodsMapper;
import com.kgc.goods.service.GoodsService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GoodsServiceApplicationTests {
    @Reference
    GoodsService goodsService;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    JestClient jestClient;
    @Test
    void contextLoads() {
        List<Goods> goodsList = goodsMapper.selectByExample(null);
        System.out.println("goodsList:"+goodsList);
        List<Goods> goodsInfos=new ArrayList<>();
        for (Goods goods : goodsList) {
            Goods good = new Goods();
            BeanUtils.copyProperties(goods,good);
            goodsInfos.add(good);
        }
        System.out.println(goodsInfos);
        for (Goods goods : goodsInfos) {
            Index index=new Index.Builder(goods).index("goods").type("goodsinfo").id(goods.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
