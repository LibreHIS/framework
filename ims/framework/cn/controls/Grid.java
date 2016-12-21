package ims.framework.cn.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.Menu;
import ims.framework.cn.data.GridData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.GridAnswerBoxChanged;
import ims.framework.cn.events.GridButtonClicked;
import ims.framework.cn.events.GridCheckBoxClicked;
import ims.framework.cn.events.GridComboBoxSelected;
import ims.framework.cn.events.GridCommentChanged;
import ims.framework.cn.events.GridDateControlChanged;
import ims.framework.cn.events.GridDecimalBoxChanged;
import ims.framework.cn.events.GridHeaderClicked;
import ims.framework.cn.events.GridIntBoxChanged;
import ims.framework.cn.events.GridMutableAnswerBoxSelected;
import ims.framework.cn.events.GridMutableComboBoxSelected;
import ims.framework.cn.events.GridNodeExpanded;
import ims.framework.cn.events.GridPartialDateControlChanged;
import ims.framework.cn.events.GridQueryComboBoxTextSubmited;
import ims.framework.cn.events.GridRowSelectionCleared;
import ims.framework.cn.events.GridSelected;
import ims.framework.cn.events.GridTextBoxChanged;
import ims.framework.cn.events.GridTimeBoxChanged;
import ims.framework.cn.events.GridWrapTextChanged;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.InvalidControlValue;
import ims.framework.controls.AnswerBoxOption;
import ims.framework.controls.GridRow;
import ims.framework.controls.GridRowCollection;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.GridColumnType;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public class Grid extends ims.framework.controls.Grid implements IVisualControl
{ 
	private static final long serialVersionUID = 1L;
	
	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canSelect, boolean canUnselect, int headerHeight, boolean groupParents, Menu menu, boolean alwaysShowVScroll, boolean autoPostBackTreeNode, int footerMaxHeight, String footerValue, boolean shadow, boolean alternateRowColor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, canSelect, canUnselect, headerHeight, menu, autoPostBackTreeNode);
		this.tabIndex = tabIndex;
		this.groupParents = groupParents;
		this.alwaysShowVScroll = alwaysShowVScroll;
		this.footerMaxHeight = footerMaxHeight;
		this.footerValue = footerValue;
		this.shadow = shadow;
		this.alternateRowColor = alternateRowColor;
	}
	protected void free()
	{
		super.free();
		
		this.data.setColumns(null);
		this.data = null;
		this.columns.clear();
		this.columns.trimToSize();
	}
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setRowSelectionChangedEventRequirePdsAuthentication(boolean value)
    {
    	this.data.setRowSelectionChangedEventRequirePdsAuthentication(value);
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
	public void addBoolColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnBoolean(caption, captionAlignment, alignment, width, readOnly, autoPostBack, sortOrder, canGrow));
	}

	public void addComboBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean canGrow, int maxDropDownItems)
	{
		this.columns.add(new GridColumnComboBox(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, autoPostBack, bold, canGrow, maxDropDownItems));
	}
	
	public void addCommentColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int maxLength, boolean canGrow)
	{
		this.columns.add(new GridColumnComment(caption, captionAlignment, alignment, width, readOnly, autoPostBack, maxLength, canGrow));
	}

	public void addDateColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnDate(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, validationString, bold, sortOrder, canGrow));
	}

	public void addDecimalColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, int precision, int scale, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnDecimal(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, precision, scale, validationString, bold, sortOrder, canGrow));
	}

	public void addImageColumn(String caption, int captionAlignment, int alignment, int width, boolean canGrow, int sortOrder)
	{
		this.columns.add(new GridColumnImage(caption, captionAlignment, alignment, width, canGrow, sortOrder));
	}

	public void addIntColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow, int maxLength)
	{
		this.columns.add(new GridColumnInteger(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, validationString, bold, sortOrder, canGrow, maxLength));
	}

	public void addButtonColumn(String caption, int captionAlignment, int alignment, int width, boolean bold, boolean canGrow)
	{
		this.columns.add(new GridColumnButton(caption, captionAlignment, alignment, width, bold, canGrow));
	}

	public void addMutableComboBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean searchable, boolean canGrow, int maxDropDownItems)
	{
		this.columns.add(new GridColumnMutableComboBox(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, autoPostBack, bold, searchable, canGrow, maxDropDownItems));
	}

	public void addStringColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, int maxLength, boolean canGrow, CharacterCasing casing)
	{
		this.columns.add(new GridColumnString(caption, captionAlignment, alignment, width, readOnly, bold, sortOrder, maxLength, canGrow, casing));
	}
	public void addHtmlColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnHtml(caption, captionAlignment, alignment, width, readOnly, bold, sortOrder, canGrow));
	}

	public void addTimeColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnTime(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, autoPostBack, validationString, bold, sortOrder, canGrow));
	}

	public void addTreeColumn(String caption, int captionAlignment, int width, boolean bold, boolean canGrow)
	{
		this.columns.add(new GridColumnTree(caption, captionAlignment, width, bold, canGrow));
	}

	public void addWrapTextColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int maxLength, boolean canGrow)
	{
		this.columns.add(new GridColumnWrapText(caption, captionAlignment, alignment, width, readOnly, bold, maxLength, canGrow));
	}
	
	public void addAnswerBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int imageHeight, boolean canBeEmpty, boolean canGrow)
	{
		this.columns.add(new GridColumnAnswerBox(caption, captionAlignment, alignment, width, readOnly, autoPostBack, imageHeight, canBeEmpty, canGrow));
	}

	public void addMutableAnswerBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, boolean canBeEmpty, boolean canGrow)
	{
		this.columns.add(new GridColumnMutableAnswerBox(caption, captionAlignment, alignment, width, readOnly, autoPostBack, canBeEmpty, canGrow));
	}
	
	public void addPartialDateColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		this.columns.add(new GridColumnPartialDate(caption, captionAlignment, alignment, width, readOnly, canBeEmpty, validationString, bold, sortOrder, canGrow));
	}
	
	public void columnClear(int column)
	{
		this.data.clearComboBox(column);
	}

	public void columnNewRow(int column, Object value, String text, Image image, Color textColor)
	{
		this.data.newRowComboBox(column, value, text, image, textColor);
	}

	public int columnSize(int column)
	{
		return this.data.sizeComboBox(column);
	}

	public void answerBoxColumnNewOption(int column, AnswerBoxOption option)
	{
		this.data.answerBoxColumnNewOption(column, option);
	}

	public void answerBoxColumnClear(int column) 
	{
		this.data.answerBoxColumnClear(column);
	}

	public GridRowCollection getRows()
	{
		return this.data.getRows();
	}

	public Object getValue()
	{
		return this.data.getValue();
	}

	public void setValue(Object value)
	{
		this.data.setSelection(value);
	}
	public void removeSelectedRow()
	{
		this.data.removeSelectedRow();
	}
	
	public ims.framework.controls.GridRow getRowByValue(Object value)
	{
		if (value == null)
			return null;
		return this.data.getRowByValue(value);
	}

	public GridRow getSelectedRow()
	{
		return this.data.getSelectedRow();
	}
	
	public int getSelectedRowIndex()
	{
		return this.data.getSelectedRowIndex();
	}

	public boolean rowSelected() 
	{
		return (this.getSelectedRowIndex() != -1);
	}

	public boolean canMoveCurrentUp()
	{
		return this.data.canMoveCurrentUp();		
	}

	public boolean canMoveCurrentDown()
	{
		return this.data.canMoveCurrentDown();
	}


	/**
	 * Moves the currently select row up one place.
	 * If no current row, method has no effect.
	 * If current row is at top of grid, method has no effect
	 */
	public void moveUp()
	{
		this.data.moveUp();
	}

	/**
	 * Moves the currently select row down one place.
	 * If no current row, method has no effect.
	 * If current row is at bottom of grid, method has no effect
	 */
	public void moveDown()
	{
		this.data.moveDown();
	}
	
	/**
	 * Moves the currently select row to the position specified by toIndex.
	 * If no current row, method has no effect.
	 * If toIndex is beyond last row index, method has no effect
	 * If toIndex < 0, method has effect
	 * If index of current row = toIndex, method has no effect
	 */	
	public void moveTo(int toIndex)
	{
		this.data.moveTo(toIndex);		
	}	
	
	/**
	 * Swaps the 2 rows specified by index1 and index2
	 * If either index1 or index1 does not exist, method has no effect
	 * If either index1 or index1 < 0, method has no effect
	 * If index1 = index2 , method has no effect
	 */
	public void swap(int index1, int index2)
	{
		this.data.swap(index1, index2);
	}

	public void sort(int columnIndex)
	{
		sort(columnIndex,SortOrder.ASCENDING);
	}
	public void sort(int columnIndex, SortOrder dir)
	{
		this.data.sort(columnIndex, dir);
	}
	public void setColumnCaption(int column, String caption)
	{
		this.columns.get(column).setCaption(caption);
		this.data.setColumnCaption(column, caption);
	}
	public void setColumnReadOnly(int column, boolean value)
	{
		this.columns.get(column).setReadOnly(value);
	}	
	public void resetScrollPosition()
	{
		this.resetScrollPosition = true; 
	}
	public boolean isUnselectable() 
	{
		return this.canUnselect;
	}
	public void setUnselectable(boolean value) 
	{
		this.canUnselect = value;
		this.data.setUnselectable(value);
	}
	@Override
	public boolean isSelectable()
	{
		return canSelect;
	}
	@Override
	public void setSelectable(boolean value)
	{
		canSelect = value;
		this.data.setSelectable(value);
	}
	public void setFooterMaxHeight(int value)
	{
		if(value < 0)
			throw new CodingRuntimeException("Invalid footer height");
		
		this.footerMaxHeight = value;
		this.data.setFooterMaxHeight(value);
	}
	public void setFooterValue(String value)
	{
		this.footerValue = value;
		this.data.setFooterValue(value);
	}
	public String getColumnHeaderTooltip(int column)
	{
		return this.columns.get(column).getHeaderTooltip();
	}
	public void setColumnHeaderTooltip(int column, String value)
	{
		this.columns.get(column).setHeaderTooltip(value);
	}

	public void restore(IControlData data, boolean isNew)
	{		
		this.data = (GridData) data;		
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		super.readOnly = this.data.isReadOnly();		
				
		this.data.setColumns(this.columns);
	    this.data.setGroupParents(this.groupParents);
		//this.data.setAnswerBoxData(this.answerBoxesData);
		
		if(isNew)
		{
			this.data.setUnselectable(this.canUnselect);
			this.data.setFooterMaxHeight(this.footerMaxHeight);
			this.data.setFooterValue(this.footerValue);
			this.data.setSelectable(super.canSelect);
			this.resetScrollPosition = true;
		}
		else
		{
			this.canUnselect = this.data.isUnselectable();
			this.footerMaxHeight = this.data.getFooterMaxHeight();
			this.footerValue = this.data.getFooterValue();
			super.canSelect = this.data.isSelectable();
		}
		
		this.data.dataWasRendered = false;
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(event instanceof InvalidControlValue)
    	{
    		// ignore the event, there is nothing we can do at this stage. 		
    		return true;
    	}
		else if(event instanceof GridRowSelectionCleared)
		{
			boolean selectedRowChanged = this.data.isSelectedRowChanged();
			this.data.setSelection(-1);			
			if(!selectedRowChanged)
				this.data.setSelectedRowUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.selectionClearedDelegate != null)
				super.selectionClearedDelegate.handle();
			
			return true;
		}
		else if(event instanceof GridSelected)
		{
			boolean selectedRowChanged = this.data.isSelectedRowChanged();			
			this.data.setSelection(((GridSelected)event).getRow());			
			if(!selectedRowChanged)
				this.data.setSelectedRowUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.selectionChangedDelegate != null)
				super.selectionChangedDelegate.handle(((GridSelected)event).getMouseButton());
			
			return true;
		}
		else if(event instanceof GridCheckBoxClicked)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridCheckBoxClicked tmp = (GridCheckBoxClicked)event;
			ims.framework.controls.GridRow checkedRow = this.data.setBooleanValue(tmp.getRow(), tmp.getColumn(), tmp.isChecked());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridCheckBoxClickedDelegate != null)
			{	
				super.gridCheckBoxClickedDelegate.handle(tmp.getColumn(), checkedRow, tmp.isChecked());
			}
			
			return true;
		}
		else if(event instanceof GridComboBoxSelected)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridComboBoxSelected tmp = (GridComboBoxSelected)event;
			Object[] arr = this.data.setComboBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridComboBoxSelectionChangedDelegate != null)
			{	
				super.gridComboBoxSelectionChangedDelegate.handle(tmp.getColumn(), (ims.framework.controls.GridRow)arr[0], arr[1]);
			}
			
			return true;
		}
		else if(event instanceof GridQueryComboBoxTextSubmited)
		{
			GridQueryComboBoxTextSubmited tmp = (GridQueryComboBoxTextSubmited)event;

			GridRow row = this.data.getRowByIndex(tmp.getRow());
			row.setEditedText(tmp.getColumn(), tmp.getText());

			if(super.gridQueryComboBoxTextSubmitedDelegate != null)
			{	
				super.gridQueryComboBoxTextSubmitedDelegate.handle(tmp.getColumn(), this.data.getRowByIndex(tmp.getRow()), tmp.getText());
			}
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridDateControlChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridDateControlChanged tmp = (GridDateControlChanged)event;
			this.data.setDateControlValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridPartialDateControlChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridPartialDateControlChanged tmp = (GridPartialDateControlChanged)event;
			this.data.setPartialDateControlValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridDecimalBoxChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridDecimalBoxChanged tmp = (GridDecimalBoxChanged)event;
			this.data.setDecimalBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridIntBoxChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridIntBoxChanged tmp = (GridIntBoxChanged)event;
			this.data.setIntBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridWrapTextChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridWrapTextChanged tmp = (GridWrapTextChanged)event;
			this.data.setWrapTextValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridTextBoxChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridTextBoxChanged tmp = (GridTextBoxChanged)event;
			this.data.setTextBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridCommentChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridCommentChanged tmp = (GridCommentChanged)event;
			this.data.setCommentValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridCommentChangedDelegate != null)
				super.gridCommentChangedDelegate.handle(tmp.getColumn(), this.data.getRowByIndex(tmp.getRow()));
			
			return true;
		}
		else if(event instanceof GridTimeBoxChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridTimeBoxChanged tmp = (GridTimeBoxChanged)event;
			this.data.setTimeBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridTimeControlValueChangedDelegate != null)
			{			
				super.gridTimeControlValueChangedDelegate.handle(tmp.getColumn(), this.data.getRowByIndex(tmp.getRow()), tmp.getValue());
			}
			
			return true;
		}
		else if(event instanceof GridNodeExpanded)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridNodeExpanded tmp = (GridNodeExpanded)event;
			this.data.expandRow(tmp.getRow(), tmp.isExpanded());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			if(this.autoPostBackTreeNode && this.gridRowExpandedCollapsed != null)
				this.gridRowExpandedCollapsed.handle(this.data.getRowByIndex(tmp.getRow()));
			
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridAnswerBoxChanged)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridAnswerBoxChanged tmp = (GridAnswerBoxChanged)event;
			ims.framework.controls.GridRow gridRow = this.data.setAnswerBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getIndex());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridAnswerBoxValueChangedDelegate != null)
			{	
				super.gridAnswerBoxValueChangedDelegate.handle(tmp.getColumn(), gridRow, tmp.getIndex());
			}
			
			return true;
		}
		else if (event instanceof GridButtonClicked)
		{
			GridButtonClicked tmp = (GridButtonClicked)event;
			if(super.gridButtonClickedDelegate != null)
			{	
				super.gridButtonClickedDelegate.handle(tmp.getColumn(), this.data.getRowByIndex(tmp.getRow()));
			}
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridHeaderClicked)
		{
			if(super.gridGridHeaderClickedDelegate != null)
			{	
				super.gridGridHeaderClickedDelegate.handle(((GridHeaderClicked)event).getColumn());
			}
			this.data.dataWasRendered = true;
			
			return true;
		}
		else if(event instanceof GridMutableComboBoxSelected)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridMutableComboBoxSelected tmp = (GridMutableComboBoxSelected)event;
			Object[] o = this.data.setMutableComboBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridMutableComboBoxSelected != null)
			{	
				try
				{
					super.gridMutableComboBoxSelected.handle(tmp.getColumn(), (ims.framework.controls.GridRow)o[0], o[1]);
				}
				catch (Throwable t)
				{
					throw new PresentationLogicException("Grid.MutableComboBox.SelectionChanged {ID = " + String.valueOf(super.id) + "}", t);
				}
			}
			
			return true;
		}
		else if(event instanceof GridMutableAnswerBoxSelected)
		{
			boolean rowsChanged = this.data.isRowsChanged();
			
			GridMutableAnswerBoxSelected tmp = (GridMutableAnswerBoxSelected)event;
			Object[] o = this.data.setMutableAnswerBoxValue(tmp.getRow(), tmp.getColumn(), tmp.getValue());
			
			if(!rowsChanged)
				this.data.setRowsUnchanged();
			
			this.data.dataWasRendered = true;
			
			if(super.gridMutableAnswerBoxSelected != null)
			{	
				try
				{
					super.gridMutableAnswerBoxSelected.handle(tmp.getColumn(), (ims.framework.controls.GridRow)o[0], (AnswerBoxOption)o[1]);
				}
				catch (Throwable t)
				{
					throw new PresentationLogicException("Grid.MutableAnswerBox.SelectionChanged {ID = " + String.valueOf(super.id) + "}", t);
				}
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<grid id=\"a");
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
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(super.headerHeight != 24)
		{
			sb.append("\" headerHeight=\"");
			sb.append(super.headerHeight);
		}
		if(this.alwaysShowVScroll)
		{
			sb.append("\" alwaysShowVScroll=\"true");
		}
		if(super.autoPostBackTreeNode)
		{
			sb.append("\" treeAutoPostBack=\"true");
		}
		if(!super.canSelect)
		{
			sb.append("\" selectorVisible=\"false");
		}
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
		}
		
		// Hardcoded, if a row is selected it will became visible
		// Does not need to be sent anymore as default value has been changed to true
		// sb.append("\" alwaysScrollToSelected=\"true");
		
		sb.append("\">");
		sb.append(this.data.parseColumns().toString());		
		sb.append("</grid>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<grid id=\"a");
		sb.append(super.id);	
		
		if(this.data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			this.data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					this.data.setEnabledUnchanged();
				}
			}
			
			if(resetScrollPosition)
			{
				resetScrollPosition = false; 
				sb.append("\" resetScrollPosition=\"true");				
			}
			
			if(this.data.isEnabled())
			{
				if(this.data.isReadOnlyChanged())
				{
					sb.append("\" readOnly=\"");
					sb.append(this.data.isReadOnly() ? "true" : "false");
					this.data.setReadOnlyUnchanged();
				}
			}
			
			if(this.data.isSelectableChanged())
			{
				sb.append("\" selectorVisible=\"");
				sb.append(this.canSelect ? "true" : "false");
				this.data.setSelectableUnchanged();
			}
			
			if(this.data.isUnselectableChanged())
			{
				sb.append("\" allowClearSelection=\"");
				sb.append(this.canUnselect ? "true" : "false");
				this.data.setUnselectableUnchanged();
			}
		
			if(this.data.isSelectedRowChanged())
			{
				sb.append("\" selectedRow=\"");
				sb.append(this.data.getSelectedRowIndex());
				this.data.setSelectedRowUnchanged();
			}
			
			if (data.isRowSelectionChangedEventRequirePdsAuthenticationChanged())
			{
				sb.append("\" rowSelectionChangedEventRequirePdsAuthentication=\"");
				sb.append(this.data.isRowSelectionChangedEventRequirePdsAuthenticationChanged() ? "true" : "false");
				data.setRowSelectionChangedEventRequirePdsAuthenticationUnchanged();
			}
			
			sb.append("\">");
			
			if(data.isFooterMaxHeightChanged() && this.footerMaxHeight == 0)
			{
				sb.append("<footer/>");
				data.setFooterMaxHeightUnchanged();
			}
			else if(data.isFooterMaxHeightChanged() || data.isFooterValueChanged())
			{
				sb.append("<footer ");
				
				if(data.isFooterMaxHeightChanged())
				{
					sb.append("maxHeight=\"" + this.footerMaxHeight + "\"");
					data.setFooterMaxHeightUnchanged();
				}					
				
				if(data.isFooterValueChanged())
				{
					sb.append(">");					
					sb.append("<![CDATA[");
					if(this.footerValue != null)
						sb.append(this.footerValue);
					sb.append("]]>");
					sb.append("</footer>");
					data.setFooterValueUnchanged();
				}
				else
				{
					sb.append("/>");
				}
			}
			
			if(this.data.isColumnNamesChanged())
			{
				sb.append(this.data.parseColumnNames());
				this.data.setColumnNamesUnchanged();
			}
			
			if(this.data.isRowsChanged())
			{
				sb.append("<rows>");
				this.data.renderRows(sb, this.data.getRows());
				this.data.setRowsUnchanged();
				sb.append("</rows>");
			}
			
			sb.append("</grid>");
		}
		else
		{
			sb.append("\" />");
		}
		
		this.data.dataWasRendered = true;
	}
	public boolean wasChanged() 
	{
		if(this.data.isVisibleChanged())			
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(this.data.isEnabledChanged())
				{
					return true;
				}
			}
			
			if(resetScrollPosition)
				return true;
			
			if(this.data.isEnabled())
			{
				if(this.data.isReadOnlyChanged())
				{
					return true;
				}
			}
			
			if(this.data.isFooterMaxHeightChanged())
				return true;
			
			if(this.data.isFooterValueChanged() && this.footerMaxHeight != 0)
				return true;
			
			if(this.data.isUnselectableChanged())
			{
				return true;
			}
			
			if(this.data.isSelectedRowChanged())
			{
				return true;
			}
			
			if(this.data.isColumnNamesChanged())
			{
				return true;
			}
			
			if(this.data.isRowsChanged())
			{
				return true;
			}
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}	

	private boolean resetScrollPosition = false;
	private GridData data;
	private int tabIndex;
	private boolean groupParents;
	private boolean alwaysShowVScroll = false;
	private boolean shadow = false;
	private ArrayList<GridColumn> columns = new ArrayList<GridColumn>();
	private int footerMaxHeight = 0;
	private String footerValue;
	private boolean alternateRowColor = true;
}

class GridColumnTree extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnTree(String caption, int captionAlignment, int width, boolean bold, boolean canGrow)
	{
		super(caption, captionAlignment, 0, width, true, bold, canGrow);
	}
	public GridColumnType getType()
	{
		return GridColumnType.TREE;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" type=\"TreeNode\"/>");
	}
}
class GridColumnBoolean extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnBoolean(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, false, canGrow);
		this.autoPostBack = autoPostBack;
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.BOOL;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\" type=\"Bool\"/>");
	}
	boolean isAutoPostBack()
	{
		return this.autoPostBack;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean autoPostBack;
	private int sortOrder;
}
class GridColumnInteger extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnInteger(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow, int maxLength)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.validationString = validationString;
		this.sortOrder = sortOrder;
		this.maxLength = maxLength;
	}
	public GridColumnType getType()
	{
		return GridColumnType.INTEGER;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}		
		if(this.maxLength > 0)
		{
			sb.append("\" maxLength=\"");
			sb.append(this.maxLength);			
		}
		
		sb.append("\" type=\"Int\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean canBeEmpty;
	private String validationString;
	private int sortOrder;
	private int maxLength;
}
class GridColumnString extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnString(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, int maxLength, boolean canGrow, CharacterCasing casing)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.sortOrder = sortOrder;
		this.maxLength = maxLength;
		this.casing = casing;
	}
	public GridColumnType getType()
	{
		return GridColumnType.STRING;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		if(this.maxLength > 0)
		{
			sb.append(" maxLength=\"");
			sb.append(this.maxLength);
			sb.append("\"");
		}
		sb.append(" type=\"String\"/>");
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	public CharacterCasing getCharacterCasing()
	{
		return this.casing;
	}
	
	private int sortOrder;
	private int maxLength;
	private CharacterCasing casing = CharacterCasing.NORMAL; 
}
class GridColumnHtml extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnHtml(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.HTML;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);		
		sb.append(" type=\"Html\"/>");
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private int sortOrder;	
}
class GridColumnDate extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnDate(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.validationString = validationString;
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.DATE;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		sb.append("\" type=\"Date\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean canBeEmpty;
	private String validationString;
	private int sortOrder;
}
class GridColumnPartialDate extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnPartialDate(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.validationString = validationString;
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.PARTIALDATE;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		sb.append("\" type=\"PartialDateBox\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean canBeEmpty;
	private String validationString;
	private int sortOrder;
}
class GridColumnTime extends GridColumn
{
	private static final long serialVersionUID = 1L;
	
	public GridColumnTime(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.autoPostBack = autoPostBack;
		this.validationString = validationString;
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.TIME;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		sb.append("\" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		sb.append("\" type=\"Time\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean canBeEmpty;
	private boolean autoPostBack;
	private String validationString;
	private int sortOrder;
}
class GridColumnComment extends GridColumn
{
	private static final long serialVersionUID = 1L;
	public GridColumnComment(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int maxLength, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, false, canGrow);
		this.autoPostBack = autoPostBack;
		this.maxLength = maxLength;
	}
	public GridColumnType getType()
	{
		return GridColumnType.COMMENT;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\"");
		if(this.maxLength > 0)
		{
			sb.append(" maxLength=\"");
			sb.append(this.maxLength);
			sb.append("\"");
		}
		sb.append(" type=\"Comment\"/>");
	}
	private int maxLength;
	private boolean autoPostBack;
}
class GridColumnImage extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnImage(String caption, int captionAlignment, int alignment, int width, boolean canGrow, int sortOrder)
	{
		super(caption, captionAlignment, alignment, width, true, false, canGrow);
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.IMAGE;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" type=\"Image\"/>");
	}
	private int sortOrder;
}
class GridColumnComboBox extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnComboBox(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean canGrow, int maxDropDownItems)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.autoPostBack = autoPostBack;
		super.maxDropDownItems = maxDropDownItems;
	}
	public GridColumnType getType()
	{
		return GridColumnType.COMBOBOX;
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getMaxDropDownItems()
	{
		return super.maxDropDownItems;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		sb.append("\" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\" type=\"ComboBox\">");
	}
	private boolean canBeEmpty;
	private boolean autoPostBack;	
}
class GridColumnButton extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnButton(String caption, int captionAlignment, int alignment, int width, boolean bold, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, false, bold, canGrow);
	}
	public GridColumnType getType()
	{
		return GridColumnType.BUTTON;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" autoPostBack=\"true\" type=\"Button\"/>");
	}
}
class GridColumnMutableComboBox extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnMutableComboBox(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean searchable, boolean canGrow, int maxDropDownItems)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.canBeEmpty = canBeEmpty;
		this.autoPostBack = autoPostBack;
		this.searchable = searchable;
		super.maxDropDownItems = maxDropDownItems;
	}
	public GridColumnType getType()
	{
		return GridColumnType.MUTABLECOMBOBOX;
	}
	public boolean isSearchable()
	{
		return this.searchable; 
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		sb.append("\" searchable=\"");
		sb.append(this.searchable ? "true" : "false");
		sb.append("\" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\" type=\"MutableComboBox\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getMaxDropDownItems()
	{
		return super.maxDropDownItems;
	}
	private boolean canBeEmpty;
	private boolean autoPostBack;
	private boolean searchable;	
}
class GridColumnDecimal extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnDecimal(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, int precision, int scale, String validationString, boolean bold, int sortOrder, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow, precision, scale);
		this.canBeEmpty = canBeEmpty;
		this.precision = precision;
		this.scale = scale;
		this.validationString = validationString;
		this.sortOrder = sortOrder;
	}
	public GridColumnType getType()
	{
		return GridColumnType.DECIMAL;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" precision=\"");
		sb.append(this.precision);
		sb.append("\" scale=\"");
		sb.append(this.scale);
		sb.append("\" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		if (this.validationString != null)
		{
			sb.append("\" validationString=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.validationString));
		}
		sb.append("\" type=\"Decimal\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public int getSortOrder()
	{
		return this.sortOrder;
	}
	private boolean canBeEmpty;
	private int precision;
	private int scale;
	private String validationString;
	private int sortOrder;
}
class GridColumnWrapText extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnWrapText(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int maxLength, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, bold, canGrow);
		this.maxLength = maxLength;
	}
	public GridColumnType getType()
	{
		return GridColumnType.WRAPTEXT;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		if(this.maxLength > 0)
		{
			sb.append(" maxLength=\"");
			sb.append(this.maxLength);
			sb.append("\"");
		}
		sb.append(" type=\"WrapText\"/>");
	}	
	private int maxLength;
}
class GridColumnAnswerBox extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnAnswerBox(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int imageHeight, boolean canBeEmpty, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, false, canGrow);
		this.autoPostBack = autoPostBack;
		this.imageHeight = imageHeight;
		this.canBeEmpty = canBeEmpty;
	}
	public GridColumnType getType()
	{
		return GridColumnType.ANSWERBOX;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		sb.append("\" imgHeight=\"");
		sb.append(this.imageHeight);
		sb.append("\" type=\"AnswerBox\">");
	}
	private boolean autoPostBack;
	private int imageHeight;
	private boolean canBeEmpty;
}



class GridColumnMutableAnswerBox extends GridColumn
{
	private static final long serialVersionUID = 1L;

	public GridColumnMutableAnswerBox(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, boolean canBeEmpty, boolean canGrow)
	{
		super(caption, captionAlignment, alignment, width, readOnly, false, canGrow);
		this.autoPostBack = autoPostBack;
		this.canBeEmpty = canBeEmpty;
	}
	public GridColumnType getType()
	{
		return GridColumnType.MUTABLEANSWERBOX;
	}
	public void parse(StringBuffer sb)
	{
		super.parse(sb);
		sb.append(" autoPostBack=\"");
		sb.append(this.autoPostBack ? "true" : "false");
		sb.append("\" canBeEmpty=\"");
		sb.append(this.canBeEmpty ? "true" : "false");
		sb.append("\" type=\"MutableAnswerBox\"/>");
	}
	public boolean canBeEmpty()
	{
		return this.canBeEmpty;
	}
	public boolean wasChanged() 
	{
		return true;
	}
	public void markUnchanged() 
	{
	}
	
	private boolean autoPostBack;
	private boolean canBeEmpty;	
}
