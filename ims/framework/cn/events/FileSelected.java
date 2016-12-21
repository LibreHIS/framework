package ims.framework.cn.events;

import java.io.Serializable;

public class FileSelected implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public FileSelected(int controlID, String fileName)
	{
		this.controlID = controlID;
		this.fileName = fileName;	
	}
	
	public int getControlID()
	{
		return this.controlID;
	}
	public String getFileName()
	{
		return this.fileName;
	}
	private int controlID;
	private String fileName;	
}
