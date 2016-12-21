package ims.framework.controls;

public class GaugeStyle
{
	private int id;
	private String name;
	
	public static GaugeStyle BASIC = new GaugeStyle(1, "BasicGauge");
	
	public static GaugeStyle SEMICIRCLE_TOP = new GaugeStyle(2, "SemiCircleTop");
	public static GaugeStyle SEMICIRCLE_RIGHT = new GaugeStyle(3, "SemiCircleRight");
	public static GaugeStyle SEMICIRCLE_LEFT = new GaugeStyle(4, "SemiCircleLeft");
	public static GaugeStyle SEMICIRCLE_BOTTOM = new GaugeStyle(5, "SemiCircleBottom");
	
	private GaugeStyle(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return name;
	}	
}
