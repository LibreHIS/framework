package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.Click;

public abstract class Link extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
		
		this.clickDelegate = null;
	}
	public abstract void setTooltip(String value);
	public abstract String getText();
	public void setClickEvent(Click delegate)
	{
		this.clickDelegate = delegate;
	}
	protected Click clickDelegate = null;
	protected String tooltip;
}
