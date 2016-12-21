package ims.framework.controls;

import ims.framework.Control;

import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

public abstract class GaugeControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected String caption;
	protected String unitsSuffix;
	protected float minimumValue;
	protected float maximumValue;
	protected GaugeStyle style; 
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, String unitsSuffix, float minimumValue, float maximumValue, GaugeStyle style)
	{
		setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		
		this.caption = caption;
		this.unitsSuffix = unitsSuffix;
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.style = style;
	}
	protected void free()
	{
		super.free();
	}	
	
	public abstract String getCaption();
	public abstract void setCaption(String caption);
	
	public abstract String getUnitsSuffix();
	public abstract void setUnitsSuffix(String unitsSuffix);
	
	public abstract float getMinimumValue();
	public abstract void setMinimumValue(float minimumValue);
	
	public abstract float getMaximumValue();
	public abstract void setMaximumValue(float maximumValue);	
	
	public abstract float getValue();
	public abstract void setValue(float value);
	
	public abstract GaugeStyle getStyle();
	public abstract void setStyle(GaugeStyle style);
	
	public abstract void addArea(GaugeArea area);
	public abstract void clearAreas();
}
