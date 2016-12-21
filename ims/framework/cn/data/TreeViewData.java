package ims.framework.cn.data;

import ims.base.interfaces.IModifiable;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.vo.ImsCloneable;

import java.io.Serializable;
import java.util.ArrayList;

public class TreeViewData implements IControlData
{
	private static final long serialVersionUID = -4601638725513056759L;
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
	public void init(boolean checkBoxes)
	{
		if(this.nodes == null)
		{
			this.nodes = new TreeNodeCollection(checkBoxes, null, this);
			this.rootNodes = this.nodes;
		}
	}
	public ims.framework.controls.TreeNodeCollection getNodes()
	{
		return this.nodes;
	}
	public void clear()
	{
		if(!this.selectionChanged)
			this.selectionChanged = this.selection.length() != 0;
		
		this.selection = "";
		this.nodes.clear();
	}
	public boolean setValue(Object value)
	{
		if(value == null)
		{
			if(!this.selectionChanged)
			{
				if(this.selection == null)
					this.selectionChanged = true;
				else
					this.selectionChanged = this.selection.length() > 0;
			}
				
			this.selection = "";
			return true;
		}
		
		String nodeIndex = this.findNode(this.nodes, value);
		if(nodeIndex != "")
		{
			if(!this.selectionChanged)
			{
				if(this.selection == null)
					this.selectionChanged = true;
				else
					this.selectionChanged = !this.selection.equals(nodeIndex);				
			}
			
			this.selection = nodeIndex;
			return true;
		}
		
		return false;
	}
	public ims.framework.controls.TreeNode getSelectedNode()
	{
		return this.getNodeByIndex(this.selection);
	}
	
	public ims.framework.controls.TreeNode getNodeByValue(Object value)
	{
		return this.getNodes().getNodeByValue(value);
	}	
	
	public void collapseAll()
	{		
		for(int i = 0; i < this.nodes.size(); ++i)
		{			
			TreeNode node = (TreeNode)this.nodes.get(i);
			this.collapseExpand(node, false);
		}
	}

	public void expandAll()
	{		
		for(int i = 0; i < this.nodes.size(); ++i)
		{
			TreeNode node = (TreeNode)this.nodes.get(i);
			this.collapseExpand(node, true);
		}
	}
	public String getSelection()
	{
		return this.selection;
	}
	public void setSelectedNode(TreeNode node)
	{
		String oldSelection = this.selection;
		if(oldSelection == null)
			oldSelection = "";
		
		if(node == null)
		{
			this.selection = "";
		}
		else			
		{
			this.selection = "";
			if(!setSelectedNode(this.nodes, node))
			{
				this.selection = oldSelection;
			}
			else
			{
				// We mark all nodes all the way up as EXPANDED! 				
				markHierarchyExpanded((TreeNode)this.getSelectedNode());
			}
		}
		
		if(!this.selectionChanged)
			this.selectionChanged = !this.selection.equals(oldSelection);
	}
	private void markHierarchyExpanded(TreeNode selectedNode) 
	{
		if(selectedNode == null)
			return;
		
		selectedNode.setExpanded(true);
		
		markHierarchyExpanded(selectedNode.parent);
	}
	private boolean setSelectedNode(TreeNodeCollection nodes, TreeNode nodeToBeSelected)
	{
		if(nodes == null || nodeToBeSelected == null)
			return false;
		
		String intialSelection = this.selection;
		for(int x = 0; x < nodes.size(); x++)
		{		
			if(nodeToBeSelected.equals(nodes.get(x)))
			{
				if(this.selection.length() > 0)
					this.selection += ".";			
				this.selection += String.valueOf(x);
				
				return true;
			}
			
			if(this.selection.length() > 0)
				this.selection += ".";			
			this.selection += String.valueOf(x);
				
			if(setSelectedNode((TreeNodeCollection)nodes.get(x).getNodes(), nodeToBeSelected))
				return true;
			
			this.selection = intialSelection;
		}
		
		this.selection = intialSelection;
		return false;
	}
	public void setSelection(String value)
	{
		if(!this.selectionChanged)
		{
			if(this.selection == null)
				this.selectionChanged = value != null;
			else 
				this.selectionChanged = !this.selection.equals(value);
		}
		
		this.selection = value;
	}
	public void setExpanded(String node, boolean expanded)
	{
		this.getNodeByIndex(node).setExpanded(expanded);
	}
	public void setChecked(String node, boolean checked)
	{
		this.getNodeByIndex(node).setChecked(checked);
	}
	public void render(StringBuffer sb, boolean allowDragDrop) throws ConfigurationException
	{
		if(this.nodes.size() > 0)
			this.nodes.render(sb, allowDragDrop);
		else
			sb.append("<nonodes/>");
	}
	
	private ArrayList split(String value)
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		int i = value.indexOf('.');
		while (i != -1)
		{
			indexes.add(new Integer(Integer.parseInt(value.substring(0, i))));
			value = value.substring(i + 1);
			i = value.indexOf('.');
		}
		indexes.add(new Integer(Integer.parseInt(value)));
		return indexes;
	}
	private String findNode(ims.framework.controls.TreeNodeCollection nodes, Object value)
	{
		for (int i = 0; i < nodes.size(); ++i)
		{
			ims.framework.controls.TreeNode node = nodes.get(i);
			if (node.getValue() != null && node.getValue().equals(value))
				return String.valueOf(i);
			
			String tmp = findNode(node.getNodes(), value);
			if (!tmp.equals(""))
				return i + "." + tmp;
		}
		return "";
	}
	
	private void collapseExpand(ims.framework.controls.TreeNode node, boolean expand)
	{		
		ims.framework.controls.TreeNodeCollection nodes = node.getNodes();
		for(int i = 0; i < nodes.size(); ++i)
			collapseExpand(nodes.get(i), expand);
		node.setExpanded(expand);
	}
	
	public ims.framework.controls.TreeNode getNodeByIndex(String value)
	{		
		if(this.nodes.size() == 0 || value == "")
			return null;
		
		int index = 0;		
		ims.framework.controls.TreeNodeCollection nodes = this.nodes;
		ArrayList indexes = split(value);
		for(int i = 0; i < indexes.size() - 1; ++i)
		{
			index = ((Integer)indexes.get(i)).intValue();
			if(index < 0 || nodes.size() - 1 < index)
				return null;			
			nodes = nodes.get(index).getNodes();
			if(nodes.size() == 0)
				return null;
		}
		
		if(nodes.size() == 0)
			return null;		
		index = ((Integer)indexes.get(indexes.size() - 1)).intValue();		
		if(index < 0 || nodes.size() - 1 < index)
			return null;
		
		return nodes.get(index);
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
	public boolean dragNode(String selection, String newIndex)
	{
		if(selection == null)
			throw new RuntimeException("Dragged node is invalid");
		if(newIndex == null)
			throw new RuntimeException("Dragged node destination is invalid");		
		ims.framework.controls.TreeNode node = getNodeByIndex(selection);
		if(node == null)
			throw new RuntimeException("Dragged node not found, selection was " + selection);
		
		// removing the node from it's old position
		if(node.getParent() == null)
		{
			this.nodes.remove(node);
		}
		else
		{
			node.getParent().getNodes().remove(node);
		}
		
		if(newIndex.indexOf(".") < 0)
		{
			// node was moved to the top collection			
			//node.setParent(null);
			((TreeNode)node).parent = null;
			this.nodes.add(Integer.parseInt(newIndex), node);
		}
		else
		{
			int lastIndex = newIndex.lastIndexOf(".");			
			String parent = newIndex.substring(0, lastIndex);
			ims.framework.controls.TreeNode parentNode = getNodeByIndex(parent);
			if(parentNode == null)
				throw new RuntimeException("Parent node for dragged node not found, new index was " + newIndex);
			int newNodeIndex = Integer.parseInt(newIndex.substring((lastIndex + 1)));	
			//node.setParent(parentNode);
			((TreeNode)node).parent = (TreeNode)parentNode;			
			((TreeNodeCollection)parentNode.getNodes()).add(newNodeIndex, node);
		}
						
		this.setSelection(newIndex);		
		return true;
	}
	
	private boolean enabled = true;
	private boolean visible = true;
	private TreeNodeCollection nodes = null;
	private TreeNodeCollection rootNodes = null;
	private boolean allowDragDrop = false;
	private boolean unselectable = false;	
	private String selection = "";
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean allowDragDropChanged = false;
	private boolean unselectableChanged = false;
	private boolean selectionChanged = false;
	
	class TreeNodeCollection implements ims.framework.controls.TreeNodeCollection, ImsCloneable, IModifiable, Serializable
	{
		private static final long serialVersionUID = 1L;
		
		private boolean dataWasChanged = false;
		public boolean wasChanged() 
		{
			if(this.dataWasChanged)
				return true;
			
			if(this.nodes != null)
			{
				for(int x = 0; x < this.nodes.size(); x++)
				{					
					if(this.nodes.get(x).wasChanged())
						return true;
				}
			}
			
			return false;
		}
		public void markUnchanged() 
		{
			this.dataWasChanged = false;
			
			if(this.nodes != null)
			{
				for(int x = 0; x < this.nodes.size(); x++)
				{
					this.nodes.get(x).markUnchanged();					
				}
			}
		}		
		public TreeNodeCollection(boolean checkBoxes, TreeNode parent, TreeViewData control)
		{
			super();
			this.checkBoxes = checkBoxes;
			this.parent = parent;
			this.control = control;
		}
	
        public int indexOf(ims.framework.controls.TreeNode node)
        {
            return this.nodes.indexOf(node);
        }
        public ims.framework.controls.TreeNode add(Object value, String text)
        {
        	return add(value, text, false, null, null, null);
        }        
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect)
        {
        	return add(value, text, autoSelect, null, null, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType)
        {
        	return add(value, text, false, new Integer(dragType), null, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType)
        {
        	return add(value, text, autoSelect, new Integer(dragType), null, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType, int[] dropToTypes)
        {
        	return add(value, text, false, new Integer(dragType), dropToTypes, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType, int[] dropToTypes)
        {
        	return add(value, text, autoSelect, new Integer(dragType), dropToTypes, null);
        }        
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType, int dropToType)
        {
        	return add(value, text, false, new Integer(dragType), new int[] { dropToType }, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType, int dropToType)
        {
        	return add(value, text, autoSelect, new Integer(dragType), new int[] { dropToType }, null);
        }
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType, int[] dropToTypes, boolean allowDragDropSorting)
        {
        	return add(value, text, false, new Integer(dragType), dropToTypes, new Boolean(allowDragDropSorting));
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType, int[] dropToTypes, boolean allowDragDropSorting)
        {
        	return add(value, text, autoSelect, new Integer(dragType), dropToTypes, new Boolean(allowDragDropSorting));
        }
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType, int dropToType, boolean allowDragDropSorting)
        {
        	return add(value, text, false, new Integer(dragType), new int[] { dropToType }, new Boolean(allowDragDropSorting));
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType, int dropToType, boolean allowDragDropSorting)
        {
        	return add(value, text, autoSelect, new Integer(dragType), new int[] { dropToType }, new Boolean(allowDragDropSorting));
        }
        public ims.framework.controls.TreeNode add(Object value, String text, int dragType, boolean allowDragDropSorting)
        {
        	return add(value, text, false, new Integer(dragType), null, new Boolean(allowDragDropSorting));
        }
        public ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, int dragType, boolean allowDragDropSorting)
        {
        	return add(value, text, autoSelect, new Integer(dragType), null, new Boolean(allowDragDropSorting));
        }
		private ims.framework.controls.TreeNode add(Object value, String text, boolean autoSelect, Integer dragType, int[] dropToTypes, Boolean allowDragDropSorting)
		{
			this.dataWasChanged = true;
			TreeNode node = new TreeNode(this.checkBoxes, this.parent, value, text, dragType, dropToTypes, allowDragDropSorting, this.control);
			this.nodes.add(node);
			if(autoSelect)
				this.control.setSelectedNode(node);
			return node;
		}
		public void add(int index, ims.framework.controls.TreeNode node)
		{
			this.nodes.add(index, (TreeNode)node);
		}
		public ims.framework.controls.TreeNode get(int index)
		{
			return this.nodes.get(index);
		}
		public boolean remove(ims.framework.controls.TreeNode node)
		{
            boolean shouldClearSelection = getSelectedNode() != null && getSelectedNode().equals(node);
			if(this.nodes.remove(node))
			{
				this.dataWasChanged = true;
                if(shouldClearSelection)
                    setSelection("");
                return true;				
			}
			
			for (int i = 0; i < this.nodes.size(); ++i)
			{
				TreeNode childnode = this.nodes.get(i);
                shouldClearSelection = getSelectedNode() != null && getSelectedNode().equals(childnode);
				if(childnode.remove(node))
                {
					this.dataWasChanged = true;
                    if(shouldClearSelection)
                        setSelection("");
					return true;
                }
			}	
            
			return false;			
		}
		public int size()
		{
			return this.nodes.size();
		}		
		public void clear()
		{
			for(int i = 0; i < this.nodes.size(); ++i)
			{
				this.dataWasChanged = true;
				this.nodes.get(i).clear();
			}
			
			if(!this.dataWasChanged)
				this.dataWasChanged = this.nodes.size() != 0;
			
			this.nodes.clear();
		}
		public void render(StringBuffer sb, boolean treeDragDropAllowed) throws ConfigurationException
		{
			for(int i = 0; i < this.nodes.size(); ++i)
				this.nodes.get(i).render(sb, treeDragDropAllowed);
		}
		
		//Only there to satisfy ImsCloneable so can put in Local Context
		//Not a true clone as arraylist is simply re-pointed to.
		public Object clone()
		{
			TreeNodeCollection tc = new TreeNodeCollection(this.checkBoxes, this.parent, this.control);
			tc.nodes = this.nodes;
			return tc;			
		}		
		public void moveUp() 
		{			
			ims.framework.controls.TreeNode node = getSelectedNode();
			if (node == null) 
				return;			
			int i = this.nodes.indexOf(node);
			this.dataWasChanged = true;
			swap(i, i - 1);								
		}
		public void moveDown() 
		{
			ims.framework.controls.TreeNode node = getSelectedNode();
			if (node == null) 
				return;
			int i = this.nodes.indexOf(node);
			this.dataWasChanged = true;
			swap(i, i + 1);										
		}
		public void moveTo(int toIndex) 
		{
			ims.framework.controls.TreeNode node = getSelectedNode();
			if (node == null) 
				return;
			int i = this.nodes.indexOf(node);
			if (i == toIndex) 
				return;
			if (toIndex >= this.nodes.size() || toIndex < 0 ) 
				return;
			
			this.dataWasChanged = true;
			swap(i, toIndex);
		}
		public void swap(int index1, int index2) 
		{			
			if (index1 < 0 || index1 >= this.nodes.size() || index2 < 0 || index2 >= this.nodes.size()) 
				return;
			
			this.dataWasChanged = true;
			TreeNode o1 = this.nodes.get(index1);
			this.nodes.set(index1,this.nodes.get(index2));
			this.nodes.set(index2, o1);			
		}
		public boolean canMoveCurrentUp() 
		{
			ims.framework.controls.TreeNode node = getSelectedNode();
			if (node == null) 
				return false;
			if (this.nodes.size() == 0) 
				return false;
			return (this.nodes.indexOf(node) > 0);
		}
		public boolean canMoveCurrentDown() 
		{
			ims.framework.controls.TreeNode node = getSelectedNode();
			if (node == null) 
				return false;
			if (this.nodes.size() == 0) 
				return false;
			return ((this.nodes.indexOf(node) + 1) < this.nodes.size());
		}
		public void sortByText()
		{
			sortByText(true, false, false);
		}
		public void sortByText(boolean ascending)
		{
			sortByText(ascending, false, false);
		}
		public void sortByText(boolean ascending, boolean recursive)
		{
			sortByText(ascending, recursive, false);
		}
		public void sortByText(boolean ascending, boolean recursive, boolean caseSensitive)
		{
			int count = this.nodes.size();
			int[] sorted = new int[count];
			for(int x = 0; x < count; x++)
			{
				sorted[x] = x;
			}
			
			for(int x = 0; x < count; x++)
			{
				for(int y = 0; y < count; y++)
				{
					String source = caseSensitive ? this.nodes.get(sorted[x]).getText() : this.nodes.get(sorted[x]).getText().toLowerCase();
					String compare = caseSensitive ? this.nodes.get(sorted[y]).getText() : this.nodes.get(sorted[y]).getText().toLowerCase();
					
					if((ascending && source.compareTo(compare) < 0) || (!ascending && source.compareTo(compare) > 0))
					{					
						int temp = sorted[x]; 
						sorted[x] = sorted[y];
						sorted[y] = temp;	
						this.dataWasChanged = true;								
					}					
				}
			}
			
			ArrayList<TreeNode> sortedNodes = new ArrayList<TreeNode>();
			for(int x = 0; x < count; x++)
			{
				sortedNodes.add(this.nodes.get(sorted[x]));
			}
			
			this.nodes = sortedNodes;			
			
			if(recursive)
			{
				for(int x = 0; x < count; x++)
				{
					this.nodes.get(x).sortChildrenByText(ascending, recursive, caseSensitive);
				}
			}			
		}
		
		public ims.framework.controls.TreeNode getNodeByValue(Object value)
		{
			return getNodeByValue(this, value);
		}
		
		private ims.framework.controls.TreeNode getNodeByValue(ims.framework.controls.TreeNodeCollection coll,  Object value)
		{
			if (coll == null)
				return null;
			
			for (int i = 0; i < coll.size(); i++)
			{
				if (coll.get(i).getValue().equals(value))
				{
					return coll.get(i);
				}
				
				ims.framework.controls.TreeNode ret = getNodeByValue(coll.get(i).getNodes(), value);
				if(ret != null)
				{
					return ret;
				}
			}
			return null;			
		}
		
		private ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		private boolean checkBoxes;
		private TreeNode parent;
		private TreeViewData control;
	}
	
	class TreeNode implements ims.framework.controls.TreeNode, IModifiable, Serializable
	{ 
		private static final long serialVersionUID = 1L;
		
		private boolean dataWasChanged = true;
		
		public boolean wasChanged() 
		{
			if(this.dataWasChanged)
				return true;
			if(this.nodes != null)
				return this.nodes.wasChanged();
			
			return false;
		}
		public void markUnchanged()
		{
			this.dataWasChanged = false;
			if(this.nodes != null)
				this.nodes.markUnchanged();
		}
		public TreeNode(boolean checkBoxes, TreeNode parent, Object value, String text, TreeViewData control)
		{
			this(checkBoxes, parent, value, text, null, null, null, control);
		}
		public TreeNode(boolean checkBoxes, TreeNode parent, Object value, String text, Integer dragDropType, int[] dropToTypes, Boolean allowDragDropSorting, TreeViewData control)
		{
			super();
			
			this.nodes = new TreeNodeCollection(checkBoxes, this, control);
			this.checkBoxVisible = checkBoxes;
			this.parent = parent;
			this.value = value;
			this.text = text;
			this.control = control;
			
			this.dragDropType = dragDropType;				
			this.dropToTypes = dropToTypes;
			this.allowDragDropSorting = allowDragDropSorting; 
		}
		public Integer getDragDropType()
		{
			return this.dragDropType;
		}
		public ims.framework.controls.TreeNodeCollection getNodes()
		{
			return this.nodes;
		}
		public ims.framework.controls.TreeNode getParent()
		{
			return this.parent;
		}
		public String getText()
		{
			return this.text;
		}
		public Object getValue()
		{
			return this.value;
		}		
		public void setEnabled(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.enabled != value;
			
			this.enabled = value;
		}		
		public boolean isCheckBoxVisible()
		{
			return this.checkBoxVisible;
		}
		public boolean isChecked()
		{
			return this.checked;
		}
		public boolean isExpanded()
		{
			return this.expanded;
		}		
		public boolean isEnabled()
		{
			return this.enabled;
		}		
		public void setCheckBoxVisible(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.checkBoxVisible != value;
			
			this.checkBoxVisible = value;
		}
		public void setChecked(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.checked != value;
			
			this.checked = value;
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
		public void setExpanded(boolean value)
		{
			if(!this.dataWasChanged)
				this.dataWasChanged = this.expanded != value;
			
			this.expanded = value;
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
		public void setText(String value)
		{
			if(!this.dataWasChanged)
			{
				if(this.text == null)
					this.dataWasChanged = value != null;
				else 
					this.dataWasChanged = !this.text.equals(value);
			}
			
			this.text = value;
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
            return this.textColor;
        }
        
		public void clear()
		{
			this.dataWasChanged = true;
			this.nodes.clear();
			this.value = null;
			this.text = null;
			this.tooltip = null;
			this.collapsedImage = null;
			this.expandedImage = null;
			this.selectedImage = null;
			this.parent = null;	
            this.textColor = Color.Default;
		}
		public void render(StringBuffer sb, boolean treeDragDropAllowed) throws ConfigurationException
		{
		    sb.append("<node text=\"");
			sb.append(ims.framework.utils.StringUtils.encodeXML(this.text));
			sb.append("\" checkBoxVisible=\"");
			sb.append(this.checkBoxVisible ? "true" : "false");
			
            sb.append("\" textColor=\"" + this.textColor);
            
            if (!this.enabled)
			{
				sb.append("\" enabled=\"");
				sb.append(this.enabled ? "true" : "false");
			}
            if (this.checkBoxVisible)
			{
				sb.append("\" checked=\"");
				sb.append(this.checked ? "true" : "false");
			}
			if (this.nodes.size() > 0)
			{
				sb.append("\" expanded=\"");
				sb.append(this.expanded ? "true" : "false");
			}			
			if(treeDragDropAllowed)
			{
				if(this.dragDropType != null)
				{
					sb.append("\" dropType=\"");
					sb.append(this.dragDropType);
				}
				if(this.dropToTypes != null)
				{
					String resp = "";
					for(int x = 0; x < this.dropToTypes.length; x++)
					{
						if(resp.length() > 0)
							resp += ",";
						resp += this.dropToTypes[x];
					}
					sb.append("\" dropToType=\"");
					sb.append(resp);
				}
				if(this.allowDragDropSorting != null)
				{
					sb.append("\" allowSort=\"");
					sb.append(this.allowDragDropSorting.booleanValue() ? "true" : "false");				
				}
			}
			if (this.collapsedImage != null)
			{
				sb.append("\" collapsedImage=\"");
				sb.append(this.collapsedImage.getImagePath());
			}
			if (this.expandedImage != null)
			{
				sb.append("\" expandedImage=\"");
				sb.append(this.expandedImage.getImagePath());
			}
			if (this.selectedImage != null)
			{
				sb.append("\" selectedImage=\"");
				sb.append(this.selectedImage.getImagePath());
			}
			if (this.tooltip != null)
			{
				sb.append("\" tooltip=\"");
				sb.append(ims.framework.utils.StringUtils.encodeXML(this.tooltip));
			}
			sb.append('\"');
			if (this.nodes.size() == 0)
				sb.append("/>");
			else
			{
				sb.append('>');
				this.nodes.render(sb, treeDragDropAllowed);
				sb.append("</node>");
			}			
		}
		public Object clone()
		{
			return this;
		}
		public boolean remove(ims.framework.controls.TreeNode node)
		{
            boolean shouldClearSelection = getSelectedNode() != null && getSelectedNode().equals(node);
            boolean result = this.nodes.remove(node);
            
            if(!this.dataWasChanged)
            	this.dataWasChanged = result;
            
            if(result && shouldClearSelection)
                setSelection("");
            return result;		
		}
		public void setParent(ims.framework.controls.TreeNode newParent) 
		{
			if(this.parent != null)
			{
				this.dataWasChanged = true;
				this.parent.nodes.nodes.remove(this);				
			}
			else
			{
				this.dataWasChanged = true;
				TreeViewData.this.rootNodes.nodes.remove(this);
			}
			
			if(newParent == null)
			{
				this.dataWasChanged = true;
				this.parent = null;
				TreeViewData.this.rootNodes.nodes.remove(this);
				TreeViewData.this.rootNodes.nodes.add(this);
			}
			else
			{
				this.dataWasChanged = true;
				this.parent = (TreeNode)newParent;
				this.parent.nodes.nodes.remove(this);
				this.parent.nodes.nodes.add(this);				
			}
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
		public void sortChildrenByText()
		{
			sortChildrenByText(true, false, false);			
		}
		public void sortChildrenByText(boolean ascending)
		{
			sortChildrenByText(ascending, false, false);			
		}
		public void sortChildrenByText(boolean ascending, boolean recursive)
		{
			sortChildrenByText(ascending, recursive, false);
		}
		public void sortChildrenByText(boolean ascending, boolean recursive, boolean caseSensitive)
		{	
			if(this.nodes != null)
			{
				this.nodes.sortByText(ascending, recursive, caseSensitive);
				
				if(recursive)
				{
					for(int x = 0; x < this.nodes.size(); x++)
					{
						((TreeNode)this.nodes.get(x)).sortChildrenByText(ascending, recursive, caseSensitive);
					}
				}
			}
		}
		public Object getIdentifier()
		{			
			return identifier;
		}
		public void setIdentifier(Object identifier)
		{
			this.identifier = identifier;
		}
		
		private TreeNodeCollection nodes;
		private Object value;
		private String text;
		protected TreeViewData control; 
		private String tooltip;
		private boolean checked = false;
		private boolean expanded = false;
		private boolean checkBoxVisible;
		private boolean enabled = true;		
		private Image collapsedImage = null;
		private Image expandedImage = null;
		private Image selectedImage = null;
		private TreeNode parent;
        private Color textColor = Color.Default;
        private Integer dragDropType;
        private int[] dropToTypes;
        private Boolean allowDragDropSorting;
        private Object identifier = null;		
	}
	
	public void moveUp() 
	{		
		ims.framework.controls.TreeNode node = getSelectedNode();
		if (node == null) 
			return;
		
		Object currVal = node.getValue();
		if (node.getParent() == null) 
			this.rootNodes.moveUp();
		else 
			node.getParent().getNodes().moveUp();
		
		setValue(currVal);
	}
	public void moveDown() 
	{
		ims.framework.controls.TreeNode node = getSelectedNode();
		if (node == null) 
			return;
		
		Object currVal = node.getValue();
		if (node.getParent() == null) 
			this.rootNodes.moveDown();
		else 
			node.getParent().getNodes().moveDown();
		
		setValue(currVal);
	}
	public boolean canMoveCurrentUp() 
	{
		ims.framework.controls.TreeNode node = getSelectedNode();
		if (node == null) 
			return false;
		if (node.getParent() == null) 
			return this.rootNodes.canMoveCurrentUp();

		return node.getParent().getNodes().canMoveCurrentUp();
	}
	public boolean canMoveCurrentDown() 
	{
		ims.framework.controls.TreeNode node = getSelectedNode();
		if (node == null) 
			return false;
		if (node.getParent() == null) 
			return this.rootNodes.canMoveCurrentDown();
		
		return node.getParent().getNodes().canMoveCurrentDown();
	}
	public void moveTo(int index)
	{		
		ims.framework.controls.TreeNode node = getSelectedNode();
		if (node == null) 
			return;
		
		Object currVal = node.getValue();
		if (node.getParent() == null) 
			this.rootNodes.moveTo(index);
		else 
			node.getParent().getNodes().moveTo(index);
		
		setValue(currVal);		
	}
	public void setAllowDragDrop(boolean value) 
	{
		if(!this.allowDragDropChanged)
			this.allowDragDropChanged = this.allowDragDrop != value;
		
		this.allowDragDrop = value;		
	}
	public boolean getDragDropAllowed() 
	{
		return this.allowDragDrop;	
	}
	public TreeNode setNodeText(String selection, String text) 
	{		
		TreeNode node = (TreeNode)this.getNodeByIndex(selection);
		if(node == null)
			return null;
		
		node.setText(text);
		return node;
	}
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setAllowDragDropUnchanged()
	{
		this.allowDragDropChanged = false;
	}
	public boolean isAllowDragDropChanged()
	{
		return allowDragDropChanged;
	}
	public void setUnselectableUnchanged()
	{
		this.unselectableChanged = false;
	}
	public boolean isUnselectableChanged()
	{
		return unselectableChanged;
	}
	public void setSelectionUnchanged()
	{
		this.selectionChanged = false;
	}
	public boolean isSelectionChanged()
	{
		// Server must resend the selection all the time
		return true;
		//return selectionChanged;
	}
	public boolean isNodesChanged()
	{
		if(this.nodes == null)
			return false;

		return this.nodes.wasChanged();
	}
	public void setNodesUnchanged()
	{
		if(this.nodes != null)
			this.nodes.markUnchanged();
	}
	public boolean isRootNodesChanged()
	{
		if(this.rootNodes == null)
			return false;

		return this.rootNodes.wasChanged();
	}
	public void setRootNodesUnchanged()
	{
		if(this.rootNodes != null)
			this.rootNodes.markUnchanged();
	}
}
