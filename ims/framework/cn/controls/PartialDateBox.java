package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.PartialDateBoxData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.cn.events.PartialDateBoxChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.PartialDate;

public class PartialDateBox extends ims.framework.controls.PartialDateBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String validationString, boolean required, boolean autoPostBack)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, required);
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
	public PartialDate getValue()
	{
		return this.data.getValue();
	}
	public void setValue(PartialDate value)
	{
		this.data.setValue(value);
	}
	public PartialDate getMinValue()
	{
		return this.data.getMinValue();
	}
	public void setMinValue(PartialDate value)
	{
		this.data.setMinValue(value);
	}
	public PartialDate getMaxValue()
	{
		return this.data.getMaxValue();
	}
	public void setMaxValue(PartialDate value)
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
		this.data = (PartialDateBoxData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof InvalidControlValue)
		{
			data.setValue(null);
			data.setValueChanged(true);
			
			return true;
		}
		else if(event instanceof PartialDateBoxChanged)
		{
			boolean valueChanged = data.isValueChanged();
			this.data.setValue(((PartialDateBoxChanged)event).getValue());
			data.setValueChanged(valueChanged);
			
			if(super.valueChangedDelegate != null)
			{	
				super.valueChangedDelegate.handle();
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<partialdatebox id=\"a");
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
			sb.append("\" required=\"true");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		
		sb.append("\" />");		
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<partialdatebox id=\"a");
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
			
			if(data.isValueChanged())
			{
				PartialDate date = this.data.getValue();
				sb.append("\" year=\"");
				if (date != null && date.getYear() != null && date.getYear().intValue() > 0)
					sb.append(date.getYear().intValue());
				sb.append("\" month=\"");
				if (date != null && date.getMonth() != null && date.getMonth().intValue() > 0)
				{
					int month = date.getMonth().intValue(); 
					sb.append((month < 10 ? "0" : "") + month);
				}
				sb.append("\" day=\"");
				if (date != null && date.getDay() != null && date.getDay().intValue() > 0)
				{
					int day = date.getDay().intValue(); 		
					sb.append((day < 10 ? "0" : "") + day);
				}
				
				data.setValueChanged(false);
			}
			
			if(data.isMinValueChanged())
			{
				PartialDate date = this.data.getMinValue();
				sb.append("\" minvalue=\"");
				
				if (date != null && date.getYear() != null && date.getYear().intValue() > 0)
				{
					sb.append(date.getYear().intValue());
				
					if (date.getMonth() != null && date.getMonth().intValue() > 0)
					{
						int month = date.getMonth().intValue(); 
						sb.append((month < 10 ? "0" : "") + month);
					
						if (date.getDay() != null && date.getDay().intValue() > 0)
						{
							int day = date.getDay().intValue(); 		
							sb.append((day < 10 ? "0" : "") + day);
						}
					}
				}
				
				data.setMinValueChanged(false);
			}
			
			if(data.isMaxValueChanged())
			{
				PartialDate date = this.data.getMaxValue();
				sb.append("\" maxvalue=\"");
				
				if (date != null && date.getYear() != null && date.getYear().intValue() > 0)
				{
					sb.append(date.getYear().intValue());
				
					if (date.getMonth() != null && date.getMonth().intValue() > 0)
					{
						int month = date.getMonth().intValue(); 
						sb.append((month < 10 ? "0" : "") + month);
					
						if (date.getDay() != null && date.getDay().intValue() > 0)
						{
							int day = date.getDay().intValue(); 		
							sb.append((day < 10 ? "0" : "") + day);
						}
					}
				}
				
				data.setMaxValueChanged(false);
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
			
			if(data.isValueChanged())
				return true;
			if(data.isMinValueChanged())
				return true;
			if(data.isMaxValueChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{	
	}
	
	private PartialDateBoxData data;
	private int tabIndex;
	private String validationString;
	private boolean autoPostBack;
}
