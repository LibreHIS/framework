package ims.framework;

import ims.base.interfaces.IModifiable;
import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;

import java.io.Serializable;

public abstract class TopButtonExtension implements IModifiable, Serializable 
{	
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_COLUMN_WIDTH = 200;
	private boolean wasChanged = true;
	
	protected boolean visible = true;	
	protected TopButtonSectionCollection items = new TopButtonSectionCollection();	
	protected int noColumns;
	protected int columnWidth;
	protected boolean includePatientsSelectionHistory = true;
	
	public TopButtonExtension(boolean visible)
	{
		this(visible, 3, DEFAULT_COLUMN_WIDTH);
	}
	public TopButtonExtension(boolean visible, int noColumns, int columnWidth)
	{
		this.visible = visible;
		this.noColumns = noColumns;
		this.columnWidth = columnWidth;
	}

	public boolean isVisible()
	{
		if(includePatientsSelectionHistory && getPreviouslySelectedPatients().size() > 0)
			return true;
		
		boolean hasVisibleItems = false;
		for(int x = 0; x < items.size(); x++)
		{
			if(items.get(x).isVisible())
			{
				hasVisibleItems = true;
				break;
			}
		}
		
		return visible && hasVisibleItems;
	}
	public void setVisible(boolean value)
	{
		if(!wasChanged)
			wasChanged = value != visible;
		visible = value;
	}	
	public int getNoColumns()
	{
		return noColumns;
	}
	public void setNoColumns(int value)
	{
		if(!wasChanged)
			wasChanged = value != noColumns;
		noColumns = value;
	}
	public int getColumnWidth()
	{
		return columnWidth;
	}
	public void setIncludePatientsSelectionHistory(boolean value)
	{
		includePatientsSelectionHistory = value;
	}
	public void setColumnWidth(int value)
	{
		if(!wasChanged)
			wasChanged = value != columnWidth;
		columnWidth = value;
	}
	public TopButtonSectionCollection getItems()
	{
		return items;
	}
	
	public abstract void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception;
	public abstract void render(StringBuffer sb, SessionData sessData);
	public abstract TopButton find(int id);
	public abstract TopButtonCollection getPreviouslySelectedPatients();
	
	public void markUnchanged()
	{
		wasChanged = false;		
		items.markUnchanged();
		if(includePatientsSelectionHistory)
			getPreviouslySelectedPatients().markUnchanged();
	}
	public boolean wasChanged()
	{
		if(includePatientsSelectionHistory && getPreviouslySelectedPatients().wasChanged())
			return true;
		
		return wasChanged || items.wasChanged();			
	}	
}
