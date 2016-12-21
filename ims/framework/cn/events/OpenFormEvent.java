package ims.framework.cn.events;

import ims.framework.interfaces.IFormIdSelection;

import java.io.Serializable;

public class OpenFormEvent implements Serializable, IFormIdSelection
{
	private static final long serialVersionUID = 1L;
	public OpenFormEvent(int formID)
	{
		this.formID = formID;
	}
	public int getFormID()
	{
		return this.formID;
	}
	private int formID;
}
