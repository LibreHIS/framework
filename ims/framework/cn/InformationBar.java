package ims.framework.cn;

import ims.framework.utils.Color;
import ims.framework.utils.StringUtils;

public class InformationBar extends ims.framework.InformationBar
{
	public InformationBar()
	{
		this("NONE");
	}
	public InformationBar(String text)
	{
		this(text, null);
	}
	public InformationBar(String text, Color textColor)
	{
		super.text = text;
		super.textColor = textColor;
	}
	
	public void render(StringBuffer sb)
	{
		sb.append("<patient");
		if(textColor == null)
		{
			sb.append(">");
		}
		else
		{
			sb.append(" textColor=\"");
			sb.append(textColor.toString());
			sb.append("\" >");
		}
		
		if(text != null)
		{
			sb.append(StringUtils.encodeXML(text));
		}
		sb.append("</patient>");
	}
}
