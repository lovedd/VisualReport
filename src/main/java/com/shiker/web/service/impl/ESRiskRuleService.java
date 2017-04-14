package com.shiker.web.service.impl;

import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.shiker.web.entity.ESRiskIp;
import com.shiker.web.entity.ESRiskRule;
import com.shiker.web.service.BaseESService;
import com.shiker.web.service.util.ESClientUtil;
/**
 * 操作日志risky_ip表服务类
 * 
 * @Author RenXintao
 * @Date 04/10/17
 */
@Service("esRiskRuleService")
public class ESRiskRuleService extends BaseESService<ESRiskRule> {
	public static final String INDEX_NAME = "datawarehouse"; // 索引名称
	public static final String INDEX_TYPE = "risky_rules"; // 索引类型

	public ESRiskRuleService() {
		super();
	}

	public ESRiskRuleService(int batchSize) {
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

	public List<ESRiskRule> TimeSearch(String startTime, String endTime) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.filter(QueryBuilders.rangeQuery("cdate").from(startTime).to(endTime));
		List<ESRiskRule> list = null;
		SortOrder sortOrder = SortOrder.ASC;
		try {
			list = this.query(query, 0, 200, "cdate", sortOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list.size());
		for (ESRiskRule esRiskRule : list) {
			System.out.println(esRiskRule.getCdate() + ", " + esRiskRule.getRuleRiskTip() + ", " + esRiskRule.getNum());
		}
		return list;
	}
}
