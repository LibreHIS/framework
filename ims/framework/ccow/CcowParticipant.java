package ims.framework.ccow;

public class CcowParticipant
{
	private String ipAddress;
	private String jSessionId;
	private String url;
	private String appName;
	private boolean survey = false;
	private boolean wait = true;
	private int participantCoupon;
	
	
	public boolean isWait()
	{
		return wait;
	}

	public void setWait(boolean wait)
	{
		this.wait = wait;
	}

	public CcowParticipant()
	{
		
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getJSessionId()
	{
		return jSessionId;
	}

	public void setJSessionId(String sessionId)
	{
		jSessionId = sessionId;
	}

	public boolean isSurvey()
	{
		return survey;
	}

	public void setSurvey(boolean survey)
	{
		this.survey = survey;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public int getParticipantCoupon()
	{
		return participantCoupon;
	}

	public void setParticipantCoupon(int coupon)
	{
		this.participantCoupon = coupon;
	}
	
}
