package ims.framework.controls;

import ims.framework.Control;

import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public abstract class FormDesignerControl extends Control
{
	private static final long serialVersionUID = 1L;
		
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
	}
	protected void free()
	{
		super.free();
	}
		
	public abstract String getFormXml();
	public abstract void setFormXml(String formXml);
}
