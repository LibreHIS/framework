package ims.framework.cn.data;

import java.util.ArrayList;

import ims.framework.controls.DrawingControlGroup;
import ims.framework.controls.DrawingControlShape;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.controls.DrawingControlColorBrush;
import ims.framework.controls.DrawingControlImageBrush;

public class DrawingControlData extends ChangeableData implements IControlData
{	
	private static final long serialVersionUID = 5319205684723685885L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public void setEnabled(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.visible != value;
		
		this.visible = value;
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
	public Image getImage()
	{
		return this.image;
	}
	public void setImage(Image value)
	{
		if(!this.dataWasChanged)
		{
			if(this.image == null)
				this.dataWasChanged = true;
			else 
				this.dataWasChanged = !this.image.equals(value);
		}
		
		this.image = value;		
	}
	public void addBrush(int id, String caption, Image image, boolean multipleMarkings, String tooltip)
	{
		this.dataWasChanged = true;
		this.bitmaps.add(new DrawingControlImageBrush(id, caption, image, multipleMarkings, tooltip));		
	}
	public void addBrush(int id, String caption, Color color, boolean multipleMarkings, String tooltip)
	{
		this.dataWasChanged = true;
		this.colors.add(new DrawingControlColorBrush(id, caption, color, multipleMarkings, tooltip));		
	}
	public void clearBrushes()
	{		
		if(!this.dataWasChanged)
			this.dataWasChanged = this.bitmaps.size() != 0 || this.colors.size() != 0;
		
		this.bitmaps.clear();
		this.colors.clear();	
	}
	public DrawingControlImageBrush[] getImageBrushes()
	{
		DrawingControlImageBrush[] result = new DrawingControlImageBrush[this.bitmaps.size()];
		for(int x = 0; x < this.bitmaps.size(); x++)
			result[x] = this.bitmaps.get(x);
		return result;
	}
	public DrawingControlColorBrush[] getColorBrushes()
	{
		DrawingControlColorBrush[] result = new DrawingControlColorBrush[this.colors.size()];
		for(int x = 0; x < this.colors.size(); x++)
			result[x] = this.colors.get(x);
		return result;
	}
	public DrawingControlGroup getRoot()
	{
		return this.root;
	}
	public void setRoot(DrawingControlGroup value)
	{
		if(!this.dataWasChanged)
		{
			if(this.root == null)
				this.dataWasChanged = true;
			else 
				this.dataWasChanged = !this.root.equals(value);
		}
		
		this.root = value;
	}
	public void addShape(DrawingControlShape shape)
	{
		boolean exists = false;
		for (int i = 0; i < this.shapes.size(); ++i)
		{
			DrawingControlShape existingShape = this.shapes.get(i);
			if (existingShape.getIndex() == shape.getIndex())
			{	
				if(existingShape.getReadOnly())
					shape.setReadOnly(true);
				
				this.shapes.set(i, shape);
				exists = true;
			}
		}
		
		if(!exists)
		{
			this.dataWasChanged = true;
			this.shapes.add(shape);
		}
	}
	public ArrayList<DrawingControlShape> getShapes()
	{
		ArrayList<DrawingControlShape> result = new ArrayList<DrawingControlShape>();
		for (int i = 0; i < this.shapes.size(); ++i)
			result.add(this.shapes.get(i));
		return result;
	}
	public void clearShapes()
	{
		if(!this.dataWasChanged)
			this.dataWasChanged = this.shapes.size() != 0;
		
		this.shapes.clear();
	}
	public void removeShape(int index)
	{
		for(int i = 0; i < this.shapes.size(); ++i)
		{
			if(this.shapes.get(i).getIndex() == index)
			{
				this.dataWasChanged = true;
				this.shapes.remove(i);
				break;
			}
		}
	}
	public DrawingControlShape getShape(int index)
	{
		for(int i = 0; i < this.shapes.size(); ++i)
		{
			DrawingControlShape shape = this.shapes.get(i);
			if (shape.getIndex() == index)
				return shape;
		}
		
		return null;				
	}
	
	public void setPrintTitle(String value)
	{
		if(!this.dataWasChanged)
		{
			if(this.printTitle == null)
				this.dataWasChanged = true;
			else 
				this.dataWasChanged = !this.printTitle.equals(value);
		}
		
		this.printTitle = value; 
	}
	public String getPrintTitle()
	{
		return this.printTitle == null ? "" : this.printTitle; 
	}
	public void setPrintSubTitle(String value)
	{
		if(!this.dataWasChanged)
		{
			if(this.printSubTitle == null)
				this.dataWasChanged = true;
			else 
				this.dataWasChanged = !this.printSubTitle.equals(value);
		}
		
		this.printSubTitle = value;
	}
	public String getPrintSubTitle()
	{
		return this.printSubTitle == null ? "" : this.printSubTitle;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private boolean readOnly = true;
	private String printTitle = "Untitled";
	private String printSubTitle = "";
	
	private Image image = null;
	private ArrayList<DrawingControlImageBrush> bitmaps = new ArrayList<DrawingControlImageBrush>();
	private ArrayList<DrawingControlColorBrush> colors = new ArrayList<DrawingControlColorBrush>();
	private DrawingControlGroup root = null;
	private ArrayList<DrawingControlShape> shapes = new ArrayList<DrawingControlShape>();	
}
