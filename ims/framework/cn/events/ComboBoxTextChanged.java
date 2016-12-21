package ims.framework.cn.events;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public class ComboBoxTextChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
    public ComboBoxTextChanged(int controlID, String text)
	{
		this.controlID = controlID;
		this.text = text;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getText()
	{
		return this.text;
	}
	private int controlID;
	private String text;
}
