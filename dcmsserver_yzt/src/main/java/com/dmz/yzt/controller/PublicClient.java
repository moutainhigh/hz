package com.dmz.yzt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmz.common.exception.BusinessException;
import com.dmz.common.exception.util.ErrorEnum;
import com.dmz.yzt.feign.service.YZTClient;
import com.dmz.yzt.model.MessageRequest;
import com.dmz.yzt.util.Config;
import com.dmz.yzt.util.SignUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zhangf on 2018/7/3.
 */


@RestController
@RequestMapping(value = "/publicClientbase")
public class PublicClient {
    private static final Logger logger = Logger.getLogger(PublicClient.class);

    private static final String CHARSET = "UTF-8";

    MessageRequest messageRequest;

    @Autowired
    YZTClient yztClient;

    @RequestMapping(value = "/yztSendMsg", method = RequestMethod.POST)
    public String yztSendMsg(String  tranxCode,String requestMsg) throws Exception {
        MessageRequest messageRequest = MessageRequest.getDefaultMessageRequest();
        messageRequest.setMsgType(tranxCode);
        messageRequest.setData(requestMsg);
        String sendMessage=JSON.toJSONString(messageRequest);
        JSONObject data = JSONObject.parseObject(sendMessage);
        //加密
        JSONObject param = SignUtil.envolopData(data, Config.MY_PRIVATE_KEY);
        String responseMsg = yztClient.noticeYZT(param);
        logger.info("responseMsg :" + responseMsg);
        JSONObject paramJson = JSONObject.parseObject(responseMsg);
        //解密
        JSONObject decryptStr = SignUtil.decryptData(Config.YZT_PUBLIC_KEY, paramJson.getString("sign"), paramJson.getString("msg"));
        decryptStr.toString();
        if (!"success".equals(responseMsg)) {
            logger.error("responseMsg :" + responseMsg);
        }
        return responseMsg;
    }

}
