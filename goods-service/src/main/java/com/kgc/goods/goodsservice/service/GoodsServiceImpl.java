package com.kgc.goods.goodsservice.service;

import com.alibaba.fastjson.JSON;
import com.kgc.goods.bean.Goods;
import com.kgc.goods.config.RedisConfig;
import com.kgc.goods.goodsservice.mapper.GoodsMapper;
import com.kgc.goods.service.GoodsService;
import com.kgc.goods.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    JestClient jestClient;

    @Resource
    RedisConfig redisConfig;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedissonClient redissonClient;
    @Override
    public List<Goods> queryGoodsBydistrict(Integer district) {
        List<Goods> goodslist=new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if(district!=null&&district!=0) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("district", district);
            boolQueryBuilder.must(matchQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("id", SortOrder.DESC);
        }
        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("goods").addType("goodsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Goods,Void>> hits=searchResult.getHits(Goods.class);
            for (SearchResult.Hit<Goods,Void> hit: hits){
                goodslist.add(hit.source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return goodslist;
    }

    @Override
    public Goods queryGoodsByGoods(Integer id) {
        String goodskey="goods:"+id+":info";

        Jedis jedis=redisUtil.getJedis();

        String GoodsInfoJSOn=jedis.get(goodskey);

        Goods goods=null;

        if(GoodsInfoJSOn!=null){//redis有缓存
            if(GoodsInfoJSOn.equals("empty")){
                return null;
            }
            goods= JSON.parseObject(jedis.get(goodskey),Goods.class);
        }else{//无缓存

            Lock lock = redissonClient.getLock("lock");// 声明锁
            lock.lock();//上锁
            //查询sku
            goods = goodsMapper.selectByPrimaryKey(id);

            if(goods!=null){
                //随机时间，防止缓存雪崩
                Random random=new Random();
                int i = random.nextInt(10);
                jedis.setex(goodskey,i*60*10,JSON.toJSONString(goods));
            }else{
                //空数据，存储30S，防止缓存穿透
                jedis.setex(goodskey,5*6*1,"empty");
            }
            // 删除分布式锁
            lock.unlock();
        }
        jedis.close();
        return goods;
    }
    @Resource
    EsService esService;
    @Override
    public int UpdateGoods(Goods goods) {
        int result=goodsMapper.updateByPrimaryKeySelective(goods);
        if(result>0){
            //es
            this.setEs();
            //redis
            String goodskey="goods:"+goods.getId()+":info";
            Jedis jedis=redisUtil.getJedis();
            jedis.del(goodskey);
            jedis.setex(goodskey,60*10, JSON.toJSONString(goods));
        }
        return result;
    }

    public  List<Goods> getEs(){
        List<Goods> Goodslist=new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("goods").addType("goodsinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Goods,Void>> hits=searchResult.getHits(Goods.class);
            for (SearchResult.Hit<Goods,Void> hit: hits){
                Goods GoodsInfo=hit.source;
                Goodslist.add(GoodsInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Goodslist;
    }

    public void setEs(){
        List<Goods> goodsList = goodsMapper.selectByExample(null);
        System.out.println("goodsList:"+goodsList);
        List<Goods> assetsInfos=new ArrayList<>();
        for (Goods goods : goodsList) {
            Goods good = new Goods();
            BeanUtils.copyProperties(goods,good);
            assetsInfos.add(good);
        }
        System.out.println(assetsInfos);
        for (Goods goods : assetsInfos) {
            Index index=new Index.Builder(goods).index("goods").type("goodsinfo").id(goods.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
