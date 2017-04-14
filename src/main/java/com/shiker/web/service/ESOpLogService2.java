//package com.shiker.web.service;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.commons.lang.StringUtils;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.sort.SortOrder;
//
//import com.trusfort.base.common.OpState;
//import com.trusfort.base.oplog.bean.ESOpLog;
//import com.trusfort.base.service.BaseESService;
//import com.trusfort.base.util.DateTimeUtil;
//import com.trusfort.base.util.JsonUtil;
//import com.trusfort.base.util.StringUtil;
//import com.trusfort.base.util.es.ESClientUtil;
//
///**
// * 操作日志ElasticSearch服务类
// * 
// * @author DangT
// * @date 2016年8月25日 下午3:25:31
// * @version V1.0
// */
//public class ESOpLogService2 extends BaseESService<ESOpLog> {
//	
//	public static final String INDEX_NAME = "op_log"; // 索引名称
//	public static final String INDEX_TYPE = "op_log"; // 索引类型
//
//	private Thread stateSyncThread;
//	private boolean isStateSyncThreadToRunning = false;
//	private Map<String, OpInfo> stateCache; // 状态信息缓存
//	private Map<String, Long> stateSyncDelay; // 同步状态延迟时间缓存
//	
//	public ESOpLogService2() {
//		super();
//	}
//
//	public ESOpLogService2(int batchSize) {
//		
//		super(batchSize);
//		
//	}
//	
//	public ESOpLogService2(boolean enableStateSync) {
//		
//		this();
//		
//		if (enableStateSync) {
//			isStateSyncThreadToRunning = true;
//			
//			stateCache = new ConcurrentHashMap<String, OpInfo>();
//			stateSyncDelay = new ConcurrentHashMap<String, Long>();
//			
//			this.stateSyncThread = new Thread(new StateSyncThread(), "StateSyncThread");
//			this.stateSyncThread.start();
//			
//			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						// 释放资源
//						close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}));
//		}
//		
//	}
//
//	/**
//	 * 批量入库线程
//	 * 
//	 * @author DangT
//	 * @date 2016年8月26日 下午1:44:13
//	 * @version V1.0
//	 */
//	class StateSyncThread implements Runnable {
//
//		@Override
//		public void run() {
//
//			while (isStateSyncThreadToRunning) {
//				int size = stateSyncDelay.size();
//				if (size > 0) {
//					Map<String, Long> tmp = null;
//					synchronized (stateSyncDelay) {
//						tmp = new HashMap<String, Long>(stateSyncDelay);
//						stateSyncDelay.clear();
//					}
//					
//					// 同步状态
//					syncState(tmp);
//				}
//					
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//	}
//	
//	/**
//	 * 同步状态
//	 *
//	 * @param map
//	 */
//	private void syncState(Map<String, Long> map) {
//		
//		if (map != null) {
//			for (String opId : map.keySet()) {
//				long start = map.get(opId);
//				long timeDiff = System.currentTimeMillis() - start;
//				if (timeDiff > 5000) { // 超期未处理成功的再处理一次直接丢弃
//					try {
//						updateToValidState(opId, stateCache.get(opId));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					stateCache.remove(opId);
//				} else if (timeDiff > 2000) { // 2秒提交一次
//					try {
//						updateToValidState(opId, stateCache.get(opId));
//						stateCache.remove(opId);
//					} catch (Exception e) {
//						// 未处理成功则重新放回缓存
//						stateSyncDelay.put(opId, start);
//					}
//				} else { // 未达到处理时间则重新放回缓存
//					stateSyncDelay.put(opId, start);
//				}
//			}
//		}
//		
//	}
//
//	class OpInfo {
//		
//		private String opType;
//		private String userId;
//		private String userName;
//		
//		public String getOpType() {
//			return opType;
//		}
//
//		public void setOpType(String opType) {
//			this.opType = opType;
//		}
//
//		public String getUserId() {
//			return userId;
//		}
//		
//		public void setUserId(String userId) {
//			this.userId = userId;
//		}
//		
//		public String getUserName() {
//			return userName;
//		}
//		
//		public void setUserName(String userName) {
//			this.userName = userName;
//		}
//		
//	}
//	
//	public void updateState(String opId, String opType, String userId, String userName) {
//		
//		OpInfo info = new OpInfo();
//		info.setOpType(opType);
//		info.setUserId(userId);
//		info.setUserName(userName);
//		stateCache.put(opId, info);
//		stateSyncDelay.put(opId, System.currentTimeMillis());
//		
//	}
//	
//	/**
//	 * 更新操作状态
//	 *
//	 * @param opType
//	 * @param opId
//	 * @throws Exception
//	 */
//	public void updateToValidState(String opId, OpInfo info) throws Exception {
//		
//		if (info != null) {
//			BoolQueryBuilder query = QueryBuilders.boolQuery();
//			query.filter(QueryBuilders.matchQuery("opId", opId));
//			query.filter(QueryBuilders.matchQuery("opType", info.getOpType()));
//			query.filter(QueryBuilders.matchQuery("opState", OpState.NOT_VALID.get()));
//			
//			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(INDEX_NAME)
//					.setTypes(INDEX_TYPE)
//					.setQuery(query)
//					.addSort("createTime", SortOrder.DESC)
//					.setFrom(0)
//					.setSize(1);
//			
//			SearchResponse response = searchRequestBuilder.execute().actionGet();
//			SearchHits hits = response.getHits();
//			
//			if (hits.getTotalHits() > 0) {
//				SearchHit hit = hits.getHits()[0];
//				ESOpLog opLog = JsonUtil.toBean(hit .getSourceAsString(), ESOpLog.class);
//				
//				opLog.setOpState(OpState.VALID.get());
//				opLog.setUserId(info.getUserId());
//				opLog.setUserName(info.getUserName());
//				opLog.setLoginAccount(opLog.getAppid() + "_" + info.getUserId());
//				opLog.setUpdateTime(Long.parseLong(DateTimeUtil.getCurrentDate()));
//				this.add(hit.getId(), opLog);
//			} else {
////			throw new NullPointerException("No query result!");
//			}
//		}
//		
//	}
//	
//	/**
//	 * 查询账号在一段时间内的操作次数
//	 *
//	 * @param appid
//	 * @param userName
//	 * @param opType
//	 * @param opState
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryOpCount(String appid, String userName, String opType, int opState, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("userName", userName));
//		if (-1 != opState) {			
//			query.filter(QueryBuilders.matchQuery("opState", opState));
//		}
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//
//		return this.count(query);
//		
//	}
//	
//	/**
//	 * 查询账号在一段时间内的操作位置数
//	 *
//	 * @param appid
//	 * @param userName
//	 * @param opType
//	 * @param state
//	 * @param city
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryLocationCount(String appid, String userName, String opType, OpState state, String city, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.existsQuery("city"));
//		if (StringUtil.isNotEmpty(city)) {			
//			query.mustNot(QueryBuilders.matchQuery("city", city));
//		}
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("userName", userName));				
//		if (state != null) {
//			query.filter(QueryBuilders.matchQuery("opState", state.get()));
//		}
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> cities = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			String preCity = opLog.getCity();
//			if (StringUtil.isNotEmpty(city)) {				
//				if (!(preCity.equals(city) || preCity.equals(city + "市") || preCity.equals(city.replace("市", "")))) {				
//					if (!(cities.contains(preCity) || cities.contains(preCity + "市") || cities.contains(preCity.replace("市", "")))) {					
//						cities.add(preCity);				
//					}
//				}
//			} else {
//				if (!(cities.contains(preCity) || cities.contains(preCity + "市") || cities.contains(preCity.replace("市", "")))) {					
//					cities.add(preCity);				
//				}
//			}
//		}
//		
//		return cities.size();
//		
//	}
//
//	/**
//	 * 判断设备是否存在
//	 *
//	 * @param devFp
//	 * @param imei
//	 * @param androidId
//	 * @param mac
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean isDeviceExist(String devFp, String imei, String androidId, String mac, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("devFp", devFp));
//		
//		if (StringUtils.isNotEmpty(imei)) {
//			query.filter(QueryBuilders.matchQuery("imei", imei));
//		}
//		
//		if (StringUtils.isNotEmpty(androidId)) {
//			query.filter(QueryBuilders.matchQuery("androidId", androidId));
//		}
//		
//		if (StringUtils.isNotEmpty(mac)) {
//			query.filter(QueryBuilders.matchQuery("mac", mac));
//		}
//		
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		long count = this.count(query);
//		if (count > 0) {
//			return true;
//		}
//		return false;
//		
//	}
//
//	/**
//	 * 查询设备/账号第一次/最近一次的位置信息
//	 *
//	 * @param devFp
//	 * @param userName
//	 * @param isFirst
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public ESOpLog queryLocation(String devFp, String userName, boolean isFirst, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.existsQuery("city"));
//		if (StringUtils.isNotEmpty(devFp)) {
//			query.filter(QueryBuilders.matchQuery("devFp", devFp));
//		}
//		if (StringUtils.isNotEmpty(userName)) {
//			query.filter(QueryBuilders.matchQuery("userName", userName));
//		}
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		} else if (startTime == null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").to(endTime));
//		}
//
//		SortOrder sortOrder = null;
//		if (isFirst) {
//			sortOrder = SortOrder.ASC;
//		} else {
//			sortOrder = SortOrder.DESC;
//		}
//		
//		List<ESOpLog> list = this.query(query, 0, 1, "createTime", sortOrder);
//		if (list.size() > 0) {
//			return list.get(0);
//		}
//		return null;
//		
//	}
//	
//	/**
//	 * 查询操作的账号数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param opState
//	 * @param userName
//	 * @param startTime
//	 * @param endTime
//	 * @param accountDistinct
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryOpAccount(String field, String fieldValue, String appid, String opType, int opState, String userName, Long startTime, Long endTime, boolean accountDistinct) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.mustNot(QueryBuilders.matchQuery("userName", userName));
//		if (-1 != opState) {			
//			query.filter(QueryBuilders.matchQuery("opState", opState));
//		}
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		if (accountDistinct) {
//			Set<String> accounts = new HashSet<String>();
//			
//			List<ESOpLog> list = this.query(query, 0, 100);
//			for (ESOpLog opLog : list) {
//				accounts.add(opLog.getUserName());
//			}
//			
//			return accounts.size();
//		} else {			
//			return this.count(query);
//		}
//		
//	}
//	
//	/**
//	 * 查询操作使用IP个数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param ip
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryOpUseIpCount(String field, String fieldValue, String appid, String opType, String ip, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.mustNot(QueryBuilders.matchQuery("ip", ip));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> ips = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			ips.add(opLog.getIp());
//		}
//		
//		return ips.size();
//		
//	}
//	
//	/**
//	 * 查询操作使用身份证个数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param idNo
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryFieldOpUseIdNoCount(String field, String fieldValue, String appid, String opType, String idNo, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.existsQuery("idNo"));
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.mustNot(QueryBuilders.matchQuery("idNo", idNo));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> idNoSet = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			String no = opLog.getIdNo();
//			if (StringUtil.isNotEmpty(no)) {				
//				idNoSet.add(no);
//			}
//		}
//		
//		return idNoSet.size();
//		
//	}
//	
//	/**
//	 * 查询操作使用银行卡个数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param bankCard
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryFieldOpUseBankCardCount(String field, String fieldValue, String appid, String opType, String bankCard, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.existsQuery("bankCard"));
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.mustNot(QueryBuilders.matchQuery("bankCard", bankCard));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> bankCardSet = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			String card = opLog.getBankCard();
//			if (StringUtil.isNotEmpty(card)) {				
//				bankCardSet.add(card);
//			}
//		}
//		
//		return bankCardSet.size();
//		
//	}
//	
//	/**
//	 * 查询同一密码操作次数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param userPwd
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryFiledUseSamePwdOpCount(String field, String fieldValue, String appid, String opType, String userPwd, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.existsQuery("userPwd"));
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("userPwd", userPwd));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		return this.count(query);
//		
//	}
//	
//	/**
//	 * 查询同一密码操作账号数
//	 *
//	 * @param appid
//	 * @param opType
//	 * @param opState
//	 * @param userPwd
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long querySamePwdOpAccountCount(String appid, String opType, int opState, String userPwd, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.existsQuery("userPwd"));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("userPwd", userPwd));
//		if (-1 != opState) {
//			query.filter(QueryBuilders.matchQuery("opState", opState));
//		}
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> userNameSet = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			userNameSet.add(opLog.getUserName());
//		}
//		
//		return userNameSet.size();
//		
//	}
//	
//	/**
//	 * 查询操作设备数
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param appid
//	 * @param opType
//	 * @param opState
//	 * @param devFp
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryOpDeviceCount(String field, String fieldValue, String appid, String opType, int opState, String devFp, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("opState", opState));
//		query.mustNot(QueryBuilders.matchQuery("devFp", devFp));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		Set<String> devices = new HashSet<String>();
//		List<ESOpLog> list = this.query(query, 0, 100);
//		for (ESOpLog opLog : list) {
//			devices.add(opLog.getDevFp());
//		}
//		
//		return devices.size();
//		
//	}
//
//	/**
//	 * 查询时间段内最近一次的操作信息
//	 *
//	 * @param appid
//	 * @param opType
//	 * @param opState
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public ESOpLog queryLastOpInfo(String appid, String opType, String userName, OpState opState, Long startTime, Long endTime) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		query.filter(QueryBuilders.matchQuery("opState", opState.get()));
//		query.filter(QueryBuilders.matchQuery("userName", userName));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		List<ESOpLog> list = this.query(query, 0, 1, "createTime", SortOrder.DESC);
//		
//		if (list.size() > 0) {
//			return list.get(0);
//		}
//		return null;
//		
//	}
//	
//	/**
//	 * 查询时间段内账号操作次数
//	 *
//	 * @param appid
//	 * @param opType
//	 * @param opState
//	 * @param startTime
//	 * @param endTime
//	 * @return
//	 * @throws Exception
//	 */
//	public long queryOpCount(String appid, String userName, Long startTime, Long endTime, String... opTypes) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("appid", appid));
//		query.filter(QueryBuilders.termsQuery("opType", opTypes));
//		query.filter(QueryBuilders.matchQuery("userName", userName));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		return this.count(query);
//		
//	}
//
//	/**
//	 * 查询操作间隔是否过短
//	 *
//	 * @param devFp
//	 * @param opType
//	 * @param startTime
//	 * @param endTime
//	 * @param intervalThrehold
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean isOpIntervalTooShort(String devFp, String opType, Long startTime, Long endTime, int intervalThrehold) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery("devFp", devFp));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		List<ESOpLog> list = this.query(query, 0, 100, "createTime", SortOrder.ASC);
//		int size = list.size();
//		intervalThrehold = intervalThrehold * 1000;
//		for (int i = 0; i < size; i++) {
//			if (i != size - 1) {
//				long preTime = list.get(i).getCreateTime();
//				long nextTime = list.get(i + 1).getCreateTime();
//				preTime = DateTimeUtil.parse(String.valueOf(preTime), DateTimeUtil.YYYYMMDDHHMMSS).getTime();
//				nextTime = DateTimeUtil.parse(String.valueOf(nextTime), DateTimeUtil.YYYYMMDDHHMMSS).getTime();
//				if ((nextTime - preTime) <= intervalThrehold) {
//					return true;
//				}
//			}
//		}
//		
//		return false;
//		
//	}
//	
//	/**
//	 * 判断同一操作是否在短时间内频繁进行
//	 *
//	 * @param field
//	 * @param fieldValue
//	 * @param opType
//	 * @param startTime
//	 * @param endTime
//	 * @param intervalThrehold
//	 * @param opThrehold
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean isOpIntervalTooShortFrequent(String field, Object fieldValue, String opType, Long startTime, Long endTime, int intervalThrehold, int opThrehold) throws Exception {
//		
//		BoolQueryBuilder query = QueryBuilders.boolQuery();
//		query.filter(QueryBuilders.matchQuery(field, fieldValue));
//		query.filter(QueryBuilders.matchQuery("opType", opType));
//		if (startTime != null && endTime != null) {
//			query.filter(QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime));
//		}
//		
//		int opIntervalTooShortCount = 0;
//		List<ESOpLog> list = this.query(query, 0, 100, "createTime", SortOrder.ASC);
//		int size = list.size();
//		intervalThrehold = intervalThrehold * 1000;
//		for (int i = 0; i < size; i++) {
//			if (i != size - 1) {
//				long preTime = list.get(i).getCreateTime();
//				long nextTime = list.get(i + 1).getCreateTime();
//				preTime = DateTimeUtil.parse(String.valueOf(preTime), DateTimeUtil.YYYYMMDDHHMMSS).getTime();
//				nextTime = DateTimeUtil.parse(String.valueOf(nextTime), DateTimeUtil.YYYYMMDDHHMMSS).getTime();
//				if ((nextTime - preTime) <= intervalThrehold) {
//					opIntervalTooShortCount += 1;
//					
//					if (opIntervalTooShortCount >= opThrehold) {
//						return true;
//					}
//				}
//			}
//		}
//		
//		return false;
//		
//	}
//	
//	@Override
//	public void close() throws Exception {
//		
//		// 停止批量入库线程
//		isStateSyncThreadToRunning = false;
//		if (stateSyncThread != null) {			
//			stateSyncThread.join();
//		}
//		
//		// 未同步的状态进行同步
//		syncState(stateSyncDelay);
//		
//		super.close();
//		
//	}
//
//	@Override
//	public String getIndexName() {
//		return INDEX_NAME;
//	}
//
//	@Override
//	public String getIndexType() {
//		return INDEX_TYPE;
//	}
//
//	@Override
//	public TransportClient getClient() {
//		
//		try {
//			return ESClientUtil.getDefaultClient();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return client;
//		
//	}
//	
//}
