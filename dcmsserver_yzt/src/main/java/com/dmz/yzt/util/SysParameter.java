package com.dmz.yzt.util;

/** 
 * @author lixh
 * @Descption: 
 * @ModificationHistory  
 * Who          When             What 
 * ----------   -------------    -----------------------------------
 * lixh         2018-02-06       create   
 */
public class SysParameter
{
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	public static final String PIAN_KEY_CODE = "PIAN_AES_KEY";

	public static final String RETURN_CODE_SUCCESS = "S";
	public static final String RETURN_CODE_SUCCESS_DESC = Native2AsciiUtils.ascii2Native("\u4ea4\u6613\u6210\u529f");

	public static final String RETURN_CODE_SERIA_REPEAT = "R";
	public static final String RETURN_CODE_SERIA_REPEAT_DESC = Native2AsciiUtils.ascii2Native("\u63a5\u53e3\u5e8f\u5217\u53f7\u91cd\u590d");

	public static final String RETURN_CODE_CRYPT_ERROR = "C";
	public static final String RETURN_CODE_CRYPT_ERROR_DESC = Native2AsciiUtils.ascii2Native("\u62a5\u6587\u89e3\u5bc6\u5f02\u5e38");

	public static final String RETURN_CODE_SIGN_CHECKED = "U";
	public static final String RETURN_CODE_SIGN_CHECKED_DESC = Native2AsciiUtils.ascii2Native("\u7b7e\u540d\u6821\u9a8c\u672a\u901a\u8fc7");

	public static final String RETURN_CODE_TRANX_FAIL = "E";
	public static final String RETURN_CODE_TRANX_FAIL_DESC = Native2AsciiUtils.ascii2Native("\u4ea4\u6613\u5931\u8d25");

	public static final String RETURN_CODE_NON_WORK = "W";
	public static final String RETURN_CODE_NON_WORK_DESC = Native2AsciiUtils.ascii2Native("\u975e\u5de5\u4f5c\u65e5");

	public static final String TRANS_CODE_APPROVERESULT = "ApproveResult";
	public static final String TRANS_CODE_LOANDETAIL = "LoanDetail";
	public static final String APPROVERESULT_S1 = "S1";
	public static final String APPROVERESULT_S2 = "S2";
	public static final String APPROVERESULT_E1 = "E1";
	public static final String APPROVERESULT_E2 = "E2";
	public static final String APPROVERESULT_P = "P";
	public static final String QUD_LAY = "19";

	public static final String ACCT_NO_TYPE_INNER = "01";

	public static final String TRANS_TYPE_CRED = "CRED"; //额度申请
	public static final String TRANS_TYPE_APPLY = "APPLY";//额度动用
	public static final String TRANS_TYPE_LOAN = "LOAN";//额度放款

}
