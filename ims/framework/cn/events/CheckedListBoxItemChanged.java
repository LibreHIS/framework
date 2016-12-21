package ims.framework.cn.events;

import java.io.Serializable;

public class CheckedListBoxItemChanged implements Serializable
{
	private static final long serialVersionUID = 1L;
	public CheckedListBoxItemChanged(int index, boolean checked)
	{
		this.index = index;
		this.checked = checked;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	public boolean isChecked()
	{
		return this.checked;
	}
	
	private int index;
	private boolean checked;
}
