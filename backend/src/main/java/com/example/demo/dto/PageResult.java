package com.example.demo.dto;

import java.util.List;

/**
 * 分页结果封装DTO
 * @param <T> 数据类型
 */
public class PageResult<T> {
    private List<T> records;    // 数据列表
    private long total;         // 总记录数
    private long current;       // 当前页码
    private long size;          // 每页大小

    public PageResult() {}

    public PageResult(List<T> records, long total, long current, long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * 获取总页数
     */
    public long getPages() {
        if (size == 0) return 0;
        return (total + size - 1) / size;
    }
}
