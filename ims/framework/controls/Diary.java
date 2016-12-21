package ims.framework.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.delegates.DiaryCurrentDateChanged;
import ims.framework.delegates.DiarySelectionChanged;
import ims.framework.enumerations.DiaryView;
import ims.framework.utils.Date;

public abstract class Diary extends Control implements IVisualControl
{	
	private static final long serialVersionUID = 1L;
	
	protected DiaryCurrentDateChanged currentDateChangedDelegate = null;
	protected DiarySelectionChanged selectionChangedDelegate = null;
	
	protected void free()
	{
		super.free();		
		
		this.currentDateChangedDelegate = null;
		this.selectionChangedDelegate = null;
	}
	public void setCurrentDateChangedEvent(DiaryCurrentDateChanged delegate)
	{
		this.currentDateChangedDelegate = delegate;
	}
	public void setSelectionChangedEvent(DiarySelectionChanged delegate)
	{
		this.selectionChangedDelegate = delegate;
	}
	
	public abstract void setTitle(String title);
	public abstract String getTitle();
	public abstract void clear();
	public abstract DiaryEvents getEvents();	
	public abstract DiaryEvent getSelectedEvent();
	public abstract DiaryView getCurrentView();
	public abstract void setCurrentView(DiaryView currentView);
	public abstract void setCurrentDate(Date currentDate);
	public abstract Date getCurrentDate();
}
