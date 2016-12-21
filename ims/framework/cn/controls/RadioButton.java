package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.RadioButtonData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.RadioBoxChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.PresentationLogicException;

public class RadioButton extends ims.framework.controls.RadioButton implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean autoPostBack)
	{
		super.setContext(parentControl, id, 0, 0, 0, 0, viewMode, editMode, anchor);
		
		this.autoPostBack = autoPostBack;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
		this.buttons.clear();
		this.buttons.trimToSize();
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
	public int getValue()
	{
		return this.data.getValue();
	}
	public void setValue(int value)
	{
		this.data.setValue(value);
	}
	public boolean isEnabled(int option)
	{
		if(option == -1) // no radiobutton
			return false;
		
		return this.data.getEnabledOptions().get(option).booleanValue();
	}	
	public void setEnabled(int option, boolean value)
	{
		if(option == -1) // no radiobutton
			return;
		
		Boolean existingValue = this.data.getEnabledOptions().get(option);
		if(existingValue == null || existingValue.booleanValue() != value)
		{	
			this.data.markChanged();
			this.data.getEnabledOptions().set(option, new Boolean(value));
		}
	}
	public boolean isVisible(int option)
	{
		if(option == -1) // no radiobutton
			return false;
		
		return this.data.getVisibleOptions().get(option).booleanValue();
	}
	public void setVisible(int option, boolean value)
	{
		if(option == -1) // no radiobutton
			return;
		
		Boolean existingValue = this.data.getVisibleOptions().get(option);
		if(existingValue == null || existingValue.booleanValue() != value)
		{	
			this.data.markChanged();
			this.data.getVisibleOptions().set(option, new Boolean(value));
		}
	}
	public void addButton(int index, int x, int y, int width, String text, int tabIndex)
	{
		this.buttons.add(new RadioButtonInfo(index, x, y, width, text, tabIndex));
	}
	public void setText(int index, String text)
	{
		this.buttons.get(index).setText(text);
		this.data.renameOption(index, text);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (RadioButtonData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(this.data.getEnabledOptions().size() == 0)
		{
			for(int i = 0; i < this.buttons.size(); ++i)
			{
				this.data.getEnabledOptions().add(Boolean.TRUE);
			}
			
			this.data.markUnchanged();
		}
		
		if(this.data.getVisibleOptions().size() == 0)
		{
			for(int i = 0; i < this.buttons.size(); ++i)
			{
				this.data.getVisibleOptions().add(Boolean.TRUE);
			}
			
			this.data.markUnchanged();
		}	
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof RadioBoxChanged)
		{
			int value = ((RadioBoxChanged)event).getSelection();
			if(value != this.data.getValue())
			{
				boolean wasChanged = this.data.wasChanged();
				
				this.data.setValue(value);
				
				if(!wasChanged)
					this.data.markUnchanged();
				
				if(super.valueChangedDelegate != null)
				{	
					super.valueChangedDelegate.handle();
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<radiobox id=\"a");
		sb.append(super.id);
		
		/*
		 * There is no need to send those values - jsCN ignores them (including anchoring!)
		 * 
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" anchor=\"");
		sb.append(super.anchor);
		*/
				
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		// no need to send tabindex on group, each option will have tabIndex 
		// sb.append("\" tabIndex=\"");
		// sb.append(this.tabIndex);
		
		sb.append("\">");
		for (int i = 0; i < this.buttons.size(); ++i)
		{
			RadioButtonInfo item = this.buttons.get(i);
			sb.append("<item text=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(item.getText()));
			sb.append("\" x=\"");
			sb.append(item.getX());
			sb.append("\" y=\"");
			sb.append(item.getY());
			sb.append("\" width=\"");
			sb.append(item.getWidth());
			sb.append("\" height=\"19");
			
			if(super.anchor != ControlAnchoring.TOPLEFT)
			{
				sb.append("\" anchor=\"");
				sb.append(super.anchor);
			}
			
			sb.append("\" bold=\"");
			sb.append("true");
			sb.append("\" tabIndex=\"");
			sb.append(item.getTabIndex());
			sb.append("\"/>");
		}
		sb.append("</radiobox>");
	}
	public void renderData(StringBuffer sb)
	{
		sb.append("<radiobox id=\"a");
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
		
			sb.append("\" selection=\"");
			sb.append(this.data.getValue());
			sb.append("\">");
			for (int i = 0; i < this.data.getEnabledOptions().size(); ++i)
			{			
				sb.append("<item ");	
				
				if(this.data.getVisibleOptions().get(i).booleanValue())
				{
					if(this.data.hasNewName(i))
					{
						sb.append(" text=\"");
						sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getNewName(i)));
						sb.append("\"");
					}
					
					sb.append(" visible=\"true\"");
					sb.append(" enabled=\"");
					sb.append(this.data.getEnabledOptions().get(i).booleanValue() ? "true" : "false");
					sb.append("\"/>");
				}
				else
				{
					sb.append(" visible=\"false\" />");
				}
			}
			sb.append("</radiobox>");
		}
		else
		{
			sb.append("\" />");
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
	
	private RadioButtonData data;
	private ArrayList<RadioButtonInfo> buttons = new ArrayList<RadioButtonInfo>();
	private boolean autoPostBack;
}

class RadioButtonInfo
{
	public RadioButtonInfo(int index, int x, int y, int width, String text, int tabIndex)
	{
		this.index = index;
		this.x = x;
		this.y = y;
		this.width = width;
		this.text = text;
		this.tabIndex = tabIndex;
	}
	public int getIndex()
	{
		return this.index;
	}

	public String getText()
	{
		return this.text;
	}
	public void setText(String value)
	{
		this.text = value;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getTabIndex()
	{
		return this.tabIndex;
	}

	private int index, x, y, width, tabIndex;
	private String text;
}