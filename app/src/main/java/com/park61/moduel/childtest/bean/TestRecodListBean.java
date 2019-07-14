package com.park61.moduel.childtest.bean;

import com.park61.moduel.firsthead.bean.CommentItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/12/6.
 */

public class TestRecodListBean implements Serializable {


    private int offset;
    private String order;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private List<TestRecordItemBean> rows;
    private String sort;
    private int total;
    private int currentPage;
    private int totalPage;
    private int totalSize;
    private int esPageIndex;

    public int getOffset() {
        return offset;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }



    public String getOrder() {
        return order;
    }

    public String getSort() {
        return sort;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }



    public int getEsPageIndex() {
        return esPageIndex;
    }

    public void setEsPageIndex(int esPageIndex) {
        this.esPageIndex = esPageIndex;
    }

    public List<TestRecordItemBean> getRows() {
        return rows;
    }

    public void setRows(List<TestRecordItemBean> rows) {
        this.rows = rows;
    }
}
