package com.park61.moduel.firsthead.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/14.
 */

public class SearchImpointBean implements Serializable {

    private int esPageIndex;
    private int pageIndex;
    private int pageSize;
    private List<SearchImpointContentBean>rows;

    public int getPageSize() {
        return pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getEsPageIndex() {
        return esPageIndex;
    }

    public List<SearchImpointContentBean> getRows() {
        return rows;
    }

    public void setRows(List<SearchImpointContentBean> rows) {
        this.rows = rows;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setEsPageIndex(int esPageIndex) {
        this.esPageIndex = esPageIndex;
    }
}
