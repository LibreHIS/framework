package ims.framework.utils;

import ims.framework.enumerations.ControlAnchoring;

public class RuntimeAnchoring 
{
	SizeInfo parentDesignSize;
	SizeInfo parentRuntimeSize;
	int controlX;
	int controlY;
	int controlWidth;
	int controlHeight;
	ControlAnchoring controlAnchoring;
	
	public RuntimeAnchoring(SizeInfo parentDesignSize, SizeInfo parentRuntimeSize, int controlX, int controlY, int controlWidth, int controlHeight, ims.framework.enumerations.ControlAnchoring controlAnchoring)
	{
		this.parentDesignSize = parentDesignSize;
		this.parentRuntimeSize = parentRuntimeSize;
		this.controlX = controlX;
		this.controlY = controlY;
		this.controlWidth = controlWidth;
		this.controlHeight = controlHeight;
		this.controlAnchoring = controlAnchoring;
	}
	
	public int getX()
	{
		int result = 0;
		
		if(controlAnchoring.isAnchoredLeft())
			result = controlX;
		else
			result = controlX + (parentRuntimeSize.getWidth() - parentDesignSize.getWidth());
		
		return result > 0 ? result : 0;
	}
	public int getY()
	{
		int result = 0;
		
		if(controlAnchoring.isAnchoredTop())
			result = controlY;
		else
			result = controlY + (parentRuntimeSize.getHeight() - parentDesignSize.getHeight());
		
		return result > 0 ? result : 0;
	}
	public int getWidth()
	{
		int result = 0;
		
		if(!controlAnchoring.isAnchoredLeft() || !controlAnchoring.isAnchoredRight())
			result = controlWidth;
		else
			result = controlWidth + (parentRuntimeSize.getWidth() - parentDesignSize.getWidth());
		
		return result > 0 ? result : 0;
	}
	public int getHeight()
	{
		int result = 0;
		
		if(!controlAnchoring.isAnchoredTop() || !controlAnchoring.isAnchoredBottom())
			result = controlHeight;
		else			
			result = controlHeight + (parentRuntimeSize.getHeight() - parentDesignSize.getHeight());
		
		return result > 0 ? result : 0;
	}
	public SizeInfo getSize()
	{
		return new SizeInfo(getWidth(), getHeight());
	}
}
