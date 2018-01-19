package com.wizinno.livgo.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 * 获取数据列表 参数基类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseSearchRequest {
    // 排序字段
    private List<String> sortOrders;

    // 排序方式 desc or asc
    private String sortDirection = "desc";

    private int pageIndex = 0;

    private int pageSize = 10;

    public List<String> getSortOrders() {
        return sortOrders;
    }

    public void setSortOrders(List<String> sortOrders) {
        this.sortOrders = sortOrders;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
