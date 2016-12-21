package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.FloorPlannerNote;

abstract public class FloorPlanner extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public String getPlan();
    abstract public void setPlan(String value); 
    
    abstract public void clearAreas();
    abstract public ArrayList<FloorPlannerArea> getAreas();
    abstract public void addArea(FloorPlannerArea area);
    abstract public void updateArea(FloorPlannerArea area);
        
    public void setNoteEvent(FloorPlannerNote delegate)
	{
    	this.delegate = delegate;        
    }
    protected void free()
	{
    	super.free();
    	
    	this.delegate = null;
    }    
    
    protected FloorPlannerNote delegate = null;
    
}
