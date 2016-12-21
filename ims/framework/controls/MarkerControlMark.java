package ims.framework.controls;

import java.io.Serializable;

abstract public class MarkerControlMark implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected MarkerControlMark(int id, Object info) // Redirect
	{
		this.id = id;
		this.info = info;
	}
	protected MarkerControlMark(int id, MarkerControlBitmap image, int x, int y, int area, Object info) // Redirect
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.area = area;
		this.image = image;
		this.info = info;
	}
	public int getID()
	{
		return this.id;
	}
	public void setArea(int area)
	{
		this.area = area;
	}

	public void setImage(MarkerControlBitmap value)
	{
		this.image = value;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	public int getArea()
	{
		return this.area;
	}

	public MarkerControlBitmap getImage()
	{
		return this.image;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}
	public Object getInfo()
	{
		return this.info;
	}
	public void setInfo(Object info)
	{
		this.info = info;
	}
	private int id = -1;
	private MarkerControlBitmap image = null;
	private int area = -1;
	private int x = -1;
	private int y = -1;
	private Object info = null; // VO that holds additional info

}