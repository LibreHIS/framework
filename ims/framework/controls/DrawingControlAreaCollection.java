/*
 * Created on 21-Sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;

import ims.vo.ImsCloneable;

/**
 * @author jmacenri
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DrawingControlAreaCollection implements ImsCloneable, Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<DrawingControlArea> coll = new ArrayList<DrawingControlArea>();
	
	public DrawingControlAreaCollection()
	{
	}
	
	public DrawingControlAreaCollection(ArrayList<DrawingControlArea> coll)
	{
		this.coll = coll;
	}

	public Object clone()
	{
		DrawingControlAreaCollection clone = new DrawingControlAreaCollection();
		clone.addAll(this);
		return clone;
	}

	public void add(DrawingControlArea area)
	{
		this.coll.add(area);
	}
	
	public DrawingControlArea get(int index)
	{
		return this.coll.get(index);
	}
	
	protected void addAll(DrawingControlAreaCollection areaColl)
	{
		this.coll.addAll(areaColl.getCollection());
	}
	
	private ArrayList<DrawingControlArea> getCollection()
	{
		return this.coll;
	}
	
	public int size()
	{
		return this.coll.size();
	}
}
