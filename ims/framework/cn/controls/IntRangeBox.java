package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.IntRangeBoxData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.RangeBoxChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.IntRange;

public class IntRangeBox extends ims.framework.controls.IntRangeBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean required)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, required);
		this.tabIndex = tabIndex;
	}
	protected void free()
	{
		super.free();
		
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
	public IntRange getValue()
	{
		return this.data.getValue();
	}
	public void setValue(IntRange value)
	{
		this.data.setValue(value);
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
		this.data = (IntRangeBoxData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof RangeBoxChanged)
		{
			boolean valuleChanged = data.isValueChanged();		
			RangeBoxChanged e = (RangeBoxChanged)event;
			Integer min = e.getMin().equals("") ? null : new Integer(e.getMin());
			Integer max = e.getMax().equals("") ? null : new Integer(e.getMax());		
			this.data.setValue(new IntRange(min, max));
			data.setValueChanged(valuleChanged);		
			
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
		sb.append("<rangebox id=\"a");
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
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		IntRange ir = this.data.getValue();
		
		sb.append("<rangebox id=\"a");
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
				sb.append("\" minValue=\"");
				if (ir.getMin() != null)
					sb.append(ir.getMin().intValue());
				sb.append("\" maxValue=\"");
				if (ir.getMax() != null)
					sb.append(ir.getMax().intValue());
				
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
		}
		
		return false;
	}
	public void markUnchanged() 
	{
	}
	
	private IntRangeBoxData data;
	private int tabIndex;
}
