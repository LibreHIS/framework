package ims.framework.controls;

import ims.framework.utils.Color;

public class GaugeArea
{
	private float minimum;
	private float maximum;
	private Color color;
	
	public GaugeArea(float minimum, float maximum, Color color)
	{
		this.minimum = minimum;
		this.maximum = maximum;
		this.color = color;		
	}
	
	public float getMinimum()
	{
		return minimum;
	}
	public float getMaximum()
	{
		return maximum;
	}
	public Color getColor()
	{
		return color;
	}
}
