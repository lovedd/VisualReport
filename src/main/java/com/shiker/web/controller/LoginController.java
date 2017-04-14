package com.shiker.web.controller;

import com.shiker.cache.RedisCacheUtil;
import com.shiker.web.service.IHelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author guohaifeng
 *         2016/7/16
 * @file
 */
@Controller
@RequestMapping("login")
public class LoginController {
    @Resource(name = "helloService")
    private IHelloService helloService;

    @Resource(name = "redisUtil")
    private RedisCacheUtil cache;

    private final String DATA_LIST = "datalist";

    @RequestMapping("login.ajax")
    public @ResponseBody
    Object login () {
        System.out.println("here is loginController");
        List<?> dataList;
        if (cache.contains(DATA_LIST)) {
            Object object = cache.get(DATA_LIST);
            System.out.println(object);
            System.out.println("cache");
            if (object instanceof List<?>) {
                dataList = (List) object;
                System.out.println(dataList);
            } else {
                return object;
            }
        } else {
            dataList = helloService.login();
            cache.set(DATA_LIST, dataList);
        }
        return dataList;
    }
}