package ims.framework.cn.data;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import ims.framework.IEnhancedItem;
import ims.base.interfaces.IModifiable;
import ims.framework.controls.DynamicGridCellItem;
import ims.framework.controls.DynamicGridCellItemCollection;
import ims.framework.controls.DynamicGridCellOptions;
import ims.framework.controls.DynamicGridCellTable;
import ims.framework.controls.DynamicGridRowOptions;
import ims.framework.enumerations.Alignment;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.DynamicCellDecoratorMode;
import ims.framework.enumerations.DynamicCellType;
import ims.framework.enumerations.SortMode;
import ims.framework.enumerations.VerticalAlignment;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.DateTimeFormat;
import ims.framework.utils.DecimalFormat;
import ims.framework.utils.Image;
import ims.framework.utils.PartialDate;
import ims.framework.utils.StringUtils;
import ims.framework.utils.Time;

/**
 * @author mmihalec
 */
public class DynamicGridData implements IControlData
{    	
	private static final long serialVersionUID = -812105099687949924L;
	public boolean isTree()
    {
        int rowCount = this.rows.size();
        for(int x = 0; x < rowCount; x++)
        {
            DynamicGridRow row = (DynamicGridRow)this.rows.get(x);
            if(row.getRows().size() > 0)
                return true;
        }
        
        return false;
    }
    public Object getValue() 
    {
        DynamicGridRow row = this.getSelectedRow();
        return row == null ? null : row.getValue();
    }
    public boolean setValue(Object value) 
    {
        if(value == null)
        {
        	if(!this.selectedRowChanged)
         		this.selectedRowChanged = this.selectedRowId != -1;
        	 
            this.selectedRowId = -1;
            return true;
        }
        
        int rowCount = this.rows.size();
        for(int x = 0; x < rowCount; x++)
        {
            DynamicGridRow row = (DynamicGridRow)this.rows.get(x);
            if(value.equals(row.getValue()))
            {
            	if(!this.selectedRowChanged)
            		this.selectedRowChanged = this.selectedRowId != row.id;
            	
                this.selectedRowId = row.id;
                return true;
            }
            
            if(setValue(row, value))
                return true;
        }
        
        if(!this.selectedRowChanged)
    		this.selectedRowChanged = this.selectedRowId != -1;
        
        this.selectedRowId = -1;
        return false;
    }
    private boolean setValue(DynamicGridRow parentRow, Object value)
    {
        int rowCount = parentRow.getRows().size();
        for(int x = 0; x < rowCount; x++)
        {
            DynamicGridRow childRow = (DynamicGridRow)parentRow.getRows().get(x);
            if(value.equals(childRow.getValue()))
            {
            	if(!this.selectedRowChanged)
            		this.selectedRowChanged = this.selectedRowId != childRow.id;
            	
                this.selectedRowId = childRow.id;
                return true;
            }    
            
            if(setValue(childRow, value))
                return true;
        }        
        
        return false;
    }
    public void setTooltip(String value)
    { 
    	if(!this.tooltipChanged)
		{
    		if(this.tooltip == null)
    			this.tooltipChanged = value != null;
    		else
    			this.tooltipChanged = !this.tooltip.equals(value);
		}
    	
        this.tooltip = value;
    }
    public String getTooltip()
    {
        return this.tooltip == null ? "" : this.tooltip;
    }
    public void setButtonText(String value)
    { 
    	if(!this.buttonTextChanged)
		{
    		if(this.buttonText == null)
    			this.buttonTextChanged = value != null;
    		else
    			this.buttonTextChanged = !this.buttonText.equals(value);
		}
    	
        this.buttonText = value;
    }
    public String getButtonText()
    {
        return this.buttonText == null ? "" : this.buttonText;
    }
    public void setButtonTextColor(Color value)
    { 
    	if(!this.buttonTextColorChanged)
		{
    		if(this.buttonTextColor == null)
    			this.buttonTextColorChanged = value != null;
    		else
    			this.buttonTextColorChanged = !this.buttonTextColor.equals(value);
		}
    	
        this.buttonTextColor = value;
    }
    public Color getButtonTextColor()
    {
        return this.buttonTextColor == null ? Color.Red : this.buttonTextColor;
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
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
    public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		if(!this.readOnlyChanged)
			this.readOnlyChanged = this.readOnly != value;
		
    	this.readOnly = value;
	}
	public void setTextColor(Color value)
	{
		if(!this.textColorChanged)
		{
    		if(this.textColor == null)
    			this.textColorChanged = value != null;
    		else
    			this.textColorChanged = !this.textColor.equals(value);
		}
		
	    this.textColor = value;
	}
	public Color getTextColor()
	{
	    return this.textColor == null ? Color.Default : this.textColor;
	}
	public void setBackColor(Color value)
	{
		if(!this.backColorChanged)
		{
    		if(this.backColor == null)
    			this.backColorChanged = value != null;
    		else
    			this.backColorChanged = !this.backColor.equals(value);
		}
		
	    this.backColor = value;
	}
	public Color getBackColor()
	{
	    return this.backColor == null ? Color.Default : this.backColor;
	}
	public DynamicGridColumnCollection getColumns()
	{
	    return this.columns;
	}
    public int getSelectedRowId()
    {
        return this.selectedRowId;
    }
    public void setSelectedRowIndex(int value)
    {
    	if(!this.selectedRowChanged)
			this.selectedRowChanged = this.selectedRowId != value;
        
    	this.selectedRowId = value;
    }
    public DynamicGridRow getSelectedRow()
    {
        if(this.selectedRowId == -1)
            return null;
        
        return findRow(this.selectedRowId);
    }
    public DynamicGridRow findRow(int rowID)
    {
        int noRows = this.rows.size();
        for(int x = 0; x < noRows; x++)
        {
            DynamicGridRow row = (DynamicGridRow)this.rows.get(x);
            if(row.getID() == rowID)
                return row;
            row = findRow(row, rowID);
            if(row != null)
                return row;
        }
        return null;
    }
    private DynamicGridRow findRow(DynamicGridRow parentRow, int rowID)
    {
        int noRows = parentRow.getRows().size();
        for(int x = 0; x < noRows; x++)
        {
            DynamicGridRow row = (DynamicGridRow)parentRow.getRows().get(x);
            if(row.getID() == rowID)
                return row;
            row = findRow(row, rowID);
            if(row != null)
                return row;
        }
        return null;
    }
    private DynamicGridRow findRow(DynamicGridRow row)
    {
        int noRows = this.rows.size();
        for(int x = 0; x < noRows; x++)
        {
            DynamicGridRow currentRow = (DynamicGridRow)this.rows.get(x);
            if(currentRow.equals(currentRow))
                return row;
            currentRow = findRow(currentRow, row);
            if(currentRow != null)
                return currentRow;
        }
        return null;
    }
    private DynamicGridRow findRow(DynamicGridRow parentRow, DynamicGridRow row)
    {
        int noRows = parentRow.getRows().size();
        for(int x = 0; x < noRows; x++)
        {
            DynamicGridRow currentRow = (DynamicGridRow)parentRow.getRows().get(x);
            if(currentRow.equals(row))
                return currentRow;
            currentRow = findRow(currentRow, row);
            if(currentRow != null)
                return currentRow;
        }
        return null;
    }
    /**
     * This method checks to see if the selected row was deleted.
     * If the selected row was deleted it clears the selection.
     */
    public void checkRowSelection()
    {
    	if(shouldClearRowSelection(rows))
    		setSelectedRow(null);
    }
    private boolean shouldClearRowSelection(DynamicGridRowCollection rows)
    {
    	for(int x = 0; x < rows.size(); x++)
    	{
    		DynamicGridRow row = (DynamicGridRow)rows.get(x);
    		if(row.id == this.selectedRowId)
    			return false;    
    		
    		if(shouldClearRowSelection((DynamicGridRowCollection)row.getRows()))
    			return true;
    	}
    	
    	return true;
    }
    public boolean setSelectedRow(ims.framework.controls.DynamicGridRow row)
    {
    	if(row != null)
    	{
	        DynamicGridRow searchRow = findRow((DynamicGridRow)row);
	        if(searchRow != null)
	        {
	        	if(!this.selectedRowChanged)
	        		this.selectedRowChanged = this.selectedRowId != searchRow.id;
	        	
	            this.selectedRowId = searchRow.id;
	            return true;
	        }
    	}
        
        if(!this.selectedRowChanged)
    		this.selectedRowChanged = this.selectedRowId != -1;
        
        this.selectedRowId = -1;
        return false;
    }
    public DynamicGridRowCollection getRows()
	{
	    return this.rows;
	}
	public void clear()
	{
		if(!this.selectedRowChanged)
			this.selectedRowChanged = this.selectedRowId != -1;
		
				
	    this.rows.clear();
	    this.columns.clear();
        this.selectedRowId = -1;
	}
	public void setUnselectable(boolean value) 
	{
		if(!this.unselectableChanged)
			this.unselectableChanged = this.unselectable != value;
		
		this.unselectable = value;		
	}
	public boolean isUnselectable() 
	{
		return this.unselectable;
	}
	public void setShowCheckBoxes(boolean value)
	{
		if(!this.showCheckBoxesChanged)
			this.showCheckBoxesChanged = this.showCheckBoxes != value;
		
		this.showCheckBoxes = value;
	}
	public boolean isShowingCheckBoxes()
	{
		return this.showCheckBoxes;
	}
	public void setHeaderMaxHeight(int value)
	{
		if(!this.headerMaxHeightChanged)
			this.headerMaxHeightChanged = this.headerMaxHeight != value;
		
		this.headerMaxHeight = value;
	}	
	public int getHeaderMaxHeight()
	{
		return this.headerMaxHeight;
	}
	public void setHeaderValue(String value)
	{		
		if(!this.headerValueChanged)
		{
    		if(this.headerValue == null)
    			this.headerValueChanged = value != null;
    		else
    			this.headerValueChanged = !this.headerValue.equals(value);
		}
		
		this.headerValue = value;
	}
	public String getHeaderValue()
	{
		return this.headerValue;
	}
	public void setFooterMaxHeight(int value)
	{
		if(!this.footerMaxHeightChanged)
			this.footerMaxHeightChanged = this.footerMaxHeight != value;
		
		this.footerMaxHeight = value;
	}	
	public int getFooterMaxHeight()
	{
		return this.footerMaxHeight;
	}
	public void setFooterValue(String value)
	{		
		if(!this.footerValueChanged)
		{
    		if(this.footerValue == null)
    			this.footerValueChanged = value != null;
    		else
    			this.footerValueChanged = !this.footerValue.equals(value);
		}
		
		this.footerValue = value;
	}
	public String getFooterValue()
	{
		return this.footerValue;
	}
	public void setRowSelectionChangedEventRequirePdsAuthentication(boolean value)
	{
		if(!this.rowSelectionChangedEventRequirePdsAuthenticationChanged)
			this.rowSelectionChangedEventRequirePdsAuthenticationChanged = this.rowSelectionChangedEventRequirePdsAuthentication != value;
		
		this.rowSelectionChangedEventRequirePdsAuthentication = value;
	}
	public boolean getRowSelectionChangedEventRequirePdsAuthentication()
	{
		return this.rowSelectionChangedEventRequirePdsAuthentication;
	}
	
    public String renderColumns()
    {
	    return this.columns.render();
    }
	public String renderRows() throws ConfigurationException, FileNotFoundException
	{
	    return this.rows.render(this.columns);
	}
    public void setSelectable(boolean value)
    {
    	if(!this.selectableChanged)
			this.selectableChanged = this.selectable != value;
    	if(!allChanged)
    		this.allChanged = this.selectable != value;
    	
        this.selectable = value;
    }
    public boolean isSelectable()
    {
        return this.selectable;
    }   
    public void setDefaultRowOptions(DynamicGridRowOptions value)
    {
        if(value == null)
            throw new RuntimeException("Invalid dynamic grid row default options");
        
        this.defaultRowOptionsChanged = true;
        this.defaultRowOptions = (DynamicGridRowOptions)value.clone();
    }
    public DynamicGridRowOptions getDefaultRowOptions()
    {
        return (DynamicGridRowOptions)this.defaultRowOptions.clone();
    }
    public void resetDefaultRowOptions()
    {
    	this.defaultRowOptionsChanged = true;
        this.defaultRowOptions = new DynamicGridRowOptions();
    }
    public int getNextRowID()
    {
        return nextRowID++;
    }    
    public void setSelectedRowUnchanged()
	{
		this.selectedRowChanged = false;
	}
	public boolean isSelectedRowChanged()
	{
		return allChanged || selectedRowChanged || selectedRowId != -1;
	}
	public void setTooltipChanged(boolean tooltipChanged)
	{
		this.tooltipChanged = tooltipChanged;
	}
	public boolean isTooltipChanged()
	{
		return allChanged || tooltipChanged;
	}
	public void setButtonTextChanged(boolean buttonTextChanged)
	{
		this.buttonTextChanged = buttonTextChanged;
	}
	public boolean isButtonTextChanged()
	{
		return allChanged || buttonTextChanged;
	}
	public void setButtonTextColorChanged(boolean buttonTextColorChanged)
	{
		this.buttonTextColorChanged = buttonTextColorChanged;
	}
	public boolean isButtonTextColorChanged()
	{
		return allChanged || buttonTextColorChanged;
	}
	public void setUnselectableUnchanged()
	{
		this.unselectableChanged = false;
	}
	public boolean isUnselectableChanged()
	{
		return allChanged || unselectableChanged;
	}
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return allChanged || enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return allChanged || visibleChanged;
	}
	public void setReadOnlyUnchanged()
	{
		this.readOnlyChanged = false;
	}
	public boolean isReadOnlyChanged()
	{
		return allChanged || readOnlyChanged;
	}
	public void setTextColorChanged(boolean textColorChanged)
	{
		this.textColorChanged = textColorChanged;
	}
	public boolean isTextColorChanged()
	{
		return allChanged || textColorChanged;
	}
	public void setBackColorChanged(boolean backColorChanged)
	{
		this.backColorChanged = backColorChanged;
	}
	public boolean isBackColorChanged()
	{
		return allChanged || backColorChanged;
	}
	public void setSelectableUnchanged()
	{
		this.selectableChanged = false;
	}
	public boolean isSelectableChanged()
	{
		return allChanged || selectableChanged;
	}
	public void setShowCheckBoxesChanged(boolean showCheckBoxesChanged)
	{
		this.showCheckBoxesChanged = showCheckBoxesChanged;
	}
	public boolean isShowCheckBoxesChanged()
	{
		return allChanged || showCheckBoxesChanged;
	}
	public void setHeaderMaxHeightUnchanged()
	{
		this.headerMaxHeightChanged = false;
	}
	public void setFooterMaxHeightUnchanged()
	{
		this.footerMaxHeightChanged = false;
	}
	public boolean isHeaderMaxHeightChanged()
	{
		return allChanged || headerMaxHeightChanged;
	}
	public boolean isFooterMaxHeightChanged()
	{
		return allChanged || footerMaxHeightChanged;
	}
	public void setHeaderValueUnchanged()
	{
		this.headerValueChanged = false;
	}
	public void setFooterValueUnchanged()
	{
		this.footerValueChanged = false;
	}
	public boolean isHeaderValueChanged()
	{
		return allChanged || headerValueChanged;
	}
	public boolean isRowSelectionChangedEventRequirePdsAuthenticationChanged()
	{
		return allChanged || rowSelectionChangedEventRequirePdsAuthenticationChanged;
	}
	public void setRowSelectionChangedEventRequirePdsAuthenticationUnchanged()
	{
		this.rowSelectionChangedEventRequirePdsAuthenticationChanged = false;
	}
	public boolean isFooterValueChanged()
	{
		return allChanged || footerValueChanged;
	}
	public void setColumnsUnchanged()
	{
		columns.markUnchanged();
	}
	public boolean isColumsChanged()
	{
		return allChanged || this.columns.wasChanged();
	}
	public void setRowsUnchanged()
	{
		this.rows.markUnchanged();
	}
	public boolean isRowsChanged()
	{
		return allChanged || this.rows.wasChanged();
	}

	public void setDefaultRowOptionsChanged(boolean defaultRowOptionsChanged)
	{
		this.defaultRowOptionsChanged = defaultRowOptionsChanged;
	}
	public boolean isDefaultRowOptionsChanged()
	{
		return allChanged || defaultRowOptionsChanged;
	}

	public void setCheckBoxesAutoPostBack(boolean value)
	{
		if(!this.isCheckBoxesAutoPostBackChanged())
			this.setCheckBoxesAutoPostBackChanged(this.checkBoxesAutoPostBack != value); 
		
		this.checkBoxesAutoPostBack = value;
	}
	public boolean isCheckBoxesAutoPostBack()
	{
		return checkBoxesAutoPostBack;
	}

	public void setCheckBoxesAutoPostBackChanged(boolean checkBoxesAutoPostBackChanged)
	{
		this.checkBoxesAutoPostBackChanged = checkBoxesAutoPostBackChanged;
	}
	public boolean isCheckBoxesAutoPostBackChanged()
	{
		return allChanged || checkBoxesAutoPostBackChanged;
	}
	public void setAllUnchanged()
	{
		allChanged = false;
	}

	private int selectedRowId = -1;	
	private String tooltip = null;
	private String buttonText = null;
	private Color buttonTextColor = null;
	private boolean unselectable = false;
	private boolean enabled = true;
	private boolean visible = true;
	private boolean readOnly = false;
	private DynamicGridColumnCollection columns = new DynamicGridColumnCollection(this);
	private DynamicGridRowCollection rows = new DynamicGridRowCollection(this, null);
	private Color textColor = null;
	private Color backColor = null;
    private boolean selectable = true;
    private DynamicGridRowOptions defaultRowOptions = new DynamicGridRowOptions();
    private int nextRowID = 0;
    private boolean showCheckBoxes = false;
    private int headerMaxHeight = 0;
    private int footerMaxHeight = 0;
    private String headerValue = null;
    private String footerValue = null;
    private boolean checkBoxesAutoPostBack = false;
    private boolean rowSelectionChangedEventRequirePdsAuthentication =  false;
        
    private boolean selectedRowChanged = false;
    private boolean tooltipChanged = false;
    private boolean buttonTextChanged = false;
	private boolean buttonTextColorChanged = false;
    private boolean unselectableChanged = false;
    private boolean enabledChanged = false;
    private boolean visibleChanged = false;
    private boolean readOnlyChanged = false;
    private boolean textColorChanged = false;
    private boolean backColorChanged = false;
    private boolean selectableChanged = false;
    private boolean defaultRowOptionsChanged = false;
    private boolean showCheckBoxesChanged = false;
    private boolean headerMaxHeightChanged = false;
    private boolean footerMaxHeightChanged = false;
    private boolean headerValueChanged = false;
    private boolean footerValueChanged = false;
    private boolean checkBoxesAutoPostBackChanged = false;
    private boolean rowSelectionChangedEventRequirePdsAuthenticationChanged = false;
    private boolean allChanged = false;

	class DynamicGridColumn implements ims.framework.controls.DynamicGridColumn, IModifiable, Serializable
	{
        private static final long serialVersionUID = 1L;
        
		private DynamicGridColumn(DynamicGridData grid)
        {            
            this.grid = grid;
        }
        public void setIdentifier(Object value)
        {
            if(value == null)
                throw new RuntimeException("Invalid column identifier");
            
            int noColumns = this.grid.getColumns().size();            
            for(int x = 0; x < noColumns; x++)
            {
                ims.framework.controls.DynamicGridColumn column = this.grid.getColumns().get(x);
                if(!column.equals(this) && column.getIdentifier() != null && column.getIdentifier().equals(value))
                    throw new RuntimeException("Invalid column identifier. The identifier is in use by another column");
            }
                        
            this.identifier = value;
        }
        public Object getIdentifier()
        {
            return this.identifier;
        }
	    public void setCaption(String value) 
	    {
	    	if(!this.dataWasChanged)
            {
            	if(this.caption == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.caption.equals(value);
            }
	    	 
	        this.caption = value;
	    }
	    public String getCaption() 
	    {
	        return this.caption;
	    }
	    public void setCaptionImage(Image value) 
	    {
	    	if(!this.dataWasChanged)
            {
            	if(this.captionImage == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.captionImage.equals(value);
            }
	    	 
	        this.captionImage = value;
	    }
	    public Image getCaptionImage() 
	    {
	        return this.captionImage;
	    }
	    public void setAlignment(Alignment value) 
	    {
	    	if(value == null)
	    		throw new CodingRuntimeException("Invalid column alignment");
	    	
	    	if(!this.dataWasChanged)
            {
            	if(this.alignment == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.alignment.equals(value);
            }
	    	
	        this.alignment = value;
	    }
	    public Alignment getAlignment() 
	    {
	        return this.alignment;
	    }
	    public void setVerticalAlignment(VerticalAlignment value) 
	    {
	    	if(value == null)
	    		throw new CodingRuntimeException("Invalid column alignment");
	    	
	    	if(!this.dataWasChanged)
            {
            	if(this.verticalAlignment == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.verticalAlignment.equals(value);
            }
	    	
	        this.verticalAlignment = value;
	    }
	    public VerticalAlignment getVerticalAlignment() 
	    {
	        return this.verticalAlignment;
	    }
	    public void setHeaderAlignment(Alignment value) 
	    {
	    	if(value == null)
	    		throw new CodingRuntimeException("Invalid column header alignment");
	    	
	    	if(!this.dataWasChanged)
            {
            	if(this.headerAlignment == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.headerAlignment.equals(value);
            }
	    	
	        this.headerAlignment = value;
	    }
	    public Alignment getHeaderAlignment() 
	    {
	        return this.headerAlignment;
	    }
	    public boolean isReadOnly()
		{
			return this.readOnly;
		}
		public void setReadOnly(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.readOnly != value;
			
			this.readOnly = value;
		}
	    public void setVisible(boolean value)
	    {
	    	if(!this.dataWasChanged)
				this.dataWasChanged = this.visible != value;
	    	
	        this.visible = value;
	    }
	    public boolean isVisible()
	    {
	        return this.visible;
	    }
	    public void setWidth(int value)
	    {
	    	if(!this.dataWasChanged)
				this.dataWasChanged = this.width != value;
	    	
	        this.width = value;
	    }
	    public int getWidth()
	    {
	        return this.width;
	    }
	    public void setTextColor(Color value)
	    {
	    	if(!this.dataWasChanged)
            {
            	if(this.textColor == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.textColor.equals(value);
            }
	    	
	        this.textColor = value;
	    }
	    public Color getTextColor()
	    {
	        return this.textColor == null ? Color.Default : this.textColor;
	    }
	    public void clearHeaderTooltip()
	    {
	    	setHeaderTooltip(null);
	    }
	    public void setHeaderTooltip(String value)
	    {
	    	if(!this.dataWasChanged)
            {
            	if(this.tooltip == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.tooltip.equals(value);
            }
	    	
	        this.tooltip = value;
	    }
	    public String getHeaderTooltip()
	    {
	        return this.tooltip;
	    }	
	    public void setBackColor(Color value)
	    {
	    	if(!this.dataWasChanged)
            {
            	if(this.backColor == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.backColor.equals(value);
            }
	    	
	        this.backColor = value;
	    }
	    public Color getBackColor()
	    {
	        return this.backColor == null ? Color.Default : this.backColor;
	    }
        public void setCanGrow(boolean value)
        {
        	if(!this.dataWasChanged)
				this.dataWasChanged = this.canGrow != value;
        	
            this.canGrow = value;
        }
        public boolean canGrow()
        {
            return this.canGrow;
        }
        public void setSortMode(SortMode value)
        {
        	if(!this.dataWasChanged)
            {
            	if(this.sort == null)
            		this.dataWasChanged = value != null;
            	else
            		this.dataWasChanged = !this.sort.equals(value);
            }
        	
            this.sort = value;
        }
        public SortMode getSortMode()
        {
            return this.sort;
        }
        public ims.framework.controls.DynamicGridCell[] getCellArray()
        {
            int columnIndex = this.grid.getColumns().indexOf(this);
            if(columnIndex < 0)
                return new ims.framework.controls.DynamicGridCell[0];

            int noRows = this.grid.getRows().size();    
            ArrayList<DynamicGridCell> cells = new ArrayList<DynamicGridCell>();    
            for(int x = 0; x < noRows; x++)
            {
                DynamicGridRow row = (DynamicGridRow)this.grid.getRows().get(x);                
                cells.add((DynamicGridCell)row.getCells().get(this));
                cells.addAll(getChildCells(row));
            }
            
            int noCells = cells.size();
            ims.framework.controls.DynamicGridCell[] result = new ims.framework.controls.DynamicGridCell[cells.size()];
            for(int x = 0; x < noCells; x++)
            {
                result[x] = cells.get(x);
            }            
            return result;
        }
        public void setDynamicWidthSupported(boolean value)
        {
        	if(!this.dataWasChanged)
				this.dataWasChanged = this.dynamicWidthSupported != value;
        	
        	this.dynamicWidthSupported = value;
        }
        public boolean isDynamicWidthSupported()
        {
        	return dynamicWidthSupported;
        }
        private ArrayList<DynamicGridCell> getChildCells(DynamicGridRow row)
        {
            ArrayList<DynamicGridCell> cells = new ArrayList<DynamicGridCell>();
            
            int noRows = row.getRows().size();
            for(int x = 0; x < noRows; x++)
            {
                DynamicGridRow childRow = (DynamicGridRow)row.getRows().get(x);
                cells.add((DynamicGridCell)childRow.getCells().get(this));
                cells.addAll(getChildCells(childRow));
            }
            
            return cells;
        }
	    
	    public String render()
	    {           
	       StringBuffer sb = new StringBuffer();
	       sb.append("<col caption=\"" + (this.caption == null ? "" : StringUtils.encodeXML(this.caption)));
           
	       if(this.tooltip != null)
	           sb.append("\" tooltip=\"" + StringUtils.encodeXML(this.tooltip));
	       
	       if(this.captionImage != null)
	           sb.append("\" captionImage=\"" + this.captionImage.getImagePath());
	       
	       if(this.sort != SortMode.NONE && !this.grid.isTree())
               sb.append("\" sort=\"" + this.sort);
           else
               sb.append("\" sort=\"" + SortMode.NONE);
           
           if(!this.canGrow && this.width >= 0)
               sb.append("\" fixedWidth=\"true");
	       if(!this.visible)
	           sb.append("\" visible=\"false");
	       if(this.textColor != null)
	           sb.append("\" textcolor=\"" + this.textColor);
	       if(this.backColor != null)
	           sb.append("\" backcolor=\"" + this.backColor);
           if(this.readOnly)
               sb.append("\" readOnly=\"true");
           
	       sb.append("\" width=\"" + this.width);
	       sb.append("\" align=\"" + this.alignment);
	       sb.append("\" verticalAlign=\"" + this.verticalAlignment);		     
	       sb.append("\" captionAlign=\"" + this.headerAlignment);
	       sb.append("\" />");
	       return sb.toString();
	    }
	    public boolean wasChanged()
	    {
	    	return this.dataWasChanged;	    		
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;	    	
	    }
	    
	    private boolean dataWasChanged = true;
        private Object identifier = null;
	    private String caption = "";
	    private String tooltip = "";
	    private Image captionImage = null;
	    private boolean readOnly = false;
	    private Alignment headerAlignment = Alignment.LEFT;
	    private Alignment alignment = Alignment.LEFT;
	    private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;		
	    private int width = 100;
	    private boolean visible = true;	    
	    private Color textColor = null;
	    private Color backColor = null;
        private boolean canGrow = false;
        private SortMode sort = SortMode.NONE;
        private DynamicGridData grid;
        private boolean dynamicWidthSupported = false;
	}
	public class DynamicGridColumnCollection implements ims.framework.controls.DynamicGridColumnCollection, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
        private DynamicGridColumnCollection(DynamicGridData grid)
        {
            this.grid = grid;
        }
        public int size() 
        {
            return this.columns.size();
        }
        public ims.framework.controls.DynamicGridColumn get(int index) 
        {
            return this.columns.get(index);
        }
        public ims.framework.controls.DynamicGridColumn getByIdentifier(Object value)
        {
            if(value == null)
                return null;
            
            for(int x = 0; x < this.columns.size(); x++)
            {
                DynamicGridColumn column = this.columns.get(x);
                if(column.getIdentifier() != null && column.getIdentifier().equals(value))
                    return column;             
            }
            
            return null;
        }
        public int indexOf(ims.framework.controls.DynamicGridColumn column)
        {
            if(column == null)
                return -1;
        
            for(int x = 0; x < this.columns.size(); x++)
            {
                DynamicGridColumn existingColumn = this.columns.get(x);
                if(existingColumn.equals(column))
                    return x;
            }
            
            return -1;
        }
        public ims.framework.controls.DynamicGridColumn newColumn(String caption) 
        {
            return newColumnAt(-1, caption, false);
        }
        public ims.framework.controls.DynamicGridColumn newColumn(String caption, Object identifier) 
        {
            return newColumnAt(-1, caption, identifier);
        }
        public ims.framework.controls.DynamicGridColumn newColumn(String caption, Object identifier, boolean readOnly) 
        {
            return newColumnAt(-1, caption, identifier, readOnly);            
        }
        public ims.framework.controls.DynamicGridColumn newColumn(String caption, boolean readOnly) 
        {
        	return newColumnAt(-1, caption, readOnly); 
        }        
        public ims.framework.controls.DynamicGridColumn newColumnAt(int index, String caption) 
        {
            return newColumn(index, caption, false);
        }
        public ims.framework.controls.DynamicGridColumn newColumnAt(int index, String caption, Object identifier) 
        {
            ims.framework.controls.DynamicGridColumn column = newColumn(index, caption, false);
            column.setIdentifier(identifier);
            return column;
        }
        public ims.framework.controls.DynamicGridColumn newColumnAt(int index, String caption, Object identifier, boolean readOnly) 
        {
            ims.framework.controls.DynamicGridColumn column = newColumn(index, caption, readOnly);
            column.setIdentifier(identifier);
            return column;
        }
        public ims.framework.controls.DynamicGridColumn newColumnAt(int index, String caption, boolean readOnly) 
        {
        	return newColumn(index, caption, readOnly); 
        }
        public ims.framework.controls.DynamicGridColumn newColumn(int index, String caption, boolean readOnly) 
        {
            if(caption == null)
                throw new RuntimeException("Invalid column caption");
            
            this.dataWasChanged = true;
            DynamicGridColumn column = new DynamicGridColumn(this.grid);
            column.caption = caption;            
            column.readOnly = readOnly;              
            if(index != -1)
            	this.columns.add(index, column);
            else
            	this.columns.add(column);
            return column;
        }
        public boolean remove(ims.framework.controls.DynamicGridColumn column)
        {
        	boolean removed = this.columns.remove(column);
        	if(!this.dataWasChanged)
        		this.dataWasChanged = removed;
        	return removed;
        }
        public void clear() 
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.columns.size() != 0;
        	
            this.columns.clear();            
        }
        public String render()
	    {
            if(this.columns.size() == 0)
                return "<cols/>";
            
	        StringBuffer sb = new StringBuffer();
	        for(int x = 0; x < this.columns.size(); x++)
	        {
                DynamicGridColumn column = this.columns.get(x);
                if(column.isVisible())
                    sb.append(column.render());
	        }
	        return sb.toString();
	    }
        public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	for(int x = 0; x < this.columns.size(); x++)
	    	{
	    		if(this.columns.get(x).wasChanged())
	    			return true;
	    	}
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;
	    	
	    	for(int x = 0; x < this.columns.size(); x++)
	    	{
	    		this.columns.get(x).markUnchanged();
	    	}
	    }
	    
	    private boolean dataWasChanged = false;
        private ArrayList<DynamicGridColumn> columns = new ArrayList<DynamicGridColumn>();
        private DynamicGridData grid;		
	}
	class DynamicGridRow implements ims.framework.controls.DynamicGridRow, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private DynamicGridRow(DynamicGridData grid, DynamicGridRow parent)
        {            
            this.grid = grid;
            this.parent = parent;
            this.id = grid.getNextRowID();            
        }
        public int getID()
        {
            return this.id;
        }
        public void setIdentifier(Object value)
        {
        	this.identifier = value;
        }
        public Object getIdentifier()
        {
        	return this.identifier;
        }
        public Object getValue() 
	    {
	        return this.value;
	    }
	    public void setValue(Object value) 
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
	    public void setSelectable(boolean value)
	    {
	    	if(!this.dataWasChanged)
	    		this.dataWasChanged = this.selectable != value;
	    	
	        this.selectable = value;
	    }
	    public boolean isSelectable()
	    {
	        return this.selectable;
	    }
	    public void setReadOnly(boolean value)
	    {
	    	if(!this.dataWasChanged)
	    		this.dataWasChanged = this.selectable != value;
	    	
	        this.readOnly = value;
	    }
	    public boolean isReadOnly()
	    {
	        return this.readOnly;
	    }
	    public ims.framework.controls.DynamicGridCellCollection getCells() 
	    {
            if(this.cells == null)
                this.cells = new DynamicGridCellCollection(this);
	        return this.cells;
	    }
	    public ims.framework.controls.DynamicGridCell[] getCellArray()
	    {
	    	if(this.cells == null)
	    		 return new ims.framework.controls.DynamicGridCell[0];
	    	
	    	ims.framework.controls.DynamicGridCell[] cellArray = new ims.framework.controls.DynamicGridCell[this.cells.size()];
	    	for(int x = 0; x < this.cells.size(); x++)
	    	{
	    		cellArray[x] = this.cells.get(x);
	    	}
	    	
	    	return cellArray;
	    }
	    public void setTextColor(Color value)
	    {
	    	if(!this.dataWasChanged)
	    	{
	    		if(this.textColor == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.textColor.equals(value);
	    	}
	    	
	        this.textColor = value;
	    }
	    public Color getTextColor()
	    {
	        return this.textColor == null ? Color.Default : this.textColor;
	    }	    
	    public void setBackColor(Color value)
	    {
	    	if(!this.dataWasChanged)
	    	{
	    		if(this.backColor == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.backColor.equals(value);
	    	}
	    	
	        this.backColor = value;
	    }
	    public Color getBackColor()
	    {
	        return this.backColor == null ? Color.Default : this.backColor;
	    }
        public void setBold(boolean value)
        {
        	if(!this.dataWasChanged)
	    		this.dataWasChanged = this.bold != value;
        	
            this.bold = value;
        }
        public boolean isBold()
        {
            return this.bold;
        }
        public void setExpandedImage(Image value)
        {
        	if(!this.dataWasChanged)
	    	{
	    		if(this.expandedImage == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.expandedImage.equals(value);
	    	}
        	
            this.expandedImage = value;
        }
        public void setCollapsedImage(Image value)
        {
        	if(!this.dataWasChanged)
	    	{
	    		if(this.collapsedImage == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.collapsedImage.equals(value);
	    	}
        	
            this.collapsedImage = value;
        }
        public void setSelectedImage(Image value)
        {
        	if(!this.dataWasChanged)
	    	{
	    		if(this.selectedImage == null)
	    			this.dataWasChanged = value != null;
	    		else
	    			this.dataWasChanged = !this.selectedImage.equals(value);
	    	}
        	
            this.selectedImage = value;
        }
        public void setExpanded(boolean value)
        {
        	setExpanded(value, false);
        }
        public void setExpanded(boolean value, boolean recursive)
        {
        	if(!this.dataWasChanged)
	    		this.dataWasChanged = this.expanded != value;
        	
            this.expanded = value;
            
            if(recursive && this.rows != null)
            {
            	for(int x = 0; x < this.rows.size(); x++)
            	{
            		((DynamicGridRow)this.rows.get(x)).setExpanded(value, true);
            	}
            }
        }
        public boolean isExpanded()
        {
            return this.expanded;
        }
        public void setOptions(DynamicGridRowOptions value)
        {
            if(value == null)
                throw new RuntimeException("Invalid dynamic grid row options");
            
            setReadOnly(value.readOnly);
            setBold(value.bold);
            setTextColor(value.textColor);
            setBackColor(value.backColor);
            setSelectable(value.selectable);
            setSelectedImage(value.selectedImage);
            setCollapsedImage(value.collapsedImage);
            setExpandedImage(value.expandedImage);
            setExpanded(value.expanded);
        }
        public DynamicGridRowOptions getOptions()
        {
            DynamicGridRowOptions options = new DynamicGridRowOptions();
            options.readOnly = this.readOnly;
            options.bold = this.bold;
            options.textColor = this.textColor;
            options.backColor = this.backColor;
            options.selectable = this.selectable;
            options.collapsedImage = this.collapsedImage;
            options.selectedImage = this.selectedImage;
            options.expandedImage = this.expandedImage;
            options.expanded = this.expanded;
            return options;
        }
        public ims.framework.controls.DynamicGridRowCollection getRows()
        {
            if(this.rows == null)
                this.rows = new DynamicGridRowCollection(this.grid, this);
            return this.rows;
        }
        public ims.framework.controls.DynamicGridRow getParent()
        {
            return this.parent;
        }
        public boolean canMoveUp()
        {
        	return getCurrentLevelRows().indexOf(this) > 0;            
        }
        public boolean canMoveDown()
        {
            return getCurrentLevelRows().indexOf(this) < getCurrentLevelRows().size() - 1;
        }
        public boolean moveUp()
        {
            if(!canMoveUp())
                return false;
            
            return moveTo(getCurrentLevelRows().indexOf(this) - 1);
        }
        public boolean moveDown()
        {
            if(!canMoveDown())
                return false;
            
            return moveTo(getCurrentLevelRows().indexOf(this) + 1);
        }
        public boolean moveTo(int index)
        {
            if(index < 0 || index >= getCurrentLevelRows().size())
                return false;
                        
            getCurrentLevelRows().remove(this, false);
            getCurrentLevelRows().rows.add(index, this);            
            
            return true;
        }
	    public String render(DynamicGridColumnCollection columns) throws ConfigurationException, FileNotFoundException
	    {
	       StringBuffer sb = new StringBuffer();
	       
           sb.append("<r id=\"" + this.id + "\" ");
           if(this.rows != null && this.rows.size() > 0)
           {
               sb.append("type=\"folder\" ");
               sb.append("expanded=\"" + (this.expanded ? "true" : "false") + "\" ");
           }
           else
        	   sb.append("type=\"item\" ");
           
           
           
           if(this.grid.isShowingCheckBoxes())
           {
	           if(this.checkBoxVisible)
	        	   sb.append("checked=\"" + (this.checked ? "true" : "false") + "\" ");
	           else
	           {
	        	   sb.append("checkBoxVisible=\"false\" ");
	           }
           }
           
           if(!this.selectable)
	           sb.append("selectable=\"false\" ");
	       if(this.bold)
               sb.append("bold=\"true\" ");
           sb.append("readOnly=\"" + (this.readOnly ? "true" : "false") + "\" ");
           if(this.textColor != null)
               sb.append("textcolor=\"" + this.textColor + "\" ");
           if(this.backColor != null)
               sb.append("backcolor=\"" + this.backColor + "\" ");
           
           if(this.expandedImage != null)
           {   
               sb.append("expandedImageUrl=\"");
               sb.append(this.expandedImage.getImagePath());
               sb.append("\" ");
           }
           if(this.collapsedImage != null)
           {   
               sb.append("imageUrl=\"");
               sb.append(this.collapsedImage.getImagePath());
               sb.append("\" ");
           }
           if(this.selectedImage != null)
           {   
               sb.append("selectedImageUrl=\"");
               sb.append(this.selectedImage.getImagePath());
               sb.append("\" ");
           }   
           
           sb.append(">");
           
           if(this.cells != null)
               sb.append(this.cells.render(columns));           
           
           if(this.rows != null && this.rows.size() > 0)
           {
               sb.append("<childRows>");
               for(int x = 0; x < this.rows.size(); x++)
               {
                   sb.append(((DynamicGridRow)this.rows.get(x)).render(columns));
               }
               sb.append("</childRows>");
           }
           
	       sb.append("</r>");
	       return sb.toString();
	    }
	    public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	if(this.rows != null && this.rows.wasChanged())
	    		return true;
	    	if(this.cells != null && this.cells.wasChanged())
	    		return true;
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;
	    	if(this.rows != null)
	    		this.rows.markUnchanged();
	    	if(this.cells != null)
	    		this.cells.markUnchanged();
	    }
	    private DynamicGridRowCollection getCurrentLevelRows()
	    {
	    	if(this.parent != null)
	    		return (DynamicGridRowCollection)this.parent.getRows();
	    	return this.grid.getRows();
	    }
	    public void setCheckBoxVisible(boolean value)
		{
			if(!dataWasChanged)
				dataWasChanged = checkBoxVisible != value;
			
			checkBoxVisible = value;
		}
	    public boolean isCheckBoxVisible()
	    {
	    	return checkBoxVisible;
	    }
		public void setChecked(boolean value)
		{
			if(!dataWasChanged)
				dataWasChanged = checked != value;
			
			checked = value;
		}
		public boolean isChecked()
		{
			if(!checkBoxVisible)
				return false;
			
			return checked;
		}
		
	    private boolean dataWasChanged = true;        
        private int id = 0;
	    private Object value;
	    private boolean selectable = true;
	    private boolean readOnly = false;
	    private DynamicGridCellCollection cells = null;
	    private Color textColor = null;
	    private Color backColor = null;        
        private DynamicGridData grid;
        private boolean bold = false;
        private Image collapsedImage = null;        
        private Image expandedImage = null;
        private Image selectedImage = null;
        private DynamicGridRow parent = null;
        private DynamicGridRowCollection rows = null;
        private boolean expanded = false;
        private Object identifier = null;
        private boolean checkBoxVisible = true;
        private boolean checked = false;	        
	}
	public class DynamicGridRowCollection implements ims.framework.controls.DynamicGridRowCollection, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
		private DynamicGridRowCollection(DynamicGridData grid, DynamicGridRow parent)
        {
            this.grid = grid;
            this.parent = parent;
        }
	    public ims.framework.controls.DynamicGridRow newRow() 
	    {
	        return newRow(null, false, false);
	    }
	    public ims.framework.controls.DynamicGridRow newRow(boolean autoSelect) 
	    {
	        return newRow(null, false, autoSelect);
	    }
	    public ims.framework.controls.DynamicGridRow newRowAfter(ims.framework.controls.DynamicGridRow row)
	    {
	    	return newRow(row, true, false);
	    }
	    public ims.framework.controls.DynamicGridRow newRowAfter(ims.framework.controls.DynamicGridRow row, boolean autoSelect)
	    {
	    	return newRow(row, true, autoSelect);
	    }
	    public ims.framework.controls.DynamicGridRow newRowBefore(ims.framework.controls.DynamicGridRow row)
	    {
	    	return newRow(row, false, false);
	    }
	    public ims.framework.controls.DynamicGridRow newRowBefore(ims.framework.controls.DynamicGridRow row, boolean autoSelect)
	    {
	    	return newRow(row, false, autoSelect);
	    }
        private ims.framework.controls.DynamicGridRow newRow(ims.framework.controls.DynamicGridRow referenceRow, boolean afterReferencedRow, boolean autoSelect)
        {
        	int referenceRowIndex = -1;
        	if(referenceRow != null && this.rows.indexOf(referenceRow) >= 0)
        	{
        		referenceRowIndex = this.rows.indexOf(referenceRow);
        		
        		if(afterReferencedRow)
        			referenceRowIndex++;        		
        	}
        	
            DynamicGridRow newRow = new DynamicGridRow(this.grid, this.parent);
            newRow.setOptions(this.grid.getDefaultRowOptions());
            
            if(referenceRowIndex == -1)
            {
            	this.rows.add(newRow);
            }
            else
            {
            	this.rows.add(referenceRowIndex, newRow);
            }
            
            this.dataWasChanged = true;
            
            if(autoSelect && this.grid.isSelectable() && newRow.isSelectable())
                this.grid.setSelectedRow(newRow);
            
            return newRow;
        }
	    public int indexOf(ims.framework.controls.DynamicGridRow row)
        {
            return this.rows.indexOf(row);
        }
	    public int size() 
	    {
	        return this.rows.size();
	    }
	    public ims.framework.controls.DynamicGridRow get(int index) 
	    {
	        return this.rows.get(index);
	    }
	    public void remove(ims.framework.controls.DynamicGridRow row)
	    {
	    	remove(row, true);
	    }
	    public void remove(ims.framework.controls.DynamicGridRow row, boolean clearSelection) 
	    {
            if(row == null)
                return;
            
            if(row.equals(this.grid.getSelectedRow()) && clearSelection)
                this.grid.setSelectedRow(null);
            
            this.dataWasChanged = true;
            
            if(this.rows.contains(row))
            {
            	this.rows.remove(row);
            }
            else
            {
            	for(int x = 0; x < this.rows.size(); x++)
            	{
            		ims.framework.controls.DynamicGridRowCollection childRows = this.rows.get(x).getRows();
            		if(remove(childRows, (DynamicGridRow)row))
            			break;
            	}
            }
            
	        this.grid.checkRowSelection();
	    }	    
	    private boolean remove(ims.framework.controls.DynamicGridRowCollection rows, DynamicGridRow row)
		{
	    	if(rows == null || row == null)
	    		return false;
	    	
	    	if(rows.indexOf(row) >= 0)
	    	{
	    		rows.remove(row);
	    		return true;
	    	}
	    	
			for(int x = 0; x < rows.size(); x++)
			{
				if(remove(rows.get(x).getRows(), row))
					return true;
			}
			
			return false;
		}
		public void clear() 
	    {
	    	if(!this.dataWasChanged)
	    		this.dataWasChanged = this.rows.size() != 0;
	    	
	        this.rows.clear();	        
	        this.grid.checkRowSelection();
	    }    
	    public void expandAll()
	    {
	    	expandCollapseAll(true);
	    }
	    public void collapseAll()
	    {
	    	expandCollapseAll(false);
	    }
	    private void expandCollapseAll(boolean value)
	    {
	    	 for(int x = 0; x < this.rows.size(); x++)
	    	 {
	    		 this.rows.get(x).setExpanded(value, true);
	    	 }
	    }
	    public ims.framework.controls.DynamicGridRow getParent()
        {
            return this.parent;
        }
        public String render(DynamicGridColumnCollection columns) throws ConfigurationException, FileNotFoundException
	    {
	        if(columns.size() == 0 || this.rows.size() == 0)
                return "<rows/>";
            
	        StringBuffer sb = new StringBuffer();
	        sb.append("<rows>");
	        for(int x = 0; x < this.rows.size(); x++)
	        {
	            sb.append(this.rows.get(x).render(columns));
	        }
	        sb.append("</rows>");
	        return sb.toString();
	    }
        public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	for(int x = 0; x < this.rows.size(); x++)
	    	{
	    		if(this.rows.get(x).wasChanged())
	    			return true;
	    	}
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;
	    	for(int x = 0; x < this.rows.size(); x++)
	    	{
	    		this.rows.get(x).markUnchanged();
	    	}
	    }
	    
	    private boolean dataWasChanged = false;    
	    private ArrayList<DynamicGridRow> rows = new ArrayList<DynamicGridRow>();
        private DynamicGridData grid;
        private DynamicGridRow parent = null;
	}
	class DynamicGridCell implements ims.framework.controls.DynamicGridCell, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
        private DynamicGridCell(DynamicGridRow row)
        {
        	this.items = new DynamicGridCellItemCollection(this);
            this.row = row;
        }
        public void setIdentifier(Object value)
        {
        	this.identifier = value;
        }
        public Object getIdentifier()
        {
        	return this.identifier;
        }
        public void clear()
        {
        	this.type = DynamicCellType.EMPTY;
        	this.value = null;
        	this.maxVisibleItemsForMultiSelect = 3;
            this.maxCheckedItemsForMultiSelect = null;
            this.maxLength = -1;
            this.precision = 5;
            this.scale = 2;
            this.readOnly = false;
            this.tooltip = null;
            this.buttonText = null;
            this.buttonTextColor = null;
            this.items = new DynamicGridCellItemCollection(this); 
            this.textColor = null;
            this.backColor = null;
            this.validationMessage = null;
            this.canBeEmpty = true;
            this.autoPostBack = false;      
            this.selectedIndex = -1;
            this.typedText = null;
            this.identifier = null;
            this.decoratorType = DynamicCellDecoratorMode.DEFAULT;
            this.showOpened = false;
            this.fixedFont = false;
            this.casing = CharacterCasing.NORMAL;
            this.maxDropDownItems = -1;
            this.autoWrapForMultiSelect = false;
        	
        	this.dataWasChanged = true;        	
        }
        public Object getValue() 
        {
            if(this.type.isSupportingItems())
            {
                if(this.selectedIndex < 0)
                    return null;
                return this.items.get(this.selectedIndex).getValue();
            }
            
            return this.value;
        }
        public boolean setValue(Object value) 
        {
            // checking type
            if(value != null)
                this.type.checkType(value.getClass());
            
            if(this.type.isSupportingItems())
            {
                if(value == null)
                {
                	if(!this.dataWasChanged)
                		this.dataWasChanged = this.selectedIndex != -1;
                	
                    this.selectedIndex = -1;
                    return true;
                }
                
                int indexOf = this.items.indexOf(value);
                if(indexOf < 0)
                	indexOf = -1;
                
                if(!this.dataWasChanged)
            		this.dataWasChanged = this.selectedIndex != indexOf;
                
                this.selectedIndex = indexOf;
                return indexOf >= 0;
            }
           
        	if(!this.dataWasChanged)
        	{
        		if(this.value == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.value.equals(value);
        	}
        	
        	if(this.type.equals(DynamicCellType.TABLE))
        			this.dataWasChanged = true;
        	
            this.value = value;    
                return true;
        }
        public String getTypedText()
        {
            return this.typedText;
        }
        public void setTypedText(String value)
        {
        	if(!this.dataWasChanged)
        	{
        		if(this.typedText == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.typedText.equals(value);
        	}
        	
            this.typedText = value;
        }
        public String getButtonText() 
        {
        	return this.buttonText;
		}
		public void setButtonText(String value) 
		{
			if(!this.dataWasChanged)
        	{
        		if(this.buttonText == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.buttonText.equals(value);
        	}
        	
            this.buttonText = value;			
		}
		public Color getButtonTextColor() 
        {
        	return this.buttonTextColor;
		}
		public void setButtonTextColor(Color value) 
		{
			if(!this.dataWasChanged)
        	{
        		if(this.buttonTextColor == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.buttonTextColor.equals(value);
        	}
        	
            this.buttonTextColor = value;			
		}
        public void setSelectedItemIndex(int value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.selectedIndex != value;
        	
            this.selectedIndex = value;
        }
        public int getSelectedItemIndex()
        {
            return this.selectedIndex;
        }
        public DynamicCellType getType() 
        {
            return this.type;
        }
        public void setReadOnly(boolean value) 
        {
        	if(this.alwaysReadOnly)
        		return;
        	
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.readOnly != value;
        	
        	this.readOnly = value;
        }
        public boolean isReadOnly() 
        {
        	if(this.alwaysReadOnly)
        		return true;        	
            return this.readOnly;
        }
        public void setTooltip(String value) 
        {
        	if(!this.dataWasChanged)
        	{
        		if(this.tooltip == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.tooltip.equals(value);
        	}
        	
            this.tooltip = value;
        }
        public String getTooltip()
        {
            return this.tooltip == null ? "" : this.tooltip;
        }
        public DynamicGridCellItemCollection getItems() 
        {
            return this.items;
        }
        public void setColumn(ims.framework.controls.DynamicGridColumn value)
        {
        	this.dataWasChanged = true;
            this.column = (DynamicGridColumn)value;
        }
        public ims.framework.controls.DynamicGridColumn getColumn()
        {
            return this.column;
        }
        public ims.framework.controls.DynamicGridRow getRow()
        {
            return this.row;
        }
        public void setTextColor(Color value)
	    {
        	if(!this.dataWasChanged)
        	{
        		if(this.textColor == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.textColor.equals(value);
        	}
        	
	        this.textColor = value;
	    }
	    public Color getTextColor()
	    {
	        return this.textColor == null ? Color.Default : this.textColor;
	    }	    
	    public void setBackColor(Color value)
	    {
	    	if(!this.dataWasChanged)
        	{
        		if(this.backColor == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.backColor.equals(value);
        	}
	    	
	        this.backColor = value;
	    }
	    public Color getBackColor()
	    {
	        return this.backColor == null ? Color.Default : this.backColor;
	    }
        public void setValidationMessage(String value)
        {
        	if(!this.dataWasChanged)
        	{
        		if(this.validationMessage == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.validationMessage.equals(value);
        	}
        	
            this.validationMessage = value;
        }
        public String getValidationMessage()
        {
            return this.validationMessage;
        }
        public void setCanBeEmpty(boolean value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.canBeEmpty != value;
        	
            this.canBeEmpty = value;
        }
        public boolean canBeEmpty()
        {
            return this.canBeEmpty;
        }
        public void setAutoPostBack(boolean value)
        {
        	if(this.alwaysReadOnly)
        		return;
        	
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.autoPostBack != value;
        	
            this.autoPostBack = value;
        }
        public boolean hasAutoPostBack()
        {
        	if(this.alwaysReadOnly)
        		return false;
        	
            return this.autoPostBack;
        }
        public void setDecimalPrecisionScale(int precision, int scale)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.precision != precision || this.scale != scale;
        	
        	this.precision = precision;
            this.scale = scale;
        }
        public int getDecimalPrecision()
        {
            return this.precision;
        }
        public int getDecimalScale()
        {
            return this.scale;
        }
        public void setStringMaxLength(int value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.maxLength != value;
        		
            this.maxLength = value;
        }
        public int getStringMaxLength()
        {
            return this.maxLength;
        }
        public void setIntMaxLength(int value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.maxLength != value;
        		
            this.maxLength = value;
        }
        public int getIntMaxLength()
        {
            return this.maxLength;
        }
        public int getMaxVisibleItemsForMultiSelect()
        {
        	return this.maxVisibleItemsForMultiSelect;
        }
        public void setMaxVisibleItemsForMultiSelect(int value)
        {
        	if(value < 0)
        		value = 0;
        	
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.maxVisibleItemsForMultiSelect != value;
        	
        	this.maxVisibleItemsForMultiSelect = value;
        }
        public void setMaxCheckedItemsForMultiSelect(Integer value)
        {
        	if(!this.dataWasChanged)
        	{
        		if(this.maxCheckedItemsForMultiSelect == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.maxCheckedItemsForMultiSelect.equals(value);
        	}
        	
        	this.maxCheckedItemsForMultiSelect = value;
        }
        public Integer getMaxCheckedItemsForMultiSelect()
        {
        	return this.maxCheckedItemsForMultiSelect;
        }
        public boolean getAutoWrapForMultiSelect()
		{
			return autoWrapForMultiSelect;
		}
		public void setAutoWrapForMultiSelect(boolean value)
		{			
			if(!this.dataWasChanged)
        		this.dataWasChanged = this.autoWrapForMultiSelect != value;
			autoWrapForMultiSelect = value;			
		}
        public void setWidth(int value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.width != value;
        	
        	this.width = value;
        }
        public int getWidth()
        {
        	return this.width;
        }
        public void setDecoratorType(DynamicCellDecoratorMode value)
        {
        	if(!this.dataWasChanged)
        	{
        		if(this.decoratorType == null)
        			this.dataWasChanged = value != null;
        		else
        			this.dataWasChanged = !this.decoratorType.equals(value);
        	}
        	
        	this.decoratorType = value;
        }
        public DynamicCellDecoratorMode getDecoratorType()
        {
        	return this.decoratorType;
        }
        public void showOpened()
        {
        	if(!this.type.equals(DynamicCellType.QUERYCOMBOBOX))
        		throw new CodingRuntimeException("showOpened() method is supported only by query combo box cell");
        	
        	if(!this.showOpened)
        	{
        		this.dataWasChanged = true;
        		this.showOpened = true;
        	}
        }
        public void setOptions(DynamicGridCellOptions options) 
        {
        	if(options == null)
        	{
        		resetOptions();
        		return;
        	}
        	
        	setBackColor(options.getBackColor());
        	setTextColor(options.getTextColor());
        	setReadOnly(options.isReadOnly());        	
		}
        public void resetOptions()
        {
        	setOptions(DynamicGridCellOptions.DEFAULT);
        }
        public void setFixedFont(boolean value) 
		{
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.fixedFont != value;
        	
        	this.fixedFont = value;
		}
		public boolean isFixedFont() 
		{
			return this.fixedFont;
		}
		public CharacterCasing getCharacterCasing()
		{
			return this.casing;
		}
		public void setCharacterCasing(CharacterCasing value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.casing != value;
			
			this.casing = value;
		}
		public int getMaxDropDownItems()
		{
			return maxDropDownItems;
		}
		public void setMaxDropDownItems(int value)
		{
			if(value <= 0)
				value = -1;
			
			if(!this.dataWasChanged)
        	{
        		this.dataWasChanged = maxDropDownItems != value;
        	}
			
			maxDropDownItems = value;
		}
		public int getTableRowId()
		{
			return this.tableRowId;
		}
		public void setTableRowId(int id)
		{
			this.tableRowId = id;
		}
		public int getTableCellId() 
		{
			return this.tableCellId;
		}
		public void setTableCellId(int id) 
		{
			this.tableCellId = id;
		}
		private String renderValue()
        {
            // checking type
        	if(this.value != null)
        		this.type.checkType(this.value.getClass());
            
            if(this.type.equals(DynamicCellType.BOOL))
            {
            	if(this.value == null)
            		return "false";
            	
                return ((Boolean)this.value).booleanValue() ? "true" : "false";
            }
            
            if(this.value == null)
                return "";
            
            if(this.type.equals(DynamicCellType.STRING))
                return StringUtils.encodeXML(((String)this.value));
            else if(this.type.equals(DynamicCellType.BUTTON) && this.value != null)
                return StringUtils.encodeXML(((String)this.value));
            else if(this.type.equals(DynamicCellType.LABEL))
                return StringUtils.encodeXML(((String)this.value));
            else if(this.type.equals(DynamicCellType.DYNAMICLABEL))
                return StringUtils.encodeXML(((String)this.value));
            else if(this.type.equals(DynamicCellType.WRAPTEXT))
                return StringUtils.encodeXML(((String)this.value));
            else if(this.type.equals(DynamicCellType.COMMENT))
                return ims.framework.utils.StringUtils.encodeXML(this.value.toString());
            else if(this.type.equals(DynamicCellType.DECIMAL))
                return DecimalFormat.format(this.value, this.precision, this.scale);
            else if(this.type.equals(DynamicCellType.INT))
                return ((Integer)this.value).toString();
            else if(this.type.equals(DynamicCellType.DATE))
                return ((Date)this.value).toString();
            else if(this.type.equals(DynamicCellType.DATETIME))
                return ((DateTime)this.value).toString(DateTimeFormat.ISO);
            else if(this.type.equals(DynamicCellType.TIME))
                return ((Time)this.value).toString();
            else if(this.type.equals(DynamicCellType.PARTIALDATE))
                return ((PartialDate)this.value).toString();
            else if(this.type.equals(DynamicCellType.IMAGE))
            {
            	if(this.value == null)
            		return "";
                return ((Image)this.value).getImagePath();
            }
            else if(this.type.equals(DynamicCellType.HTMLVIEW))
            {
            	return "<![CDATA[ " + (String)this.value + " ]]>";            	 
            }
            else if(this.type.equals(DynamicCellType.IMAGEBUTTON))
            {
            	if(this.value == null)
            		return "";
                return ((Image)this.value).getImagePath();
            }
            else if(this.type.equals(DynamicCellType.TABLE))
            {   
            	if(this.value == null)
            		return "";
            	
            	return ((DynamicGridCellTable)this.value).renderValue();            	 
            }           
            
            return "";
        }
        
        public String render()
        {
            StringBuffer sb = new StringBuffer();
            
            if(this.type != DynamicCellType.EMPTY)
            {
                sb.append("<c");
                
                if(this.type == DynamicCellType.QUERYCOMBOBOX)
                {
                    sb.append(" searchable=\"true\""); // searchable combo box
                    
                    if(this.showOpened)
                    {
                    	this.showOpened = false;
                    	sb.append(" opened=\"true\"");
                    }
                }
                
                if(this.type == DynamicCellType.ENUMERATION || this.type == DynamicCellType.QUERYCOMBOBOX)
                {
                	if(maxDropDownItems != -1)
                	{
                		sb.append(" maxPopupItems=\"");
                        sb.append(this.maxDropDownItems);
                        sb.append("\"");                		
                	}
                }
                
                sb.append(" type=\"");
               	sb.append(this.type);
                sb.append("\"");
                
                sb.append(" fixedFont=\"");
                sb.append(this.fixedFont ? "true" : "false");
                sb.append("\"");
                
                if(maxLength > 0 && (this.type == DynamicCellType.STRING || this.type == DynamicCellType.COMMENT || this.type == DynamicCellType.WRAPTEXT || this.type == DynamicCellType.INT))
                {
                	sb.append(" maxLength=\"" + maxLength + "\"");
                }
                
                if(this.casing != CharacterCasing.NORMAL && (this.type == DynamicCellType.STRING || this.type == DynamicCellType.WRAPTEXT))
                {
                	sb.append(" casing=\"" + this.casing.render() + "\"");
                }
                
                if(this.type == DynamicCellType.IMAGEBUTTON && this.value != null)
                {
                	if(((Image)this.value).getImageWidth() != 8 || ((Image)this.value).getImageHeight() != 8)
                	{
	                	sb.append(" imgWidth=\"");
	                	sb.append(((Image)this.value).getImageWidth());
	                	sb.append("\"");
	                	sb.append(" imgHeight=\"");
	                	sb.append(((Image)this.value).getImageHeight());
	                	sb.append("\"");
                	}
                }
                
                if(this.type == DynamicCellType.BUTTON && this.buttonText != null)
                {                	
                	sb.append(" buttonText=\"");
	                sb.append(ims.framework.utils.StringUtils.encodeXML(this.buttonText));
	                sb.append("\"");    
	                
	                if(this.buttonTextColor != null)
	                {
	                	sb.append(" buttonTextColor=\"");
		                sb.append(this.buttonTextColor);
		                sb.append("\"");
	                }
                }
                
                if(this.type.isSupportingCellDecorator())
                {
	                if(this.alwaysReadOnly)
	                {
	                	sb.append(" showEditorRectangle=\"");
	  	                sb.append(DynamicCellDecoratorMode.NEVER);
	  	                sb.append("\"");
	                }
	                else if(this.decoratorType.getID() != DynamicCellDecoratorMode.DEFAULT.getID())
	                {
		                sb.append(" showEditorRectangle=\"");
		                sb.append(this.decoratorType);
		                sb.append("\"");
	                }
                }
                
                if(this.getColumn().isDynamicWidthSupported() && this.type != DynamicCellType.DYNAMICLABEL)
                {
                	sb.append(" width=\"");
	                sb.append(this.width);
	                sb.append("\"");                	
                }
                
                if(this.type == DynamicCellType.MULTISELECT)
                {
                	sb.append(" maxVisibleLines=\"" + this.maxVisibleItemsForMultiSelect + "\"");
                	
                	if(this.maxCheckedItemsForMultiSelect != null)
                	{
                		sb.append(" maxCheckedItems=\"" + this.maxCheckedItemsForMultiSelect.intValue() + "\"");
                	}
                	
                	if(autoWrapForMultiSelect)
                	{
                		sb.append(" wrap=\"true\"");
                	}
                }
                
                if(this.type.alwaysHasAutoPortBack() || (this.autoPostBack && this.type.isSupportingAutoPostBack()))
                    sb.append(" autoPostBack=\"true\"");
                
                if(this.alwaysReadOnly || this.readOnly)
                    sb.append(" readOnly=\"true\"");
                if(this.textColor != null)
                    sb.append(" textcolor=\"" + this.textColor + "\"");
                if(this.backColor != null)
                    sb.append(" backcolor=\"" + this.backColor + "\"");
                
	            if(this.tooltip != null && this.tooltip.length() > 0)
	            {
	                sb.append(" tooltip=\"");
	                sb.append(ims.framework.utils.StringUtils.encodeXML(this.tooltip));
	                sb.append("\"");
	            }
	            
                sb.append(" canBeEmpty=\"" + (this.canBeEmpty ? "true" : "false") + "\"");
	                                           
                if(this.type.equals(DynamicCellType.QUERYCOMBOBOX) && this.getValue() == null && this.typedText != null && this.typedText.length() > 0)
                {
                    sb.append(" value=\"" + ims.framework.utils.StringUtils.encodeXML(this.typedText) + "\"");
                }                                
                if(this.type.isSupportingValidation() && this.validationMessage != null && this.validationMessage.length() > 0)
                    sb.append(" validationString=\"" + StringUtils.encodeXML(this.validationMessage) + "\"");
                
                if(this.type.isSupportingItems())
                {
                    if(this.type.equals(DynamicCellType.ANSWER))
                        sb.append(" selectedIndex=\"" + this.selectedIndex + "\"");
                    else
                        sb.append(" selectedValue=\"" + this.selectedIndex + "\"");
                }
                
	            sb.append(" >");
	            if(this.type.isSupportingItems())
	            {
	            	if(this.type == DynamicCellType.MULTISELECT)
	            		sb.append("<items>");
	            	
	                for(int x = 0; x < this.items.size(); x++)
	                {
                        sb.append(renderItem(this.items.get(x)));	                    
	                }
	                
	                if(this.type == DynamicCellType.MULTISELECT)
	            		sb.append("</items>");
	            }
                
	            //setting the value...
                sb.append(this.renderValue());   
                
	            sb.append("</c>");
            }
            else
            {
                sb.append("<c empty=\"true\" />"); // empty cell
            }
            
            return sb.toString();
        }
        
        private StringBuffer renderItem(DynamicGridCellItem item)
        {        	
            StringBuffer sb = new StringBuffer();
         
            if(this.type == DynamicCellType.MULTISELECT)            	
            	sb.append("<item checked=\"" + (item.isChecked() ? "true" : "false") + "\"");
            else
            	sb.append("<option");
            
            if(item.getValue() != null)
            {
                sb.append(" text=\"");
                sb.append(StringUtils.encodeXML(item.getIItemText().toString()));
                sb.append("\" ");
            }
            if(item.getTextColor() != null)
            {
                sb.append(" textColor=\"");
                sb.append(item.getTextColor());
                sb.append("\" ");
            }
            if(item.getTooltip() != null)
            {
                sb.append(" tooltip=\"");
                sb.append(StringUtils.encodeXML(item.getTooltip()));
                sb.append("\" ");
            }
            if(this.type == DynamicCellType.MULTISELECT)     
            {
	            if(item.getMarkerColor() != null)
	            {
	                sb.append(" markerColor=\"");
	                sb.append(item.getMarkerColor());
	                sb.append("\" ");
	            }
            }            
            if(item.getImage() != null)
            {
                sb.append(" img=\"");
                sb.append(item.getImage().getImagePath());
                sb.append("\" ");
            }
            else if(item.getValue() != null && item.getValue() instanceof IEnhancedItem && ((IEnhancedItem)item.getValue()).getIItemImage() != null)
            {
            	sb.append(" img=\"");
                sb.append(((IEnhancedItem)item.getValue()).getIItemImage().getImagePath());
                sb.append("\" ");
            }
            sb.append("/>");
            
            return sb;
        }
        public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;	    	
	    	if(this.items.wasChanged())
	    		return true;	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;
	    	this.items.markUnchanged();
	    }
	    
	    private boolean dataWasChanged = true;           
        private int maxVisibleItemsForMultiSelect = 3;
        private Integer maxCheckedItemsForMultiSelect = null;
        private int maxLength = -1;
        private int precision = 5;
        private int scale = 2;
        private DynamicCellType type = DynamicCellType.EMPTY;
        private Object value = null;
        private int width = 100;
        private boolean readOnly = false;
        private boolean alwaysReadOnly = true;
        private String tooltip = null;
        private String buttonText = null;
        private Color buttonTextColor = null;
        private DynamicGridCellItemCollection items; 
        private DynamicGridColumn column = null;
        private DynamicGridRow row = null;
        private Color textColor = null;
	    private Color backColor = null;
        private String validationMessage = null;
        private boolean canBeEmpty = true;
        private boolean autoPostBack = false;      
        private int selectedIndex = -1;
        private String typedText = null;
        private Object identifier = null;
        private DynamicCellDecoratorMode decoratorType = DynamicCellDecoratorMode.DEFAULT;
        private boolean showOpened = false;
        private boolean fixedFont = false;
        private CharacterCasing casing = CharacterCasing.NORMAL;
        private int maxDropDownItems = -1;
        private boolean autoWrapForMultiSelect = false;
        private int tableRowId = -1;
        private int tableCellId = -1;		
	}
	class DynamicGridCellCollection implements ims.framework.controls.DynamicGridCellCollection, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
        private DynamicGridCellCollection(DynamicGridRow row)
        {
            this.row = row;
        }
        public int indexOf(ims.framework.controls.DynamicGridCell cell)
        {
            return this.cells.indexOf(cell);
        }
        public int size() 
        {
            return this.cells.size();
        }
        public ims.framework.controls.DynamicGridCell get(int index) 
        {
            return this.cells.get(index);
        }
        public ims.framework.controls.DynamicGridCell get(ims.framework.controls.DynamicGridColumn column) 
        {
            int noCells = this.cells.size();
            for(int x = 0; x < noCells; x++)
            {
                DynamicGridCell cell = this.cells.get(x);
                if(cell.getColumn().equals(column))
                    return cell;
            }
            return null;
        }
        public ims.framework.controls.DynamicGridCell newCell(ims.framework.controls.DynamicGridColumn column, DynamicCellType type)
        {
        	return newCell(column, type, null);
        }
        public ims.framework.controls.DynamicGridCell newCell(ims.framework.controls.DynamicGridColumn column, DynamicCellType type, DynamicGridCellOptions options) 
        {
        	if(column == null)
        		throw new CodingRuntimeException("Invalid column for the new cell - the column cannot be null");
        	
        	boolean columnFound = false;
        	for(int x = 0; x < this.row.grid.columns.size(); x++)
        	{
        		if(column.equals(this.row.grid.columns.get(x)))
        		{
        			columnFound = true;
        			break;
        		}
        	}
        	if(!columnFound)
        		throw new CodingRuntimeException("Invalid column for the new cell - the column does not belong to the dynamic grid");
        	
            DynamicGridCell newCell = new DynamicGridCell(this.row);
            newCell.column = (DynamicGridColumn)column;
            newCell.type = type;
            newCell.width = type.getDefaultWidth();
            newCell.alwaysReadOnly = type.isAlwaysReadOnly();
            
            if(type.isAlwaysReadOnly())
            {
            	newCell.readOnly = true;
            	newCell.autoPostBack = false;
            }            
            
            this.dataWasChanged = true;
            
            int existingCellIndex = -1;
            for(int x = 0; x < this.cells.size(); x++)
            {
            	ims.framework.controls.DynamicGridCell cell = this.cells.get(x);
            	if(column.equals(cell.getColumn()))
            	{
            		existingCellIndex = x;
            		break;
            	}
            }
            
            setDefaultCellValueIfNeeded(newCell);
            
            if(options != null)
            	newCell.setOptions(options);
            
            if(existingCellIndex == -1)
            {
            	this.cells.add(newCell);
            }
            else
            {
            	this.cells.set(existingCellIndex, newCell);
            }
            
            return newCell;
        }
        public boolean remove(ims.framework.controls.DynamicGridCell cell) 
        {
            boolean modified = this.cells.remove(cell);
            
            if(!this.dataWasChanged)
            	this.dataWasChanged = modified;
            
            return modified;
        }
        public void clear() 
        {
        	if(!this.dataWasChanged)
            	this.dataWasChanged = this.cells.size() != 0;
        	
            this.cells.clear();
        }
        public void add(ims.framework.controls.DynamicGridCell cell)
        {
        	this.dataWasChanged = true;
            this.cells.add((DynamicGridCell)cell);
        }
        public DynamicGridCell getCell(DynamicGridColumn column)
        {
            if(column == null)
                return null;
            
            for(int x = 0; x < this.cells.size(); x++)
            {
                if(column.equals(this.cells.get(x).getColumn()))
                    return this.cells.get(x);                      
            }
            
            return null;
        }       
        public String render(DynamicGridColumnCollection columns)
        {
            StringBuffer sb = new StringBuffer();
            for(int x = 0; x < columns.size(); x++)
            {
                DynamicGridColumn column = (DynamicGridColumn)columns.get(x);
                if(column.isVisible())
                {
                    DynamicGridCell cell = getCell(column); 
                    if(cell == null)
                        sb.append("<c empty=\"true\"/>");
                    else
                        sb.append(cell.render());
                }
            }
            return sb.toString();
        }
        private void setDefaultCellValueIfNeeded(DynamicGridCell newCell)
        {
            if(newCell == null || newCell.type == null)
                return;
            
            if(newCell.type.equals(DynamicCellType.BOOL))
                newCell.setValue(Boolean.FALSE);
        }
        public boolean wasChanged()
	    {
	    	if(this.dataWasChanged)
	    		return true;
	    	
	    	for(int x = 0; x < this.cells.size(); x++)
	    	{
	    		DynamicGridCell cell = this.cells.get(x);
	    		if(cell.wasChanged())
	    			return true;
	    	}
	    	
	    	return false;
	    }
	    public void markUnchanged()
	    {
	    	this.dataWasChanged = false;
	    	
	    	for(int x = 0; x < this.cells.size(); x++)
	    	{
	    		this.cells.get(x).markUnchanged();
	    	}
	    }
	    
	    private boolean dataWasChanged = true;
        private DynamicGridRow row;
        private ArrayList<DynamicGridCell> cells = new ArrayList<DynamicGridCell>();
	}
}
