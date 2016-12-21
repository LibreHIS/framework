package ims.framework;

import ims.base.interfaces.IModifiable;
import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.interfaces.ITopButton;
import ims.framework.interfaces.ITopButtonSection;
import ims.framework.interfaces.ITopButtonConfig;

import java.io.Serializable;

public abstract class TopButtonConfig implements IModifiable, Serializable, ITopButtonConfig
{
	private static final long serialVersionUID = 1L;
	
	protected TopButtonCollection items = new TopButtonCollection();
	
	public TopButtonConfig()
	{		
	}	
	
	public TopButtonCollection getItems()
	{
		return items;
	}
	
	public abstract TopButtonExtension getExtension();
	public abstract void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception;
	public abstract void render(StringBuffer sb, SessionData sessData);	
	public abstract TopButton find(int id);
	
	public ITopButton[] getITopButtonConfigButtons()
	{
		return items.getItems();
	}
	public ITopButtonSection[] getITopButtonConfigSections()
	{
		if(getExtension() == null)
			return new ITopButtonSection[0];
		
		ITopButtonSection[] sections = new ITopButtonSection[getExtension().getItems().size()];
		
		for(int x = 0; x < getExtension().getItems().size(); x++)
		{
			sections[x] = getExtension().getItems().get(x);
		}
		
		return sections;
	}
	public int getITopButtonConfigNoColumns()
	{
		if(getExtension() == null)
			return 0;
		
		return getExtension().getNoColumns();
	}
	public boolean getITopButtonConfigIncludePatientSelectionHistory()
	{
		return true;
	}
	
	public void markUnchanged()
	{
		items.markUnchanged();
		getExtension().markUnchanged();
	}
	public boolean wasChanged()
	{
		return items.wasChanged() || getExtension().wasChanged();
	}
}
