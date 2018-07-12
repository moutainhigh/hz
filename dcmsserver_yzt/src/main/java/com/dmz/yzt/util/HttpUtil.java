package com.dmz.yzt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmz.common.exception.BusinessException;
import com.dmz.common.exception.util.ErrorEnum;
import com.dmz.yzt.controller.PublicService;
import com.dmz.yzt.model.BaseBody;
import com.dmz.yzt.model.MessageRequest;
import com.dmz.yzt.model.MessageResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhangf on 2018/6/27.
 */
public class HttpUtil {
    private static final Logger logger = Logger.getLogger(HttpUtil.class);
    //字符串读取
    public static String charReader(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while ((str = br.readLine()) != null) {
            wholeStr += str;
        }
        return wholeStr;
    }

    //组装返回报文
    public static void returnMsg(HttpServletResponse httpServletResponse, MessageResponse messageResponse) throws IOException {
        String responseMessage = JSONObject.toJSONString(messageResponse);
        JSONObject retJson = JSONObject.parseObject(responseMessage);
        responseMessage = SignUtil.envolopData(retJson, Config.MY_PRIVATE_KEY).toString();
        httpServletResponse.setContentType(SysParameter.CONTENT_TYPE);
        httpServletResponse.setCharacterEncoding(SysParameter.DEFAULT_ENCODING);
        httpServletResponse.getWriter().append(responseMessage);
    }
    //组装返回报文
    public static void returnMsgT(HttpServletResponse httpServletResponse, MessageResponse messageResponse) throws IOException {
        String responseMessage = JSONObject.toJSONString(messageResponse);
        JSONObject  retJson= JSONObject.parseObject(responseMessage);
        responseMessage =JSON.toJSONString(retJson);
        httpServletResponse.setContentType(SysParameter.CONTENT_TYPE);
        httpServletResponse.setCharacterEncoding(SysParameter.DEFAULT_ENCODING);
        httpServletResponse.getWriter().append(responseMessage);
    }
    private static String callSvr(String sendMessage,String connectionURL) throws BusinessException
    {

        String recvMessage = null;
        try
        {
            StringBuffer url = new StringBuffer();
            url.append(connectionURL);
            connectionURL = url.toString();
            logger.info("HttpClick Send Post Request URL: " + connectionURL);
            recvMessage = sendPostRcvUtf8(connectionURL, sendMessage);
        }
        catch (Exception e)
        {
            logger.error("connectionURL:=" + connectionURL + " connection error");
            throw new BusinessException(ErrorEnum.TRANX_CONN);
        }
        return recvMessage;
    }


    public static String sendPostRcvUtf8(String url, String param)
    {
        OutputStream out = null;
        BufferedReader in = null;
        OutputStreamWriter wr = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = conn.getOutputStream();
            wr = new OutputStreamWriter(out, "UTF-8");
            wr.write(param);
            wr.close();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**

     */
    public static BaseBody callSvr(String tranxCode, String requestBodyMessage,String connUrl) throws Exception
    {
        BaseBody responseBody = null;
        try
        {
            requestBodyMessage = requestBodyMessage.replace("null", "\"\"");
            logger.info("PublicClient Send BodyMsg =[" + requestBodyMessage + "]");
            MessageRequest messageRequest = MessageRequest.getDefaultMessageRequest();
            messageRequest.setMsgType(tranxCode);
            messageRequest.setData(requestBodyMessage);
            String requestSeqNo = messageRequest.getRequestId();
            String sendMessage = JSON.toJSONString(messageRequest);

            logger.info("HttpClick MessageRequest RequestSeqNo:[" + requestSeqNo + "] + Send Message:[" + sendMessage + "]");

            String responseMessage = callSvr(sendMessage,connUrl);
            //-decrypt
            logger.info("Http responseMessage " + responseMessage);
            if (!"success".equals(responseMessage))
            {
                throw new BusinessException(ErrorEnum.TRANX_FAIL);
            }

        }
        catch (BusinessException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            logger.error("HttpClick Transcation Unknown Error", e);
            throw new BusinessException(ErrorEnum.TRANX_FAIL);
        }
        finally
        {

        }
        return responseBody;
    }

}
