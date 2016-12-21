package ims.framework.enumerations;

import java.io.Serializable;

import ims.framework.controls.DynamicGridCellTable;
import ims.framework.utils.Date;
import ims.framework.utils.DateTime;
import ims.framework.utils.Image;
import ims.framework.utils.PartialDate;
import ims.framework.utils.Time;

/**
 * @author mmihalec
 */
public class DynamicCellType implements Serializable
{
	private static final long serialVersionUID = 1L;
    public static final DynamicCellType EMPTY = new DynamicCellType(0, null, false, false, false, true, 50, false, false);
    public static final DynamicCellType ANSWER = new DynamicCellType(1, Object.class, true, false, true, false, 80, false, false);
    public static final DynamicCellType BOOL = new DynamicCellType(2, Boolean.class, false, false, true, false, 30, false, false);
    public static final DynamicCellType BUTTON = new DynamicCellType(3, String.class, false, false, true, false, 80, true, false);
    public static final DynamicCellType COMMENT = new DynamicCellType(4, String.class, false, false, true, false, 200, false, true);
    public static final DynamicCellType DATE = new DynamicCellType(5, Date.class, false, true, true, false, 100, false, true);
    public static final DynamicCellType DECIMAL = new DynamicCellType(6, Float.class, false, true, true, false, 80, false, true);
    public static final DynamicCellType ENUMERATION = new DynamicCellType(7, Object.class, true, false, true, false, 140, false, false);
    public static final DynamicCellType IMAGE = new DynamicCellType(8, Image.class, false, false, false, false, 30, false, false);
    public static final DynamicCellType INT = new DynamicCellType(9, Integer.class, false, true, true, false, 80, false, true);
    public static final DynamicCellType QUERYCOMBOBOX = new DynamicCellType(10, Object.class, true, true, true, false, 140, false, false);
    public static final DynamicCellType STRING = new DynamicCellType(11, String.class, false, true, true, false, 200, false, true);
    public static final DynamicCellType TIME = new DynamicCellType(12, Time.class, false, true, true, false, 80, false, true);
    public static final DynamicCellType WRAPTEXT = new DynamicCellType(13, String.class, false, true, true, false, 200, false, true);    
    public static final DynamicCellType MULTISELECT = new DynamicCellType(15, Object.class, true, false, true, false, 200, false, false);
    public static final DynamicCellType PARTIALDATE = new DynamicCellType(16, PartialDate.class, false, true, true, false, 80, false, true);
    public static final DynamicCellType DATETIME = new DynamicCellType(17, DateTime.class, false, true, true, false, 130, false, true);
    public static final DynamicCellType HTMLVIEW = new DynamicCellType(18, String.class, false, false, false, true, 200, false, false);
    public static final DynamicCellType LABEL = new DynamicCellType(19, String.class, false, false, false, true, 80, false, false);
    public static final DynamicCellType DYNAMICLABEL = new DynamicCellType(20, String.class, false, false, false, true, 80, false, false);
    public static final DynamicCellType IMAGEBUTTON = new DynamicCellType(21, Image.class, false, false, true, false, 80, true, false);
    public static final DynamicCellType TABLE = new DynamicCellType(22,DynamicGridCellTable.class, false, false, false, true, 160, false, false);
    
    private DynamicCellType(int id, Class type, boolean supportsItems, boolean supportsValidation, boolean supportsAutoPostBack, boolean alwaysReadOnly, int defaultWidth, boolean alwaysHasAutoPortBack, boolean supportsCellDecorator)
    {
        this.id = id;
        this.type = type;
        this.supportsItems = supportsItems;
        this.supportsValidation = supportsValidation;
        this.supportsAutoPostBack = supportsAutoPostBack;
        this.alwaysReadOnly = alwaysReadOnly;
        this.defaultWidth = defaultWidth;
        this.alwaysHasAutoPortBack = alwaysHasAutoPortBack;
        this.supportsCellDecorator = supportsCellDecorator;
    }
    public Class getCellType()
    {
        return this.type;
    }
    @SuppressWarnings("unchecked")
	public void checkType(Class currentType)
    {
        if(this.type == null) // empty
            throw new RuntimeException("Could not set value for cell of type " + this.toString());
        	
        if(this.type.isAssignableFrom(currentType))
            return;
        
        throw new RuntimeException("Could not set cell value. Type expected: " + this.type + ", current type: " + currentType);
    }
    public String toString()
    {
        if(this.id == 0)
            return "Empty";
        if(this.id == 1)            
            return "AnswerBox";
        else if(this.id == 2)
            return "Bool";
        else if(this.id == 3)
            return "Button";
        else if(this.id == 4)
            return "Comment";
        else if(this.id == 5)
            return "Date";
        else if(this.id == 6)
            return "Decimal";
        else if(this.id == 7)
            return "ComboBox";
        else if(this.id == 8)
            return "Image";
        else if(this.id == 9)
            return "Int";
        else if(this.id == 10)
            return "ComboBox";
        else if(this.id == 11)
            return "String";
        else if(this.id == 12)
            return "Time";
        else if(this.id == 13)
            return "WrapText";
        else if(this.id == 15)
            return "CheckedListBox";
        else if(this.id == 16)
            return "PartialDateBox";
        else if(this.id == 17)
            return "DateTime";
        else if(this.id == 18)
            return "Html";
        else if(this.id == 19)
            return "String";
        else if(this.id == 20)
            return "FixedString";        
        else if(this.id == 21)
            return "ImageButton";
        else if(this.id == 22)
            return "Table";
        else return "";
    }
    public String render()
    {
        return toString();
    }
    public boolean isSupportingItems()
    {
        return this.supportsItems;
    }
    public boolean isSupportingValidation()
    {
        return this.supportsValidation;
    }
    public boolean isSupportingAutoPostBack()
    {
        return this.supportsAutoPostBack;
    }
    public int getID()
    {
        return this.id;
    }
    public boolean isAlwaysReadOnly()
    {
    	return this.alwaysReadOnly;
    }
    public int getDefaultWidth()
    {
    	return this.defaultWidth;
    }
    public boolean alwaysHasAutoPortBack()
    {
    	return this.alwaysHasAutoPortBack;              
    }
    public boolean isSupportingCellDecorator()
    {
        return this.supportsCellDecorator;
    }
        
    private int id;
    private Class type;
    private boolean supportsItems;
    private boolean supportsValidation;
    private boolean supportsAutoPostBack;
    private boolean alwaysReadOnly;
    private int defaultWidth;
    private boolean alwaysHasAutoPortBack;
    private boolean supportsCellDecorator;
}
