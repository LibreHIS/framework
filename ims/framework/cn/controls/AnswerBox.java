package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.AnswerBoxData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.AnswerBoxChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.controls.AnswerBoxOption;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class AnswerBox extends ims.framework.controls.AnswerBox implements IVisualControl
{	
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, boolean autoPostBack , boolean canBeEmpty, int imgHeight, boolean required)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, text, canBeEmpty, imgHeight, required);		
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
	}
	protected void free() // free resources
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
	public void addOption(AnswerBoxOption option)
	{
		this.data.addOption(option);
	}
	public AnswerBoxOption getValue()
	{
		return this.data.getOption();
	}
	public void setValue(AnswerBoxOption value)
	{
		if(super.valueSetDelegate != null)
			super.valueSetDelegate.handle(value);
		
		if (value == null)
		{
			if (super.canBeEmpty) {
				this.data.setValue(-1);
			}
			else {
				return; //Causes no change as can't set value to null if canBeEmpty == false
			}
		}
		else 
		{
			this.data.setOption(value);
		}
	}
	public void setValue(int value)
	{
		if (value == -1) {
			this.setValue(null);
		}
		else {
			AnswerBoxOption opt = this.data.getOption(value);
			this.setValue(opt);
		}
	}
	
	public ArrayList getValues() 
	{
		return this.data.getOptions();
	}

	public void clear()
	{
		this.data.clear();
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
		this.data = (AnswerBoxData) data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof AnswerBoxChanged)
		{
			boolean valueChanged = data.isValueChanged();
			this.data.setValue(((AnswerBoxChanged) event).getSelection());
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
	    sb.append("<answerbox id=\"a");
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
		if(super.canBeEmpty)
		{
			sb.append("\" canBeEmpty=\"true");
		}		
		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		if(super.imgHeight != 10)
		{
			sb.append("\" imgHeight=\"");
			sb.append(super.imgHeight);
		}
		
		sb.append("\" text=\"");
		sb.append(ims.framework.utils.StringUtils.encodeXML(super.text));
		sb.append("\">");
		
		for (int i = 0; i < this.data.getOptions().size(); ++i)
		{
			AnswerBoxOption option = this.data.getOption(i);
			sb.append("<option text=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(option.getText()));
            if(option.getTextColor() != null)
            {
                sb.append("\" textColor=\"");
                sb.append(option.getTextColor());
            }
            
            if (option.getImage() != null)
            {
            	sb.append("\" img=\"");
            	sb.append(option.getImage().getImagePath());
            }
            
			sb.append("\"/>");
		}
		sb.append("</answerbox>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<answerbox id=\"a");
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
				sb.append("\" selection=\"");
				sb.append(this.data.getValue());
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
	
	private AnswerBoxData data;
	private int tabIndex;
	private boolean autoPostBack;
}
