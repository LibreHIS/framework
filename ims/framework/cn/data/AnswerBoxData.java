package ims.framework.cn.data;

import ims.framework.controls.AnswerBoxOption;

import java.util.ArrayList;

public class AnswerBoxData implements IControlData
{
	private static final long serialVersionUID = 2027836179646849938L;
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
	public int getValue()
	{
		return this.value;
	}
	public void setValue(int value)
	{
		if (value < -1 || value >= this.options.size())
		{
			if(!this.valueChanged)
				this.valueChanged = this.value != -1;
			
			this.value = -1;
		}
		else 
		{
			if(!this.valueChanged)
				this.valueChanged = this.value != value;
			
			this.value = value;
		}
	}	
	public void setOption(AnswerBoxOption option)
	{
		if (option == null)
		{
			setValue(-1);
		}
		else 
		{			
			setValue(this.options.indexOf(option));
		}
	}
	public void addOption(AnswerBoxOption option)
	{
		if (this.options.indexOf(option) != -1) 
			return;
				
		this.options.add(option);
	}	
	public ArrayList getOptions()
	{
		return this.options;
	}	
	public AnswerBoxOption getOption()
	{
		if (this.value == -1)
		{
			return null;
		}
		
		return this.options.get(this.value);		
	}
	public AnswerBoxOption getOption(int value)
	{
		if (value < 0 || value >= this.options.size())
		{
			return null;
		}
		return this.options.get(value);
	}
	public void clear()
	{
		this.options.clear();
		setValue(-1);
	}
	public void setRequired(boolean value)
	{
		if(!this.requiredChanged)
			this.requiredChanged = this.required != value;
		
		this.required = value;		
	}
	public boolean isRequired()
	{
		return this.required;
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
	public void setRequiredChanged(boolean requiredChanged)
	{
		this.requiredChanged = requiredChanged;
	}
	public boolean isRequiredChanged()
	{
		return requiredChanged;
	}
	public void setValueChanged(boolean valueChanged)
	{
		this.valueChanged = valueChanged;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}

	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private ArrayList<AnswerBoxOption> options = new ArrayList<AnswerBoxOption>();
	private int value = -1;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean requiredChanged = false;	
	private boolean valueChanged = false;
}
