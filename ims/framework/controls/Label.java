package ims.framework.controls;

import ims.framework.Control;
import ims.framework.utils.Color;

public abstract class Label extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public String getValue();
	abstract public void setValue(String value);
	abstract public void setTextColor(Color color);
	public abstract void setTooltip(String value);	
}
