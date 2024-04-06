package com.minibank.demo.user;

public class Pagination {
    private int page;
    private int limit;
    private int offset;

    public Pagination(int page, int limit) {
        this.page = (page > 0) ? page - 1 : 0 ;
        this.limit = (limit > 0) ? limit : 10;
    }

    public void computeOffset() {
        int offset = (this.page - 1) * this.limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getPage() { return page; }
}
