package com.example.scott.bonus.sqlite.entity;

/**
 * Created by Scott on 15/4/26.
 */
public class InvoiceItem {
    private String storeName;
    private String deadline;
    private String invoiceNum;
    private String currentTime;
    private String storeNum;
    private String storePhone;
    private String goodsQuantity;
    private String totalMoney;
    private String payDetail;
    private String payBack;
    private String signature;
    private String goodsHash;
    private int isExchanged = 0;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(String payDetail) {
        this.payDetail = payDetail;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getIsExchanged() {
        return isExchanged;
    }

    public void setIsExchanged(int isExchanged) {
        this.isExchanged = isExchanged;
    }

    public String getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(String goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public String getPayBack() {
        return payBack;
    }

    public void setPayBack(String payBack) {
        this.payBack = payBack;
    }

    public String getGoodsHash() {
        return goodsHash;
    }

    public void setGoodsHash(String goodsHash) {
        this.goodsHash = goodsHash;
    }

    public InvoiceItem()
    {

    }

    public InvoiceItem(String storeName, String deadline, String invoiceNum,
                       String currentTime, String storeNum, String storePhone,
                       String goodsQuantity, String totalMoney, String payDetail,
                       String payBack, String signature, String goodsHash, int isExchanged)
    {
        this.storeName = storeName;
        this.deadline = deadline;
        this.invoiceNum = invoiceNum;
        this.currentTime = currentTime;
        this.storeNum = storeNum;
        this.storePhone = storePhone;
        this.goodsQuantity = goodsQuantity;
        this.totalMoney = totalMoney;
        this.payDetail = payDetail;
        this.payBack = payBack;
        this.signature = signature;
        this.goodsHash = goodsHash;
        this.isExchanged = isExchanged;
    }
}
