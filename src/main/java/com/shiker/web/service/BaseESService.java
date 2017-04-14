package com.shiker.web.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.shiker.web.service.util.ESUtil;
import com.shiker.web.service.util.GenericsClassUtils;
import com.shiker.web.service.util.JsonUtil;
import com.shiker.web.service.util.Page;

/**
 * ElasticSearch服务基类
 * 
 * @author DangT
 * @date 2016年8月26日 下午1:11:58
 * @version V1.0
 * @param <T>
 */
public abstract class BaseESService<T> {
	
	private String indexName; // 索引名称
	private String indexType; // 索引类型

	private Class<T> cls; // 泛型类
	private long start; // 初始化时间
	private boolean batchEnabled = false; // 是否启用批量操作
	private int batchSize = 50; // 批量大小
	private List<T> lists; // 缓存入ES数据
	private boolean isBatchSaveThreadToRunning = false; // 批量入库线程是否运行
	private Thread batchSaveThread; // 批量入库线程实例
	public TransportClient client;
	
	public BaseESService() {
		this(false, 1);
	}

	public BaseESService(int batchSize) {
		this(true, batchSize);
	}
	
	@SuppressWarnings("unchecked")
	private BaseESService(boolean enableBatch, int batchSize) {
		
		try {
			// 初始化参数
			this.indexName = getIndexName();
			this.indexType = getIndexType();
			
			// 初始化客户端连接
			this.client = getClient();
			
			// 初始化泛型类型
			this.cls = GenericsClassUtils.getSuperClassGenricType(getClass());
			
			// 设置初始化时间
			this.start = System.currentTimeMillis();
			
			// 初始化缓存列表
			this.lists = Collections.synchronizedList(new ArrayList<T>());
			
			this.batchEnabled = enableBatch;
			if (batchEnabled) { // 启用批量操作
				this.batchSize = batchSize;
				// 启动批量入库线程
				this.isBatchSaveThreadToRunning = true;
				this.batchSaveThread = new Thread(new SaveThread(), "BatchSaveThread");
				this.batchSaveThread.start();
				
				Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// 释放资源
							close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 获取客户端连接
	 *
	 * @return
	 */
	public abstract TransportClient getClient();

	/**
	 * 批量入库线程
	 * 
	 * @author DangT
	 * @date 2016年8月26日 下午1:44:13
	 * @version V1.0
	 */
	class SaveThread implements Runnable {

		@Override
		public void run() {

			while (isBatchSaveThreadToRunning) {
				try {
					int size = lists.size();
					if (size > 0) {
						if (size >= batchSize) {
							addBeans();
						} else {
							long timeDiff = System.currentTimeMillis() - start;
							if (timeDiff > 2000) { // 2秒提交一次
								addBeans();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
					
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 批量入对象
	 *
	 * @throws Exception
	 */
	private void addBeans() throws Exception {

//		synchronized (lists) {
//			if (lists.size() > 0) {
//				addBatch(lists);
//				lists.clear();
//				start = System.currentTimeMillis();
//			}
//		}
		List<T> newLists = null;
		synchronized (lists) {
			newLists = new ArrayList<T>(lists);
			lists.clear();
		}
		
		if (newLists != null && newLists.size() > 0) {
			addBatch(newLists);
			start = System.currentTimeMillis();
		}

	}
	
	public XContentBuilder getMapping() {
		return null;
	}

	/**
	 * 获取索引名称
	 *
	 * @return
	 */
	public abstract String getIndexName();

	/**
	 * 获取索引类型
	 *
	 * @return
	 */
	public abstract String getIndexType();

	/**
	 * 添加对象
	 *
	 * @param t
	 */
	public void add(T t) {
		
		client.prepareIndex(indexName, indexType)
			.setSource(JsonUtil.toJson(t))
			.execute()
			.actionGet();
		
	}
	
	/**
	 * 添加对象
	 *
	 * @param id
	 * @param t
	 */
	public void add(String id, T t) {
		
		client.prepareIndex(indexName, indexType)
			.setId(id)
			.setSource(JsonUtil.toJson(t))
			.execute()
			.actionGet();
		
	}
	
	/**
	 * 批量添加对象
	 *
	 * @param t
	 */
	public void addBatch(T t) {
		
		if (batchEnabled) {			
			lists.add(t);
		} else {
			add(t);
		}
		
	}
	
	/**
	 * 批量添加对象
	 *
	 * @param list
	 * @throws Exception
	 */
	public void addBatch(Collection<T> list) throws Exception {
		
		if (list.size() > 0) {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for (T t : list) {
				IndexRequestBuilder indexRequest = client.prepareIndex(indexName, indexType)
					.setSource(ESUtil.toJson(t));
				bulkRequest.add(indexRequest);
			}
			
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				System.out.println(bulkResponse.buildFailureMessage());
			}
		}
		
	}

	/**
	 * 根据ID查询
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T get(String id) throws Exception {
		
		GetResponse response = client.prepareGet(indexName, indexType, id)
			.execute()
			.actionGet();
		
		String result = response.getSourceAsString();
		if (StringUtils.isNotEmpty(result)) {
			return JsonUtil.toBean(result, cls);
		}
		return null;
		
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String... indexTypes) throws Exception {
		
		return query(query, from, size, null, null, indexTypes);
		
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size) throws Exception {
		
		return query(query, from, size, null, null);
		
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String sortField, SortOrder sortOrder, String... indexTypes) throws Exception {
		
		List<T> list = new ArrayList<T>();
		
		SearchRequestBuilder builder = client.prepareSearch(indexName)
			.setTypes(indexTypes)
			.setQuery(query)
			.setFrom(from)
			.setSize(size);
		if (StringUtils.isNotEmpty(sortField)) {
			builder.addSort(sortField, sortOrder);
		}
		
		SearchResponse response = builder.execute().actionGet();
		SearchHits hits = response.getHits();
		
		if (hits.getTotalHits() > 0) {
			for (SearchHit hit : hits) {
				list.add(JsonUtil.toBean(hit.getSourceAsString(), cls));
			}
		}
		
		return list;
		
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String sortField, SortOrder sortOrder) throws Exception {
		
		return query(query, from, size, sortField, sortOrder, indexType);
		
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param indexTypes
	 * @return
	 * @throws Exception
	 */
	public List<T> query(Page<T> page, String... indexTypes) throws Exception {
		
		List<T> list = new ArrayList<T>();
		
		if (page.getTotal() == 0) {
			page.setTotal(this.count(page.getQuery()));
		}
		
		SearchRequestBuilder builder = client.prepareSearch(indexName)
				.setTypes(indexTypes)
				.setQuery(page.getQuery())
				.setFrom(page.getStart())
				.setSize(page.getPageSize());
		String sortField = page.getSortField();
		if (StringUtils.isNotEmpty(sortField)) {
			SortOrder sortOrder = page.getSortOrder();
			if (sortOrder == null) {				
				builder.addSort(sortField, SortOrder.DESC);
			} else {
				builder.addSort(sortField, sortOrder);				
			}
		}
		
		SearchResponse response = builder.execute().actionGet();
		SearchHits hits = response.getHits();
		
		if (hits.getTotalHits() > 0) {
			for (SearchHit hit : hits) {
				String id = hit.getId();
				T bean = JsonUtil.toBean(hit.getSourceAsString(), cls);
				Field[] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					if ("id".equals(field.getName())) {
						field.setAccessible(true);
						field.set(bean, id);
						
						break;
					}
				}
				list.add(bean);
				
				page.setDatas(list);
			}
		}
		
		return list;
		
	}
	
	/**
	 * 根据ID删除数据
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(String id) {
		
		DeleteResponse response = client.prepareDelete(indexName, indexType, id).execute().actionGet();
		return response.isFound();
		
	}
	
	/**
	 * 统计总数
	 *
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public long count(BoolQueryBuilder query) throws Exception {
		
		SearchResponse response = client.prepareSearch(indexName)
			.setTypes(indexType)
			.setSearchType(SearchType.QUERY_THEN_FETCH)
			.setQuery(query)
			.setFrom(0)
			.setSize(0)
			.execute()
			.actionGet();
		SearchHits hits = response.getHits();
		
		return hits.totalHits();
		
	}
	
	/**
	 * 关闭连接
	 *
	 * @throws Exception
	 */
	public void close() throws Exception {
		
		// 停止批量入库线程
		isBatchSaveThreadToRunning = false;
		if (batchSaveThread != null) {			
			batchSaveThread.join();
		}
		
		// 未入库的数据入库
		addBeans();
		
		// 关闭连接
//		client.close();
//		client = null;
		
	}
	
}
