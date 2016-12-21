package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Date;

abstract public class TimeLineEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	abstract public void addDate(Date date, String value);
	abstract public void clear();
	abstract public Object getValue();
}
