package ims.framework.controls;

import java.util.Collection;

import ims.framework.Control;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

abstract public class MarkerControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, int source, int formID, Class voType)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.source = source;
		this.formID = formID;
		this.voType = voType;
	}
	protected void free() // free resources
	{
		super.free();
		this.voType = null;
	}
	
	abstract public void addBitmap(MarkerControlBitmap bitmap);
	abstract public void clear();
	abstract public void addMark(MarkerControlMark mark);
	abstract public Collection getMarks(); 

	protected String getBitmapName(MarkerControlBitmap bitmap) // hide implementation details
	{
		return bitmap.getID();
	}
	
	protected int source;
	protected int formID;
	protected Class voType;
}