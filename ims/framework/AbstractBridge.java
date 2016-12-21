package ims.framework;

import java.io.Serializable;
import java.util.ArrayList;

import ims.framework.controls.AnswerBoxBridge;
import ims.framework.controls.CheckedListBoxBridge;
import ims.framework.controls.ComboBoxBridge;
import ims.framework.controls.GridBridge;
import ims.framework.controls.RadioButtonBridge;
import ims.framework.controls.RecordBrowserBridge;

abstract public class AbstractBridge implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	
	void freeCollections()
	{
		this.containers.clear();

		this.grids.clear();

		this.comboboxes.clear();
		
		this.recordbrowsers.clear();
		
		this.checkedListBoxes.clear();

		this.layers.clear();

		this.radioButtons.clear();

		this.answerBoxes.clear();

		if (this.formReferences != null)
		{
			this.formReferences = null;
		}
		if (this.imageReferences != null)
		{
			this.imageReferences = null;
		}
		if (this.globalContext != null)
		{
			this.globalContext = null;
		}
		if (this.localContext != null)
		{
			this.localContext = null;
		}
	}
	abstract protected void addControl(Control control);

	protected void addContainer(ContainerBridge container)
	{
		this.containers.add(container);
	}
	protected Object getContainer(int index)
	{
		return this.containers.get(index);
	}
	protected void addGrid(GridBridge value)
	{
		this.grids.add(value);
	}
	protected Object getGrid(int index)
	{
		return this.grids.get(index);
	}
	protected void addComboBox(ComboBoxBridge value)
	{
		this.comboboxes.add(value);
	}
	protected void addRecordBrowser(RecordBrowserBridge value)
	{
		this.recordbrowsers.add(value);
	}
	protected void addCheckedListBox(CheckedListBoxBridge value)
	{
		this.checkedListBoxes.add(value);
	}
	protected Object getCheckedListBox(int index)
	{
		return this.checkedListBoxes.get(index);
	}
	protected Object getComboBox(int index)
	{
		return this.comboboxes.get(index);
	}
	protected Object getRecordBrowser(int index)
	{
		return this.recordbrowsers.get(index);
	}
	protected void addLayer(Layer value)
	{
		this.layers.add(value);
	}
	protected Object getLayer(int index)
	{
		return this.layers.get(index);
	}
	protected void addRadioButton(RadioButtonBridge value)
	{
		this.radioButtons.add(value);
	}
	protected Object getRadioButton(int index)
	{
		return this.radioButtons.get(index);
	}
	// Answer box
	protected void addAnswerBox(AnswerBoxBridge value)
	{
		this.answerBoxes.add(value);
	}
	protected Object getAnswerBox(int index)
	{
		return this.answerBoxes.get(index);
	}

	protected void setFormReferences(Object references)
	{
		this.formReferences = references;
	}
	protected Object getFormReferences()
	{
		return this.formReferences;
	}

	protected void setImageReferences(Object references)
	{
		this.imageReferences = references;
	}
	protected Object getImageReferences()
	{
		return this.imageReferences;
	}

	protected void setGlobalContext(ContextBridge context)
	{
		this.globalContext = context;
	}
	protected ContextBridge getGlobalCtx()
	{
		return this.globalContext;
	}
	protected void setLocalContext(ContextBridge context)
	{
		this.localContext = context;
	}
	protected ContextBridge getLocalCtx()
	{
		return this.localContext;
	}

	private ArrayList<ContainerBridge> containers = new ArrayList<ContainerBridge>();
	private ArrayList<GridBridge> grids = new ArrayList<GridBridge>();
	private ArrayList<ComboBoxBridge> comboboxes = new ArrayList<ComboBoxBridge>();
	private ArrayList<RecordBrowserBridge> recordbrowsers = new ArrayList<RecordBrowserBridge>();
	private ArrayList<CheckedListBoxBridge> checkedListBoxes = new ArrayList<CheckedListBoxBridge>();
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private ArrayList<RadioButtonBridge> radioButtons = new ArrayList<RadioButtonBridge>();
	private ArrayList<AnswerBoxBridge> answerBoxes = new ArrayList<AnswerBoxBridge>();
	private Object formReferences = null;
	private Object imageReferences = null;
	private ContextBridge globalContext = null;
	private ContextBridge localContext = null;
}
