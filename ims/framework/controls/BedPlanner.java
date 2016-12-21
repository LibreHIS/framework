package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.BedPlannerBedAttachedImageClicked;
import ims.framework.delegates.BedPlannerBedClicked;
import ims.framework.delegates.BedPlannerBedAdded;
import ims.framework.delegates.BedPlannerBedEdited;
import ims.framework.delegates.BedPlannerBedInfo;
import ims.framework.delegates.BedPlannerBedRemoved;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.utils.Image;

abstract public class BedPlanner extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void setPlan(String plan);
	abstract public void addArea(FloorPlannerArea area);
	abstract public void addBedImage(Image image);
	
	abstract public void addBed(Bed bed);
	abstract public ArrayList<Bed> getBeds();
	abstract public void clearBeds();
	
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

	public void setBedClickedEvent(BedPlannerBedClicked delegate)
	{
		this.bedClickedDelegate = delegate;        
	}
	public void setBedAddedEvent(BedPlannerBedAdded delegate)
	{
		this.newBedDelegate = delegate;        
	}
	public void setBedEditedEvent(BedPlannerBedEdited delegate)
	{
		this.editBedDelegate = delegate;        
	}
	public void setBedRemovedEvent(BedPlannerBedRemoved delegate)
	{
		this.removeBedDelegate = delegate;        
	}
	public void setBedInfoEvent(BedPlannerBedInfo delegate)
	{
		this.bedInfoDelegate = delegate;        
	}
	public void setBedAttachedImageClickedEvent(BedPlannerBedAttachedImageClicked delegate)
	{
		this.bedAttachedImageClickedDelegate = delegate;        
	}
	
	public abstract void clear();
	protected void free()
	{
		super.free();
		
		this.bedAttachedImageClickedDelegate = null;
		this.bedClickedDelegate = null;
		this.bedInfoDelegate = null;
		this.newBedDelegate = null;
		this.editBedDelegate = null;
		this.removeBedDelegate = null;		
	}    
	protected BedPlannerBedAttachedImageClicked bedAttachedImageClickedDelegate = null;
	protected BedPlannerBedClicked bedClickedDelegate = null;
	protected BedPlannerBedInfo bedInfoDelegate = null;
	protected BedPlannerBedAdded newBedDelegate = null;
	protected BedPlannerBedEdited editBedDelegate = null;
	protected BedPlannerBedRemoved removeBedDelegate = null;
	protected boolean readOnly = false;
}
