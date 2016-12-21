package ims.framework.cn.data;

import ims.framework.interfaces.ISpellCheckResult;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author mmihalec
 * 
 */
public class RichTextControlData extends ChangeableData implements IControlData
{	
    private static final long serialVersionUID = 5552322636944723650L;
    
	public class AutoGuess
    {
        public AutoGuess(String from, String to)
        {
            this.from = from;
            this.to = to;
        }
        
        public String from;
        public String to;
    }
    public void setGlossaryVisible(boolean value)
    {
    	if(!this.dataWasChanged)
    		this.dataWasChanged = this.glossaryVisible == value;
    	
        this.glossaryVisible = value;
    }
    public boolean isGlossaryVisible()
    {
        return this.glossaryVisible;
    }
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
	}
	
	public void setValue(String value)
	{	
		if(!this.dataWasChanged)
		{
			if(this.value == null)
				this.dataWasChanged = value != null;
			else 
				this.dataWasChanged = !this.value.equals(value);
		}
		
		this.value = value;
	}
	public String getValue()
	{
		return this.value == null ? "" : this.value;
	}
	public String getText()
	{
		return this.value == null ? "" :  Jsoup.clean(this.value, Whitelist.none());
	}
	
	public void addAutoCorrect(String fromText, String toText)
	{
		this.dataWasChanged = true;
	    this.autoCorrectWasChanged = true;
		this.autoCorrect.put(fromText, toText);
	}
	public void addAutoGuess(String fromText, String toText)
	{
		this.dataWasChanged = true;
	    this.autoGuessWasChanged = true;
		this.autoGuess.add(new AutoGuess(fromText, toText));
	}
    public void clearAutoCorrect()
    {
        if(this.autoCorrect.size() > 0)
        {
        	this.dataWasChanged = true;
            this.autoCorrectWasChanged = true;
            this.autoCorrect.clear();
        }
    }
    public void clearAutoGuess()
    {
        if(this.autoGuess.size() > 0)
        {
        	this.dataWasChanged = true;
            this.autoGuessWasChanged = true;
            this.autoGuess.clear();
        }
    }
    public HashMap getAutoCorrect()
    {
    	return this.autoCorrect;
    }
    public ArrayList getAutoGuess()
    {
    	return this.autoGuess;
    }
    public String getTextToPaste()
    {
        return this.pasteText;
    }
    public void setTextToPaste(String value)
    {
    	if(this.pasteText == null)
			this.dataWasChanged = value != null;
		else 
			this.dataWasChanged = !this.pasteText.equals(value);
    	
        this.pasteText = value;
    }
    public void setSpellCheckResult(ISpellCheckResult value)
    {
    	if(!this.dataWasChanged)
    		this.dataWasChanged = value != null;
    	
    	spellCheckResult = value;
    }
    public ISpellCheckResult getSpellCheckResult()
    {
    	return spellCheckResult;    	
    }
    public void setAutoCorrectWasChanged()
    {
        this.autoCorrectWasChanged = true;
    }
    public void setAutoGuessWasChanged()
    {
        this.autoGuessWasChanged = true;
    }
    public boolean autoCorrectWasChanged()
    {
        return this.autoCorrectWasChanged;
    }
    public boolean autoGuessWasChanged()
    {
        return this.autoGuessWasChanged;
    }
    public void setValueWasChanged()
    {
        this.valueWasChanged = true;
    }
    public boolean valueWasChanged()
    {
        return this.valueWasChanged;
    }
    public void markAsRendered()
    {
        this.valueWasChanged = false;
        this.autoCorrectWasChanged = false;
        this.autoGuessWasChanged = false;
    }
    public void setRequired(boolean value)
    {
    	if(this.required != value)
    	{
    		this.dataWasChanged = true;		
    		this.required = value;
    	}
    }
    public boolean isRequired()
    {
    	return this.required;
    }
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean required = false;
	private boolean glossaryVisible = true;
	private ISpellCheckResult spellCheckResult = null;
	private String value = null;	
	private HashMap<String, String> autoCorrect = new HashMap<String, String>();
	private ArrayList<AutoGuess> autoGuess = new ArrayList<AutoGuess>();
	private String pasteText = null;
	private boolean valueWasChanged = false;
	private boolean autoCorrectWasChanged = false;
	private boolean autoGuessWasChanged = false;	
}
