package com.shiker.web.service.impl;

import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.shiker.web.entity.ESRiskDev;
import com.shiker.web.service.BaseESService;
import com.shiker.web.service.util.ESClientUtil;
/**
 * 操作日志risky_dev表服务类
 * 
 * @Author RenXintao
 * @Date 04/10/17
 */
@Service("esRiskDevService")
public class ESRiskDevService extends BaseESService<ESRiskDev> {
	public static final String INDEX_NAME = "datawarehouse"; // 索引名称
	public static final String INDEX_TYPE = "risky_dev"; // 索引类型

	public ESRiskDevService() {
		super();
	}

	public ESRiskDevService(int batchSize) {
		super(batchSize);
	}

	@Override
	public String getIndexName() {
		return INDEX_NAME;
	}

	@Override
	public String getIndexType() {
		return INDEX_TYPE;
	}

	@Override
	public TransportClient getClient() {
		try {
			return ESClientUtil.getDefaultClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public List<ESRiskDev> TimeSearch(String startTime, String endTime) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.filter(QueryBuilders.rangeQuery("cdate").from(startTime).to(endTime).includeLower(true) // 包括下界
				.includeUpper(true)); // 包括上界
		List<ESRiskDev> list = null;
		SortOrder sortOrder = SortOrder.ASC;
		try {
			list = this.query(query, 0, 200, "cdate", sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list.size());
		for (ESRiskDev esRiskDev : list) {
			System.out.println(esRiskDev.getCdate() + ", " + esRiskDev.getDevFp()+ ", " + esRiskDev.getScores());
		}
		return list;
	}
}
