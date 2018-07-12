package com.dmz.yzt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmz.common.exception.BusinessException;
import com.dmz.common.exception.util.ErrorEnum;
import com.dmz.yzt.feign.service.YZTFeignService;
import com.dmz.yzt.model.LoanCommonDto;
import com.dmz.yzt.model.MessageRequest;
import com.dmz.yzt.model.MessageResponse;
import com.dmz.yzt.util.Config;
import com.dmz.yzt.util.HttpUtil;
import com.dmz.yzt.util.SignUtil;
import com.dmz.yzt.util.TransEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by zhangf on 2018/6/27.
 */

@Controller
@WebServlet(urlPatterns = "/serviceTest/")
public class PublicServiceTest extends HttpServlet {
    private static final Logger logger = Logger.getLogger(PublicServiceTest.class);
    @Autowired
    private YZTFeignService yztFeignService;


    @Value("${time.timeStart}")
    private static String timeStart;
    @Value("${time.timeEnd}")
    private static String timeEnd;

    private static void validateTime() throws BusinessException {
        Calendar localCalendar = Calendar.getInstance();
        int hour = localCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= Integer.parseInt(timeStart) || hour < Integer.parseInt(timeEnd)) {
            logger.error("Non Working Date.........");
            throw new BusinessException(ErrorEnum.NON_WORK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        logger.info("-----------doPost----------------");
        String requestSeqNo = null;
        String transEnum;
        MessageResponse messageResponse = new MessageResponse();
        MessageRequest messageRequest;
        try {
            //获取请求报文
            String requestJson = HttpUtil.charReader(request);
            logger.info("请求报文信息：" + requestJson);
            JSONObject paramJson = JSONObject.parseObject(requestJson);
            //解密
            JSONObject decryptStr = paramJson;
            if (decryptStr == null) {
                logger.error("Decrypt Message Failure");
                throw new BusinessException(ErrorEnum.TRANX_FAIL);
            }
            requestJson = decryptStr.toString();
            messageRequest = JSON.parseObject(requestJson, MessageRequest.class);
            transEnum = messageRequest.getMsgType();
            requestSeqNo = messageRequest.getRequestId();
            //validateTime();
            String responseMsg = this.sendOtsServiceByTransCode(transEnum, messageRequest.getData());
            if ("TransERR".equals(responseMsg)) {
                logger.error("transEnum Undefined :[" + transEnum + "]");
                throw new BusinessException(ErrorEnum.TRANX_CODE_ERR);
            }
            JSONObject jsonObject = JSONObject.parseObject(responseMsg);
            messageResponse.setData(jsonObject);
            HttpUtil.returnMsgT(httpServletResponse, messageResponse);
        } catch (BusinessException e) {
            logger.error("dmz_yzt_error  RequestSeqNo:[" + requestSeqNo + "] BusinessException ", e);
            messageResponse.setRtnCode(e.getCode());
            messageResponse.setRtnMsg(e.getDscp());
            HttpUtil.returnMsgT(httpServletResponse, messageResponse);
        } catch (Exception e) {
            logger.error("dmz_yzt_error  RequestSeqNo:[" + requestSeqNo + "] Exception  ", e);
            messageResponse.setRtnCode(ErrorEnum.SYS_ERROR.getCode());
            messageResponse.setRtnMsg(ErrorEnum.SYS_ERROR.getDscp());
            HttpUtil.returnMsgT(httpServletResponse, messageResponse);
        }
    }

    private String sendOtsServiceByTransCode(String transEnum, String requestContentJson) {
        logger.info("origin :"+requestContentJson);
        System.out.println("branchCode :"+requestContentJson);
        JSONObject jsonObject = JSONObject.parseObject(requestContentJson);

        jsonObject.put("branchCode", "WZZX01");
        jsonObject.put("channel", "ZJ01");
        jsonObject.put("LoanType", "SME");
        jsonObject.put("projectNo", "WZ001");
        jsonObject.put("stripline", "SME01");
        requestContentJson = jsonObject.toString();
        logger.info("trans :"+requestContentJson);
        System.out.println("branchCode :"+requestContentJson);

        if (TransEnum.fqdksq.getTransCode().equals(transEnum)) {
            return yztFeignService.loanApp(requestContentJson);
        } else if (TransEnum.yxwjplsc.getTransCode().equals(transEnum)) {
            return yztFeignService.imageUpload(requestContentJson);
        } else if (TransEnum.cjhkjh.getTransCode().equals(transEnum)) {
            return yztFeignService.createRepayPlan(requestContentJson);
        } else if (TransEnum.xzhzfqmhtwj.getTransCode().equals(transEnum)) {
            return yztFeignService.imageContFile(requestContentJson);
        } else if (TransEnum.xghkrxx.getTransCode().equals(transEnum)) {
            return yztFeignService.modifyRepayInfo(requestContentJson);
        } else if (TransEnum.cxfkzt.getTransCode().equals(transEnum)) {
            return yztFeignService.queryLuState(requestContentJson);
        } else if (TransEnum.plcxfkzt.getTransCode().equals(transEnum)) {
            return yztFeignService.queryNewRepayPlan(requestContentJson);
        } else if (TransEnum.xzdzwj.getTransCode().equals(transEnum)) {
            return yztFeignService.downReconciliationFile(requestContentJson);
        }
        return "TransERR";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("-----------doGet----------------");
        String reqString = JSON.toJSONString(req);
        JSONObject data = JSONObject.parseObject(reqString);
        JSONObject param = SignUtil.envolopData(data, Config.MY_PRIVATE_KEY);
        doPost(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

}
