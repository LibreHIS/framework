package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.IntBoxData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.IntBoxChanged;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.StringUtils;

public class IntBox extends ims.framework.controls.IntBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canBeEmpty, boolean allowSign, boolean autoPostBack, String validationString, String tooltip, boolean required, int maxLength)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, canBeEmpty, allowSign, required, maxLength);
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
		this.validationString = validationString;
		this.tooltip = tooltip;
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
	public Integer getValue()
	{
		return this.data.getValue();
	}
	public void setValue(Integer value)
	{
		this.data.setValue(value);
	}
	public void setTextColor(Color color)
	{
		this.data.setTextColor(color);
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
		this.data = (IntBoxData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
				
		if(this.data.getValue() == null && !super.canBeEmpty)
			this.data.setValue(new Integer(0));
		
		if(isNew)
		{
			this.data.setTooltip(super.tooltip);
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
		else if(event instanceof IntBoxChanged)
		{
			Integer value = ((IntBoxChanged)event).getValue();
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
		sb.append("<intbox id=\"a");
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
		if(super.canBeEmpty)
		{
			sb.append("\" canBeEmpty=\"true");
		}				
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		if(this.allowSign)
		{
			sb.append("\" allowSign=\"true");
		}		
		
		sb.append("\" maxLength=\"");
		sb.append(this.maxLength);
		
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<intbox id=\"a");
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
				
			if(this.data.getTextColor() != null)
			{
				if(data.isTextColorChanged())
				{
					sb.append("\" textColor=\"");
					sb.append(this.data.getTextColor());
					this.data.setTextColor(null);
					data.setTextColorChanged(false);
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
				Integer intValue = this.data.getValue();
				sb.append(intValue == null ? "" : intValue.toString());
				data.setValueChanged(false);
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
			if(data.isTextColorChanged())
				return true;
			if(data.isTooltipChanged())
				return true;
		}			
		
		return false;
	}	
	public void markUnchanged() 
	{		
	}
	
	private IntBoxData data;
	private int tabIndex;
	private boolean autoPostBack;
	private String validationString;	
}
