package ims.framework.ccow;

import java.util.HashMap;
import java.util.Map;

public class CommonContext
{
	private String ipAddress;
	private int contextCoupon = 0;
	private Map<Integer, CcowParticipant> participants = new HashMap<Integer, CcowParticipant>();
	
	public CommonContext(String ip)
	{
		this.ipAddress = ip;
	}
	
	public int getContextCoupon()
	{
		return contextCoupon;
	}
	public void setContextCoupon(int contextCoupon)
	{
		this.contextCoupon = contextCoupon;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	
	public CcowParticipant getParticipant(Integer coupon)
	{
		return participants.get(coupon);
	}
	
	public CcowParticipant getParticipant(String cpUrl)
	{
		return participants.get(new Integer(cpUrl.hashCode()));
	}
	
	public synchronized CcowParticipant addParticipant(String ipAddress, String jSessionId, String url)
	{
		Integer key = new Integer(url.hashCode());
		CcowParticipant ret = new CcowParticipant();
		ret.setParticipantCoupon(url.hashCode());
		ret.setIpAddress(ipAddress);
		ret.setJSessionId(jSessionId);
		ret.setUrl(url);

		participants.put(key, ret);		
		return ret;
	}
	
	
}
