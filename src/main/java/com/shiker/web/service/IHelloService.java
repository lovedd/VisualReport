package com.shiker.web.service;

import java.util.List;
import java.util.Map;

/**
 * @Author RenXintao
 * @Date 12/25/16
 */
public interface IHelloService {
    String getHello();
    public List<Map<String, Object>> login();
}
