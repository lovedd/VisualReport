package com.shiker.web.service.util;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * ElasticSearch分页查询工具类
 * 
 * @author DangT
 * @date 2017年1月20日 上午10:03:54
 * @version V1.0
 */
public class Page<T> {

	// 每页显示条数，默认为50条
	public static final int DEFAULT_SIZE = 10;

	// 当前页码， 从1开始计
	private int currentPageNo;

	// 每页条数
	private int pageSize;

	// 总条数
	private long total;

	// 查询条件
	private String condition;

	// 当前页数据
	private List<T> datas;
	
	// 查询条件
	private BoolQueryBuilder query;

	// 排序字段
	private String sortField;
	
	// 排序类型
	private SortOrder sortOrder;
	
	public Page() {
		
		this.currentPageNo = 1; // 默认第一页
		this.pageSize = DEFAULT_SIZE;
		
	}
	
	public Page(int pageSize) {
		
		this.currentPageNo = 1; // 默认第一页
		this.pageSize = pageSize;
		
	}
	
	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public BoolQueryBuilder getQuery() {
		return query;
	}

	public void setQuery(BoolQueryBuilder query) {
		this.query = query;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * 获取总页数
	 *
	 * @return
	 */
	public long getTotalPages() {
		
		long totalPages = total / pageSize;
		if (total % pageSize != 0) {
			totalPages++;
		}

		return totalPages;
		
	}

	/**
	 * 获取从第几条数据开始查询
	 *
	 * @return
	 */
	public int getStart() {
		
		return (currentPageNo - 1) * pageSize;
		
	}

	/**
	 * 判断是否还有后一页
	 *
	 * @return
	 */
	public boolean hasNext() {
		
		return (getTotalPages() != 0 && getTotalPages() != currentPageNo) ? true : false;
		
	}

}
