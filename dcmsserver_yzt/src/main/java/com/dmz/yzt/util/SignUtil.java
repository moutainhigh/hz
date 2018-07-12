package com.dmz.yzt.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author FANGBINGGANG615 on 2018/3/27.
 * @version 1.0
 * @description
 */
public class SignUtil {
    private static Log log = LogFactory.getLog(SignUtil.class);

    private static BASE64Encoder base64encoder = new BASE64Encoder();
    private static BASE64Decoder base64decoder = new BASE64Decoder();

    public static JSONObject envolopData(JSONObject data, String privateKey) {
        JSONObject param = new JSONObject();
        try {
            String dataStr = base64encoder.encode(data.toJSONString().getBytes("UTF-8"));
            String sign = URLEncoder.encode(rsaSign(privateKey, dataStr), "UTF-8");
            String encodeContent = URLEncoder.encode(dataStr, "UTF-8");
            param.put("sign", sign);
            param.put("msg", encodeContent);
        } catch (Exception e) {
            log.error("sign failed...");
            return data;
        }
        return param;
    }

    public static String rsaSign(String privKey, String dataStr) {
        String plainText = "";
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64decoder.decodeBuffer(privKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");

            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);

            Signature sigEng = Signature.getInstance("SHA1withRSA");
            sigEng.initSign(privateKey);
            sigEng.update(dataStr.getBytes("UTF-8"));
            byte[] signature = sigEng.sign();
            plainText = base64encoder.encodeBuffer(signature);
            plainText = plainText.replaceAll("\r|\n", "");
        } catch (Exception e) {
            log.error("sign failed...");
        }
        return plainText;
    }

    public static JSONObject decryptData(String publicKey, String sign, String msg) {
        String plainText = "";
        try {
            boolean verifyFlag = false;
            sign = URLDecoder.decode(sign, "UTF-8");
            msg = URLDecoder.decode(msg, "UTF-8");
            verifyFlag = rsaVerify(publicKey, sign, msg);
            if (verifyFlag) {
                plainText = new String(base64decoder.decodeBuffer(msg), "UTF-8");
            }
        } catch (Exception e) {
            log.error("unsign failed...");
            return null;
        }
        if ("".equals(plainText)) {
            return null;
        }
        log.info("unsign succeed json=" + plainText);
        return JSONObject.parseObject(plainText);
    }

    private static boolean rsaVerify(String pubKey, String sign, String src) {
        boolean rs = false;
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64decoder.decodeBuffer(pubKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

            Signature sigEng = Signature.getInstance("SHA1withRSA");
            sigEng.initVerify(rsaPubKey);
            sigEng.update(src.getBytes("UTF-8"));
            byte[] signature = base64decoder.decodeBuffer(sign);
            rs = sigEng.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void main(String[] args) {
        String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMEqAY52eFAPM5Mm9o6tbDRNy/vs/KOQY1pZf9m6AsLyvoOv5Afh2YOwIKI4yn+rDrKNtlKb+R6rJ2lddkZxBHAwS2NWXwWVXlZToReiWKoAkTn8tjvyoAlf9bVQVXOwuGORPCTnTXL5WR4ZbqRMLAlGkL76dLF0DGib6IVU4hfnAgMBAAECgYEAgWlmH47pbiGxGP46oz42xxfz+LU+UUrT9V0O1czUOs2sq8c0i79ZHevrASPxLvVoZGGi69eJYG2keufd7LWdFW6RDDl8I6HWv4SNuXErlHIjUzs7NhOVh23sgQMBjQgCMfcBN1azQOjKjJ2arShgNiQgltLKz52AhxJHUu0a96ECQQDijlh93Z0nq6KSWyIZakKAsuIELVDzM/cEWY0T8I0FRbDc9Ukaz8o/N3JupgAe6c2tRvkag+oHCvZVre3VrjrRAkEA2kSwtvRkANceD5HZWFJ8tmcPneofUmd6KVn2vN8zlpvGLUswS4LDV9OXhxT1S9rlT6gC/mU5Nk2Sa0YKrQ9lNwJAMNCs/onGt+qCf/iE82by51oj2vSMsapruhM1k6tMJ2upvSJY2x8Um+RtkqvP8Nk9yO029WChB110UGfGjDkbUQJAfhZ+S0JE3uNmHzJu7wG/v5Mrx1qvO2hBfs/UpGkAITndst6jiIuCkyLNfyBRT7+lBm4VRKZu9Xq8YbH1iuHA4wJACBtbdd5XmyGF+3wQlavZEy3EaBJK8qz2q7czbyBe2C8BN54vk0PIcOJOyLjCE17BA4+GCDBcSAw4Z0b+h41Ngw==";
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBKgGOdnhQDzOTJvaOrWw0Tcv77PyjkGNaWX/ZugLC8r6Dr+QH4dmDsCCiOMp/qw6yjbZSm/keqydpXXZGcQRwMEtjVl8FlV5WU6EXoliqAJE5/LY78qAJX/W1UFVzsLhjkTwk501y+VkeGW6kTCwJRpC++nSxdAxom+iFVOIX5wIDAQAB";


//        String jsonStr = "{\"company\":\"0028\",\"msgType\":\"120050\",\"data\":{\"productCode\":\"CS1001\",\"requestId\":\"9E3D3ABF6E534554\",\"idCardNo\":\"342212198908061258\",\"data\":{\"productCode\":\"CS1001\",\"requestId\":\"9E3D3ABF6E534554\",\"idCardNo\":\"342212198908061258\"},\"array\":[{\"productCode\":\"CS1001\",\"requestId\":\"9E3D3ABF6E534554\",\"idCardNo\":\"342212198908061258\"},{\"productCode\":\"CS1001\",\"requestId\":\"9E3D3ABF6E534554\",\"idCardNo\":\"342212198908061258\"},{\"productCode\":\"CS1001\",\"requestId\":\"9E3D3ABF6E534554\",\"idCardNo\":\"342212198908061258\"}]}}";

        String jsonStr="{\n" +
                "  \"enterpriseId\": \"49e08a28-1506-4148-94ea-04986ab25e7a\",\n" +
                "  \"personInfo\": {\n" +
                "    \"personName\": \"lsr\",\n" +
                "    \"identTypeCode\": 0,\n" +
                "    \"identNo\": \"123456\",\n" +
                "    \"email\": \"lsr@163.com\",\n" +
                "    \"mobilePhone\": \"15183291239\",\n" +
                "    \"address\": \"上海平安\",\n" +
                "    \"authenticationMode\": \"你大爷认证\",\n" +
                "    \"sealData\": \"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\"\n" +
                "  },\n" +
                "  \"contracts\": [\n" +
                "    {\n" +
                "      \"file\": \"contract2\",\n" +
                "      \"contractType\": \"2\",\n" +
                "      \"name\": \"测试1\",\n" +
                "      \"enterpriseSeals\": [\n" +
                "        {\n" +
                "          \"sealId\": \"c3493abf-6cf0-4e65-aae8-fa9eac6fa711\",\n" +
                "          \"location\": {\n" +
                "            \"page\": 1,\n" +
                "            \"x\": \"20\",\n" +
                "            \"y\": \"20\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"personalSealLocation\": {\n" +
                "        \"page\": 1,\n" +
                "        \"x\": \"40\",\n" +
                "        \"y\": \"80\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONObject data = JSONObject.parseObject(jsonStr);

        JSONObject param = SignUtil.envolopData(data, priKey);

        System.out.println(param);

        JSONObject decryptStr = SignUtil.decryptData(pubKey, param.getString("sign"), param.getString("msg"));

        System.out.println(decryptStr);

    }
}
