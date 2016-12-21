package ims.framework.cn.events;

import java.io.Serializable;

public class NewPasswordEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public NewPasswordEvent(boolean cancelled, String newPassword)
	{
		this.cancelled = cancelled;
		this.newPassword = newPassword;
	}
	public boolean isCancelled()
	{
		return this.cancelled;
	}
	public String getNewPassword()
	{
		return this.newPassword;
	}
	private boolean cancelled;
	private String newPassword;
}
