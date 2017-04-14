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

import com.shiker.web.entity.ESRiskIp;
import com.shiker.web.entity.ESRiskRule;
import com.shiker.web.entity.ESRiskTime;
import com.shiker.web.entity.ESUserDistribution;
import com.shiker.web.entity.EsEnsemble;
import com.shiker.web.service.impl.ESRiskRuleService;
import com.shiker.web.service.impl.ESRiskTimeService;
import com.shiker.web.service.impl.EsEnsembleService;

import net.sf.json.JSONObject;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("esRiskTime")
public class EsRiskTimeController {
    @Resource(name = "esRiskTimeService")
    private ESRiskTimeService esRiskTimeService;

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
        Map<String, Double> timeMap = new HashMap<String, Double>();
        List<ESRiskTime> list = esRiskTimeService.TimeSearch(startTime, endTime);
        List<Map<String, Object>> timeScoreList = new ArrayList<>();
    	Map<String, Object> itemMap = null;
    	Map<String, Object> mapObj = null;
        double num = (double) 0;    
        if (list != null && list.size() != 0) {
        	for (int i = 0; i < list.size(); i++) {
        		ESRiskTime rt = list.get(i);
        		if (timeMap.containsKey(rt.getTime())) {
        			num = timeMap.get(rt.getTime());
        			timeMap.put(rt.getTime(), num + rt.getScores());
        		} else {
        			timeMap.put(rt.getTime(), num + rt.getScores());
        		}
        	}
        	List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(timeMap.entrySet()); 
        	Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
				@Override
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
        	for(Map.Entry<String, Double> entry : entryList) {
        		itemMap = new HashMap<String, Object>();
        		mapObj = new HashMap<String, Object>();
        		itemMap.put("time", entry.getKey());
        		itemMap.put("score", entry.getValue());
        		mapObj.put("timeScore", itemMap);
        		timeScoreList.add(mapObj);
        	}
        	dataMap.put("timeScore", timeScoreList);
        	map.put("data", dataMap);
        	map.put("status", true);
        } else {
        	map.put("status", false);
        }
		return map;
    }
}
