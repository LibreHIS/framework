package ims.framework.cn.data;

import java.util.ArrayList;
import java.util.List;

import ims.framework.controls.GaugeArea;
import ims.framework.controls.GaugeStyle;

public class GaugeControlData implements IControlData
{
	private static final long serialVersionUID = 376608968040585607L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}	
	public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}

	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;		
	}
	
		public float getValue()
	{
		return value;
	}
	public void setValue(float value)
	{
		if(this.value != value)
		{
			dataChanged = true;
			this.value = value;
		}
	}
	public float getMinimumValue()
	{
		return minimum;
	}
	public void setMinimumValue(float minimumValue)
	{
		if(this.minimum != minimumValue)
		{
			dataChanged = true;
			this.minimum = minimumValue;
		}
	}
	public float getMaximumValue()
	{
		return maximum;
	}
	public void setMaximumValue(float maximumValue)
	{
		if(this.maximum != maximumValue)
		{
			dataChanged = true;
			this.maximum = maximumValue;
		}
	}
	public String getCaption()
	{
		return caption;
	}
	public void setCaption(String caption)
	{
		if(caption == null)
			caption = "";
		
		if(!this.caption.equals(caption))
		{
			dataChanged = true;
			this.caption = caption;
		}
	}
	public String getUnitsSuffix()
	{
		return unitsSuffix;
	}
	public void setUnitsSuffix(String unitsSuffix)
	{
		if(unitsSuffix == null)
			unitsSuffix = "";
		
		if(!this.unitsSuffix.equals(unitsSuffix))
		{
			dataChanged = true;
			this.unitsSuffix = unitsSuffix;
		}
	}
	public GaugeStyle getStyle()
	{
		return style;
	}
	public void setStyle(GaugeStyle style)
	{
		if(style == null)
			style = GaugeStyle.BASIC;
		
		if(this.style != style)
		{
			dataChanged = true;
			this.style = style;
		}
	}
	public void addArea(GaugeArea area)
	{
		areas.add(area);
		dataChanged = true;
	}
	public void clearAreas()
	{
		if(areas.size() > 0)
		{
			dataChanged = true;
			areas.clear();
		}		
	}
	public List<GaugeArea> getAreas()
	{
		return areas;
	}
	
	public void setDataChanged(boolean dataChanged)
	{
		this.dataChanged = dataChanged;
	}
	public boolean isDataChanged()
	{
		return dataChanged;		
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	
	private float value = 0;
	private float minimum = 0;
	private float maximum = 0;
	private String caption = "";
	private String unitsSuffix = "";
	private GaugeStyle style = GaugeStyle.BASIC; 
	private List<GaugeArea> areas = new ArrayList<GaugeArea>();
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	
	private boolean dataChanged = true;
}
