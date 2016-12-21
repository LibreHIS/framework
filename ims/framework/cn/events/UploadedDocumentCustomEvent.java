package ims.framework.cn.events;

import ims.framework.CustomEvent;

public class UploadedDocumentCustomEvent extends CustomEvent
{
	private static final long serialVersionUID = 1L;

	public UploadedDocumentCustomEvent(String fileName, String absolutePath)
	{
		this.fileName = fileName;
		this.absolutePath = absolutePath;
	}
	public String getFileName()
	{
		return this.fileName;
	}
	public String getAbsolutePath()
	{
		return absolutePath;
	}
	
	private String fileName;
	private String absolutePath;
}
