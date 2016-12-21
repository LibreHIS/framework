package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.Menu;
import ims.framework.cn.data.DateTimeControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DateTimeControlChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.DateTime;
import ims.framework.utils.DateTimeFormat;
import ims.framework.utils.StringUtils;

public class DateTimeControl extends ims.framework.controls.DateTimeControl implements IVisualControl
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
	public DateTime getValue()
	{
		return this.data.getValue();
	}
	public void setValue(DateTime value)
	{
		this.data.setValue(value);
	}
	public DateTime getMinValue()
	{
		return this.data.getMinValue();
	}
	public void setMinValue(DateTime value)
	{
		this.data.setMinValue(value);
	}
	public DateTime getMaxValue()
	{
		return this.data.getMaxValue();
	}
	public void setMaxValue(DateTime value)
	{
		this.data.setMaxValue(value);
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
		this.data = (DateTimeControlData)data;
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
		else if(event instanceof DateTimeControlChanged)
	    {
			DateTime value = ((DateTimeControlChanged)event).getValue();
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
		sb.append("\" time=\"true");
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
			sb.append("\" required=\"true");

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
				DateTime date = this.data.getValue();
				sb.append(date == null ? "" : date.toString(DateTimeFormat.ISO));
				data.setValueChanged(false);
			}
			
			if(this.data.isEnabled())
			{
				if(this.data.getMinValue() != null)
				{
					if(data.isMinValueChanged())
					{
						sb.append("\" minvalue=\"");
						sb.append(this.data.getMinValue().toString(DateTimeFormat.ISO));
						data.setMinValueChanged(false);
					}
				}
				
				if(this.data.getMaxValue() != null)
				{
					if(data.isMaxValueChanged())
					{
						sb.append("\" maxvalue=\"");
						sb.append(this.data.getMaxValue().toString(DateTimeFormat.ISO));
						data.setMaxValueChanged(false);
					}
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
	
	private DateTimeControlData data;
	private int tabIndex;
	private String validationString;
	private boolean autoPostBack;
}
