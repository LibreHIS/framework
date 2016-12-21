package ims.framework.cn.controls;

import java.io.FileNotFoundException;
import java.text.ParseException;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.DynamicGridData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.DynamicGridCellButtonClicked;
import ims.framework.cn.events.DynamicGridCellItemChecked;
import ims.framework.cn.events.DynamicGridCellTextSubmited;
import ims.framework.cn.events.DynamicGridCellValueChanged;
import ims.framework.cn.events.DynamicGridColumnHeaderClicked;
import ims.framework.cn.events.DynamicGridNodeExpandedCollapsed;
import ims.framework.cn.events.DynamicGridRowSelectionChanged;
import ims.framework.cn.events.DynamicGridRowSelectionCleared;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.controls.DynamicGridCell;
import ims.framework.controls.DynamicGridColumn;
import ims.framework.controls.DynamicGridColumnCollection;
import ims.framework.controls.DynamicGridRow;
import ims.framework.controls.DynamicGridRowCollection;
import ims.framework.controls.DynamicGridRowOptions;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.DynamicCellType;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.PartialDate;
import ims.framework.utils.Time;

/**
 * @author mmihalec
 */
public class DynamicGrid extends ims.framework.controls.DynamicGrid implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, Menu menu, boolean autoPostBackTreeNode, boolean shadow, boolean alternateRowColor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
        this.tabIndex = tabIndex;
        this.autoPostBackTreeNode = autoPostBackTreeNode;
        this.shadow = shadow;
        this.alternateRowColor = alternateRowColor;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;
	}
	public void setHeaderHeight(int value)
	{
		if(value < 0)
			throw new CodingRuntimeException("Invalid header height");
		
		super.headerHeight = value;
	}
	public void resetScrollPosition()
	{
		this.resetScrollPosition = true;
	}
	public void setHeaderMaxHeight(int value)
	{
		if(value < 0)
			throw new CodingRuntimeException("Invalid header height");
		
		this.data.setHeaderMaxHeight(value);
	}
    public void setHeaderValue(String value)
    {
    	this.data.setHeaderValue(value);
    }
	public void setFooterMaxHeight(int value)
	{
		if(value < 0)
			throw new CodingRuntimeException("Invalid footer height");
		
		this.data.setFooterMaxHeight(value);
	}
    public void setFooterValue(String value)
    {
    	this.data.setFooterValue(value);
    }
    public void setRowSelectionChangedEventRequirePdsAuthentication(boolean value)
    {
    	this.data.setRowSelectionChangedEventRequirePdsAuthentication(value);
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
    public void setReadOnly(boolean value)
    {
        super.setReadOnly(value);
        this.data.setReadOnly(value);
    }    
    public boolean setSelectedRow(ims.framework.controls.DynamicGridRow row)
    {
        return this.data.setSelectedRow(row);
    }
    public ims.framework.controls.DynamicGridRow getSelectedRow()
    {
        return this.data.getSelectedRow();
    }
    public Object getValue() 
    {
        return this.data.getValue();
    }
    public boolean setValue(Object value) 
    {
        return this.data.setValue(value);
    }
    public void setTooltip(String value)
    {
        this.data.setTooltip(value);
    }
    public String getTooltip()
    {
        return this.data.getTooltip();
    }
    public DynamicGridColumnCollection getColumns()
    {
        return this.data.getColumns();
    }
    public DynamicGridRowCollection getRows() 
    {
        return this.data.getRows();
    }
    public void setTextColor(Color value)
    {
        this.data.setTextColor(value);
    }
    public Color getTextColor()
    {
        return this.data.getTextColor();
    }
    public void setBackColor(Color value)
    {
        this.data.setBackColor(value);
    }
    public Color getBackColor()
    {
        return this.data.getBackColor();
    }
    public void setSelectable(boolean value)
    {
        this.data.setSelectable(value);
    }
    public boolean isSelectable()
    {
        return this.data.isSelectable();
    }
    public void clear()
    {
        this.data.clear();
    }
    public void setDefaultRowOptions(DynamicGridRowOptions value)
    {
        this.data.setDefaultRowOptions(value);
    }
    public DynamicGridRowOptions getDefaultRowOptions()
    {
        return this.data.getDefaultRowOptions();
    }
    public void resetDefaultRowOptions()
    {
        this.data.resetDefaultRowOptions();
    }
    public void setUnselectable(boolean value) 
	{
    	this.data.setUnselectable(value);
	}
	public boolean isUnselectable() 
	{
		return this.data.isUnselectable();
	}
	public void showCheckBoxes(boolean value)
	{
		this.data.setShowCheckBoxes(value);
	}
	public void setCheckBoxesAutoPostBack(boolean value)
	{
		this.data.setCheckBoxesAutoPostBack(value);		
	}
    public void restore(IControlData data, boolean isNew) 
    {
        this.data = (DynamicGridData) data;
        super.enabled = this.data.isEnabled();
        super.visible = this.data.isVisible();
        super.readOnly = this.data.isReadOnly();
        
        if(isNew)
        	this.resetScrollPosition = true;
    }
    public boolean fireEvent(IControlEvent event) throws PresentationLogicException 
    {        
    	if(event instanceof InvalidControlValue)
    	{
    		// ignore the event, there is nothing we can do at this stage. 		
    		return true;
    	}
    	else if(event instanceof DynamicGridCellButtonClicked)
        {            
            if(this.cellButtonClickedDelegate != null)
            {
                DynamicGridCellButtonClicked typedEvent = (DynamicGridCellButtonClicked)event;                
                DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
                if(row == null)
                	throw new RuntimeException("Dynamic grid row not found");
                DynamicGridColumn column = this.data.getColumns().get(typedEvent.getColumn());
                if(column == null)
                	throw new RuntimeException("Dynamic grid column not found");
                DynamicGridCell cell = row.getCells().get(column);
                if(cell == null)
                	throw new RuntimeException("Dynamic grid cell not found");
               
                cell.setTableRowId(typedEvent.getTableRowId());
                cell.setTableCellId(typedEvent.getTableCellId());
                
                this.cellButtonClickedDelegate.handle(cell);
            }
            
            return true;
        }
        else if(event instanceof DynamicGridCellValueChanged)
        {            
        	boolean selectedRowChanged = this.data.isSelectedRowChanged();
        	boolean rowsChanged = this.data.isRowsChanged();    	
            
            DynamicGridCellValueChanged typedEvent = (DynamicGridCellValueChanged)event;
            DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
            if(row == null)
            	throw new RuntimeException("Dynamic grid row not found");
            DynamicGridColumn column = this.data.getColumns().get(typedEvent.getColumn());
            if(column == null)
            	throw new RuntimeException("Dynamic grid column not found");
            DynamicGridCell cell = row.getCells().get(column);
            if(cell == null)
            	throw new RuntimeException("Dynamic grid cell not found");
            if(typedEvent.getValue() != null)
            {
                int selectedIndex = Integer.parseInt(typedEvent.getValue());
                if(selectedIndex < 0)
                    cell.setValue(null);
                else
                    cell.setValue(cell.getItems().get(selectedIndex).getValue());
            }   
            else
            {
                String value = typedEvent.getText();
                
                if(value == null || value.length() == 0)
                    cell.setValue(null);
                else
                {
                    if(cell.getType().equals(DynamicCellType.STRING))
                    {
                        cell.setValue(value);
                    }                    
                    else if(cell.getType().equals(DynamicCellType.WRAPTEXT))
                    {
                        cell.setValue(value);
                    }
                    else if(cell.getType().equals(DynamicCellType.COMMENT))
                    {
                        cell.setValue(value);
                    }
                    else if(cell.getType().equals(DynamicCellType.DATE))
                    {
                        try 
                        {
                            cell.setValue(new Date(value));
                        } 
                        catch (ParseException e) 
                        {
                            e.printStackTrace();
                            throw new RuntimeException("Parse error, unable to set the date value for dynamic grid cell, the value was: " + value);
                        }                            
                    }
                    else if(cell.getType().equals(DynamicCellType.DATETIME))
                    {
                        try 
                        {
                            cell.setValue(new DateTime(value));
                        } 
                        catch (ParseException e) 
                        {
                            e.printStackTrace();
                            throw new RuntimeException("Parse error, unable to set the date time value for dynamic grid cell, the value was: " + value);
                        }                            
                    }
                    else if(cell.getType().equals(DynamicCellType.TIME))
                    {
                        cell.setValue(new Time(value));                            
                    }
                    else if(cell.getType().equals(DynamicCellType.INT))
                    {
                        cell.setValue(new Integer(Integer.parseInt(value)));                            
                    }
                    else if(cell.getType().equals(DynamicCellType.BOOL))
                    {
                        cell.setValue(Boolean.valueOf(value));                            
                    }
                    else if(cell.getType().equals(DynamicCellType.DECIMAL))
                    {
                        cell.setValue(new Float(Float.parseFloat(value)));                            
                    }
                    else if(cell.getType().equals(DynamicCellType.PARTIALDATE))
                    {
                    	try
                    	{
                    		cell.setValue(new PartialDate(value));
                    	}
	                	catch (Exception e) 
	                    {
	                        e.printStackTrace();
	                        throw new RuntimeException("Parse error, unable to set the partial date value for dynamic grid cell, the value was: " + value);
	                    }   
                    	
                    }                    
                }
            }
            
            if(!selectedRowChanged)
            	this.data.setSelectedRowUnchanged();
            if(!rowsChanged)
            	this.data.setRowsUnchanged();
            
            if(this.cellValueChangedDelegate != null)
                this.cellValueChangedDelegate.handle(cell);
            
            return true;
        }
        else if(event instanceof DynamicGridRowSelectionCleared)
        { 
        	boolean selectedRowChanged = this.data.isSelectedRowChanged();
        	
        	this.data.setSelectedRow(null);
        	
        	if(!selectedRowChanged)
            	this.data.setSelectedRowUnchanged();
        	
        	if(this.rowSelectionClearedDelegate != null)
        		this.rowSelectionClearedDelegate.handle();
        	
        	return true;
        }
        else if(event instanceof DynamicGridRowSelectionChanged)
        {       
        	boolean selectedRowChanged = this.data.isSelectedRowChanged();    	
            
            DynamicGridRowSelectionChanged typedEvent = (DynamicGridRowSelectionChanged)event;
            DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
            this.data.setSelectedRow(row);
            
            if(!selectedRowChanged)
            	this.data.setSelectedRowUnchanged();
            
            if(this.rowSelectionChangedDelegate != null)
                this.rowSelectionChangedDelegate.handle(row, typedEvent.getMouseButton());
            
            return true;
        }
        else if(event instanceof DynamicGridCellTextSubmited)
        {       
        	boolean rowsChanged = this.data.isRowsChanged();	
                    	
            DynamicGridCellTextSubmited typedEvent = (DynamicGridCellTextSubmited)event;
            DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
            DynamicGridColumn column = this.data.getColumns().get(typedEvent.getColumn());
            DynamicGridCell cell = row.getCells().get(column);
            cell.setTypedText(typedEvent.getText());
            cell.setValue(null);
            
            if(!rowsChanged)
            	this.data.setRowsUnchanged();
            
            if(this.cellTextSubmitedDelegate != null)
                this.cellTextSubmitedDelegate.handle(cell);
            
            return true;
        }
        else if(event instanceof DynamicGridColumnHeaderClicked)
        {            
            if(this.columnHeaderClickedDelegate != null)
            {
                DynamicGridColumnHeaderClicked typedEvent = (DynamicGridColumnHeaderClicked)event;
                this.columnHeaderClickedDelegate.handle(this.data.getColumns().get(typedEvent.getColumn()));
            }
            
            return true;
        }
        else if(event instanceof DynamicGridNodeExpandedCollapsed)
        {
        	boolean rowsChanged = this.data.isRowsChanged();    	
                    	
            DynamicGridNodeExpandedCollapsed typedEvent = (DynamicGridNodeExpandedCollapsed)event;
            DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
            
            if(row == null)
            	throw new RuntimeException("Dynamic grid row not found");
        
            boolean shouldFireCheckEvent = row.isChecked() != typedEvent.isChecked();
            boolean shouldFireExpandCollapseEvent = row.isExpanded() != typedEvent.isExpanded();
            
            row.setExpanded(typedEvent.isExpanded());
            row.setChecked(typedEvent.isChecked());
            
            if(!rowsChanged)
            	this.data.setRowsUnchanged();
            
            if(autoPostBackTreeNode && shouldFireExpandCollapseEvent && this.rowExpandedCollapsedDelegate != null)
            	this.rowExpandedCollapsedDelegate.handle(row);
            
            if(shouldFireCheckEvent && this.rowCheckedDelegate != null)
            	this.rowCheckedDelegate.handle(row);
            
            return true;
        }
        else if(event instanceof DynamicGridCellItemChecked)
        {
        	boolean rowsChanged = this.data.isRowsChanged(); 	
        	
        	DynamicGridCellItemChecked typedEvent = (DynamicGridCellItemChecked)event;        	
        	DynamicGridRow row = this.data.findRow(typedEvent.getRowId());
            DynamicGridColumn column = this.data.getColumns().get(typedEvent.getColumn());
            DynamicGridCell cell = row.getCells().get(column);
            
            if(cell.getMaxCheckedItemsForMultiSelect() != null && cell.getMaxCheckedItemsForMultiSelect().intValue() == 1 && typedEvent.getIsChecked())
            	cell.getItems().uncheckAll();
            
            for(int x = 0; x < cell.getItems().size(); x++)
            {
            	if(x == typedEvent.getItemIndex())
            	{
            		cell.getItems().get(x).setChecked(typedEvent.getIsChecked());
            		break;
            	}
            }
        	
            if(!rowsChanged)
            	this.data.setRowsUnchanged();

            if(this.cellValueChangedDelegate != null)
                this.cellValueChangedDelegate.handle(cell);
            
            return true;
        }
    	
    	return false;
    }
    public void renderControl(StringBuffer sb) throws ConfigurationException 
    {
        sb.append("<dynamicgrid2 id=\"a");
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
		
		sb.append("\" alternateBackcolor=\"");
		sb.append(alternateRowColor ? "true" : "false");
		//if(!alternateRowColor)
		{
			//sb.append("\" alternateBackcolor=\"false");
			//sb.append("\" gridLines=\"vertical");
		}		
		
		if(shadow)
		{
			sb.append("\" shadow=\"true");
		}
		if(super.headerHeight != 24)
		{
			sb.append("\" headerHeight=\"");
			sb.append(super.headerHeight);
		}
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
		}
		if(this.autoPostBackTreeNode)
		{
			sb.append("\" treeAutoPostBack=\"true");
		}
		
		// Hardcoded, if a row is selected it will became visible
		// Does not need to be sent anymore as default value has been changed to true
		// sb.append("\" alwaysScrollToSelected=\"true");
		
		sb.append("\" />");
    }
    public void renderData(StringBuffer sb) throws ConfigurationException 
    {
        sb.append("<dynamicgrid2 id=\"a");		
        sb.append(super.id);        
        
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledUnchanged();
				}
			}
			
			if(this.data.isEnabled())
			{
				if(data.isReadOnlyChanged())
				{
					sb.append("\" readOnly=\"");
					sb.append(this.data.isReadOnly() ? "true" : "false");
					data.setReadOnlyUnchanged();
				}				
				
				if(data.isShowingCheckBoxes() && data.isCheckBoxesAutoPostBackChanged())
				{
					sb.append("\" treeAutoPostBack=\"");
					sb.append(this.data.isCheckBoxesAutoPostBack() ? "true" : "false");
					data.setCheckBoxesAutoPostBackChanged(false);
				}
			}
			
			if(resetScrollPosition)
			{
				resetScrollPosition = false; 
				sb.append("\" resetScrollPosition=\"true");				
			}
			
			if(data.isSelectableChanged())
			{
				sb.append("\" selectorVisible=\"");
				sb.append(this.data.isSelectable() ? "true" : "false");
				data.setSelectableUnchanged();
			}
			
			if(data.isUnselectableChanged())
			{
				sb.append("\" allowClearSelection=\"");
				sb.append(this.data.isUnselectable() ? "true" : "false");
				data.setUnselectableUnchanged();
			}
		
			if(data.isSelectedRowChanged())
			{
				sb.append("\" selectedRowID=\"");
				sb.append(this.data.getSelectedRowId());
				data.setSelectedRowUnchanged();
			}
	        		
			if(data.isRowsChanged())
			{
				sb.append("\" isTree=\"");
				sb.append(this.data.isTree() ? "true" : "false");
			}
			
			if (data.isRowSelectionChangedEventRequirePdsAuthenticationChanged())
			{
				sb.append("\" rowSelectionChangedEventRequirePdsAuthentication=\"");
				sb.append(this.data.isRowSelectionChangedEventRequirePdsAuthenticationChanged() ? "true" : "false");
				data.setRowSelectionChangedEventRequirePdsAuthenticationUnchanged();
			}
			
			sb.append("\">");
	        
			if(data.isHeaderMaxHeightChanged() && this.data.getHeaderMaxHeight() == 0)
			{
				sb.append("<header/>");
				data.setHeaderMaxHeightUnchanged();
			}
			else if(data.isHeaderMaxHeightChanged() || data.isHeaderValueChanged())
			{
				sb.append("<header ");
				
				if(data.isHeaderMaxHeightChanged())
				{
					sb.append("maxHeight=\"" + data.getHeaderMaxHeight() + "\"");
					data.setHeaderMaxHeightUnchanged();
				}					
				
				if(data.isHeaderValueChanged())
				{
					sb.append(">");					
					sb.append("<![CDATA[");
					sb.append(data.getHeaderValue() == null ? "" : data.getHeaderValue());
					sb.append("]]>");
					sb.append("</header>");
					data.setHeaderValueUnchanged();
				}
				else
				{
					sb.append("/>");
				}
			}
			if(data.isFooterMaxHeightChanged() && this.data.getFooterMaxHeight() == 0)
			{
				sb.append("<footer/>");
				data.setFooterMaxHeightUnchanged();
			}
			else if(data.isFooterMaxHeightChanged() || data.isFooterValueChanged())
			{
				sb.append("<footer ");
				
				if(data.isFooterMaxHeightChanged())
				{
					sb.append("maxHeight=\"" + data.getFooterMaxHeight() + "\"");
					data.setFooterMaxHeightUnchanged();
				}					
				
				if(data.isFooterValueChanged())
				{
					sb.append(">");					
					sb.append("<![CDATA[");
					sb.append(data.getFooterValue() == null ? "" : data.getFooterValue());
					sb.append("]]>");
					sb.append("</footer>");
					data.setFooterValueUnchanged();
				}
				else
				{
					sb.append("/>");
				}
			}
			
			if(data.isRowsChanged())
			{
				if(this.data.isTree())
				{
					sb.append("<nodetype type=\"folder\" imageUrl=\"g/fold.gif\" expandedImageUrl=\"g/openfold.gif\" checkBox=\"" + (data.isShowingCheckBoxes() ? "true" : "false") + "\"/>");
				}
		        
				//sb.append("<nodetype type=\"item\" imageUrl=\"g/deficon.gif\" checkBox=\"" + (data.isShowingCheckBoxes() ? "true" : "false") + "\"/>");
		        sb.append("<nodetype type=\"item\" checkBox=\"" + (data.isShowingCheckBoxes() ? "true" : "false") + "\"/>");
			}
			
			// columns
	        if(data.isColumsChanged())
	        {
	        	sb.append(this.data.renderColumns());
	        	data.setColumnsUnchanged();
	        }
			
			// rows
			try 
			{
				if(data.isRowsChanged())
				{
					sb.append(this.data.renderRows());
					data.setRowsUnchanged();
				}
			} 
			catch (FileNotFoundException e) 
			{		
				throw new ConfigurationException(e.getMessage());
			} 			
			
			sb.append("</dynamicgrid2>");
		}
		else
		{
			sb.append("\" />");
		}
		
		this.data.setAllUnchanged();
    }   
    public boolean wasChanged() 
    {
    	if(resetScrollPosition)
    		return true;
    	
    	if(data.isVisibleChanged())
    		return true;
    	
    	if(visible)
    	{
    		if(!hasAnyParentDisabled())
    		{
    			if(data.isEnabledChanged())
    				return true;
    		}
    		
			if(enabled)
			{
				if(data.isReadOnlyChanged())
					return true;
				
				if(data.isShowingCheckBoxes() && data.isCheckBoxesAutoPostBackChanged())
					return true;
			}    			
			
			if(data.isSelectableChanged())
    			return true;
			
			if(data.isUnselectableChanged())
    			return true;
		
			if(data.isSelectedRowChanged())
    			return true;
	        
			if(data.isFooterMaxHeightChanged())
				return true;
			
			if(data.isFooterValueChanged() && this.data.getFooterMaxHeight() != 0)
				return true;
			
			if(data.isColumsChanged())
	    		return true;
			
	    	if(data.isRowsChanged())
	    		return true;
    	}
    	
    	return false;
	}
	public void markUnchanged() 
	{			
	}
    
    private DynamicGridData data;
    private boolean resetScrollPosition = false;
    private int tabIndex;
    private boolean autoPostBackTreeNode = false;
    private boolean shadow = false;
    private boolean alternateRowColor = true;
}
