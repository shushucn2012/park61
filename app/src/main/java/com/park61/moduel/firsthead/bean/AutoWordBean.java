package com.park61.moduel.firsthead.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nieyu on 2017/11/14.
 */

public class AutoWordBean implements Serializable {

    private int pageIndex;
    private int pageSize;
    private List<AutoWordAboutBean>rows;

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<AutoWordAboutBean> getRows() {
        return rows;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRows(List<AutoWordAboutBean> rows) {
        this.rows = rows;
    }
}
