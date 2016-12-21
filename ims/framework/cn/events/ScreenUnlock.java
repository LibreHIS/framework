package ims.framework.cn.events;

import java.io.Serializable;

public class ScreenUnlock implements Serializable
{
	private static final long serialVersionUID = 1L;

	public ScreenUnlock(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	public String getUsername()
	{
		return username;
	}
	public String getPassword()
	{
		return password;
	}
	
	private String username;
	private String password;
}
