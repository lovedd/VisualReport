package com.shiker.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiker.web.entity.ESRiskRule;
import com.shiker.web.entity.ESUserDistribution;
import com.shiker.web.entity.EsEnsemble;
import com.shiker.web.service.impl.ESRiskRuleService;
import com.shiker.web.service.impl.EsEnsembleService;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("esRiskRule")
public class EsRiskRuleController {
    @Resource(name = "esRiskRuleService")
    private ESRiskRuleService esRiskRuleService;

    @RequestMapping(value = "get.ajax", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> get(@RequestParam(value="start") String startTime,
    		@RequestParam(value="end") String endTime) {
    	Map <String, Object> map = new HashMap<String, Object>();
		if (startTime == null || startTime.length() == 0 || endTime == null || endTime.length() == 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // 设置日期格式
			Date nowDate = new Date();
			String nowTime = df.format(nowDate);
			Calendar now = Calendar.getInstance();
			now.setTime(nowDate);
			now.set(Calendar.DATE, now.get(Calendar.DATE) - 20);
			Date preDate = now.getTime();
			String preTime = df.format(preDate);
			
			startTime = preTime;
    		endTime = nowTime;
		}
        System.out.println("this is EsEnsembleController");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Long> ruleMap = new HashMap<String, Long>();
        List<ESRiskRule> list = esRiskRuleService.TimeSearch(startTime, endTime);
        List<String> xAxis = new ArrayList<String>();
        List<Long> series = new ArrayList<Long>();
        Long num = (long)0;       
        if (list != null && list.size() != 0) {
        	for (int i = 0; i < list.size(); i++) {
        		ESRiskRule rr = list.get(i);
        		if (ruleMap.containsKey(rr.getRuleRiskTip())) {
        			num = ruleMap.get(rr.getRuleRiskTip());
        			ruleMap.put(rr.getRuleRiskTip(), num + rr.getNum());
        		} else {
        			ruleMap.put(rr.getRuleRiskTip(), rr.getNum());
        		}
        	}
        	List<Map.Entry<String, Long>> entryList = new ArrayList<Map.Entry<String, Long>>(ruleMap.entrySet()); 
        	Collections.sort(entryList, new Comparator<Map.Entry<String, Long>>() {
				@Override
				public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
        	for(Map.Entry<String, Long> entry : entryList) {
        		xAxis.add(entry.getKey());
        		series.add(entry.getValue());
        	}
        	dataMap.put("xAxis", xAxis);
        	dataMap.put("series", series);
        	map.put("data", dataMap);
        	map.put("status", true);
        } else {
        	map.put("status", false);
        }
		return map;
    }
}
