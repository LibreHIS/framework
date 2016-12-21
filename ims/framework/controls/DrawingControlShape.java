package ims.framework.controls;

import java.io.Serializable;

public class DrawingControlShape implements Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlShape(int index, int targetID, String vml, int brushID)
	{
		this.index = index;
		this.targetID = targetID;
		this.vml = vml;
		this.brushID = brushID;
		this.readOnly = false;
	}
	public DrawingControlShape(int index, int targetID, String vml, int brushID, boolean readOnly)
	{
		this.index = index;
		this.targetID = targetID;
		this.vml = vml;
		this.brushID = brushID;
		this.readOnly = readOnly;
	}
	public DrawingControlShape(int index, int targetID, String vml, int brushID, String tooltip)
	{
		this.index = index;
		this.targetID = targetID;
		this.vml = vml;
		this.brushID = brushID;
		this.readOnly = false;
		this.tooltip = tooltip;
	}
	public DrawingControlShape(int index, int targetID, String vml, int brushID, boolean readOnly, String tooltip)
	{
		this.index = index;
		this.targetID = targetID;
		this.vml = vml;
		this.brushID = brushID;
		this.readOnly = readOnly;
		this.tooltip = tooltip;
	}
	public int getIndex()
	{
		return this.index;
	}
	public int getTargetID()
	{
		return this.targetID;
	}
	public String getVML()
	{
		return this.vml;
	}
	public int getBrushID()
	{
		return this.brushID;
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public void setTooltip(String value)
	{
		this.tooltip = value == null ? "" : value;
	}
	public boolean getReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		this.readOnly = value;
	}
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(obj instanceof DrawingControlShape)
			return this.index == ((DrawingControlShape)obj).index;
		return false;
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	
	private int index;
	private int targetID;
	private String vml;
	private int brushID;
	private boolean readOnly;
	private String tooltip;
}
