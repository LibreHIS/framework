package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.GaugeControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.GaugeArea;
import ims.framework.controls.GaugeStyle;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;

public class GaugeControl extends ims.framework.controls.GaugeControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String caption, String unitsSuffix, int minimumValue, int maximumValue, GaugeStyle style)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, caption, unitsSuffix, minimumValue, maximumValue, style);
	}	
	protected void free()
	{
		super.free();		
		this.data = null;		
	}	
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (GaugeControlData)data;
		
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();	
		
		if(isNew)
		{
			this.data.setCaption(caption);
			this.data.setUnitsSuffix(unitsSuffix);
			this.data.setMinimumValue(minimumValue);
			this.data.setMaximumValue(maximumValue);
			this.data.setStyle(style);
		}
		else
		{
			super.caption = this.data.getCaption();
			super.unitsSuffix = this.data.getUnitsSuffix();
			super.minimumValue = this.data.getMinimumValue();
			super.maximumValue = this.data.getMaximumValue();
			super.style = this.data.getStyle();
		}
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<flash id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);		
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" src=\"");
		sb.append("flash/gauge.swf");
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<flash id=\"a");
		sb.append(super.id);
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledChanged(false);
				}
			}
				
			sb.append("\" >");
			
			if(this.data.isDataChanged())
			{
				sb.append("<![CDATA[");
				
				sb.append("<gaugedata>");
				
				sb.append("<caption>");
				sb.append(StringUtils.encodeXML(this.data.getCaption()));
				sb.append("</caption>");
				
				sb.append("<unitsSuffix>");
				sb.append(StringUtils.encodeXML(this.data.getUnitsSuffix()));
				sb.append("</unitsSuffix>");
				
				sb.append("<minimum>");
				sb.append(this.data.getMinimumValue());
				sb.append("</minimum>");
				
				sb.append("<maximum>");
				sb.append(this.data.getMaximumValue());
				sb.append("</maximum>");
				
				sb.append("<value>");
				sb.append(this.data.getValue());
				sb.append("</value>");
				
				sb.append("<scaledivisor>");
				sb.append((this.data.getMaximumValue() - this.data.getMinimumValue()) / 4);
				sb.append("</scaledivisor>");
				
				sb.append("<scalemarkdivisor>");
				sb.append("3");
				sb.append("</scalemarkdivisor>");
				
				sb.append("<type>");
				sb.append(super.style.toString());
				sb.append("</type>");
				
				if(data.getAreas().size() > 0)
				{
					sb.append("<areas>");
					
					for(int x = 0; x < this.data.getAreas().size(); x++)
					{
						GaugeArea area = this.data.getAreas().get(x);
						sb.append("<area minimum=\"");
						sb.append(area.getMinimum());
						sb.append("\" maximum=\"");
						sb.append(area.getMaximum());
						sb.append("\" color=\"");
						sb.append(area.getColor().toString());
						sb.append("\" />");
					}
					
					sb.append("</areas>");
				}
				
				sb.append("</gaugedata>");
				
				sb.append("]]>");
				
				this.data.setDataChanged(false);
			}
			
			sb.append("</flash>");
		}
		else
		{
			sb.append("\" />");
		}	
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(data.isEnabledChanged())
				return true;
			if(data.isDataChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private GaugeControlData data;

	@Override
	public String getCaption()
	{
		return super.caption;
	}
	@Override
	public float getMaximumValue()
	{
		return super.maximumValue;
	}
	@Override
	public float getMinimumValue()
	{
		return super.minimumValue;
	}
	@Override
	public String getUnitsSuffix()
	{
		return super.unitsSuffix;
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	@Override
	public void setCaption(String caption)
	{
		super.caption = caption;
		this.data.setCaption(caption);
	}
	@Override
	public void setMaximumValue(float maximumValue)
	{
		super.maximumValue = maximumValue;	
		this.data.setMaximumValue(maximumValue);
	}
	@Override
	public void setMinimumValue(float minimumValue)
	{
		super.minimumValue = minimumValue;
		this.data.setMinimumValue(minimumValue);
	}
	@Override
	public void setUnitsSuffix(String unitsSuffix)
	{
		super.unitsSuffix = unitsSuffix;
		this.data.setUnitsSuffix(unitsSuffix);
	}	
	@Override
	public float getValue()
	{
		return this.data.getValue();
	}
	@Override
	public void setValue(float value)
	{						
		this.data.setValue(value);
	}
	@Override
	public GaugeStyle getStyle()
	{
		return super.style;
	}
	@Override
	public void setStyle(GaugeStyle style)
	{
		super.style = style;
		this.data.setStyle(style);
	}
	@Override
	public void addArea(GaugeArea area)
	{
		this.data.addArea(area);
	}
	@Override
	public void clearAreas()
	{
		this.data.clearAreas();
	}
}
