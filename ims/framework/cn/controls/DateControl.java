package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.Menu;
import ims.framework.cn.data.DateControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DateControlChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.StringUtils;

public class DateControl extends ims.framework.controls.DateControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, String validationString, boolean autoPostBack, Menu menu, boolean required, String tooltip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, canBeEmpty, menu, required, tooltip);
		this.tabIndex = tabIndex;
		this.validationString = validationString;
		this.autoPostBack = autoPostBack; 
	}
	protected void free()
	{
		super.free();
		
		this.validationString = null;
		this.data = null;
	}
	public void setTooltip(String value)
	{
		super.tooltip = value;
		this.data.setTooltip(value);		
	}	
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public Date getValue()
	{
		return this.data.getValue();
	}
	public void setValue(Date value)
	{
		this.data.setValue(value);
	}
	public Date getMinValue()
	{
		return this.data.getMinValue();
	}
	public void setMinValue(Date value)
	{
		this.data.setMinValue(value);
	}
	public Date getMaxValue()
	{
		return this.data.getMaxValue();
	}
	public void setMaxValue(Date value)
	{
		this.data.setMaxValue(value);
	}
	public void setNoFutureDates()
	{
		setMaxValue(new Date());
	}
	public void setRequired(boolean value)
	{
		if(super.required == true)
			throw new CodingRuntimeException("The control does not allow setting the required property at runtime as it was already marked as required at design time");
		this.data.setRequired(value);
	}
	public boolean isRequired()
	{
		if(super.required == true)
			return true;
		return this.data.isRequired();
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (DateControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setTooltip(this.tooltip);
		}
		else 			
		{
			super.tooltip = this.data.getTooltip();
		}
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof InvalidControlValue)
		{
			data.setValue(null);
			data.setValueChanged(true);
			
			return true;
		}
		else if(event instanceof DateControlChanged)
		{
			Date value = ((DateControlChanged)event).getValue();
			if((value == null && this.data.getValue() != null) || (value != null && !value.equals(this.data.getValue())))
			{
				boolean valueChanged = data.isValueChanged();			
				this.data.setValue(value);
				data.setValueChanged(valueChanged);
				
				if(super.valueChangedDelegate != null)
				{	
					super.valueChangedDelegate.handle();
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<datebox id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(super.canBeEmpty)
		{
			sb.append("\" canBeEmpty=\"true");
		}		
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<datebox id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledChanged(false);
				}
				
				if(!super.required && enabled)
				{
					if(data.isRequiredChanged())
					{
						sb.append("\" required=\"");
						sb.append(this.data.isRequired() ? "true" : "false");
						data.setRequiredChanged(false);
					}
				}
			}
			
			if(data.isTooltipChanged())
			{
				sb.append("\" tooltip=\"");
				sb.append(super.tooltip == null ? "" : StringUtils.encodeXML(super.tooltip));
				data.setTooltipChanged(false);
			}
				
			if(data.isValueChanged())
			{
				sb.append("\" value=\"");
				Date date = this.data.getValue();
				sb.append(date == null ? "" : date.toString());
				data.setValueChanged(false);
			}
			
			if(this.data.isEnabled())
			{
				if(data.isMinValueChanged())
				{
					sb.append("\" minvalue=\"");
					if(this.data.getMinValue() != null)
						sb.append(this.data.getMinValue().toString(DateFormat.ISO));
					
					data.setMinValueChanged(false);
				}
				
				if(data.isMaxValueChanged())
				{
					sb.append("\" maxvalue=\"");
					if(this.data.getMaxValue() != null)
						sb.append(this.data.getMaxValue().toString(DateFormat.ISO));
					
					data.setMaxValueChanged(false);
				}
			}
		}
		
		sb.append("\" />");		
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
				
				if(!super.required && enabled)
				{
					if(data.isRequiredChanged())
						return true;
				}
			}
			
			if(data.isTooltipChanged())
				return true;
			
			if(data.isValueChanged())
				return true;
			
			if(enabled)
			{
				if(data.isMinValueChanged())
					return true;
				if(data.isMaxValueChanged())
					return true;
			}
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private DateControlData data;
	private int tabIndex;
	private String validationString;
	private boolean autoPostBack;
}
