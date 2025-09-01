package org.uma_gym.web_event_booker.controller.dto;

import java.util.List;

public class PagedResult<T> {
    private List<T> data;
    private long totalCount;

    public PagedResult(List<T> data, long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    // Getteri
    public List<T> getData() { return data; }
    public long getTotalCount() { return totalCount; }
}