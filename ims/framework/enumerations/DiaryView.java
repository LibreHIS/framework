package ims.framework.enumerations;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public class DiaryView implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String modeCode;
	private int noEventsInRow;
	
	public static final DiaryView THREE_MONTHS = new DiaryView(1, "3m", 2);
	
	private DiaryView(int id, String modeCode, int noEventsInRow)
	{
		this.id = id;
		this.modeCode = modeCode;
		this.noEventsInRow = noEventsInRow;
	}	
	
	public int getID()
	{
		return this.id;
	}	
	public String getModeCode()
	{
		return modeCode;
	}
	public int getNoEventsInRow()
	{
		return noEventsInRow;
	}
}
