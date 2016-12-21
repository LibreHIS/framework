package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.RecordBrowserData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.RecordBrowserChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.StringUtils;

public class RecordBrowser extends ims.framework.controls.RecordBrowser implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
        this.tabIndex = tabIndex;		
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (RecordBrowserData) data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();		
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof RecordBrowserChanged)
		{
			int newSelection = ((RecordBrowserChanged) event).getSelectedID();
			if(this.data.getSelection() != newSelection) 
			{
				boolean selectionChanged = data.isSelectionChanged();				
				this.data.setSelection(newSelection);				
				data.setSelectionChanged(selectionChanged);
				
				if(super.valueChangedDelegate != null)
				{
				    if(this.autoPostBack)
				        super.valueChangedDelegate.handle();
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<recordnavigator id=\"a");
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
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<recordnavigator id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleChanged(false);
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledChanged(false);
				}
			}
			
			if(data.hasTooltip()) 
	        {
				if(data.isTooltipChanged())
				{
					if(data.getTooltip() != null) 
					{
						sb.append("\" tooltip=\"");
						sb.append(StringUtils.encodeXML(data.getTooltip()));
					}
					else
					{
						sb.append("\" cleartooltip=\"");
						sb.append(StringUtils.encodeXML(Boolean.valueOf(true).toString()));
					}
					
					data.setTooltipChanged(false);
				}
	        }
	        
			if(data.isSelectionChanged())
			{
				sb.append("\" selectedID=\"");
				sb.append(this.data.getSelection());
				data.setSelectionChanged(false);
			}
			
			if(data.isValuesChanged())
			{
				if(this.data.getValues().size() == 0)
				{
					sb.append("\" >");
					sb.append("<nooptions/>");
					sb.append("</recordnavigator>");
				}
				else
				{
				    sb.append("\" >");
					for (int i = 0; i < this.data.size(); ++i)
					{
						sb.append("<option id=\"");
						sb.append(i);
	                    sb.append("\" text=\"");
	                    sb.append("Record " + (i+1) + " of " + this.data.size() + ": ");
						sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getText(i)));
	                    sb.append("\" ");
	                    
	                    if(this.data.getTextColor(i) != null)
	                        sb.append(" textColor=\"" + this.data.getTextColor(i) + "\"");
	                    if(this.data.getImage(i) != null)
	                    {
	                        sb.append(" img=\"");
	                        sb.append(this.data.getImage(i).getImagePath());
	                        sb.append("\" ");
	                    }
	                    sb.append(" />");
					}			
					sb.append("</recordnavigator>");
				}
				
				data.setValuesChanged(false);
			}
			else
			{	
				sb.append("\" />");
			}
		}
		else
		{
			sb.append("\" />");
		}
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
	public void newRow(Object value, String text)
	{
		newRow(value, text, null, null);
	}
	public void newRow(Object value, String text, Color textColor)
	{
		newRow(value, text, null, textColor);
	}
	public void newRow(Object value, String text, Image image)
	{
		newRow(value, text, image, null);
	}
	public void newRow(Object value, String text, Image image, Color textColor)
	{
		this.data.add(value, text, image, textColor);
	}		
	public void newRow(int index, Object value, String text)
	{
		newRow(index, value, text, null, null);
	}
	public void newRow(int index, Object value, String text, Color textColor)
	{
		newRow(index, value, text, null, textColor);
	}
	public void newRow(int index, Object value, String text, Image image)
	{
		newRow(index, value, text, image, null);
	}
	public void newRow(int index, Object value, String text, Image image, Color textColor)
	{
		this.data.add(index, value, text, image, textColor);
	}	
	public Object getValue()
	{
		if(this.data.isEmpty() || this.data.getSelection() == -1)
			return null;		
		return this.data.getValue(this.data.getSelection());
	}
	public ArrayList getValues()
	{
		return this.data.getValues();
	}
	public void setValue(Object value)
	{
		if (value == null)
			this.data.setSelection(-1);
		else
			this.data.setSelection(value);
	}
	public int size()
	{
		return this.data.size();
	}
	public int getSelectedIndex()
	{
		return this.data.getSelection();
	}
	public void clear()
	{
		this.data.clear();
	}
	public void setTooltip(String value)
	{		
        this.data.setTooltip(value);
	}
	public boolean wasChanged()
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
			}
			
			if(data.isSelectionChanged())
				return true;
			if(data.isValuesChanged())
				return true;
			if(data.isTooltipChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged()
	{		
	}
	
	private boolean autoPostBack = true;
	private RecordBrowserData data;	
	private int tabIndex;	
}
