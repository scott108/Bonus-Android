package com.example.scott.bonus.sqlite.entity;

/**
 * Created by Scott on 15/4/27.
 */
public class InvoiceGoodsItem {
    private String invoiceNum;
    private String goodsName;
    private String goodsPrice;
    private String goodsQuantity;
    private String goodsTotalPrice;

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(String goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public String getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(String goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public InvoiceGoodsItem() {

    }

    public InvoiceGoodsItem(String invoiceNum, String goodsName, String goodsPrice, String goodsQuantity, String goodsTotalPrice) {
        this.invoiceNum = invoiceNum;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsQuantity = goodsQuantity;
        this.goodsTotalPrice = goodsTotalPrice;
    }
}
