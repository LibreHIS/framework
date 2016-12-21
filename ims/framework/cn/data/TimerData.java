package ims.framework.cn.data;

import ims.framework.exceptions.CodingRuntimeException;

public class TimerData extends ChangeableData implements IControlData
{
	private static final long serialVersionUID = 1L;
	
	private boolean enabled;
	private int interval;
	
	public boolean isEnabled()
	{
		return enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!dataWasChanged)
			dataWasChanged = this.enabled != value;
		enabled = value;
	}
	public int getInterval()
	{
		return interval;
	}
	public void setInterval(int value)
	{
		if(value < 0)
			throw new CodingRuntimeException("Invalid timer interval");
		
		if(!dataWasChanged)
			dataWasChanged = interval != value;
		interval = value;		
		
		if(value == 0)
			setEnabled(false);
	}

}
