package com.dmz.yzt.controller;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 接口调用客户端（模拟行方调用小贷）
 */
public class TestClientWithSpring {
	
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String REQ_CHANNEL_NO = "hongta";
	public static final String RSP_CHANNEL_NO = "loan";
	public static final String API_400010 = "400010";
	public static final String ESG_HOST = "114.141.178.28:20080";
	
	public static final String URL_OAUTH= "/oauth/oauth2/access_token?client_id=P_COOPBANK_DMZ_C&grant_type=client_credentials&client_secret=CvTY654g";
	public static final String URL_OPEN= "/open/appsvr/credoo/apiTest";
	public static final String BANK= "hongta";


	public void test400010() throws Exception{
		
		//步骤一：获取token：
        String token = accessToken(ESG_HOST + URL_OAUTH);
			
		//步骤二：调用http请求：
		StringBuffer restUrl = new StringBuffer(ESG_HOST)
	       .append(URL_OPEN)
	       .append("?access_token=").append(token)
	       .append("&request_id=").append(System.currentTimeMillis())
	       .append("&request_sys=")
	       .append(BANK);
		
		
		//1、根据详细接口文档，生成要发送的报文体（json字符串格式）data。
		JSONObject data = new JSONObject();
		data.put("apply_no", "x1aw3d12r322212d2312");//小贷贷款标的号
		data.put("bank_apply_no", "MT170226153501");//行方贷款号
		data.put("loan_time", "20170724121212");//放款时间
		data.put("loan_amount", "100");//实际放款金额
		data.put("state", "1");//放款结果
		data.put("message", "");//放款失败原因
		data.put("fee_amount", "10");//一次性手续费
		data.put("trans_req_no", "123");//壹账通企业网银交易流水号
		
		String infoContentJsonStr = data.toJSONString();
		
		//2、针对data明文做md5摘要，用摘要结果设置sign签名字段。
		String sign = null;
		try {
			sign = md5(infoContentJsonStr, Charset.forName(CHARSET_UTF8));
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		}
		
		//3、针对data进行des加密再base64编码设置。
		String infoContentData = null;
		try {
			infoContentData = encrypt(infoContentJsonStr);
		} catch (Exception e) {
			System.err.println("des加密失败：" + e.getMessage());
		}
		
		//4、补充报文头相关信息，生成报文（json字符串格式）,并base64编码。
		JSONObject jso = new JSONObject();
		jso.put("api_id", API_400010);
		jso.put("trans_no", "shandong400010201707241001");
		jso.put("req_channel_no", REQ_CHANNEL_NO);
		jso.put("rsp_channel_no", RSP_CHANNEL_NO);
		jso.put("sign", sign);
		jso.put("info_content", infoContentData);
		String contentData = encode(jso.toJSONString());

		//5、把4中字符串设置在CONTENT_DATA参数值中。
		JSONObject param= new JSONObject();
		param.put("CONTENT_DATA", contentData);
		System.out.println("请求体：" + param.toJSONString());
		
		//6、参考【使用步骤】，调用服务端服务接口。
		String result = restPost(restUrl.toString(), param.toString());
		System.out.println(result.toString());
	}
	
	/**
	 * 获取token
	 * @param url
	 * @return
	 */
    public String accessToken(String url) {
    	String token = "";
    	System.out.println("[getAccessToken.url]:" + url);
    	
        RestTemplate restTemplate = getCustmRestTemplate();
        String tokenResult = restTemplate.getForObject(url, String.class);
    	System.out.println("[getAccessToken.tokenResult]:" + tokenResult);
    	
    	try{
    		JSONObject data = JSON.parseObject(tokenResult).getJSONObject("data");
    		if(data != null){
    			token = data.getString("access_token");
    		}
    	}catch(Exception e){
        	System.err.println(e.getMessage());
    	}
    	
        return token;
    }
    
    /**
     * 调用ESG平台注册的服务
     * @param url
     * @param param
     * @return
     */
    public String restPost(String url, String param){
    	return restPost(url, param, CHARSET_UTF8);
    }
    
    /**
     * 调用ESG平台注册的服务
     * @param url
     * @param param
     * @param charset
     * @return
     */
    public String restPost(String url, String param, String charset){
    	
    	System.out.println("[restPost.url]:" + url);
    	System.out.println("[restPost.param]:" + param);
    	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName(charset)));
		headers.setAccept(Arrays.asList(new MediaType[] { new MediaType("application", "json", Charset.forName(charset)) }));
		HttpEntity<String> requestEntity = new HttpEntity<String>(param, headers);
		return tryPost(url, requestEntity, 1);
	}
    
    private String tryPost(String url, HttpEntity<String> requestEntity, int retryTimes){
    	RestTemplate restTemplate = getCustmRestTemplate();

    	ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    	String response = exchange.getBody();
    	System.out.println("[restPost.result]:" + response);
    	
    	String retCode = "";
    	if (StringUtils.isNotBlank(response)) {
    		if(response.startsWith("{")){
    			JSONObject result = JSON.parseObject(response);
    			retCode = result.getString("ret");
    		}
            
            if (StringUtils.isNotBlank(retCode) && !"0".equals(retCode)){
            	if(retrySet.contains(retCode)) {
                // 重新发送
	                if (retryTimes <= RETRY_TIMES_MAX) {
	                    System.out.println("[post]:" + "[interface call error] - 通过esg调用接口调用失败,response:"
	                            + response + "正在第" + retryTimes + "次重试");
	                    
	                    retryTimes++;
	                    try {
	                        Thread.sleep(1000);
	                    } catch (InterruptedException e) {
	                        System.err.println("当前线程被关闭");
	                    }
	                    
	                    return tryPost(url, requestEntity, retryTimes);
	                } else {
	                    System.out.println("[post]:" + "[interface call error] - 通过esg调用接口调用失败,response:" + response);
	                }
	            } else {
                    System.out.println("[post]:" + "[interface call error] - 通过esg调用接口调用失败,response:" + response);
	            }
	        } else {
	        	// 正常返回
	        	return response;
	        }
    	}
    	
    	return "";
    }
    
    /*
     * 返回码 描述 排错方法 0 正确返回 N/A 13000 未知错误 请联系我们 13001 请求权限资源失败
     * 无法调用access_token验证服务，请联系我们 13002 非法的access_token
     * access_token值错误，请检查access_token 13003 请求openapi资源失败 无法调用open
     * api资源服务，请联系我们 13004 您的应用没有可调用的open api资源 请申请调用权限后再次调用 13005
     * access_token为空 请检查调用open api服务的输入参数access_token值 13006 url地址受限 没有访问对应open
     * api的权限，请检查调用方式(GET/POST/PUT/DELETE/...)以及是否有调用权限 13007
     * access_token对应的环境不匹配 请检查您的oauth地址与open api地址是否匹配 13008 请求open api遭遇未知错误
     * 请联系我们 13009 无法与open api服务建立连接 请检查是否服务运行正常 13010 open api调用发生异常 请检查您调用open
     * api的输入参数 13011 open api服务发生异常 请检查是否服务是否异常 13012 已失效的access_token
     * 请重新获取access_token 13013 连接池资源繁忙 请稍候重试 13014 未知主机,open api服务网络链路不通
     * 请添加DNS域名解析或者联系我们 13015 连接拒绝,open api服务不可达 请联系服务方是否需要相关授权 13016 open
     * api服务响应超时 请检查是否服务运行正常 13017 SSL证书错误 请导入正确的SSL证书 13018 open api服务连接超时
     * 请检查是否服务运行正常 13023 open api需要经过加密调用 请下载sdk进行加密调用 13024 加密open api url
     * 格式不正确 请通过使用我们提供的sdk来调用 13025 open 解密url异常 请检查sdk版本是否正确 13026 请求资源忙
     * 已经达到服务承载上限，联系服务提供方 13027 服务地址格式不正确 请检查录入到esg-admin的负载地址格式是否正确
     */

    private static String ESG_SERVIER_TIMEOUT = "13016";

    private static String ESG_SERVER_BUSY = "13026";

    private static String ESG_CONNECTION_SERVER_ERROR = "13009";

    private static String ESG_CONNECTION_ERROR = "13011";

    private static String ESG_TOKEN_TIMEOUT = "13012";

    private static String ESG_CONNECTION_POOL_BUSY = "13013";

    private static String ESG_SOCKET_TIMEOUT = "13018";

    private static String ESG_SOCKET_CANNOT_CONNECT = "13014";

    private static Set<String> retrySet = new HashSet<String>();
    static {
        retrySet.add(ESG_SERVIER_TIMEOUT);
        retrySet.add(ESG_SERVER_BUSY);
        retrySet.add(ESG_CONNECTION_SERVER_ERROR);
        retrySet.add(ESG_CONNECTION_ERROR);
        retrySet.add(ESG_TOKEN_TIMEOUT);
        retrySet.add(ESG_CONNECTION_POOL_BUSY);
        retrySet.add(ESG_SOCKET_TIMEOUT);
        retrySet.add(ESG_SOCKET_CANNOT_CONNECT);
    }

    public static int RETRY_TIMES_MAX = 5;
    
    
    public RestTemplate getCustmRestTemplate(){
    	
    	DefaultHttpRequestRetryHandler rrh = new DefaultHttpRequestRetryHandler(3,true);
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(5000).build();
    			CloseableHttpClient client = HttpClients.custom()  
    	        .setRetryHandler(rrh).setDefaultRequestConfig(requestConfig)
    	        .build();  
    	HttpComponentsClientHttpRequestFactory hrf = new HttpComponentsClientHttpRequestFactory(client);
    	RestTemplate restTemplate = new RestTemplate(hrf);
    	return restTemplate;
    }
    

    /**
     * DesedeCrypt
     */
    private static final String keyStorePassword = "paic1234";

    private static final String JECKS = "test.jceks";
    
    private SecretKey secretKey;

    private String keyAlias = "DESede.encryptKey";
    private String keyStoreType = "jceks";
    private String algorithm = "DESede/ECB/PKCS5Padding";
	
    public String encrypt(String data) throws Exception {
        Charset encoding = Charset.forName(CHARSET_UTF8);
        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKey key = loadSecretKeyFromKeyStore();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrptBytes = cipher.doFinal(data.getBytes(encoding));
        byte[] base64Bytes = encode(encrptBytes);
        
        return new String(base64Bytes, encoding);
    }
    
    private SecretKey loadSecretKeyFromKeyStore() throws Exception {
        if (secretKey != null) {
            return secretKey;
        }
        InputStream is = null;
        try {
        	is = TestClientWithSpring.class.getClassLoader().getResourceAsStream(JECKS);
            KeyStore jceks = KeyStore.getInstance(keyStoreType);
            jceks.load(is, keyStorePassword.toCharArray());
            KeyStore.SecretKeyEntry encryptKey = (KeyStore.SecretKeyEntry) jceks.getEntry(keyAlias,
                    new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));
            secretKey = encryptKey.getSecretKey();

            System.out.println("Secret key loaded from local keystore successfully.");
        } catch (Exception e) {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                	System.err.println(ex.getMessage());
                }
            }
            throw e;
        }
        return secretKey;
    }
    
    /**
     * Base64
     */
    public static String encode(String data){
        return encode(data, Charset.forName("UTF-8"));
    }

    public static String encode(String data, Charset encoding) {
        byte[] binaryData = data.getBytes(encoding);
        byte[] base64 = encode(binaryData);
        return new String(base64, encoding);
    }

    public static byte[] encode(byte[] data){
        return Base64.encodeBase64(data, true);
    }

    //第一次解密用
    public static String decode(String base64) {
        return decode(base64, Charset.forName("UTF-8"));
    }

    public static String decode(String base64, Charset encoding) {
        byte[] binaryData = base64.getBytes(encoding);
        byte[] data = decode(binaryData);
        return new String(data, encoding);
    }

    public static byte[] decode(byte[] base64) {
        return Base64.decodeBase64(base64);
    }
    
    //第二次解密用
    public String decrypt(String data) throws Exception {
        Charset encoding = Charset.forName(CHARSET_UTF8);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, loadSecretKeyFromKeyStore());
        return new String(cipher.doFinal(decode(data.getBytes(encoding))), encoding);
	}
    
    /**
     * MD5
     */
    public String md5(String data, Charset encoding) throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(data.getBytes(encoding));
    }
    
    public static void main(String[] args) {
    	TestClientWithSpring ts = new TestClientWithSpring();
    	try {
			String sign = ts.md5(null, Charset.forName(CHARSET_UTF8));
			System.out.println(sign);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		TestClientWithSpring ts = new TestClientWithSpring();
//		try {
//			ts.test400010();
//			//
//			String result = TestClientWithSpring.decode("eyJpbmZvX2NvbnRlbnQiOiJXUGxOdTBKbjlxa0VGMUp5cVk1YjhFTUtvZk9MbXFJQnFrR2xyYU0v"
//						+"emdIeG0ycDdVaUtJd3dRWFVuS3BqbHZ3WUowdkhNTjJXYW94XHJcbjdFZWpiRE9jN21ycnUxdE1j"
//						+"TC8xcHZCTUlvcXh6MlY0OFRZNkx3amdoTDlHeG1wVHAxRXBFaTZuZTdNK2RRc1FMOUJ3N0gxM29J"
//						+"bG5cclxuaDZUbGFSVHl5V29BM0pWYWNyNTFNTkUyb2pqakszWktHU0V3ZnpvTXNCN1IzOWFlU1Vp"
//						+"eUh5S3BlK1pBTWhIQUhsRlozYndybUpwaVxyXG5CQ0RVNXQwYXY4bnppMkNGNEE9PVxyXG4iLCJz"
//						+"aWduIjoiZDY2YjBhZGZjNmI5MTg3YmM4ZTI2OTZmNjViYjk5YmQiLCJyc3BfY2hhbm5lbF9ubyI6"
//						+"ImxvYW4iLCJhcGlfaWQiOiI0MDAwMTAiLCJ0cmFuc19ubyI6InNoYW5kb25nNDAwMDEwMjAxNzA3"
//						+"MjQxMDAxIiwicmVxX2NoYW5uZWxfbm8iOiJzaGFuZG9uZyJ9");
//			System.out.println("5: " + result);	
//			
//			JSONObject contentData = JSON.parseObject(result);
//			
//			String info = contentData.getString("info_content");
//			
//			System.out.println("6:  " + ts.decrypt(info));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
}