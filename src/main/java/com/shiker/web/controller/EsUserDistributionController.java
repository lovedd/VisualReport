package com.shiker.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiker.web.entity.ESUserDistribution;
import com.shiker.web.service.impl.EsUserDistributionService;

import net.sf.json.JSONObject;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("esUserDistribution")
public class EsUserDistributionController {
    @Resource(name = "esUserDistributionService")
    private EsUserDistributionService esUserDistributionService;

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
        Map<String, Long> RankMap = new HashMap<String, Long>();
    	Map<String, Object> seriesItemMap = null;
    	List<Map<String, Object>> seriesList = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<ESUserDistribution> list = esUserDistributionService.TimeSearch(startTime, endTime);
        List<String> legend = new ArrayList<String>();
        Long num, sum = (long) 0, highSum = (long)0;
        Map<String, Object> mapObj = null;
        double highRate = 0.0;
        
        if (list != null && list.size() != 0) {
        	for (int i = 0; i < list.size(); i++) {
        		ESUserDistribution ud = list.get(i);
        		if (RankMap.containsKey(ud.getScoreRank())) {
        			num = RankMap.get(ud.getScoreRank());
        			RankMap.put(ud.getScoreRank(), num + ud.getNum());
        		} else {
        			RankMap.put(ud.getScoreRank(), ud.getNum());
        		}
        	}
        	for (Map.Entry<String, Long> entry : RankMap.entrySet()) {
        		seriesItemMap = new HashMap<String, Object>();
        		mapObj = new HashMap<String, Object>();
        		System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        		legend.add(entry.getKey());
        		seriesItemMap.put("value", entry.getValue());
        		seriesItemMap.put("name", entry.getKey());
        		sum += entry.getValue(); 
        		if (entry.getKey().equals("10~20") || entry.getKey().equals("20+")) {
        			highSum += entry.getValue();
        		}
        		mapObj.put("series", seriesItemMap);
        		seriesList.add(mapObj);
        	}     
        	highRate = (double)highSum / (double)sum;
        	dataMap.put("legend", legend);
        	dataMap.put("seriesList", seriesList);
        	dataMap.put("total", sum);
        	dataMap.put("highRate", highRate);
        	
        	map.put("data", dataMap);
        	map.put("status", true);
        } else {
        	map.put("status", false);
        }
		return map;
    }
}
