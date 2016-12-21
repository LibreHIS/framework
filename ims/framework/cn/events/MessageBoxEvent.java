package ims.framework.cn.events;

import ims.framework.enumerations.DialogResult;

import java.io.Serializable;

public class MessageBoxEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int id;
	private DialogResult result = DialogResult.OK;

	public MessageBoxEvent(int id, String button)
	{	
		this.id = id;
		
		if(button.toLowerCase().equals("no"))
			result = DialogResult.NO;
		else if(button.toLowerCase().equals("yes"))
			result = DialogResult.YES;
		else if(button.toLowerCase().equals("ok"))
			result = DialogResult.OK;
		else if(button.toLowerCase().equals("cancel"))
			result = DialogResult.CANCEL;
		else if(button.toLowerCase().equals("retry"))
			result = DialogResult.RETRY;
		else if(button.toLowerCase().equals("abort"))
			result = DialogResult.ABORT;
		else if(button.toLowerCase().equals("confirm"))
			result = DialogResult.CONFIRM;
	}
	public int getID()
	{
		return this.id;
	}
	public DialogResult getResult()
	{
		return result;
	}
}
