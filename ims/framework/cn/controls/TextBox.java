package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.Menu;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.TextBoxData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.cn.events.TextBoxChanged;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.TextTrimming;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.StringUtils;

public class TextBox extends ims.framework.controls.TextBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean multiLine, int maxLength, boolean hasBorder, boolean isPassword, Menu menu, String tooltip, boolean required, CharacterCasing casing, TextTrimming textTrimming, String mask, String validationString)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu, required, casing, mask);
		
		this.tabIndex = tabIndex;
		this.multiLine = multiLine;
		this.maxLength = maxLength;
		this.hasBorder = hasBorder;
		this.isPassword = isPassword;
		this.tooltip = tooltip;
		this.textTrimming = textTrimming;
		this.validationString = validationString;
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
	public String getValue()
	{
		String value = this.data.getValue();
		if(value == null)
			return null;
		
		if(textTrimming == TextTrimming.GET || textTrimming == TextTrimming.BOTH)
		{
			value = value.trim();
		
			if(value.length() == 0)
				return null;
		}
		
		return value;
	}
	public void setValue(String value)
	{
		if(value != null && (textTrimming == TextTrimming.SET || textTrimming == TextTrimming.BOTH))
		{
			value = value.trim();
			if(value.length() == 0)
				value = null;
		}
		
		this.data.setValue(value);
	}
	public void setTextColor(Color color)
	{
		this.data.setTextColor(color);
	}	
	public String getSelectedText()
	{
		return this.data.getSelectedText();
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
		this.data = (TextBoxData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
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
		else if(event instanceof TextBoxChanged)
		{
			TextBoxChanged textBoxChangedEvent = (TextBoxChanged)event;
			boolean selectedTextChanged = data.isSelectedTextChanged();
			
			if(textBoxChangedEvent.getSelectedText() != null)
			{
				String cleanSelectedText = StringUtils.cleanHtml(textBoxChangedEvent.getSelectedText());
				if(!cleanSelectedText.equals(textBoxChangedEvent.getSelectedText()))
					selectedTextChanged = true;
			
				this.data.setSelectedText(cleanSelectedText);
			}
			else
			{
				this.data.setSelectedText(null);
			}
			
			data.setSelectedTextChanged(selectedTextChanged);
			
			if(!textBoxChangedEvent.getValue().equals(this.data.getValue()))
			{
				boolean valueChanged = data.isValueChanged();
				
				String cleanValue = StringUtils.cleanHtml(textBoxChangedEvent.getValue());
				if(!cleanValue.equals(textBoxChangedEvent.getValue()))
					valueChanged = true;
				
				this.data.setValue(cleanValue);				
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
		sb.append("<textbox id=\"a");
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
		
		if(this.casing != CharacterCasing.NORMAL)
		{
			sb.append("\" casing=\"");
			sb.append(this.casing.render());
		}
		
		if(super.required)
			sb.append("\" required=\"true");

		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		if(this.isPassword)
		{
			sb.append("\" password=\"true");
		}
		if(this.multiLine)
		{
			sb.append("\" multiLine=\"true");
		}
		if(this.mask.length() > 0)
		{
			sb.append("\" mask=\"");
			sb.append(StringUtils.encodeXML(mask));
		}
		if (this.maxLength > 0)
		{	
			sb.append("\" maxLength=\"");
			sb.append(this.maxLength);
		}		
		if(!this.hasBorder)
		{
			sb.append("\" hasBorder=\"false");
		}
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
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
		sb.append("<textbox id=\"a");
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
			
			if(data.isTextColorChanged())
			{
				if(this.data.getTextColor() != null)
				{
					sb.append("\" textColor=\"");
					sb.append(this.data.getTextColor());
					this.data.setTextColor(null);
				}
				
				data.setTextColorChanged(false);
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
				sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getValue()));
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
			if(data.isSelectedTextChanged())
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
	
	private TextBoxData data;
	private int tabIndex;
	private boolean multiLine;
	private int maxLength;
	private boolean hasBorder;
	private boolean isPassword;	
	private TextTrimming textTrimming = TextTrimming.NONE;
	private String validationString;
}