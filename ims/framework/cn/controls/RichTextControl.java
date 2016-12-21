package ims.framework.cn.controls;

import java.util.Iterator;

import ims.configuration.ConfigFlag;
import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.RichTextControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.RichTextAddToDictionary;
import ims.framework.cn.events.RichTextControlGlossary;
import ims.framework.cn.events.RichTextSpellCheck;
import ims.framework.cn.events.RichTextValueChanged;
import ims.framework.cn.utils.SpellCheckRendering;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.interfaces.ISpellChecker;

/**
 * @author mmihalec
 * 
 */
public class RichTextControl extends ims.framework.controls.RichTextControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	private ISpellChecker spellChecker;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean required, ISpellChecker spellChecker, boolean simpleMode, int maxLength)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, required, simpleMode);
		this.tabIndex = tabIndex;
		this.spellChecker = spellChecker;
		this.maxLength = maxLength;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public boolean spellCheck()
	{
		if(spellChecker != null)
		{
			String text = this.data.getValue() == null ? "" : this.data.getValue();
			this.data.setSpellCheckResult(spellChecker.spellCheck(text));
			return true;
		}
		return false;
	}
	public void setGlossaryVisible(boolean value)
	{
	    this.data.setGlossaryVisible(value);
	}
	public void addAutoCorrect(String fromText, String toText)
	{
		this.data.addAutoCorrect(fromText, toText);
	}
    public void clearAutoCorrect()
    {
    	this.data.clearAutoCorrect();
    }
    public void addAutoGuess(String fromText, String toText)
	{
		this.data.addAutoGuess(fromText, toText);
	}
    public void clearAutoGuess()
    {
    	this.data.clearAutoGuess();
    }
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public String getValue()
	{
		if(this.data.getValue().length() > 0) 
		{
			if (super.simpleMode)
			{
				return this.data.getValue();
			}
			else
			{
				return ims.framework.controls.RichTextControl.IdentityTag + this.data.getValue();	
			}
		}		
		 
		return this.data.getValue();		    
	}
    public void setValue(String value)
    {
        if(value != null && value.indexOf(ims.framework.controls.RichTextControl.IdentityTag) >= 0)
            value = value.replaceAll(ims.framework.controls.RichTextControl.IdentityTag, "");
        
        if((this.data.getValue() == null && value != null) || !this.data.getValue().equals(value))
        {
	        this.data.setValueWasChanged();
	    	this.data.setValue(value);
        }
    }
    public String getText()
    {
        return this.data.getText();
    }
    public void pasteText(String value)
    {
        this.data.setTextToPaste(value);
    }
    public void setRequired(boolean value)
	{
		if(super.required == true)
			throw new CodingRuntimeException("The control does not allow setting the required property at runtime as it was already marked as required at design time");
		this.data.setRequired(value);
	}
	public boolean isRequired()
	{
		if(super.required == true)
			return true;
		return this.data.isRequired();
	}
	public void restore(IControlData data, boolean isNew) 
	{
		this.data = (RichTextControlData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException 
	{
		if(event instanceof RichTextControlGlossary)
	    {
	        if(super.glossaryClickDelegate != null)
	        {
	            super.glossaryClickDelegate.handle();
	        }
	        
	        return true;
	    }	        
	    else if(event instanceof RichTextValueChanged)
	    {
	    	boolean wasChanged = this.data.wasChanged();
	        
	    	this.data.setValue(((RichTextValueChanged)event).getValue());
	        
	    	if(!wasChanged)
	        	this.data.markUnchanged();
	        
	        if(super.valueChangedDelegate != null)
	        {
	            super.valueChangedDelegate.handle();
	        }
	        
	        return true;
	    }    
	    else if(event instanceof RichTextSpellCheck)
	    {
	    	if(spellChecker != null)
	    	{
	    		this.data.setSpellCheckResult(spellChecker.spellCheck(((RichTextSpellCheck)event).getValue()));
	    	}
	    	
	    	return true;
	    }
	    else if(event instanceof RichTextAddToDictionary)
	    {
	    	if(spellChecker != null)
	    	{
	    		spellChecker.addToDictionary(((RichTextAddToDictionary)event).getValue());
	    	}
	    	
	    	return true;	    	
	    }
		
		return false;
	}

	public void renderControl(StringBuffer sb) throws ConfigurationException 
	{
		sb.append("<wysiwygeditor id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		if (this.maxLength > 0)
		{	
			sb.append("\" maxLength=\"");
			sb.append(this.maxLength);
		}		

		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(super.simpleMode)
		{
			sb.append("\" simpleMode=\"true");			
		}
		
		sb.append("\" fontName=\"");
		sb.append(ConfigFlag.UI.RICHTEXT_CONTROL_DEFAULT_FONT_NAME.getValue());
		
		sb.append("\" fontSize=\"");
		sb.append(ConfigFlag.UI.RICHTEXT_CONTROL_DEFAULT_FONT_SIZE.getValue());		
		
		sb.append("\"/>");
	}

	public void renderData(StringBuffer sb) throws ConfigurationException 
	{
		if(this.data.getSpellCheckResult() != null)
		{
			sb.append("<wysiwygeditor id=\"a");
			sb.append(super.id);
			sb.append("\">");
			
			SpellCheckRendering.render(sb, this.data.getSpellCheckResult());
			data.setSpellCheckResult(null);
			
			sb.append("</wysiwygeditor>");
		}
		else
		{		
			sb.append("<wysiwygeditor id=\"a");
			sb.append(super.id);
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");		
					
			if(this.data.isVisible())
			{
				if(!hasAnyParentDisabled())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
				}
				
				if(!super.required)
				{
					sb.append("\" required=\"");
					sb.append(this.data.isRequired() ? "true" : "false");
				}
					
				if(this.glossaryClickDelegate != null && this.data.isGlossaryVisible())
				    sb.append("\" glossaryVisible=\"true");
				
				sb.append("\">");
				
				if(this.data.autoCorrectWasChanged())
				{			
					sb.append("<autocorrect>");
					
					Iterator fromIterator = this.data.getAutoCorrect().keySet().iterator();
					Iterator toIterator = this.data.getAutoCorrect().values().iterator();
					while(fromIterator.hasNext())
					{
						sb.append("<item from=\"" + ims.framework.utils.StringUtils.encodeXML(((String)fromIterator.next())) + "\" to=\"" + ims.framework.utils.StringUtils.encodeXML((String)toIterator.next()) + "\"/>");
					}
								
					sb.append("</autocorrect>");							
				}
				
				if(this.data.autoGuessWasChanged())
				{			
					sb.append("<autoguess>");
								
					for(int x = 0; x < this.data.getAutoGuess().size(); x++)
					{
					    RichTextControlData.AutoGuess item = (RichTextControlData.AutoGuess)this.data.getAutoGuess().get(x); 
						sb.append("<item from=\"" + ims.framework.utils.StringUtils.encodeXML(item.from) + "\" to=\"" + ims.framework.utils.StringUtils.encodeXML(item.to) + "\"/>");
					}
								
					sb.append("</autoguess>");							
				}
				
				if(this.data.valueWasChanged())
				{
				    if(this.data.getValue() != null)
					{
						sb.append("<document>");		
						sb.append("<![CDATA[");
						sb.append(this.data.getValue());
						sb.append("]]>");
						sb.append("</document>");
					}
					else
					    sb.append("<document/>");
				}
							
				if(this.data.getTextToPaste() != null)
				{
				    sb.append("<paste>");
				    sb.append("<![CDATA[");
					sb.append(this.data.getTextToPaste());
					sb.append("]]>");
					sb.append("</paste>");
					
				    this.data.setTextToPaste(null);
				}
				
				sb.append("</wysiwygeditor>");
				this.data.markAsRendered();
			}
			else
			{
				sb.append("\" />");
			}
		}
	}
	public boolean wasChanged() 
	{
		return this.data.wasChanged();
	}
	public void markUnchanged() 
	{
		this.data.markUnchanged();
	}
	
	private RichTextControlData data;	
	private int tabIndex;
	private int maxLength;    
}
