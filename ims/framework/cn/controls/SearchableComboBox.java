package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.SearchableComboBoxData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class SearchableComboBox extends ims.framework.controls.SearchableComboBox implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean autoPostBack, int maxVisibleItems)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.autoPostBack = autoPostBack;
		this.tabIndex = tabIndex;
		this.maxVisibleItems = maxVisibleItems;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
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
	public void add(Object value, String text)
	{
		this.data.add(value, text);
	}
	public Object getValue()
	{
		return this.data.getValue(this.data.getSelection());
	}
	public void clearOptions()
	{
		this.data.clearOptions();
	}
	public void setCriteria(String value)
	{
		this.data.setCriteria(value);
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (SearchableComboBoxData) data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<combobox id=\"a");
		sb.append(super.id);
		sb.append("\" searchable=\"true");
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		if(this.maxVisibleItems > 0)
		{
			sb.append("\" maxPopupItems=\"");
			sb.append(this.maxVisibleItems);
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<combobox id=\"a");
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
		
			if (this.data.getCriteria().equals(this.data.getText(this.data.getSelection())))
			{	
				if(data.isSelectionChanged())
				{
					sb.append("\" selection=\"");
					sb.append(this.data.getSelection());
					data.setSelectionChanged(false);
				}
			}
			else
			{
				if(data.isCriteriaChanged())
				{
					sb.append("\" value=\"");
					sb.append(this.data.getCriteria());
					data.setCriteriaChanged(false);
				}
			}
			sb.append("\">");
			
			if(data.isOptionsChanged())
			{
				for (int i = 0; i < this.data.size(); ++i)
				{
					sb.append("<option text=\"");
					sb.append(ims.framework.utils.StringUtils.encodeXML(this.data.getText(i)));
					sb.append("\"/>");
				}
				
				data.setOptionsChanged(false);
			}
			
			sb.append("</combobox>");
		}
		else
		{
			sb.append("\" />");
		}
	}
	public boolean wasChanged()
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(data.isEnabledChanged())
				return true;		
			if(data.isSelectionChanged())
				return true;
			if(data.isOptionsChanged())
				return true;
			if(data.isCriteriaChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private SearchableComboBoxData data;
	private boolean autoPostBack;
	private int tabIndex;
	private int maxVisibleItems = -1;
}