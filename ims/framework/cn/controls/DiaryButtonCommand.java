package ims.framework.cn.controls;

import java.io.Serializable;

public class DiaryButtonCommand implements Serializable
{
	private static final long serialVersionUID = -1L;
		
	private String text;
	
	public static DiaryButtonCommand FORWARD = new DiaryButtonCommand("forward");
	public static DiaryButtonCommand BACK = new DiaryButtonCommand("back");
	
	private DiaryButtonCommand(String text)
	{
		this.text = text;
	}
	
	public static DiaryButtonCommand parse(String text)
	{
		if(text == null)
			throw new RuntimeException("Invalid diary command");
		
		if(text.equals(FORWARD.text))
			return FORWARD;
		if(text.equals(BACK.text))
			return BACK;
		
		throw new RuntimeException("Unknown diary command");
	}
}
