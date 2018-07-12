
package com.dmz.yzt.model;



import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/** 
 * @author lixh
 * @Descption: 
 * @ModificationHistory  
 * Who          When             What 
 * ----------   -------------    -----------------------------------
 * lixh         2018-02-06       create   
 */
public class MessageResponse implements Digest
{
	@JSONField(ordinal = 0)
	private String rtnCode;
	@JSONField(ordinal = 1)
	private String rtnMsg;
	@JSONField(ordinal = 2)
	private JSONObject data;

	public MessageResponse()
	{
	}

	@Override
	@JSONField(serialize = false)
	public byte[] getMD5Bytes()
	{
		return null;
	}

	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}
	

}
