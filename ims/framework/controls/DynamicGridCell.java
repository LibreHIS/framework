package ims.framework.controls;

import ims.framework.enumerations.CharacterCasing;
import ims.framework.enumerations.DynamicCellDecoratorMode;
import ims.framework.enumerations.DynamicCellType;
import ims.framework.utils.Color;

/**
 * @author mmihalec
 */
public interface DynamicGridCell 
{
	void setIdentifier(Object value);
    Object getIdentifier();
    Object getValue();
    boolean setValue(Object value);
    String getTypedText();
    void setTypedText(String value);
    DynamicCellType getType();
    void setReadOnly(boolean value);
    boolean isReadOnly();
    void setTooltip(String value);
    String getTooltip();
    void setColumn(DynamicGridColumn value);
    DynamicGridColumn getColumn();
    DynamicGridRow getRow();
    void setTextColor(Color value);
    Color getTextColor();
    void setBackColor(Color value);
    Color getBackColor();
    void setValidationMessage(String value);
    String getValidationMessage();
    void setCanBeEmpty(boolean value);
    boolean canBeEmpty();
    void setAutoPostBack(boolean value);
    boolean hasAutoPostBack();
    void setDecimalPrecisionScale(int precision, int scale);
    int getDecimalPrecision();
    int getDecimalScale();    
    void setStringMaxLength(int value);
    int getStringMaxLength();    
    void setIntMaxLength(int value);
    int getIntMaxLength();    
    void setMaxVisibleItemsForMultiSelect(int value);
    int getMaxVisibleItemsForMultiSelect();  
    void setMaxCheckedItemsForMultiSelect(Integer value);
    Integer getMaxCheckedItemsForMultiSelect();
    void setAutoWrapForMultiSelect(boolean value);
    boolean getAutoWrapForMultiSelect();
    void setWidth(int value);
    int getWidth();
    void setDecoratorType(DynamicCellDecoratorMode value);
    DynamicCellDecoratorMode getDecoratorType();    
    void showOpened();
    void setOptions(DynamicGridCellOptions options);
    void resetOptions();
    void setFixedFont(boolean value);
    boolean isFixedFont();    
    CharacterCasing getCharacterCasing();
    void setCharacterCasing(CharacterCasing value);
    int getMaxDropDownItems();
    void setMaxDropDownItems(int value);    
    void clear();
    int getTableRowId();
    void setTableRowId(int id);
    int getTableCellId();
    void setTableCellId(int id);
    String getButtonText();
    void setButtonText(String text);
    Color getButtonTextColor();
    void setButtonTextColor(Color value);
    
    DynamicGridCellItemCollection getItems();
}
