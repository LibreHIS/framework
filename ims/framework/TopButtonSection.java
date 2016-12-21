package ims.framework;

import ims.base.interfaces.IModifiable;
import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.interfaces.ITopButton;
import ims.framework.interfaces.ITopButtonSection;

import java.io.Serializable;

public abstract class TopButtonSection implements IModifiable, Serializable, ITopButtonSection
{
	private static final long serialVersionUID = 1L;
	private boolean wasChanged = true;
	
	public static final int PREVIOUS_PATIENTS_SECTION_ID = -99;
	public static final int MAX_PATIENTS_SECTION_ITEMS = 6;
	
	protected int id;
	protected String text;
	protected boolean visible = true;	
	protected TopButtonCollection items = new TopButtonCollection(); 
		
	public TopButtonSection()
	{
	}	
	public TopButtonSection(int id, String text, boolean visible)
	{
		this.id = id;
		this.text = text;
		this.visible = visible;
	}
	
	public final int getID()
	{
		return id;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String value)
	{
		if(value == null)
			value = "";
		
		if(!wasChanged)
			wasChanged = value != text;
		text = value;
	}
	public boolean isVisible()
	{
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
	public TopButtonCollection getItems()
	{
		return items;
	}	
	
	public abstract void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception;
	public abstract void render(StringBuffer sb);
	public abstract ims.framework.TopButton find(int id);
	
	public int getITopButtonSectionID() 
	{
		return id;
	}
	public String getITopButtonSectionText() 
	{
		return text == null ? " " : text;
	}

	public ITopButton[] getITopButtonSectionButtons()
	{
		return items.getItems();
	}
	
	public void markUnchanged()
	{
		wasChanged = false;
		items.markUnchanged();		
	}
	public boolean wasChanged()
	{
		return wasChanged || items.wasChanged();			
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof TopButtonSection)
			return ((TopButtonSection)obj).id == id;
		return false;
	}
	public int hashCode()
	{
		return id;
	}
	public String toString()
	{
		return text;
	}
}
