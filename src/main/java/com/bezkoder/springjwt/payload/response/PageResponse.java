package com.bezkoder.springjwt.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * 分页响应格式
 * @param <T> 数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    
    /**
     * 数据列表
     */
    private List<T> content;
    
    /**
     * 当前页码（从0开始）
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 总记录数
     */
    private Long totalElements;
    
    /**
     * 是否为第一页
     */
    private Boolean first;
    
    /**
     * 是否为最后一页
     */
    private Boolean last;
    
    /**
     * 当前页元素数量
     */
    private Integer numberOfElements;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    public PageResponse() {}
    
    public PageResponse(List<T> content, Integer page, Integer size, Long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.first = page == 0;
        this.last = page >= totalPages - 1;
        this.numberOfElements = content.size();
        this.hasNext = !last;
        this.hasPrevious = !first;
    }
    
    // Getters and Setters
    public List<T> getContent() {
        return content;
    }
    
    public void setContent(List<T> content) {
        this.content = content;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
    public Long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }
    
    public Boolean getFirst() {
        return first;
    }
    
    public void setFirst(Boolean first) {
        this.first = first;
    }
    
    public Boolean getLast() {
        return last;
    }
    
    public void setLast(Boolean last) {
        this.last = last;
    }
    
    public Integer getNumberOfElements() {
        return numberOfElements;
    }
    
    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
    
    public Boolean getHasNext() {
        return hasNext;
    }
    
    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
    
    public Boolean getHasPrevious() {
        return hasPrevious;
    }
    
    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
