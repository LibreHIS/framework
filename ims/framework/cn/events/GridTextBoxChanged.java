/*
 * Created on Jun 1, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ims.framework.cn.events;

import java.io.Serializable;

/**
 * @author vpurdila
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GridTextBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridTextBoxChanged(int controlID, int row, int column, String value)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getRow()
	{
		return this.row;
	}
	public int getColumn()
	{
		return this.column;
	}
	public String getValue()
	{
		return this.value;
	}
	private int controlID;
	private int row;
	private int column;
	private String value;	
}
