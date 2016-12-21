package ims.framework.cn;

import ims.framework.MessageButtons;
import ims.framework.MessageDefaultButton;
import ims.framework.MessageIcon;

public class MessageBox extends ims.framework.MessageBox
{
	public MessageBox(String text)
	{
		super(text);
	}
	public MessageBox(String text, String title)
	{
		super(text, title);
	}
	public MessageBox(int id, String text, String title, MessageButtons buttons)
	{
		super(id, text, title, buttons);
	}
	public MessageBox(int id, String text, String title, MessageButtons buttons, MessageIcon icon)
	{
		super(id, text, title, buttons, icon);
	}
	public MessageBox(int id, String text, String title, MessageButtons buttons, MessageIcon icon, MessageDefaultButton defaultButton)
	{
		super(id, text, title, buttons, icon, defaultButton);
	}
	
	public void render(StringBuffer sb)
	{
		sb.append("<messagebox ");
		
		if(hasId)
		{
			sb.append("id=\"");
			sb.append(id);
			sb.append("\" ");
		}
		
		sb.append("caption=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(title));
		sb.append("\" ");		
		
		sb.append("buttons=\"");
		sb.append(buttons);
		sb.append("\" ");		
		
		sb.append("img=\"");
		sb.append(icon);
		sb.append("\" ");
		
		if(defaultButton != null)
		{
			sb.append("focus=\"");
			sb.append(getDefaultButton());
			sb.append("\" ");
		}
		
		sb.append(">");				
		sb.append(ims.framework.utils.StringUtils.encodeXML(text));
		sb.append("</messagebox>");
	}
	private String getDefaultButton()
	{
		if(buttons.equals(MessageButtons.OK))
		{
			return "OK";
		}
		else if(buttons.equals(MessageButtons.OKCANCEL))
		{
			if(defaultButton.equals(MessageDefaultButton.BUTTON1))
				return "OK";
			
			return "Cancel";
		}
		else if(buttons.equals(MessageButtons.YESNO))
		{
			if(defaultButton.equals(MessageDefaultButton.BUTTON1))
				return "Yes";
			
			return "No";
		}
		else if(buttons.equals(MessageButtons.YESNOCANCEL))
		{
			if(defaultButton.equals(MessageDefaultButton.BUTTON1))
				return "Yes";
			else if(defaultButton.equals(MessageDefaultButton.BUTTON2))
				return "No";
			
			return "Cancel";
		}
		else if(buttons.equals(MessageButtons.CONFIRMCANCEL))
		{
			if(defaultButton.equals(MessageDefaultButton.BUTTON1))
				return "Confirm";
			
			return "Cancel";
		}
		
		throw new RuntimeException("Default button case not supported");
	}
}
