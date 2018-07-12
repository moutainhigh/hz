
package com.dmz.yzt.model;


import com.alibaba.fastjson.annotation.JSONField;
import com.dmz.yzt.util.GUIDProvider;
import org.springframework.context.annotation.Bean;

/** 
 * @author lixh
 * @Descption: 
 * @ModificationHistory  
 * Who          When             What 
 * ----------   -------------    -----------------------------------
 * lixh         2018-02-06       create   
 */
public class MessageRequest implements Digest
{

	@JSONField(ordinal = 0)
	private String RequestId;
	@JSONField(ordinal = 1)
	private String msgType;
	@JSONField(ordinal = 2)
	private String bankCode;
	@JSONField(ordinal = 3)
	private String data;

	public MessageRequest()
	{
	}

	public static MessageRequest getDefaultMessageRequest() throws Exception
	{
		MessageRequest request = new MessageRequest();
		request.setRequestId(GUIDProvider.getGUID());
		request.setBankCode("HXB");
		return request;
	}

	public String getRequestId() {
		return RequestId;
	}

	public void setRequestId(String requestId) {
		RequestId = requestId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "MessageRequest [RequestId=" + RequestId + ", msgType=" + msgType + ", bankCode=" + bankCode + ", data=" + data + "]";
	}

	@Override
	public byte[] getMD5Bytes() {
		// TODO 自动生成的方法存根
		return null;
	}

}
