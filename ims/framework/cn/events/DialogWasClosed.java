package ims.framework.cn.events;

import java.io.Serializable;

public class DialogWasClosed implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public DialogWasClosed(int id, boolean byServer)
	{
		this.id = id;
		this.byServer = byServer;
	}
	
	public int getID()
	{
		return id;
	}
	public boolean getByServer()
	{
		return byServer;
	}
	
	private int id;
	private boolean byServer;
}
