package ims.framework.cn.data;

import ims.base.interfaces.IModifiable;
import ims.framework.IEnhancedItem;
import ims.framework.IItem;
import ims.framework.cn.controls.GridColumn;
import ims.framework.controls.AnswerBoxOption;
import ims.framework.enumerations.GridColumnType;
import ims.framework.enumerations.SortOrder;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.utils.Color;
import ims.framework.utils.Date;
import ims.framework.utils.DecimalFormat;
import ims.framework.utils.Image;
import ims.framework.utils.PartialDate;
import ims.framework.utils.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class GridData implements IControlData
{	
	public boolean dataWasRendered = false;	
	private static final long serialVersionUID = -167630240043553719L;
	public GridData()
	{
		this.rows = new GridRowCollection(null);
	}
	public void setSelectable(boolean value)
	{
		if(!selectableChanged)
			selectableChanged = selectable != value;
	
		selectable = value;
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
	public boolean isSelectable()
	{
		return selectable;
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
	public void setColumnCaption(int column, String value)
	{
		if(this.columnCaptions == null)
		{
			this.columnNamesChanged = true;
			this.columnCaptions = new HashMap<Integer, String>();
		}
		else
		{			
			Object oldValue = this.columnCaptions.get(Integer.valueOf(column));
			if(!this.columnNamesChanged)
			{
				if(oldValue == null)
					this.columnNamesChanged = value != null;
				else
					this.columnNamesChanged = !oldValue.equals(value);
			}
		}
		
		this.columnCaptions.put(Integer.valueOf(column), value);	
	}

	public Object getValue()
	{
		if(this.selectedRow == null)
			return null;
		
		return this.selectedRow.getValue();
	}
	public void setSelection(int value)
	{
		GridRow row = this.getRowByIndex(value);
		
		if(!this.selectedRowChanged)
		{
			if(this.selectedRow == null)
				this.selectedRowChanged = row != null;
			else
				this.selectedRowChanged = !this.selectedRow.equals(row);
		}
		
		this.selectedRow = row; 
	}
	
	public GridRow getRowByValue(Object value)
	{
		for (int i = 0; i < this.rows.size(); ++i)
		{
			GridRow row = (GridRow) this.rows.get(i);
			GridRow tmp = row.findValue(value);
			if (tmp != null)
				return tmp;
		}
		return null;
	}
	public GridRow getSelectedRow()
	{
		return this.selectedRow;
	}
	public void setSelection(Object value)
	{
		if(value == null)
		{
			if(!this.selectedRowChanged)
				this.selectedRowChanged = this.selectedRow != null;
			
			this.selectedRow = null;
		}
		else
		{
			for(int i = 0; i < this.rows.size(); ++i)
			{
				GridRow tmp = null;
				GridRow row = (GridRow) this.rows.get(i);
				Object o = row.getValue();
				if(o != null && o.equals(value))
				{
					tmp = row;
				}
				else
				{
					tmp = row.setSelection(value);
				}
				
				if(tmp != null)
				{
					if(!this.selectedRowChanged)
					{
						if(this.selectedRow == null)
							this.selectedRowChanged = tmp != null;
						else
							this.selectedRowChanged = !this.selectedRow.equals(tmp);
					}
					
					this.selectedRow = tmp;
					break;
				}
			}
		}
	}
	public ims.framework.controls.GridRow setBooleanValue(int row, int column, boolean value)
	{		
		GridRow realRow = this.getRowByIndex(row);		
		realRow.set(column, Boolean.valueOf(value));
		return realRow;
	}
	public Object[] setComboBoxValue(int row, int column, int selection)
	{		
		GridRow realRow = this.getRowByIndex(row);
		realRow.setComboBoxValue(column, selection);
		if(selection == -1)
			return new Object[]{realRow, null};
		ArrayList tmp = this.comboBoxesData.get(Integer.valueOf(column));
		return new Object[]{realRow, ((ComboBoxItem) tmp.get(selection)).getValue()};
	}
	public void setDateControlValue(int row, int column, Date value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setPartialDateControlValue(int row, int column, PartialDate value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setDecimalBoxValue(int row, int column, Float value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setIntBoxValue(int row, int column, Integer value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setWrapTextValue(int row, int column, String value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setTextBoxValue(int row, int column, String value)
	{		
		this.getRowByIndex(row).set(column, value);
	}
	public void setCommentValue(int row, int column, String value)
	{
		this.getRowByIndex(row).set(column, value);
	}
	public void setTimeBoxValue(int row, int column, Time value)
	{
		this.getRowByIndex(row).set(column, value);
	}
	public void expandRow(int rowIndex, boolean expanded)
	{
		GridRow row = this.getRowByIndex(rowIndex);
		if(row == null)
			return;

		row.setExpanded(expanded);
	}
	public ims.framework.controls.GridRow setAnswerBoxValue(int row, int column, int index)
	{
		GridRow realRow = this.getRowByIndex(row);
		realRow.set(column, Integer.valueOf(index));
		return realRow;
	}

	public void answerBoxColumnNewOption(int column, AnswerBoxOption option)
	{
		Integer tmp = Integer.valueOf(column);
		ArrayList<AnswerBoxOption> ar = this.answerBoxesData.get(tmp);
		if (ar == null)
		{
			ar = new ArrayList<AnswerBoxOption>();
			this.answerBoxesData.put(tmp, ar);
		}
		
		//Don't add the option if it is already there!
		if (ar.indexOf(option) != -1) 
			return;
		
		ar.add(option);
	}

	public void answerBoxColumnClear(int column) 
	{
		Integer tmp = Integer.valueOf(column);
		ArrayList ar = this.answerBoxesData.get(tmp);
		if (ar == null) return;
		ar.clear();
	}

	public Object[] setMutableComboBoxValue(int row, int column, int value)
	{
		GridRow realRow = this.getRowByIndex(row);
		Object o = realRow.setMutableComboBoxValue(column, value);		
		return new Object[]{realRow, o};
	}
	public Object[] setMutableAnswerBoxValue(int row, int column, int value)
	{
		GridRow realRow = this.getRowByIndex(row);
		AnswerBoxOption option = realRow.setMutableAnswerBoxValue(column, value);
		return new Object[]{realRow, option};
	}	
	public GridRowCollection getRows()
	{
		return this.rows;
	}
	public void clearComboBox(int columnIndex)
	{
		ArrayList ar = this.comboBoxesData.get(Integer.valueOf(columnIndex));
		if(ar != null)
		{
			if(!this.rowsChanged)
				this.rowsChanged = ar.size() != 0;
			
			ar.clear();
		}
	}
	public void newRowComboBox(int column, Object value, String text, Image image, Color textColor)
	{
		Integer tmp = Integer.valueOf(column);
		ArrayList<ComboBoxItem> ar = this.comboBoxesData.get(tmp);
		if (ar == null)
		{
			ar = new ArrayList<ComboBoxItem>();
			this.comboBoxesData.put(tmp, ar);
		}
		ComboBoxItem item = new ComboBoxItem(value, text, image, textColor);
		//Don't add the combobox item if it is already there
		if(ar.indexOf(item) != -1) 
			return;
		
		ar.add(item);		
		this.rowsChanged = true;
	}
	public int sizeComboBox(int column)
	{
		ArrayList ar = this.comboBoxesData.get(Integer.valueOf(column));
		return ar == null ? 0 : ar.size();
	}	
	public StringBuffer parseColumnNames()
	{
		StringBuffer sb = new StringBuffer();
		
		if(this.columnCaptions != null)
		{
			for (int i = 0; i < this.columns.size(); i++)
			{
				if(this.columnCaptions.containsKey(Integer.valueOf(i)))
				{
					String caption = this.columnCaptions.get(Integer.valueOf(i));			
					sb.append(" <col index=\"" + i + "\" caption=\"" + ims.framework.utils.StringUtils.encodeXML(caption) + "\" />");
				}
			}
		}
		
		return sb;
	}
	public StringBuffer parseColumns()
	{
		StringBuffer sb = new StringBuffer(256);

		for (int i = 0; i < this.columns.size(); ++i)
		{
			GridColumn column = (GridColumn) this.columns.get(i);
			column.parse(sb);
			if (column.getType().equals(GridColumnType.COMBOBOX)) // combo box
			{
				ArrayList ar = this.comboBoxesData.get(Integer.valueOf(i));
				if (ar != null)
				{
					for (int j = 0; j < ar.size(); ++j)
					{
                        ComboBoxItem comboItem = (ComboBoxItem)ar.get(j);
						sb.append("<option text=\"");
						sb.append(ims.framework.utils.StringUtils.encodeXML(comboItem.getText()));
                        
                        if(comboItem.getTextColor() != null)
                            sb.append("\" textColor=\"" + comboItem.getTextColor());
                        
                        if(comboItem.getImage() != null)
                        {
                            sb.append("\" img=\"");
                            sb.append(comboItem.getImage().getImagePath());
                        }                          
                        
						sb.append("\"/>");
					}
				}
				sb.append("</col>");
			}
			else if (column.getType().equals(GridColumnType.ANSWERBOX)) // answer box
			{
				ArrayList ar = this.answerBoxesData.get(Integer.valueOf(i));
				if (ar != null)
				{
					for (int j = 0; j < ar.size(); ++j)
					{
						AnswerBoxOption option = (AnswerBoxOption) ar.get(j);
						sb.append("<option text=\"");
						sb.append(ims.framework.utils.StringUtils.encodeXML(option.getText()));
                        if(option.getTextColor() != null)
                        {
                            sb.append("\" textColor=\"");
                            sb.append(option.getTextColor());
                        }
						sb.append("\" img=\"");
						if(option.getImage() != null)
							sb.append(option.getImage().getImagePath());
						sb.append("\"/>");
					}
				}
				sb.append("</col>");
			}
		}
		return sb;
	}

	public int getSelectedRowIndex()
	{
		if(this.selectedRow == null)
			return -1;
		return getSelectedRowIndex(this.selectedRow);
	}

	public boolean canMoveCurrentUp()
	{
		return this.rows.canMoveCurrentUp();		
	}

	public boolean canMoveCurrentDown()
	{
		return this.rows.canMoveCurrentDown();
	}

	public void moveUp()
	{
		GridRowCollection collection;
		
		if(GridData.this.selectedRow.parent == null)
			collection = this.rows;
		else
			collection = (GridRowCollection)GridData.this.selectedRow.parent.getRows();
			
		collection.moveUp();
	}
	public void moveDown()
	{
		GridRowCollection collection;
		
		if(GridData.this.selectedRow.parent == null)
			collection = this.rows;
		else
			collection = (GridRowCollection)GridData.this.selectedRow.parent.getRows();
			
		collection.moveDown();
	}
	public void moveTo(int toIndex)
	{
		GridRowCollection collection;
		
		if(GridData.this.selectedRow.parent == null)
			collection = this.rows;
		else
			collection = (GridRowCollection)GridData.this.selectedRow.parent.getRows();
			
		collection.moveTo(toIndex);
	}
	public void swap(int index1, int index2)
	{
		GridRowCollection collection;
		
		if(GridData.this.selectedRow.parent == null)
			collection = this.rows;
		else
			collection = (GridRowCollection)GridData.this.selectedRow.parent.getRows();
			
		collection.swap(index1, index2);
	}
	public void sort(int columnIndex, SortOrder dir)
	{
		this.rows.sort(columnIndex, dir);
	}	
	public void setColumns(ArrayList columns)
	{
		this.columns = columns;
	}
	public void setGroupParents(boolean value)
	{
		this.groupParents = value;
	}
	/*
	public void setAnswerBoxData(HashMap value)
	{
		this.answerBoxesData = value;
	}
	*/
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
	public void renderRows(StringBuffer outSB, ims.framework.controls.GridRowCollection collection) throws ConfigurationException
	{
		for (int k = 0; k < collection.size(); ++k)
		{
			GridData.GridRow row = (GridData.GridRow) collection.get(k);
			outSB.append("<r ");
			//			if (!row.isExpanded() && row.getRows().size() > 0)
			if (row.getRows().size() > 0)
			{
				outSB.append("expanded=\"");
				outSB.append(row.isExpanded() ? "true" : "false");
				outSB.append("\" ");
			}			
			outSB.append("id=\"");
			outSB.append(this.getSelectedRowIndex(row));			
			if (!row.isSelectable())
				outSB.append("\" selectable=\"false");
			if (row.readOnly)
				outSB.append("\" readOnly=\"true");
			if (row.isBold())
				outSB.append("\" bold=\"true");
			if (!row.getTextColor().equals(Color.Default))
			{
				outSB.append("\" textcolor=\"");
				outSB.append(row.getTextColor().toString());
			}
			if (!row.getBackColor().equals(Color.Default))
			{
				outSB.append("\" backcolor=\"");
				outSB.append(row.getBackColor().toString());
			}
			
			if (row.expandedImage != null)
			{	
				outSB.append("\" expandedImageUrl=\"");
				outSB.append(row.expandedImage.getImagePath());
			}
			if (row.collapsedImage != null)
			{	
				outSB.append("\" imageUrl=\"");
				outSB.append(row.collapsedImage.getImagePath());
			}
			if (row.selectedImage != null)
			{	
				outSB.append("\" selectedImageUrl=\"");
				outSB.append(row.selectedImage.getImagePath());
			}			
			
			outSB.append("\">");
			for (int j = 0; j < this.columns.size(); ++j)
			{
				GridColumn currentColumn = (GridColumn) this.columns.get(j);
				outSB.append("<c");
				
				if(row.isEmpty(j))
					outSB.append(" empty=\"true\" />");
				else
				{	
					Color backColor = row.getBackColor(j);
					if(backColor != null && !backColor.equals(Color.Default))
					{
						outSB.append(" backcolor=\"");
						outSB.append(backColor.toString());
						outSB.append("\"");
					}
					
					Color textColor = row.getTextColor(j);
					if(textColor != null && !textColor.equals(Color.Default))
					{
						outSB.append(" textcolor=\"");
						outSB.append(textColor.toString());
						outSB.append("\"");
					}
					
					if(currentColumn.getType().equals(GridColumnType.BUTTON))
					{
						Object cellValue = row.get(j);
						if(cellValue != null)
						{
							Image image = (Image) cellValue;
							outSB.append(" img=\"");						
							outSB.append(image.getImagePath());
							
							if(image.getImageWidth() != 8 || image.getImageHeight() != 8)
							{
								outSB.append("\" imgWidth=\"");
								outSB.append(image.getImageWidth());
								outSB.append("\" imgHeight=\"");
								outSB.append(image.getImageHeight());								
							}
							
							outSB.append("\"");
						}
					}
                    
					if(row.isReadOnly(j))
					{
						outSB.append(" readOnly=\"true");
	                    outSB.append("\"");
					}
					
					String cellTooltip = row.getTooltip(j);
					if (!cellTooltip.equals(""))
					{
						outSB.append(" tooltip=\"");
						outSB.append(ims.framework.utils.StringUtils.encodeXML(cellTooltip));
						outSB.append("\"");
					}
					
					if(row.isOpened(j))
					{
						outSB.append(" opened=\"true\"");
						row.showOpened(-1); // we need to reset the opened cell.
					}				
					
					if (currentColumn.getType().equals(GridColumnType.MUTABLECOMBOBOX)) // mutable combobox
					{
						MutableCell cell = (MutableCell)row.get(j);
						outSB.append(" selectedValue=\"");
						outSB.append(cell.getSelection());
						
						outSB.append("\" minNumberOfChars=\"");
						outSB.append(cell.getMutableComboBoxMinNumberOfChars() > 1 ? cell.getMutableComboBoxMinNumberOfChars() : 1);
					
						if(cell.getSelection() < 0)
						{
							outSB.append("\" value=\"");
							outSB.append(row.editedText[j] == null ? "" : ims.framework.utils.StringUtils.encodeXML(row.editedText[j]));
						}
						
						outSB.append("\">");
						for (int i = 0; i < cell.size(); ++i)
						{						
							outSB.append("<option text=\"");
							outSB.append(ims.framework.utils.StringUtils.encodeXML(cell.getText(i)));
							outSB.append("\"/>");
						}					
					}
					else if (currentColumn.getType().equals(GridColumnType.MUTABLEANSWERBOX)) // mutable answerbox
					{
						MutableAnswerBoxCell cell = (MutableAnswerBoxCell)row.get(j);
						if (cell.getSelection() != -1)
						{	
							outSB.append(" selectedIndex=\"");
							outSB.append(cell.getSelection());
							outSB.append("\">");
							ArrayList options = cell.getOptions();
							for (int p = 0; p < options.size(); ++p)
							{	
								AnswerBoxOption option = (AnswerBoxOption)options.get(p);
								outSB.append("<option text=\"");
								outSB.append(ims.framework.utils.StringUtils.encodeXML(option.getText()));
                                if(option.getTextColor() != null)
                                {
                                    outSB.append("\" textColor=\"");
                                    outSB.append(option.getTextColor());
                                }
								outSB.append("\" img=\"");
								outSB.append(option.getImage().getImagePath());
								outSB.append("\"/>");
							}
						}
					}
					else
					{
						outSB.append('>');
						if (currentColumn.getType().equals(GridColumnType.COMBOBOX))
						{
							outSB.append(row.getComboBoxValue(j));
						}
						else if (currentColumn.getType().equals(GridColumnType.ANSWERBOX))
						{
							Object cellValue = row.get(j);
							ArrayList ar = this.answerBoxesData.get(Integer.valueOf(j));
							if (ar != null)
							{
								outSB.append(ar.indexOf(cellValue));
							}
						} 
						else
						{
							Object cellValue = row.get(j);
							if (cellValue != null)
							{
								if(currentColumn.getType().equals(GridColumnType.STRING))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));
								}
								else if(currentColumn.getType().equals(GridColumnType.HTML))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));
								}
								else if(currentColumn.getType().equals(GridColumnType.BOOL))
								{
									outSB.append(((Boolean) cellValue).booleanValue() ? "true" : "false");
								}
								else if(currentColumn.getType().equals(GridColumnType.WRAPTEXT))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));
								}
								else if(currentColumn.getType().equals(GridColumnType.DATE))
								{
									outSB.append(cellValue.toString());
								}
								else if(currentColumn.getType().equals(GridColumnType.DECIMAL))
								{
									outSB.append(DecimalFormat.format(cellValue, currentColumn.getPrecision(), currentColumn.getScale()));
								}
								else if(currentColumn.getType().equals(GridColumnType.IMAGE))
								{
									outSB.append(((Image) cellValue).getImagePath());
								}
								else if(currentColumn.getType().equals(GridColumnType.INTEGER))
								{
									outSB.append(cellValue.toString());
								}
								else if(currentColumn.getType().equals(GridColumnType.TIME))
								{
									outSB.append(cellValue.toString());
								}
								else if(currentColumn.getType().equals(GridColumnType.TREE))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));
								}
								else if(currentColumn.getType().equals(GridColumnType.COMMENT))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));
								}	
								else if(currentColumn.getType().equals(GridColumnType.PARTIALDATE))
								{
									outSB.append(ims.framework.utils.StringUtils.encodeXML(cellValue.toString()));								
								}
							}
						}
					}
					outSB.append("</c>");
				}
				if (this.groupParents && (currentColumn.getType().equals(GridColumnType.TREE)) && (row.isParentRow || row.getRows().size() > 0))
				{
					if(row.getRows().size() == 0)
					{
						for (int ec = j; ec < this.columns.size() - 1; ec++)
						{
							outSB.append("<c empty=\"true\" />");
						}
					}
					
					break;
				}
			}
			ims.framework.controls.GridRowCollection children = row.getRows();
			if (children.size() > 0)
			{
				outSB.append("<childRows>");
				this.renderRows(outSB, children);
				outSB.append("</childRows>");
			}
			outSB.append("</r>");
		}
	}
		

	// Helper functions. 
	Integer index = null;
	private int getSelectedRowIndex(GridRow row)
	{
		this.index = Integer.valueOf(0);
		getSelectedRowIndex(row, this.rows);
		int result = this.index.intValue();
		this.index = null;
		return result;
	}
	private boolean getSelectedRowIndex(GridRow row, ims.framework.controls.GridRowCollection collection)
	{
		for (int i = 0; i < collection.size(); ++i)
		{
			GridRow tmp = (GridRow) collection.get(i);
			if (tmp.equals(row))
				return true;
			this.index = Integer.valueOf(this.index.intValue() + 1);
			if (getSelectedRowIndex(row, tmp.getRows()))
				return true;
		}
		return false;
	}
	private int getRowIndex(GridRow row, ims.framework.controls.GridRowCollection collection)
	{
		for (int i = 0; i < collection.size(); ++i)
		{
			GridRow tmp = (GridRow) collection.get(i);
			if (tmp.equals(row))
				return i;			
		}
		return -1;
	}
	public GridRow getRowByIndex(int value)
	{
		this.index = Integer.valueOf(0);
		GridRow result = getRowByIndex(value, this.rows);
		this.index = null;
		return result;
	}
	public void removeSelectedRow()
	{
		if(this.selectedRow == null)
			return;
		if(this.selectedRow.getRows().size() > 0)
			return;
				
		removeSelectedRow(this.rows);		
	}
	
	private void removeSelectedRow(ims.framework.controls.GridRowCollection collection)
	{
		for(int x = 0; x < collection.size(); x++)
		{
			if(collection.get(x).equals(this.selectedRow))
			{				
				this.selectedRowChanged = true;
				this.rowsChanged = true;
				collection.remove(x);
				this.selectedRow = null;
				return;
			}
		}
		
		for(int x = 0; x < collection.size(); x++)
		{
			if(collection.get(x).getRows() != null && collection.get(x).getRows().size() > 0)
			{
				removeSelectedRow(collection.get(x).getRows());
				if(this.selectedRow == null)
					return;
			}
		}		
	}

	private GridRow getRowByIndex(int value, ims.framework.controls.GridRowCollection collection)
	{
		for (int i = 0; i < collection.size(); ++i)
		{
			GridRow row = (GridRow) collection.get(i);

			int tmp = this.index.intValue();
			if (tmp == value)
				return row;
			this.index = Integer.valueOf(tmp + 1);
			row = getRowByIndex(value, row.getRows());
			if (row != null)
				return row;
		}
		return null;
	}
	public int getSortCol() 
	{
		return this.sortCol;
	}
	public void setSortCol(int sortCol) 
	{
		this.sortCol = sortCol;
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
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public void setSelectableUnchanged()
	{
		this.selectableChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public boolean isSelectableChanged()
	{
		return selectableChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setReadOnlyUnchanged()
	{
		this.readOnlyChanged = false;
	}
	public boolean isReadOnlyChanged()
	{
		return readOnlyChanged;
	}
	public void setSelectedRowUnchanged()
	{
		this.selectedRowChanged = false;
	}
	public boolean isSelectedRowChanged()
	{
		return selectedRowChanged || (rows != null && rows.wasChanged());
	}
	public void setColumnNamesUnchanged()
	{
		this.columnNamesChanged = false;
	}
	public boolean isColumnNamesChanged()
	{
		return columnNamesChanged;
	}
	public void setRowsUnchanged()
	{
		if(this.rows != null)			
			this.rows.markUnchanged();		
	}
	public boolean isRowsChanged()
	{
		if(this.rows == null)
			return false;
		
		return this.rows.wasChanged();
	}
	public boolean isRowSelectionChangedEventRequirePdsAuthenticationChanged()
	{
		return rowSelectionChangedEventRequirePdsAuthenticationChanged;
	}
	public void setRowSelectionChangedEventRequirePdsAuthenticationUnchanged()
	{
		this.rowSelectionChangedEventRequirePdsAuthenticationChanged = false;
	}
	public void setUnselectableUnchanged()
	{
		this.unselectableChanged = false;
	}
	public boolean isUnselectableChanged()
	{
		return unselectableChanged;
	}
	public void setFooterMaxHeightUnchanged()
	{
		this.footerMaxHeightChanged = false;
	}
	public boolean isFooterMaxHeightChanged()
	{
		return footerMaxHeightChanged;
	}
	public void setFooterValueUnchanged()
	{
		this.footerValueChanged = false;
	}
	public boolean isFooterValueChanged()
	{
		return footerValueChanged;
	}

	private boolean enabled = true;
	private boolean selectable = true;
	private boolean visible = true;
	private boolean readOnly = true;
	private GridRowCollection rows;
	private GridRow selectedRow = null;
	private ArrayList columns = null;
	private HashMap<Integer, String> columnCaptions = null;
	private HashMap<Integer, ArrayList<ComboBoxItem>> comboBoxesData = new HashMap<Integer, ArrayList<ComboBoxItem>>();
	private HashMap<Integer, ArrayList<AnswerBoxOption>> answerBoxesData = new HashMap<Integer, ArrayList<AnswerBoxOption>>();
	private boolean groupParents;
	private int sortCol = 0;	
	private boolean unselectable = false;
	private int footerMaxHeight = 0;
	private String footerValue = null;
	private boolean rowSelectionChangedEventRequirePdsAuthentication =  false;
	
	private boolean enabledChanged = false;
	private boolean selectableChanged = false;
	private boolean visibleChanged = false;
	private boolean readOnlyChanged = true;
	private boolean selectedRowChanged = false;
	private boolean columnNamesChanged = false;
	private boolean rowsChanged = false;
	private boolean unselectableChanged = false;
	private boolean footerMaxHeightChanged = false;
	private boolean footerValueChanged = false;
	private boolean rowSelectionChangedEventRequirePdsAuthenticationChanged = false;
	
	class GridCell implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public Object getValue()
		{
			return this.value;
		}
		public void setValue(Object value)
		{
			this.value = value;
		}
		private Object value = null;
	}

	class GridRowComparator implements Comparator, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private int direction = 1;
		private int columnIndex = 0;
		public GridRowComparator(int columnIndex)
		{
			this(columnIndex, SortOrder.ASCENDING);
		}
		public GridRowComparator(int columnIndex, SortOrder order)
		{
			if (order == SortOrder.DESCENDING)
			{
				this.direction = -1;
			}
			this.columnIndex = columnIndex;
		}
		public int compare(Object obj1, Object obj2)
		{
			GridRow row1 = (GridRow)obj1;
			GridRow row2 = (GridRow)obj2;
			setSortCol(this.columnIndex);
			return this.direction*(row1.compareTo(row2));
		}
		
		public boolean equals(Object obj)
		{
			return false;
		}
		public int hashCode()
		{
			return super.hashCode();
		}
	}
	
	
	class GridRow implements ims.framework.controls.GridRow, Comparable, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		GridRow(GridRow parent)
		{
			this.parent = parent;
			this.cellTooltips = new String[GridData.this.columns.size()];
			this.cellBackColor = new Color[GridData.this.columns.size()];
			this.cellTextColor = new Color[GridData.this.columns.size()];
            this.cellReadOnly = new boolean[GridData.this.columns.size()];
			this.isEmpty = new boolean[GridData.this.columns.size()];
			this.editedText = new String[columns.size()];
			this.rowsCollection = new GridRowCollection(this);            
            
			for (int i = 0; i < GridData.this.columns.size(); ++i)
			{
				this.cellTooltips[i] = null;
				GridColumn column = (GridColumn) GridData.this.columns.get(i);
				GridCell cell = new GridCell();
				
				if(column.getType().equals(GridColumnType.STRING))
				{				
					cell.setValue(null);
				}
				else if(column.getType().equals(GridColumnType.HTML))
				{				
					cell.setValue(null);
				}
				else if(column.getType().equals(GridColumnType.BOOL))
				{
					cell.setValue(Boolean.FALSE);
				}
				else if(column.getType().equals(GridColumnType.COMBOBOX))
				{
					cell.setValue(Integer.valueOf(column.canBeEmpty() ? -1 : 0));
				}
				else if(column.getType().equals(GridColumnType.WRAPTEXT))
				{
					cell.setValue("");
				}
				else if(column.getType().equals(GridColumnType.DATE))
				{
					cell.setValue(column.canBeEmpty() ? null : new Date());
				}
				else if(column.getType().equals(GridColumnType.DECIMAL))
				{
					cell.setValue(column.canBeEmpty() ? null : new Float(0));
				}
				else if(column.getType().equals(GridColumnType.INTEGER))
				{
					cell.setValue(column.canBeEmpty() ? null : Integer.valueOf(0));
				}
				else if(column.getType().equals(GridColumnType.TIME))
				{
					cell.setValue(column.canBeEmpty() ? null : new Time());
				}
				else if(column.getType().equals(GridColumnType.TREE))
				{
					cell.setValue("");
				}
				else if(column.getType().equals(GridColumnType.MUTABLECOMBOBOX))
				{
					cell.setValue(new MutableCell());
				}
				else if(column.getType().equals(GridColumnType.BUTTON))
				{
					cell.setValue(null);
				}
				else if(column.getType().equals(GridColumnType.ANSWERBOX))
				{
					cell.setValue(Integer.valueOf(column.canBeEmpty() ? -1 : 0));
				}
				else if(column.getType().equals(GridColumnType.MUTABLEANSWERBOX))
				{
					cell.setValue(new MutableAnswerBoxCell());
				}
				else if(column.getType().equals(GridColumnType.PARTIALDATE))
				{
					cell.setValue(column.canBeEmpty() ? null : new PartialDate());				
				}
				this.cells.add(cell);
			}
		}
		public ims.framework.controls.GridRow getParent()
		{
			return this.parent;
		}
		public Object get(int index)
		{
			Object value = this.cells.get(index).getValue();
			GridColumn column = (GridColumn) GridData.this.columns.get(index);
			if (column.getType().equals(GridColumnType.COMBOBOX))
			{
				int i = ((Integer) value).intValue();
				if (i == -1)
					return null;
				ArrayList ar = GridData.this.comboBoxesData.get(Integer.valueOf(index));
				ComboBoxItem item = (ComboBoxItem) ar.get(i);
				value = item.getValue();
			}
			else if (column.getType().equals(GridColumnType.ANSWERBOX))
			{
				int i = ((Integer) value).intValue();
				if (i == -1)
					return null;
				ArrayList ar = GridData.this.answerBoxesData.get(Integer.valueOf(index));
				AnswerBoxOption item = (AnswerBoxOption) ar.get(i);
				value = item;				
			}
			return value;
		}
		public void set(int index, Object value)
		{						
			set(index, value, false);
		}
		public void set(int index, Object value, boolean addValueIfNotFound)
		{
			GridColumn column = (GridColumn) GridData.this.columns.get(index);
			if (column.getType().equals(GridColumnType.COMBOBOX))
			{
				if(addValueIfNotFound && value != null && value instanceof IItem)
				{
					ArrayList<ComboBoxItem> ar = GridData.this.comboBoxesData.get(Integer.valueOf(index));
					if(ar == null)
						ar = new ArrayList<ComboBoxItem>();					
					
					ComboBoxItem newItem = null;
					
					if(value instanceof IEnhancedItem)
						newItem = new ComboBoxItem(value, ((IItem)value).toString(), ((IEnhancedItem)value).getIItemImage(), ((IEnhancedItem)value).getIItemTextColor());
					else
						newItem = new ComboBoxItem(value, ((IItem)value).toString(), null, null);
					
					boolean exists = false;
					for (int i = 0; i < ar.size(); ++i)
					{
						ComboBoxItem item = ar.get(i);						
						if (item.getValue().equals(value))
						{
							exists = true;
							break;
						}
					}
					if(!exists)
					{
						this.dataWasChanged = true;
						ar.add(newItem);
					}
				}
				
				boolean valueWasFound = false;
				ArrayList ar = GridData.this.comboBoxesData.get(Integer.valueOf(index));
				if (ar == null)
				{
					this.dataWasChanged = true;
					value = Integer.valueOf(-1);
				}
				else
				{
					for (int i = 0; i < ar.size(); ++i)
					{
						ComboBoxItem item = (ComboBoxItem) ar.get(i);						
						if (item.getValue().equals(value))
						{
							valueWasFound = true;
							this.dataWasChanged = true;
							value = Integer.valueOf(i);
							break;
						}
					}
					if(!valueWasFound)
					{
						this.dataWasChanged = true;
						value = Integer.valueOf(-1);
					}
				}
			}
			else if (column.getType().equals(GridColumnType.ANSWERBOX))
			{
				if (value == null)
				{
					this.dataWasChanged = true;
					value = Integer.valueOf(-1);
				}
				else
				{
					ArrayList<AnswerBoxOption> ar = GridData.this.answerBoxesData.get(Integer.valueOf(index));
					
					if(addValueIfNotFound && value != null && value instanceof AnswerBoxOption)
					{
						if(ar == null)
							ar = new ArrayList<AnswerBoxOption>();
						
						AnswerBoxOption newItem = (AnswerBoxOption)value;
						if(ar.indexOf(newItem) < 0)
							ar.add(newItem);
					}					
					
					if (ar == null)
					{
						this.dataWasChanged = true;
						value = Integer.valueOf(-1);
					}
					else if (value instanceof AnswerBoxOption)
					{
						this.dataWasChanged = true;
						int i = ar.indexOf(value);
						value = Integer.valueOf(i);	
					}
				}
			}
			
			Object currentValue = this.cells.get(index).getValue(); 			
			if(!this.dataWasChanged)
			{
				if(currentValue == null)
					this.dataWasChanged = value != null;
				else
					this.dataWasChanged = true; //!currentValue.equals(value); - This is to fix a bug when custom type is used, equals returns true but some fields have changed					
			}			
			this.cells.get(index).setValue(value);
		}
		
		
		
		// Mutable combo box
		public void newRow(int column, Object value, String text)
		{
			this.dataWasChanged = true;
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			cell.add(value, text);
		}
		public void clear(int column)
		{
			this.dataWasChanged = true;
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			cell.clear();			
		}
		public Object getValue(int column)
		{
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			return cell.getValue();
		}
		public void setValue(int column, Object value)
		{
			this.dataWasChanged = true;
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			cell.setValue(value);			
			this.setEditedText(column, cell.getSelection() < 0 ? null : cell.getText(cell.getSelection()));
		}
		public Object setMutableComboBoxValue(int column, int value)
		{
			this.dataWasChanged = true;
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			this.setEditedText(column, value < 0 ? null : cell.getText(value));
			return cell.setSelection(value);			
		}		
		public void showOpened(int column)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.opened != column;
			
			this.opened = column;
		}	
		public void setEditedText(int column, String value)
		{
			if(!this.dataWasChanged)
			{
				if(this.editedText[column] == null)
					this.dataWasChanged = value != null;
				else
					this.dataWasChanged = !this.editedText[column].equals(value);
			}
			
			this.editedText[column] = value;
		}
		public String getEditedText(int column)
		{
			return this.editedText[column];
		}  
		
		public void setMinNumberOfChars(int column, int value)
		{
			this.dataWasChanged = true;
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			cell.setMutableComboBoxMinNumberOfChars(value);			
		}		
		public int getMinNumberOfChars(int column)
		{
			MutableCell cell = (MutableCell)this.cells.get(column).getValue();
			return cell.getMutableComboBoxMinNumberOfChars();
		}		
		
		// Mutable answer box
		public void addAnswerBoxOption(int column, AnswerBoxOption option)
		{
			this.dataWasChanged = true;
			MutableAnswerBoxCell cell = (MutableAnswerBoxCell)this.cells.get(column).getValue();
			cell.add(option);
		}
		public void clearAnswerBox(int column)
		{
			this.dataWasChanged = true;
			MutableAnswerBoxCell cell = (MutableAnswerBoxCell)this.cells.get(column).getValue();
			cell.clear();
		}
		public AnswerBoxOption getSelectedAnswerBoxOption(int column)
		{
			this.dataWasChanged = true;
			MutableAnswerBoxCell cell = (MutableAnswerBoxCell)this.cells.get(column).getValue();
			return cell.getValue();
		}
		public void setSelectedAnswerBoxOption(int column, AnswerBoxOption option)
		{
			this.dataWasChanged = true;
			MutableAnswerBoxCell cell = (MutableAnswerBoxCell)this.cells.get(column).getValue();
			cell.setValue(option);
		}
		public AnswerBoxOption setMutableAnswerBoxValue(int column, int value)
		{
			this.dataWasChanged = true;
			MutableAnswerBoxCell cell = (MutableAnswerBoxCell)this.cells.get(column).getValue();
			return cell.setSelection(value);			
		}

		public int getComboBoxValue(int index)
		{
			return ((Integer)this.cells.get(index).getValue()).intValue();
		}
		public void setComboBoxValue(int index, int value)
		{
			this.dataWasChanged = true;
				this.cells.get(index).setValue(Integer.valueOf(value));
		}
		public ims.framework.controls.GridRowCollection getRows()
		{
			return this.rowsCollection;
		}

		public void setReadOnly(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.readOnly != value;
			
			this.readOnly = value;
		}
		public boolean isReadOnly()
		{
			return this.readOnly;
		}
		public boolean isSelectable()
		{
			return this.selectable;
		}
		public boolean isOpened(int column)
		{
			return this.opened == column;
		}
		public void setSelectable(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.selectable != value;
			
			this.selectable = value;
		}
		public boolean isExpanded()
		{
			return this.expanded;
		}
		public void setExpanded(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.expanded != value;
			
			this.expanded = value;
		}
		public boolean isBold()
		{
			return this.bold;
		}
		public void setBold(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.bold != value;
			
			this.bold = value;
		}
		public Color getBackColor()
		{
			return this.backColor == null ? Color.Default : this.backColor;
		}
		public Color getBackColor(int column)
		{
			return this.cellBackColor[column];
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
		public void setBackColor(int column, Color value)
		{
			if(!this.dataWasChanged)
			{
				if(this.cellBackColor[column] == null)
					this.dataWasChanged = value != null;
				else
					this.dataWasChanged = !this.cellBackColor[column].equals(value); 
			}
			
			this.cellBackColor[column] = value;
		}
        public void setReadOnly(int column, boolean value)
        {
        	if(!this.dataWasChanged)
        		this.dataWasChanged = this.cellReadOnly[column] != value;
        	
            this.cellReadOnly[column] = value;
        }
        public boolean isReadOnly(int column)
        {
            return this.cellReadOnly[column];
        }
		public Color getTextColor()
		{			
			return this.textColor == null ? Color.Default : this.textColor;
		}
		public Color getTextColor(int column)
		{
			return this.cellTextColor[column];
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
		public void setTextColor(int column, Color value)
		{
			if(!this.dataWasChanged)
			{
				if(this.cellTextColor[column] == null)
					this.dataWasChanged = value != null;
				else
					this.dataWasChanged = !this.cellTextColor[column].equals(value); 
			}
			
			this.cellTextColor[column] = value;
		}
		public String getTooltip(int column)
		{
			return this.cellTooltips[column] == null ? "" : this.cellTooltips[column];  
		}
		public void setTooltip(String value)
		{
			for(int i = 0; i < GridData.this.columns.size(); i++)
				setTooltip(i, value);
		}
		public void setTooltip(int column, String value)
		{
			if(!this.dataWasChanged)
			{
				if(this.cellTooltips[column] == null)
					this.dataWasChanged = value != null;
				else
					this.dataWasChanged = !this.cellTooltips[column].equals(value); 
			}
			
			this.cellTooltips[column] = value;
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

		GridRow setSelection(Object value)
		{
			for (int i = 0; i < this.rowsCollection.size(); ++i)
			{
				GridRow tmp = null;
				GridRow row = (GridRow) this.rowsCollection.get(i);
				Object o = row.getValue();
				if (o != null && o.equals(value))
					tmp = row;
				else
					tmp = row.setSelection(value);
				
				if(tmp != null)					
					return tmp;
			}
			
			return null;
		}
		
		GridRow findValue(Object value)
		{
			if (value.equals(this.value))
				return this;
			for (int i = 0; i < this.rowsCollection.size(); ++i)
			{
				GridRow row = (GridRow) this.rowsCollection.get(i);
				GridRow tmp = row.findValue(value);
				if (tmp != null)
					return tmp;
			}
			return null;
		}


		@SuppressWarnings("unchecked")
		public int compareTo(Object obj) 
		{
			if (obj == null) return -1;
			if (!(obj instanceof GridRow)) return -1;
			GridRow row = (GridRow)obj;			
			//GridColumn column = (GridColumn) columns.get(getSortCol());
			
			Object val1 = this.cells.get(getSortCol()).getValue();
			Object val2 = row.cells.get(getSortCol()).getValue();
			
			if (val1 == null) return -1;
			if (!(val1 instanceof Comparable)) return 0;
			if (val2 == null) return 1;
			if (!(val2 instanceof Comparable)) return 0;
			
			Comparable c1 = (Comparable)val1;
			Comparable c2 = (Comparable)val2;
			
			return c1.compareTo(c2);			
			
			/*
			switch (column.getType())
			{
				case 0 : // String
					String s1 = (String)((GridCell)cells.get(getSortCol())).getValue();
					String s2 = (String)((GridCell)row.cells.get(getSortCol())).getValue();
					if (s1 == null) return -1;
					else return s1.compareTo(s2);
				case 1 : // Boolean
					break;
				case 2 : // ComboBox
					break;
				case 3 : // Wrap Text
					String sw1 = (String)((GridCell)cells.get(getSortCol())).getValue();
					String sw2 = (String)((GridCell)row.cells.get(getSortCol())).getValue();
					if (sw1 == null) return -1;
					else return sw1.compareTo(sw2);
				case 4 : // Date
					Date d1 = (Date)((GridCell)cells.get(getSortCol())).getValue();
					Date d2 = (Date)((GridCell)row.cells.get(getSortCol())).getValue();
					if (d1 == null) return -1;
					else return d1.compareTo(d2);
				case 5 : // Decimal
					Float f1 = (Float)((GridCell)cells.get(getSortCol())).getValue();
					Float f2 = (Float)((GridCell)row.cells.get(getSortCol())).getValue();
					if (f1 == null) return -1;
					else return f1.compareTo(f2);
				case 7 : // Integer
					Integer i1 = (Integer)((GridCell)cells.get(getSortCol())).getValue();
					Integer i2 = (Integer)((GridCell)row.cells.get(getSortCol())).getValue();
					if (i1 == null) return -1;
					else return i1.compareTo(i2);
				case 8 : // Time
					Time t1 = (Time)((GridCell)cells.get(getSortCol())).getValue();
					Time t2 = (Time)((GridCell)row.cells.get(getSortCol())).getValue();
					if (t1 == null) return -1;
					else return t1.compareTo(t2);
				case 9 : // Tree
					break;
				case 10: // MutableComboBox
					break;
				case 11 : // Button
					break;
				case 12: //AnswerBox
					break;
				case 13: // MutableAnswerBox
					break;
			}
			*/
		}
		public void setIsParentRow(boolean value)
		{
			if(this.isParentRow != value)
			{
				this.dataWasChanged = true;
				this.isParentRow = value;
			}
		}
		public boolean isParentRow()
		{
			return this.isParentRow;
		}
		
		public void setIsEmpty(int column, boolean value) 
		{
			if(column < 0 || this.cells == null || column > this.cells.size() -1)
				return;
			
			if(!this.dataWasChanged)
				this.dataWasChanged = this.isEmpty[column] != value; 
			
			this.isEmpty[column] = value;
		}
		public boolean isEmpty(int column)
		{
			if(column < 0 || this.cells == null || column > this.cells.size() -1)
				return false;			
			return this.isEmpty[column];
		}
        public boolean wasChanged() 
        {
        	if(this.dataWasChanged)
        		return true;
        	if(this.rowsCollection != null && this.rowsCollection.wasChanged())
        		return true;
        	
        	return false;
		}
		public void markUnchanged() 
		{
			this.dataWasChanged = false;
			
			if(this.rowsCollection != null)
				this.rowsCollection.markUnchanged();
		}

		private boolean dataWasChanged = true;
		private Object value = null;
		private boolean readOnly = false;
		private boolean selectable = true;
		private boolean expanded = false;
		private boolean bold = false;
		private Color backColor = null;
		private Color textColor = null;		
		private Image collapsedImage = null;		
		private Image expandedImage = null;
		private Image selectedImage = null;
		
		//private HashMap mutableComboBoxData = new HashMap(); // for mutable comboboxes
		private ArrayList<GridCell> cells = new ArrayList<GridCell>();
		private String[] cellTooltips;
		private String[] editedText;
		private Color[] cellTextColor;
        private boolean[] cellReadOnly;
		private Color[] cellBackColor;
		private boolean[] isEmpty;
		private GridRowCollection rowsCollection;
		private GridRow parent;
		private boolean isParentRow = false;
		private int opened = -1;		     
	}

	class GridRowCollection implements ims.framework.controls.GridRowCollection, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		GridRowCollection(GridRow parent)
		{
			this.parent = parent;
		}
		public int size()
		{
			return this.rows.size();
		}
		public ims.framework.controls.GridRow get(int index)
		{
			return this.rows.get(index);
		}
		public ims.framework.controls.GridRow newRow()
		{
			return newRowAt(-1, false);
		}
		public ims.framework.controls.GridRow newRow(boolean autoSelect) 
		{			
			return newRowAt(-1, autoSelect);
		}
		public ims.framework.controls.GridRow newRowAt(int index)
		{
			return newRowAt(index, false);
		}
		public ims.framework.controls.GridRow newRowAt(int index, boolean autoSelect) 
		{			
			GridRow row = new GridRow(this.parent);			
			if(index == -1)
				this.rows.add(row);
			else
				this.rows.add(index, row);
			
			if (autoSelect) 
			{
				GridData.this.selectedRowChanged = true;			
				GridData.this.selectedRow = row;
			}
			return row;
		}
		public void remove(int index)
		{
			this.dataWasChanged = true;
			this.rows.remove(index);			
			
			if(!GridData.this.selectedRowChanged)
				GridData.this.selectedRowChanged = GridData.this.selectedRow != null;
			
			GridData.this.selectedRow = null;			
		}
		public void clear()
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.rows.size() != 0;
			
			this.rows.clear();
			
			if(this.parent == null)
			{
				if(!GridData.this.selectedRowChanged)
					GridData.this.selectedRowChanged = GridData.this.selectedRow != null;
				
				GridData.this.selectedRow = null;
			}
		}

		public boolean canMoveCurrentUp()
		{
			if (GridData.this.selectedRow == null) 
				return false;
			
			if (GridData.this.selectedRow.parent == null)
			{
				if(getSelectedRowIndex() == 0)
					return false;
			}
			else
			{
				if(getRowIndex(GridData.this.selectedRow, GridData.this.selectedRow.parent.getRows()) <= 0)
					return false;
			}				
			
			return true;
		}

		public boolean canMoveCurrentDown()
		{
			if (GridData.this.selectedRow == null) 
				return false;
			if (GridData.this.selectedRow.parent == null)
			{
				if (getSelectedRowIndex() + 1 == this.rows.size()) 
					return false;
			}
			else
			{
				if(getRowIndex(GridData.this.selectedRow, GridData.this.selectedRow.parent.getRows()) + 1 == GridData.this.selectedRow.parent.getRows().size())
					return false;
			}

			return true;
		}

		/**
		 * Moves the currently select row up one place.
		 * If no current row, method has no effect.
		 * If current row is at top of grid, method has no effect
		 */
		private void moveUp()
		{
			if(!canMoveCurrentUp()) 
				return;
						
			this.dataWasChanged = true;
			int i = getRowIndex(GridData.this.selectedRow, this);
			swap(i, i - 1);								
		}

		/**
		 * Moves the currently select row down one place.
		 * If no current row, method has no effect.
		 * If current row is at bottom of grid, method has no effect
		 */
		private void moveDown()
		{
			if(!canMoveCurrentDown()) 
				return;
			
			this.dataWasChanged = true;
			int i = getRowIndex(GridData.this.selectedRow, this);
			swap(i, i + 1);										
		}
	
		/**
		 * Moves the currently select row to the position specified by toIndex.
		 * If no current row, method has no effect.
		 * If toIndex is beyond last row index, method has no effect
		 * If toIndex < 0, method has effect
		 * If index of current row = toIndex, method has no effect
		 */	
		private void moveTo(int toIndex)
		{
			if(GridData.this.selectedRow == null) 
				return;
			int i = getRowIndex(GridData.this.selectedRow, this);
			if(i == toIndex) 
				return;
			if(toIndex >= this.rows.size() || toIndex < 0 ) 
				return;
			
			this.dataWasChanged = true;
			swap(i, toIndex);
		}	
	
		/**
		 * Swaps the 2 rows specified by index1 and index2
		 * If either index1 or index1 does not exist, method has no effect
		 * If either index1 or index1 < 0, method has no effect
		 * If index1 = index2 , method has no effect
		 */
		private void swap(int index1, int index2)
		{
			if (index1 < 0 || index1 >= this.rows.size() || index2 < 0 || index2 >= this.rows.size()) 
				return;
			
			this.dataWasChanged = true;
			GridRow o1 = this.rows.get(index1);
			this.rows.set(index1, this.rows.get(index2));
			this.rows.set(index2, o1);
		}

		@SuppressWarnings("unchecked")
		public void sort(int columnIndex, SortOrder dir)
		{
			if (GridData.this.columns.size() <= columnIndex) 
				return; //Invalid column index
			
			GridRowComparator comp = new GridRowComparator(columnIndex, dir);
			Collections.sort(this.rows,comp);
			this.dataWasChanged = true;
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
		private GridRow parent;
		private ArrayList<GridRow> rows = new ArrayList<GridRow>();
	}	
}
class ComboBoxItem implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public ComboBoxItem(Object value, String text, Image image, Color textColor)
	{
		this.value = value;
		this.text = text;
        this.image = image;
        this.textColor = textColor;
	}
    
	Object getValue()
	{
		return this.value;
	}
	String getText()
	{
		return this.text;
	}
    public Image getImage()
    {
        return this.image;        
    }
    public Color getTextColor()
    {
        return this.textColor;
    }
	
	//Useful for ensuring duplicate items don't get added to the combo
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (!(obj instanceof ComboBoxItem)) return false;
		ComboBoxItem tmp = (ComboBoxItem)obj;
		return this.value.equals(tmp.getValue());
	}
	public int hashCode()
	{
		return super.hashCode();
	}
	private Object value;
	private String text;
    private Image image;
    private Color textColor;
}

class MutableCell implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int getSelection()
	{
		return this.selection;
	}
	public Object setSelection(int selection)
	{
		this.selection = selection;
		return this.selection == -1 ? null : this.values.get(this.selection);				
	}
	public int size()
	{
		return this.values.size();
	}
	public String getText(int index)
	{
		return this.texts.get(index);
	}
	public void add(Object value, String text)
	{
		this.values.add(value);
		this.texts.add(text);
	}
	public void clear()
	{
		this.selection = -1;
		this.values.clear();
		this.texts.clear();		
	}
	public Object getValue()
	{
		if (this.selection == -1)
			return null;
		return this.values.get(this.selection);
	}
	public void setValue(Object value)
	{
		this.selection = this.values.indexOf(value);
	}
	public void setMutableComboBoxMinNumberOfChars(int value)
	{
		this.minNumberOfChars = value;
	}
	public int getMutableComboBoxMinNumberOfChars()
	{
		return this.minNumberOfChars;
	}
	
	
	private int selection = -1;
	private int minNumberOfChars = 1;
	private ArrayList<Object> values = new ArrayList<Object>();
	private ArrayList<String> texts = new ArrayList<String>();	
}
class MutableAnswerBoxCell implements Serializable
{
	private static final long serialVersionUID = 1L;
	public void add(AnswerBoxOption option)
	{
		if (this.options.size() == 0)
			this.selectedIndex = 0;
		this.options.add(option);
	}
	public void clear()
	{
		this.selectedIndex = -1;
		this.options.clear();
	}
	public AnswerBoxOption getValue()
	{
		if (this.selectedIndex == -1)
			return null;
		return this.options.get(this.selectedIndex);
	}
	public void setValue(AnswerBoxOption option)
	{
		this.selectedIndex = this.options.indexOf(option);
	}
	public int getSelection()
	{
		return this.selectedIndex;
	}
	public AnswerBoxOption setSelection(int index)
	{
		this.selectedIndex = index;
		return this.options.get(index);
	}
	public ArrayList getOptions()
	{
		return this.options;
	}
	
	private int selectedIndex = -1;
	private ArrayList<AnswerBoxOption> options = new ArrayList<AnswerBoxOption>();
}
