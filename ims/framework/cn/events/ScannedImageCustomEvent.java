package ims.framework.cn.events;

import ims.framework.CustomEvent;

public class ScannedImageCustomEvent extends CustomEvent
{
	private static final long serialVersionUID = 1L;

	public ScannedImageCustomEvent(String image, String fileName)
	{
		this.image = image;
		this.fileName = fileName;	
	}
	public String getImage()
	{
		return this.image;
	}
	public String getFileName()
	{
		return this.fileName;
	}

	private String image;
	private String fileName;	
}
