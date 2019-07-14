package com.park61.moduel.toyshare.bean;

import java.io.Serializable;

/**
 * Created by shubei on 2017/6/20.
 */

public class TSApplyOrderBean implements Serializable{


    /**
     * addressId : 1
     * applyReturnTime : 1497965068245
     * applyTime : 1497963613000
     * boxId : 11
     * createDate : 1497963629000
     * curPage : 1
     * end : 5
     * id : 68
     * lostPrice : 1
     * manageAmount : 1
     * nextPage : 0
     * pageSize : 5
     * pageTotal : 0
     * payAmount : 0
     * previousPage : 1
     * sharePrice : -1
     * start : 0
     * status : 1
     * statusName : 待领取
     * total : 0
     * toyShareBoxSeriesDTO : {"boxNum":1,"code":"00032","content":"","curPage":1,"end":5,"fitNum":1,"id":32,"introductionImg":"http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170619/1497854331780079995.png","introductionVideo":"16220de2982b4285a5fad47f1a06260c","jdLink":"https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.gTsrnb&amp;id=20672995846&amp;skuId=3156284104418&amp;user_id=918912816&amp;cat_id=2&amp;is_b=1&amp;rn=c148bbd9331a7c6f42c5e5696d555a69","jdPrice":"1.00","marketPrice":"1.00","name":"1212","nextPage":0,"pageSize":5,"pageTotal":0,"previousPage":1,"releaseNum":1,"sharePrice":1,"start":0,"status":-1,"summary":"12121","tmallLink":"https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.gTsrnb&amp;id=20672995846&amp;skuId=3156284104418&amp;user_id=918912816&amp;cat_id=2&amp;is_b=1&amp;rn=c148bbd9331a7c6f42c5e5696d555a69","tmallPrice":"1.00","total":0,"userParticipateNum":0,"userViewNum":0}
     * toyShareReceiveAddressDTO : {"address":"金融港A2栋1403","cityId":2,"cityName":"武汉","countyId":2,"countyName":"江夏","curPage":1,"end":5,"id":1,"nextPage":0,"pageSize":5,"pageTotal":0,"phone":"13434345676","previousPage":1,"provinceId":1,"provinceName":"湖北","start":0,"total":0,"townId":3,"townName":"金融港"}
     * useTimePrice : 1
     * useTimeString : 0天0小时24分
     * userId : 11
     */

    private int addressId;
    private long applyReturnTime;
    private long applyTime;
    private int boxId;
    private long createDate;
    private int curPage;
    private int end;
    private int id;
    private int lostPrice;
    private int manageAmount;
    private int nextPage;
    private int pageSize;
    private int pageTotal;
    private int payAmount;
    private int previousPage;
    private int sharePrice;
    private int start;
    private String status;
    private String statusName;
    private int total;
    private JoyShareItem toyShareBoxSeriesDTO;
    private TSAddrBean toyShareReceiveAddressDTO;
    private int useTimePrice;
    private String useTimeString;
    private int userId;
    private long orderId;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public long getApplyReturnTime() {
        return applyReturnTime;
    }

    public void setApplyReturnTime(long applyReturnTime) {
        this.applyReturnTime = applyReturnTime;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLostPrice() {
        return lostPrice;
    }

    public void setLostPrice(int lostPrice) {
        this.lostPrice = lostPrice;
    }

    public int getManageAmount() {
        return manageAmount;
    }

    public void setManageAmount(int manageAmount) {
        this.manageAmount = manageAmount;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public int getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(int sharePrice) {
        this.sharePrice = sharePrice;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUseTimePrice() {
        return useTimePrice;
    }

    public void setUseTimePrice(int useTimePrice) {
        this.useTimePrice = useTimePrice;
    }

    public String getUseTimeString() {
        return useTimeString;
    }

    public void setUseTimeString(String useTimeString) {
        this.useTimeString = useTimeString;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public JoyShareItem getToyShareBoxSeriesDTO() {
        return toyShareBoxSeriesDTO;
    }

    public void setToyShareBoxSeriesDTO(JoyShareItem toyShareBoxSeriesDTO) {
        this.toyShareBoxSeriesDTO = toyShareBoxSeriesDTO;
    }

    public TSAddrBean getToyShareReceiveAddressDTO() {
        return toyShareReceiveAddressDTO;
    }

    public void setToyShareReceiveAddressDTO(TSAddrBean toyShareReceiveAddressDTO) {
        this.toyShareReceiveAddressDTO = toyShareReceiveAddressDTO;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
