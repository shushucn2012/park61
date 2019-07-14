package com.park61.moduel.firsthead.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/17.
 */

public class SpecialContentBean implements Serializable {

    private int esPageIndex;
    private int offset;
    private String order;
    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private String sort;
    private int total;
    private List<SearchImpointContentBean>rows;


    public int getOffset() {
        return offset;
    }

    public int getEsPageIndex() {

        return esPageIndex;
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

    public String getSort() {
        return sort;
    }

    public int getTotal() {
        return total;
    }

    public List<SearchImpointContentBean> getRows() {
        return rows;
    }

    public String getOrder() {
        return order;
    }

    public void setEsPageIndex(int esPageIndex) {
        this.esPageIndex = esPageIndex;
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

    public void setRows(List<SearchImpointContentBean> rows) {
        this.rows = rows;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
