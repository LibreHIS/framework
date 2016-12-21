package ims.framework.cn.controls;

import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.TimerData;

public class Timer extends ims.framework.controls.Timer
{
	private static final long serialVersionUID = 1L;
	
	private int id;
		
	public Timer(int id, int interval)
	{
		this(id, interval, true);
	}
	public Timer(int id, int interval, boolean enabled)
	{
		this.id = id;
		
		data.setInterval(interval);
		data.setEnabled(enabled);
	}
	@Override
	public void render(StringBuffer sb)
	{		
		sb.append("<timer id=\"");
		sb.append(id);
		sb.append("\" enabled=\"");
		sb.append(data.isEnabled() ? "true" : "false");
		if(data.isEnabled())
		{
			sb.append("\" interval=\"");
			sb.append(data.getInterval());
		}
		sb.append("\" />");
	}
	
	public TimerData data = new TimerData();

	@Override
	public int getID()
	{
		return id;
	}
	@Override
	public int getInterval()
	{
		return data.getInterval();
	}
	@Override
	public boolean isEnabled()
	{
		return data.isEnabled();
	}
	@Override
	public void setEnabled(boolean value)
	{
		data.setEnabled(value);
	}
	@Override
	public void setInterval(int value)
	{
		data.setInterval(value);
	}
	public IControlData getData()
	{
		return data;
	}
	public void restore(IControlData data)
	{
		this.data = (TimerData)data;
	}
	public void markUnchanged()
	{
		data.markUnchanged();
	}
	public boolean wasChanged()
	{
		return data.wasChanged();
	}
}
