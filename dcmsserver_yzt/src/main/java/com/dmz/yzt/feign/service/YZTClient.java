package com.dmz.yzt.feign.service;

import com.alibaba.fastjson.JSONObject;
import com.dmz.yzt.model.MessageRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.*;

/**
 * Created by zhangf on 2018/7/3.
 * 访问api的接口
 */
@FeignClient(url = "${url.yztUrl}",name="yztClient")
public interface YZTClient {

    @RequestMapping(value = "/yztNotice",method =  RequestMethod.POST,consumes = "application/json",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String noticeYZT(@RequestBody JSONObject params);

}
