package com.kgc.goods.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class Goods implements Serializable {
    private Integer id;

    private String goodsname;

    private Integer status;

    private Integer district;

    private Integer counts;

    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname == null ? null : goodsname.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", goodsname='" + goodsname + '\'' +
                ", status=" + status +
                ", district=" + district +
                ", counts=" + counts +
                ", price=" + price +
                '}';
    }
}