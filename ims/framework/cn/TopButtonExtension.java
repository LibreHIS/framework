package ims.framework.cn;

import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.FormAccessLevel;
import ims.framework.TopButtonCollection;
import ims.framework.interfaces.ISelectedPatient;

public class TopButtonExtension extends ims.framework.TopButtonExtension
{ 
	private static final int PATIENT_SELECTION_HISTORY_ID = -10000;
	private static final long serialVersionUID = 1L;
	
	protected TopButtonCollection previouslySelectedPatients = new TopButtonCollection();

	public TopButtonExtension(boolean visible)
	{
		super(visible);
	}
	public void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception
	{
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).preRenderContext(contextEvalFactory, formAccessLevel, sessData, currentFormIsReadOnly);
		}
		
		buildPreviouslySelectedPatients(sessData);
	}
	public void render(StringBuffer sb, SessionData sessData)
	{
		boolean isVisible = isVisible();
		int count = 0;
		sb.append("<more visible=\"");
		sb.append(isVisible ? "true" : "false");
		
		if(isVisible)
		{
			sb.append("\" cols=\"");
			sb.append(noColumns);
			
			sb.append("\" colWidth=\"");
			sb.append(columnWidth);
			
			sb.append("\">");	
			
			for(int x = 0; x < super.items.size(); x++)
			{
				count += super.items.get(x).getItems().size();
				super.items.get(x).render(sb);
			}
						
			if(includePatientsSelectionHistory)
				renderPatientSelectionHistory(sb, count > 0);
			
			sb.append("</more>");			
		}
		else
		{
			sb.append("\" />");
		}
	}
	private void renderPatientSelectionHistory(StringBuffer sb, boolean includeLine)
	{		
		if(previouslySelectedPatients.size() == 0)
			return;
		
		sb.append("<section id=\"");
		sb.append(ims.framework.TopButtonSection.PREVIOUS_PATIENTS_SECTION_ID);		
		if(includeLine)
			sb.append("\" caption=\" ");
		else
			sb.append("\" caption=\"");			
		sb.append("\" visible=\"true\" >");				
		
		for(int x = 0; x < previouslySelectedPatients.size(); x++)
		{
			previouslySelectedPatients.get(x).render(sb);
		}
		
		sb.append("</section>");
	}
	public ims.framework.TopButton find(int id)
	{
		for(int x = 0; x < items.size(); x++)
		{
			ims.framework.TopButton result = items.get(x).find(id);
			if(result != null)
				return result;
		}
		
		for(int x = 0; x < previouslySelectedPatients.size(); x++)
		{
			if(previouslySelectedPatients.get(x).getID() == id)
				return previouslySelectedPatients.get(x);
		}
		
		return null;
	}
	private void buildPreviouslySelectedPatients(SessionData sessData)
	{
		if(!shouldRebuildPreviouslySelectedPatients(sessData))
			return;
		
		previouslySelectedPatients.clear();
		
		if(sessData.previouslySelectedPatients.get() == null)
			return;
		
		int index = PATIENT_SELECTION_HISTORY_ID;
		for (ISelectedPatient patient : sessData.previouslySelectedPatients.get())
		{
			TopButton button = new TopButton(index--, patient.getISelectedPatientName());
			button.setIdentifier(patient);
			previouslySelectedPatients.add(button);
		}
	}
	private boolean shouldRebuildPreviouslySelectedPatients(SessionData sessData)
	{
		if(!includePatientsSelectionHistory)
			return false;
		
		TopButtonCollection testPreviouslySelectedPatients = new TopButtonCollection();
		
		if(sessData.previouslySelectedPatients.get() == null)
			return previouslySelectedPatients.size() > 0;			
		
		int index = PATIENT_SELECTION_HISTORY_ID;
		for (ISelectedPatient patient : sessData.previouslySelectedPatients.get())
		{
			TopButton button = new TopButton(index--, patient.getISelectedPatientName());
			button.setIdentifier(patient);
			testPreviouslySelectedPatients.add(button);
		}
		
		if(testPreviouslySelectedPatients.size() != previouslySelectedPatients.size())
			return true;
		
		for(int x = 0; x < previouslySelectedPatients.size(); x++)
		{
			if(((ISelectedPatient)previouslySelectedPatients.get(x).getIdentifier()).getISelectedPatientID() != ((ISelectedPatient)testPreviouslySelectedPatients.get(x).getIdentifier()).getISelectedPatientID())
				return true;
		}
		
		return false;			
	}
	@Override
	public TopButtonCollection getPreviouslySelectedPatients()
	{
		return previouslySelectedPatients;
	}
}
