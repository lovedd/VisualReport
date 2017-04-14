package com.shiker.web.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shiker.web.dao.ILoginDao;
import com.shiker.web.service.IHelloService;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
@Service("helloService")
public class HelloService implements IHelloService{

    @Resource(name = "loginDao")
    private ILoginDao loginDao;

    public List<Map<String, Object>> login() {
        System.out.println("here is LoginService");
        return loginDao.getUser();
    }

    public String getHello() {
        System.out.println("this is HelloService");
        return "guohaifeng";
    }
}
