package ims.framework.controls;

import java.io.Serializable;

import ims.framework.IControlComparable;
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
import ims.framework.enumerations.MouseButton;
import ims.framework.enumerations.SortOrder;
import ims.framework.utils.Time;

public abstract class GridBridge implements IControlComparable, Serializable
{
	private static final long serialVersionUID = 1L;
	protected void setContext(Grid grid)
	{ 
		this.grid = grid;
		this.grid.setSelectionChangedEvent(new GridSelectionChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle(MouseButton mouseButton)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.selectionChangedDelegate != null)
					GridBridge.this.selectionChangedDelegate.handle(mouseButton);
			}
		});
		this.grid.setSelectionClearedEvent(new SelectionCleared()
		{
			private static final long serialVersionUID = 1L;

			public void handle()  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.selectionClearedDelegate != null)
					GridBridge.this.selectionClearedDelegate.handle();
			}
		});
		this.grid.setGridCheckBoxClickedEvent(new GridCheckBoxClicked()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, boolean isChecked)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridCheckBoxClickedDelegate != null)
					GridBridge.this.gridCheckBoxClickedDelegate.handle(column, row, isChecked);
			}
		});
		this.grid.setGridComboBoxSelectionChangedEvent(new GridComboBoxSelectionChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, Object value)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridComboBoxSelectionChangedDelegate != null)
					GridBridge.this.gridComboBoxSelectionChangedDelegate.handle(column, row, value);
			}
		});
		this.grid.setGridTimeControlValueChangedEvent(new GridTimeControlValueChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, Time value)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridTimeControlValueChangedDelegate != null)
					GridBridge.this.gridTimeControlValueChangedDelegate.handle(column, row, value);
			}
		});
		this.grid.setGridButtonClickedEvent(new GridButtonClicked()
			{
				private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridButtonClickedDelegate != null)
					GridBridge.this.gridButtonClickedDelegate.handle(column, row);
			}
		});
		this.grid.setGridAnswerBoxValueChangedEvent(new GridAnswerBoxValueChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, int index)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridAnswerBoxValueChangedDelegate != null)
					GridBridge.this.gridAnswerBoxValueChangedDelegate.handle(column, row, index);
			}
		});
		this.grid.setGridHeaderClickedEvent(new GridHeaderClicked()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridGridHeaderClickedDelegate != null)
					GridBridge.this.gridGridHeaderClickedDelegate.handle(column);
			}
		});
		this.grid.setGridMutableComboBoxSelectedEvent(new GridMutableComboBoxSelected()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, Object value) throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridMutableComboBoxSelected != null)
					GridBridge.this.gridMutableComboBoxSelected.handle(column, row, value);
			}
		});
		this.grid.setGridMutableAnswerBoxSelectedEvent(new GridMutableAnswerBoxSelected()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, AnswerBoxOption value)  throws ims.framework.exceptions.PresentationLogicException
			{
				if (GridBridge.this.gridMutableAnswerBoxSelected != null)
					GridBridge.this.gridMutableAnswerBoxSelected.handle(column, row, value);
			}
		});
		
		this.grid.setGridQueryComboBoxTextSubmitedEvent(new GridQueryComboBoxTextSubmited()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row, String text) throws ims.framework.exceptions.PresentationLogicException
			{
				if(GridBridge.this.gridQueryComboBoxTextSubmitedDelegate != null)
					GridBridge.this.gridQueryComboBoxTextSubmitedDelegate.handle(column, row, text);
			}		
		});
		
		this.grid.setGridCommentChangedEvent(new GridCommentChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle(int column, GridRow row) throws ims.framework.exceptions.PresentationLogicException
			{
				if(GridBridge.this.gridCommentChangedDelegate != null)
					GridBridge.this.gridCommentChangedDelegate.handle(column, row);
			}		
		});
		
		this.grid.setGridRowExpandedCollapsedEvent(new GridRowExpandedCollapsed()
		{
			private static final long serialVersionUID = 1L;

			public void handle(GridRow row) throws ims.framework.exceptions.PresentationLogicException
			{
				if(GridBridge.this.gridRowExpandedCollapsedDelegate != null)
					GridBridge.this.gridRowExpandedCollapsedDelegate.handle(row);
			}		
		});
	}
	public void free()
	{		
		this.grid = null;
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
		this.gridRowExpandedCollapsedDelegate = null;
	}

	public void removeSelectedRow()
	{
		this.grid.removeSelectedRow();
	}
	public void setFocus()
	{
		this.grid.setFocus();
	}
	public boolean isEnabled()
	{
		return this.grid.isEnabled();
	}
	public boolean isSelectable()
	{
		return this.grid.isSelectable();
	}
	public void setEnabled(boolean value)
	{
		this.grid.setEnabled(value);
	}
	public void setSelectable(boolean value)
	{
		this.grid.setSelectable(value);
	}
	public void setVisible(boolean value)
	{
		this.grid.setVisible(value);
	}
	public boolean getReadOnly()
	{
		return this.grid.isReadOnly();
	}
	public void setReadOnly(boolean value)
	{
		this.grid.setReadOnly(value);
	}
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
	public void setGridQueryComboBoxTextSubmitedEvent(GridQueryComboBoxTextSubmited delegate)
	{
		this.gridQueryComboBoxTextSubmitedDelegate = delegate;
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
	public void setGridCommentChangedEvent(GridCommentChanged delegate)
	{
		this.gridCommentChangedDelegate = delegate;
	}
	public void setGridRowExpandedCollapsedEvent(GridRowExpandedCollapsed delegate)
	{
		this.gridRowExpandedCollapsedDelegate = delegate;
	}

	public boolean canMoveCurrentUp()
	{
		return this.grid.canMoveCurrentUp();		
	}

	public boolean canMoveCurrentDown()
	{
		return this.grid.canMoveCurrentDown();
	}	
	public int getID()
	{
		return this.grid.getID();
	}

	/**
	 * Moves the currently select row up one place.
	 * If no current row, method has no effect.
	 * If current row is at top of grid, method has no effect
	 */
	public void moveUp()
	{
		this.grid.moveUp();
	}

	/**
	 * Moves the currently select row down one place.
	 * If no current row, method has no effect.
	 * If current row is at bottom of grid, method has no effect
	 */
	public void moveDown()
	{
		this.grid.moveDown();
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
		this.grid.moveTo(toIndex);		
	}	
	
	/**
	 * Swaps the 2 rows specified by index1 and index2
	 * If either index1 or index1 does not exist, method has no effect
	 * If either index1 or index1 < 0, method has no effect
	 * If index1 = index2 , method has no effect
	 */
	public void swap(int index1, int index2)
	{
		this.grid.swap(index1, index2);
	}
	
	public void sort(int columnIndex)
	{
		sort(columnIndex, SortOrder.ASCENDING);
	}

	public void sort(int columnIndex, SortOrder dir)
	{
		this.grid.sort(columnIndex, dir);
	}
	public boolean isUnselectable()
	{
		return this.grid.isUnselectable();
	}
	public void setUnselectable(boolean value)
	{
		this.grid.setUnselectable(value);
	}
	public void resetScrollPosition()
	{
		this.grid.resetScrollPosition();
	}
	public void setFooterMaxHeight(int value)
	{
		this.grid.setFooterMaxHeight(value);
	}
    public void setFooterValue(String value)
    {
    	this.grid.setFooterValue(value);
    }
    public void  setRowSelectionChangedEventRequirePdsAuthentication(boolean value)
    {
    	this.grid.setRowSelectionChangedEventRequirePdsAuthentication(value);
    }
		
	protected Grid grid = null;
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
	protected GridRowExpandedCollapsed gridRowExpandedCollapsedDelegate = null;
}
