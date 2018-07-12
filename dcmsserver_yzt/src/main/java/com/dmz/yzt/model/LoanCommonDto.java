
package com.dmz.yzt.model;



public class LoanCommonDto
{
	private String projectNo;
	private String branchCode;
	private String channel;
	private String stripline;
	private String loanType;
	private String notifyUrl;

	public String getProjectNo()
	{
		return projectNo;
	}

	public void setProjectNo(String projectNo)
	{
		this.projectNo = projectNo;
	}

	public String getBranchCode()
	{
		return branchCode;
	}

	public void setBranchCode(String branchCode)
	{
		this.branchCode = branchCode;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getStripline()
	{
		return stripline;
	}

	public void setStripline(String stripline)
	{
		this.stripline = stripline;
	}

	/**
	 * @return the loanType
	 */
	public String getLoanType()
	{
		return loanType;
	}

	/**
	 * @param loanType the loanType to set
	 */
	public void setLoanType(String loanType)
	{
		this.loanType = loanType;
	}

	/**
	 * @return the notifyUrl
	 */
	public String getNotifyUrl()
	{
		return notifyUrl;
	}

	/**
	 * @param notifyUrl the notifyUrl to set
	 */
	public void setNotifyUrl(String notifyUrl)
	{
		this.notifyUrl = notifyUrl;
	}

	@Override
	public String toString()
	{
		return "LoanApplDto [projectNo=" + projectNo + ", branchCode=" + branchCode + ", channel=" + channel + ", stripline=" + stripline + "]";
	}

}
