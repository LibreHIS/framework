package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.CheckBoxData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.CheckBoxChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.StringUtils;

public class CheckBox extends ims.framework.controls.CheckBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, boolean autoPostBack, String toolTip)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, text, toolTip);
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
	}
	protected void free() // free resources
	{
		super.free();
		
		this.text = null;
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
	public boolean getValue()
	{
		return this.data.getValue();
	}
	public void setValue(Boolean value)
	{
		this.data.setValue(value == null ? false : value);
	}
	public void setTooltip(String value)
	{
		super.tooltip = value;
		this.data.setTooltip(value);		
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (CheckBoxData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setTooltip(this.tooltip);
		}
		else
		{
			this.tooltip = this.data.getTooltip();
		}		
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof CheckBoxChanged)
		{
			boolean value = ((CheckBoxChanged)event).getValue();
			if(value != this.data.getValue())
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
		sb.append("<checkbox id=\"a");
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
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		
		sb.append("\" text=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(super.text));
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<checkbox id=\"a");
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
			}
		
			if(data.isValueChanged())
			{
				sb.append("\" checked=\"");
				sb.append(this.data.getValue() ? "true" : "false");
				data.setValueChanged(false);
			}
		
			if(data.isTooltipChanged())
			{
				sb.append("\" tooltip=\"");
				sb.append(super.tooltip == null ? "" : StringUtils.encodeXML(super.tooltip));
				data.setTooltipChanged(false);
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
			}
			
			if(data.isValueChanged())
				return true;
			if(data.isTooltipChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private CheckBoxData data;
	private int tabIndex;	
	private boolean autoPostBack;
}
