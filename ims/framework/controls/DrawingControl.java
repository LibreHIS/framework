package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.DrawingControlNote;
import ims.framework.delegates.DrawingControlShapeRemoved;
import ims.framework.delegates.DrawingControlShapeEdited;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

abstract public class DrawingControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void setImage(Image image);
	
	abstract public void addBrush(int id, String caption, Image image);
	abstract public void addBrush(int id, String caption, Image image, boolean multipleMarkings);
	abstract public void addBrush(int id, String caption, Image image, String tooltip);
	abstract public void addBrush(int id, String caption, Image image, boolean multipleMarkings, String tooltip);
	
	abstract public void addBrush(int id, String caption, Color color);
	abstract public void addBrush(int id, String caption, Color color, boolean multipleMarkings);
	abstract public void addBrush(int id, String caption, Color color, String tooltip);
	abstract public void addBrush(int id, String caption, Color color, boolean multipleMarkings, String tooltip);
	
	abstract public void clearBrushes();
	
	abstract public void setAreas(DrawingControlGroup root);
	abstract public DrawingControlGroup getAreas();
	
	abstract public void addShape(DrawingControlShape shape);
	abstract public ArrayList getShapes();
	abstract public void clearShapes();
	
	public abstract void setPrintTitle(String printTitle);
	public abstract void setPrintSubTitle(String printSubTitle);
	
	abstract public String getGroupOrAreaName(int targetID);
	abstract public boolean isGroup(int targetID);

	public void setDrawingControlNoteEvent(DrawingControlNote delegate)
	{
		this.drawingControlNoteDelegate = delegate;        
	}
	public void setDrawingControlShapeRemovedEvent(DrawingControlShapeRemoved delegate)
	{
		this.drawingControlShapeRemovedDelegate = delegate;        
	}
	public void setDrawingControlShapeEditedEvent(DrawingControlShapeEdited delegate)
	{
		this.drawingControlShapeEditedDelegate = delegate;        
	}
	
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
			this.readOnly = value;
		else
			super.flagIlegalControlModeChange("ReadOnly", value);
	}	
	protected void free()
	{
		super.free();
		
		this.drawingControlNoteDelegate = null;
		this.drawingControlShapeRemovedDelegate = null;
		this.drawingControlShapeEditedDelegate = null;
	}    
	
	protected DrawingControlNote drawingControlNoteDelegate = null;
	protected DrawingControlShapeRemoved drawingControlShapeRemovedDelegate = null;
	protected DrawingControlShapeEdited drawingControlShapeEditedDelegate = null;	
	protected boolean readOnly = true;
}
