package ims.framework.cn.events;

import java.io.Serializable;

public class SelectRoleEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public SelectRoleEvent(int role)
	{
		this.role = role;
	}
	public int getRole()
	{
		return this.role;
	}
	private int role;
}
