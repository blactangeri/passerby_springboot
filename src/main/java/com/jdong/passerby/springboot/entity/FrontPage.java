package com.jdong.passerby.springboot.entity;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * Created by James on 10/1/17
 */
public class FrontPage<T> {

    private boolean _search;
    private String timestamp;
    private int rows;
    private int page;
    private String sortIdx;
    private String order;
    private String keywords;

    public boolean is_search() {
        return _search;
    }

    public void set_search(boolean _search) {
        this._search = _search;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSortIdx() {
        return sortIdx;
    }

    public void setSortIdx(String sortIdx) {
        this.sortIdx = sortIdx;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Page<T> getPagePlus(){
        Page<T> pagePlus = new Page<>();
        pagePlus.setCurrent(this.page);
        pagePlus.setSize(this.rows);
        pagePlus.setAsc(this.order.equals("asc"));
        pagePlus.setOrderByField(this.sortIdx);
        return pagePlus;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
