package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.interfaces.ITopButton;
import ims.framework.utils.Image;

public abstract class TopButton implements IModifiable, Serializable, ITopButton
{
	private static final long serialVersionUID = 1L;
	private boolean wasChanged = true;
	
	protected int id;
	protected String text;
	protected Image image;
	protected boolean enabled;
	protected boolean visible;
	protected boolean builtIn;
	protected FormName form;
	protected String url;
	protected Object identifier;
	protected boolean alwaysEnabled;
	protected boolean patientMustBeSelected;
	protected boolean contextDependent;
	protected boolean displayMaximiseButton;
	protected boolean displayCloseButton;
	
	public TopButton()
	{		
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible)
	{		
		this(id, text, image, enabled, visible, false);
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible, boolean alwaysEnabled)
	{		
		this.id = id;
		this.text = text;
		this.image = image;
		this.enabled = enabled;
		this.visible = visible;
		this.alwaysEnabled = alwaysEnabled;
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible, boolean alwaysEnabled, boolean patientMustBeSelected)
	{		
		this.id = id;
		this.text = text;
		this.image = image;
		this.enabled = enabled;
		this.visible = visible;
		this.alwaysEnabled = alwaysEnabled;
		this.patientMustBeSelected = patientMustBeSelected;
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible, boolean alwaysEnabled, boolean patientMustBeSelected, boolean contextDependent)
	{		
		this.id = id;
		this.text = text;
		this.image = image;
		this.enabled = enabled;
		this.visible = visible;
		this.alwaysEnabled = alwaysEnabled;
		this.patientMustBeSelected = patientMustBeSelected;
		this.contextDependent = contextDependent;
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible, boolean alwaysEnabled, boolean patientMustBeSelected, boolean contextDependent, boolean displayMaximiseButton)
	{		
		this.id = id;
		this.text = text;
		this.image = image;
		this.enabled = enabled;
		this.visible = visible;
		this.alwaysEnabled = alwaysEnabled;
		this.patientMustBeSelected = patientMustBeSelected;
		this.contextDependent = contextDependent;
		this.displayMaximiseButton = displayMaximiseButton;
	}
	public TopButton(int id, String text, Image image, boolean enabled, boolean visible, boolean alwaysEnabled, boolean patientMustBeSelected, boolean contextDependent, boolean displayMaximiseButton, boolean displayCloseButton)
	{		
		this.id = id;
		this.text = text;
		this.image = image;
		this.enabled = enabled;
		this.visible = visible;
		this.alwaysEnabled = alwaysEnabled;
		this.patientMustBeSelected = patientMustBeSelected;
		this.contextDependent = contextDependent;
		this.displayMaximiseButton = displayMaximiseButton;
		this.displayCloseButton = displayCloseButton;
	}
	
	public final int getID()
	{
		return id;
	}
	public String getText()
	{
		return text;
	}
	public Image getImage()
	{
		return image;
	}	
	public boolean isEnabled()
	{
		return enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!wasChanged)
			wasChanged = value != enabled;
		enabled = value;
	}
	public boolean isVisible()	
	{		
		return visible;
	}
	public void setVisible(boolean value)
	{
		if(!wasChanged)
			wasChanged = value != visible;
		visible = value;
	}
	public boolean isBuiltIn()
	{
		return builtIn;
	}
		
	public abstract void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception;
	public abstract void render(StringBuffer sb);	
	
	public int getITopButtonID()
	{
		return id;
	}
	public String getITopButtonText()
	{
		return text;
	}
	public FormName getITopButtonForm() 
	{
		return form;
	}
	public String getITopButtonUrl() 
	{
		return url;
	}
	public boolean getITopButtonIsAlwaysEnabled() 
	{
		return alwaysEnabled;
	}
	public boolean getITopButtonPatientMustBeSelected() 
	{
		return patientMustBeSelected;
	}
	public boolean getITopButtonEnabled() 
	{
		return enabled;
	}
	public void setITopButtonEnabled(boolean value) 
	{
		this.enabled = value;
	}
	public boolean getITopButtonContextDependent() 
	{
		return contextDependent;
	}
	public void setITopButtonContextDependent(boolean value) 
	{
		this.contextDependent = value;
	}
	public boolean getITopButtonDisplayMaximiseButton() 
	{
		return displayMaximiseButton;
	}
	public boolean getITopButtonDisplayCloseButton() 
	{
		return displayCloseButton;
	}
	public void markUnchanged()
	{
		wasChanged = false;
	}
	public boolean wasChanged()
	{
		return wasChanged;
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof TopButton)
			return ((TopButton)obj).id == id;
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
	public Object getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(Object value)
	{
		identifier = value;
	}
}
