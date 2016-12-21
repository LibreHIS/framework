package ims.framework.cn.events;

import java.io.Serializable;

public class LoginEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public LoginEvent(String name, String pass)
	{
		this.name = name;
		this.pass = pass;
	}
	public String getName()
	{
		return this.name;
	}
	public String getPass()
	{
		return this.pass;
	}
	private String name;
	private String pass;
}
