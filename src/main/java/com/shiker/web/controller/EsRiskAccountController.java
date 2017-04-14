package com.shiker.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiker.web.entity.ESRiskAccount;
import com.shiker.web.service.impl.ESRiskAccountService;


/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("esRiskAccount")
public class EsRiskAccountController {
    @Resource(name = "esRiskAccountService")
    private ESRiskAccountService esriskAccountService;

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
        Map<String, Double> accountMap = new HashMap<String, Double>();
        List<ESRiskAccount> list = esriskAccountService.TimeSearch(startTime, endTime);
        List<Map<String, Object>> accountScoreList = new ArrayList<>();
      
    	Map<String, Object> itemMap = null;
    	Map<String, Object> mapObj = null;
        double num = (double) 0;    
        long accountTotal = (long) 0;
        if (list != null && list.size() != 0) {
        	for (int i = 0; i < list.size(); i++) {
        		ESRiskAccount ra = list.get(i);
        		if (accountMap.containsKey(ra.getAccount())) {
        			num = accountMap.get(ra.getAccount());
        			accountMap.put(ra.getAccount(), num + ra.getScores());
        		} else {
        			accountMap.put(ra.getAccount(), num + ra.getScores());
        		}
        	}
        	List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(accountMap.entrySet()); 
        	Collections.sort(entryList, new Comparator<Map.Entry<String, Double>>() {
				@Override
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
        	for(Map.Entry<String, Double> entry : entryList) {
        		itemMap = new HashMap<String, Object>();
        		mapObj = new HashMap<String, Object>();
        		itemMap.put("account", entry.getKey());
        		itemMap.put("score", entry.getValue());
        		mapObj.put("accountScore", itemMap);
        		accountScoreList.add(mapObj);
        		if (entry.getValue() > 20) {
        			accountTotal++;
        		}
        	}
        	dataMap.put("accountScore", accountScoreList);
        	dataMap.put("accountTotal", accountTotal);
        	map.put("data", dataMap);
        	map.put("status", true);
        } else {
        	map.put("status", false);
        }
		return map;
    }
}
