package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.delegates.TimeLineClick;
import ims.framework.utils.Date;
import ims.framework.utils.Image;

abstract public class TimeLineControl extends ims.framework.Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void setFromDate(Date date);
	abstract public void setToDate(Date date);
	abstract public TimeLineEvent addTimeLineEvent(String caption, Image before, Image after, Object value);
	abstract public ArrayList getTimeLineEvents();
	
	public void setClickEvent(TimeLineClick delegate)
	{
		this.delegate = delegate;        
	}
	protected void free() // free resources
	{
		super.free();
		
		this.delegate = null;
	}   
	
	protected TimeLineClick delegate = null;
}
