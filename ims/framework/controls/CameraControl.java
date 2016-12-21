package ims.framework.controls;

import ims.framework.Control;

import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public abstract class CameraControl extends Control
{
	private static final long serialVersionUID = 1L;
		
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
	}
	protected void free()
	{
		super.free();
		
		this.valueChangedDelegate = null;
	}
	
	public abstract String getImageString();
	
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	
	protected ValueChanged valueChangedDelegate = null;
}
