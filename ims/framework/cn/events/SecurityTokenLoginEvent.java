package ims.framework.cn.events;

import java.io.Serializable;

public class SecurityTokenLoginEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public SecurityTokenLoginEvent(String securityToken)
	{
		this.securityToken = securityToken;		
	}
	public String getSecurityToken()
	{
		return this.securityToken;
	}
	private String securityToken;	
}
