package ims.framework.controls;

import java.io.Serializable;

public class DrawingControlArea implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public DrawingControlArea(int id, String name, String path)
	{
		this.id = id;
		this.name = name;
		this.path = path;
	}
	public int getID()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPath()
	{
		return this.path;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	private int id;
	private String name;
	private String path;	
}
