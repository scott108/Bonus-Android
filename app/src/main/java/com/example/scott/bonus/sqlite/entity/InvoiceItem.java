package com.example.scott.bonus.sqlite.entity;

/**
 * Created by Scott on 15/4/26.
 */
public class InvoiceItem {
    private String storeName;
    private String dateline;
    private String invoiceNum;
    private String currentTime;
    private String storeNum;
    private String storePhone;
    private String totalMoney;
    private String payDetail;
    private String signature;
    private int isExchanged = 0;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
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

    public InvoiceItem()
    {

    }

    public InvoiceItem(String storeName, String dateline, String invoiceNum,
                       String currentTime, String storeNum, String storePhone,
                       String totalMoney, String payDetail, String signature, int isExchanged)
    {
        this.storeName = storeName;
        this.dateline = dateline;
        this.invoiceNum = invoiceNum;
        this.currentTime = currentTime;
        this.storeNum = storeNum;
        this.storePhone = storePhone;
        this.totalMoney = totalMoney;
        this.payDetail = payDetail;
        this.signature = signature;
        this.isExchanged = isExchanged;
    }
}
