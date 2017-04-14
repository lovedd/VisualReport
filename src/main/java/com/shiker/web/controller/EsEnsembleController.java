package com.shiker.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiker.web.entity.EsEnsemble;
import com.shiker.web.service.impl.EsEnsembleService;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("esEnsemble")
public class EsEnsembleController {
    @Resource(name = "esEnsembleService")
    private EsEnsembleService esEnsembleService;

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
        List<EsEnsemble> list = esEnsembleService.TimeSearch(startTime, endTime);
        List<EsEnsemble> newList = new ArrayList<EsEnsemble>();
        List<String> xAxis = new ArrayList<String>();
        List<Long> seriesTotalAccount = new ArrayList<Long>();
        List<Double> seriesHitRate = new ArrayList<Double>();
        List<Long> seriesRechargeAccount = new ArrayList<Long>();
        Long total = (long) 0;
        Long hit = (long)0;
        Long reCharge = (long)0;
        Double hitRate = (double) 0;
        Double reChargeRate = (double)0;

        
        if (list != null && list.size() != 0) {
        	newList.add(list.get(0));
        	for (int i = 1; i < list.size(); i++) {
        		if (list.get(i).getCdate().equals(list.get(i - 1).getCdate())) {
        			continue;
        		}
        		newList.add(list.get(i));
        	}
        	if (newList != null && newList.size() != 0) {
        		for (int i = 0; i < newList.size(); i++) {
        			EsEnsemble temp = newList.get(i);
        			total += temp.getTotalCnt();
        			hit += temp.getHitRuleCnt();
        			reCharge += temp.getReChargeCnt();
        			xAxis.add(temp.getCdate());
        			seriesTotalAccount.add(temp.getTotalCnt());
        			seriesHitRate.add((double)temp.getHitRuleCnt() / temp.getTotalCnt());
        			seriesRechargeAccount.add(temp.getReChargeCnt());
        		}
        	}
        	hitRate = (double) hit / (double) total;
        	reChargeRate = (double) reCharge / (double) total;
        	dataMap.put("xAxis", xAxis);
        	dataMap.put("seriesTotalAccount", seriesTotalAccount);
        	dataMap.put("seriesRechargeAccount", seriesRechargeAccount);
        	dataMap.put("seriesHitRate", seriesHitRate);
        	dataMap.put("total", total);
        	dataMap.put("hit", hit);
        	dataMap.put("reCharge", reCharge);
        	dataMap.put("hitRate", hitRate);
        	dataMap.put("reChargeRate", reChargeRate);
        	map.put("data", dataMap);
        	map.put("status", true);
        } else {
        	map.put("status", false);
        }
		return map;
    }
}
