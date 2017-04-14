package com.shiker.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shiker.web.entity.ESRiskAccount;
import com.shiker.web.service.IHelloService;
import com.shiker.web.service.impl.ESRiskAccountService;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Controller
@RequestMapping("hello")
public class HelloController {
    @Resource(name = "helloService")
    private IHelloService helloService;

    @RequestMapping("hello.ajax")
    public @ResponseBody
    String sayHello() {
        System.out.println("this is HelloController");
        return "hello " + helloService.getHello() + "!!";
    }

    @RequestMapping("login.ajax")
    public @ResponseBody
    List<ESRiskAccount> login() {
    	ESRiskAccountService service = new ESRiskAccountService();
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		List<ESRiskAccount> list = null;
		try {
			list = service.query(query, 0, 10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (ESRiskAccount esRiskAccount : list) {
//			System.out.println(esRiskAccount.getAccount());
//		}
		return list;
    }
}
