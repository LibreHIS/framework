package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

public abstract class GridRowBridge implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected GridRowBridge(GridRow row)
	{
		this.row = row;
	}
	public void setReadOnly(boolean value)
	{
		this.row.setReadOnly(value);
	}
	public boolean isReadOnly()
	{
		return this.row.isReadOnly();
	}
	public void setSelectable(boolean value)
	{
		this.row.setSelectable(value);
	}
	public boolean isSelectable()
	{
		return this.row.isSelectable();
	}
	public void setBackColor(Color value)
	{
		this.row.setBackColor(value);
	}
	public Color getBackColor()
	{
		return this.row.getBackColor();
	}
	public void setTextColor(Color value)
	{
		this.row.setTextColor(value);
	}
	public void setTooltip(String value)
	{
		this.row.setTooltip(value);
	}
	public void setExpanded(boolean value)
	{
		this.row.setExpanded(value);
	}	
	public boolean isExpanded()
	{
		return this.row.isExpanded();
	}
	public void setBold(boolean value)
	{
		this.row.setBold(value);
	}	
	public void setCollapsedImage(Image image)
	{
		this.row.setCollapsedImage(image);
	}
	public void setExpandedImage(Image image)
	{
		this.row.setExpandedImage(image);
	}
	public void setSelectedImage(Image image)
	{
		this.row.setSelectedImage(image);
	}
	public void setIsParentRow(boolean value)
	{
		this.row.setIsParentRow(value);
	}
	public boolean isParentRow()
	{
		return this.row.isParentRow();
	}
	
	protected GridRow row;
} 
