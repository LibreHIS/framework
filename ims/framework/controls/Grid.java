package ims.framework.controls;

import ims.framework.Control;
import ims.framework.Menu;
import ims.framework.delegates.GridAnswerBoxValueChanged;
import ims.framework.delegates.GridButtonClicked;
import ims.framework.delegates.GridCheckBoxClicked;
import ims.framework.delegates.GridComboBoxSelectionChanged;
import ims.framework.delegates.GridCommentChanged;
import ims.framework.delegates.GridHeaderClicked;
import ims.framework.delegates.GridMutableAnswerBoxSelected;
import ims.framework.delegates.GridMutableComboBoxSelected;
import ims.framework.delegates.GridQueryComboBoxTextSubmited;
import ims.framework.delegates.GridRowExpandedCollapsed;
import ims.framework.delegates.GridSelectionChanged;
import ims.framework.delegates.GridTimeControlValueChanged;
import ims.framework.delegates.SelectionCleared;
import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.enumerations.SortOrder;
import ims.framework.utils.Color;
import ims.framework.utils.Image;

public abstract class Grid extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean canSelect, boolean canUnselect, int headerHeight, Menu menu, boolean autoPostBackTreeNode)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.canSelect = canSelect;
		this.canUnselect = canUnselect;
		this.headerHeight = headerHeight;	
		this.autoPostBackTreeNode = autoPostBackTreeNode;
	}
	protected void free()
	{
		super.free();
		
		this.selectionChangedDelegate = null;
		this.selectionClearedDelegate = null;
		this.gridCheckBoxClickedDelegate = null;
		this.gridComboBoxSelectionChangedDelegate = null;
		this.gridTimeControlValueChangedDelegate = null;
		this.gridButtonClickedDelegate = null;
		this.gridAnswerBoxValueChangedDelegate = null;
		this.gridGridHeaderClickedDelegate = null;
		this.gridMutableComboBoxSelected = null;
		this.gridMutableAnswerBoxSelected = null;
		this.gridQueryComboBoxTextSubmitedDelegate = null;
		this.gridCommentChangedDelegate = null;
		this.gridRowExpandedCollapsed = null;
	}
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	public void setReadOnly(boolean value)
	{
		if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
			this.readOnly = value;
		else
			super.flagIlegalControlModeChange("ReadOnly", value);
	}
	
	public abstract boolean isUnselectable();	
	public abstract void setUnselectable(boolean value);

	public abstract Object getValue();
	public abstract void setValue(Object value);
	public abstract void removeSelectedRow();
	
	// 08.03.2004. 
	public abstract GridRow getRowByValue(Object value);
	public abstract int getSelectedRowIndex();
	public abstract GridRow getSelectedRow();	
	
	/**
     * Specifies if the grid rows are selectable.
     */    
    public abstract void setSelectable(boolean value);
    
    /**
     * Returns true if the grid rows are selectable. 
     */    
    public abstract boolean isSelectable();
    
	//JME: 20040714: Added to enable shuffling rows in a grid easily
	public abstract void moveUp();
	public abstract void moveDown();
	public abstract void moveTo(int toIndex);
	public abstract void swap(int index1, int index2);
	public abstract boolean canMoveCurrentUp();
	public abstract boolean canMoveCurrentDown();
	public abstract boolean rowSelected();
	
	public abstract void sort(int columnIndex);
	public abstract void sort(int columnIndex, SortOrder dir);

	public abstract GridRowCollection getRows();

	public abstract void addTreeColumn(String caption, int captionAlignment, int width, boolean bold, boolean canGrow);
	public abstract void addBoolColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int sortOrder, boolean canGrow);
	public abstract void addIntColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow, int maxLength);
	public abstract void addStringColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, int maxLength, boolean canGrow, CharacterCasing casing);
	public abstract void addHtmlColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int sortOrder, boolean canGrow);
	public abstract void addDateColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow);
	public abstract void addTimeColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, String validationString, boolean bold, int sortOrder, boolean canGrow);
	public abstract void addCommentColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int maxLength, boolean canGrow);
	public abstract void addImageColumn(String caption, int captionAlignment, int alignment, int width, boolean canGrow, int sortOrder);
	public abstract void addComboBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean canGrow, int maxDropDownItems);	
	public abstract void addMutableComboBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, boolean autoPostBack, boolean bold, boolean searchable, boolean canGrow, int maxDropDownItems);
	public abstract void addDecimalColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, int precision, int scale, String validationString, boolean bold, int sortOrder, boolean canGrow);
	public abstract void addWrapTextColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean bold, int maxLength, boolean canGrow);
	public abstract void addButtonColumn(String caption, int captionAlignment, int alignment, int width, boolean bold, boolean canGrow);
	public abstract void addAnswerBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, int imageHeight, boolean canBeEmpty, boolean canGrow);
	public abstract void addMutableAnswerBoxColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean autoPostBack, boolean canBeEmpty, boolean canGrow);
	public abstract void addPartialDateColumn(String caption, int captionAlignment, int alignment, int width, boolean readOnly, boolean canBeEmpty, String validationString, boolean bold, int sortOrder, boolean canGrow);
	public abstract void setColumnCaption(int column, String caption);
	public abstract void setColumnReadOnly(int column, boolean value);
	public abstract void setColumnHeaderTooltip(int column, String value);
	public abstract String getColumnHeaderTooltip(int column);

	 //PDS Stuff
    public abstract void setRowSelectionChangedEventRequirePdsAuthentication(boolean value);
    
	// Combobox
	public abstract void columnNewRow(int columnIndex, Object value, String text, Image image, Color textColor);
	public abstract void columnClear(int columnIndex);
	public abstract int columnSize(int columnIndex);
	
	// Answerbox
	public abstract void answerBoxColumnNewOption(int column, AnswerBoxOption option);
	public abstract void answerBoxColumnClear(int column);
	
	public abstract void resetScrollPosition();
	public abstract void setFooterMaxHeight(int value);
    public abstract void setFooterValue(String value);

	public void setSelectionChangedEvent(GridSelectionChanged delegate)
	{
		this.selectionChangedDelegate = delegate;
	}
	public void setSelectionClearedEvent(SelectionCleared delegate)
	{
		this.selectionClearedDelegate = delegate;
	}
	public void setGridCheckBoxClickedEvent(GridCheckBoxClicked delegate)
	{
		this.gridCheckBoxClickedDelegate = delegate;
	}
	public void setGridComboBoxSelectionChangedEvent(GridComboBoxSelectionChanged delegate)
	{
		this.gridComboBoxSelectionChangedDelegate = delegate;
	}
	public void setGridTimeControlValueChangedEvent(GridTimeControlValueChanged delegate)
	{
		this.gridTimeControlValueChangedDelegate = delegate;
	}
	public void setGridButtonClickedEvent(GridButtonClicked delegate)
	{
		this.gridButtonClickedDelegate = delegate;
	}
	public void setGridAnswerBoxValueChangedEvent(GridAnswerBoxValueChanged delegate)
	{
		this.gridAnswerBoxValueChangedDelegate = delegate;
	}
	public void setGridHeaderClickedEvent(GridHeaderClicked delegate)
	{
		this.gridGridHeaderClickedDelegate = delegate;
	}
	public void setGridMutableComboBoxSelectedEvent(GridMutableComboBoxSelected delegate)
	{
		this.gridMutableComboBoxSelected = delegate;
	}
	public void setGridMutableAnswerBoxSelectedEvent(GridMutableAnswerBoxSelected delegate)
	{
		this.gridMutableAnswerBoxSelected = delegate;
	}
	public void setGridQueryComboBoxTextSubmitedEvent(GridQueryComboBoxTextSubmited delegate)
	{
		this.gridQueryComboBoxTextSubmitedDelegate = delegate;
	}
	public void setGridCommentChangedEvent(GridCommentChanged delegate)
	{
		this.gridCommentChangedDelegate = delegate;
	}
	public void setGridRowExpandedCollapsedEvent(GridRowExpandedCollapsed delegate)
	{
		this.gridRowExpandedCollapsed = delegate;
	}
	
	protected GridSelectionChanged selectionChangedDelegate = null;
	protected SelectionCleared selectionClearedDelegate = null;
	protected GridCheckBoxClicked gridCheckBoxClickedDelegate = null;
	protected GridComboBoxSelectionChanged gridComboBoxSelectionChangedDelegate = null;
	protected GridTimeControlValueChanged gridTimeControlValueChangedDelegate = null;
	protected GridButtonClicked gridButtonClickedDelegate = null;
	protected GridAnswerBoxValueChanged gridAnswerBoxValueChangedDelegate = null;
	protected GridHeaderClicked gridGridHeaderClickedDelegate = null;
	protected GridMutableComboBoxSelected gridMutableComboBoxSelected = null;
	protected GridMutableAnswerBoxSelected gridMutableAnswerBoxSelected = null;
	protected GridQueryComboBoxTextSubmited gridQueryComboBoxTextSubmitedDelegate = null;
	protected GridCommentChanged gridCommentChangedDelegate = null;
	protected GridRowExpandedCollapsed gridRowExpandedCollapsed = null; 
	
	protected boolean canSelect;
	protected boolean canUnselect;
	protected int headerHeight;
	protected boolean readOnly = true;
	protected boolean autoPostBackTreeNode;
}