package ims.framework.cn;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ims.configuration.JNDI;
import ims.domain.SessionData;
import ims.framework.cn.controls.DiaryButtonCommand;
import ims.framework.cn.events.*;
import ims.framework.controls.DrawingControlArea;
import ims.framework.controls.DrawingControlGroup;
import ims.framework.controls.DrawingControlShape;
import ims.framework.enumerations.MouseButton;
import ims.framework.enumerations.Position;
import ims.framework.interfaces.IAppParam;
import ims.framework.interfaces.ISchedulerServlet;
import ims.framework.interfaces.IUploadServlet;
import ims.framework.utils.AppParam;
import ims.framework.utils.Date;
import ims.framework.utils.DateFormat;
import ims.framework.utils.DateTime;
import ims.framework.utils.PartialDate;
import ims.framework.utils.Time;

public class EventParser
{	
	public static String stripNonValidXMLCharacters(String in) 
	{
        StringBuffer out = new StringBuffer();
        char current; 

        if (in == null || ("".equals(in))) 
        	return ""; 
        
        for (int i = 0; i < in.length(); i++) 
        {
            current = in.charAt(i); 
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        
        return out.toString();
    }
	public static EventManager parse(SessionData sessionData, String input) throws SAXException, IOException, ParseException, NumberFormatException, DOMException, NamingException
	{
		input = stripNonValidXMLCharacters(input);
		
		EventManager events = new EventManager();
		
		if(input == null || input.trim().length() == 0)
			throw new RuntimeException("Invalid input to parse");		
				
		DOMParser parser = new DOMParser();
		org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(new java.io.ByteArrayInputStream(input.getBytes("UTF-8")));
		inputSource.setEncoding("UTF-8");
		parser.parse(inputSource);
		Document doc = parser.getDocument();
		
		boolean lightweight = false;
		if(doc.getDocumentElement() != null && doc.getDocumentElement().getAttributes() != null && doc.getDocumentElement().getAttributes().getNamedItem("lightweight") != null)
			lightweight = Boolean.parseBoolean(doc.getDocumentElement().getAttributes().getNamedItem("lightweight").getNodeValue());
		
		NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < nodes.getLength(); ++i)
		{
			Node event = nodes.item(i);
			String name = event.getNodeName();
			if (name.equals("updateTheme"))
			{
				events.add(new UpdateTheme());
			}
			else if (name.equals("empty"))
			{
				if(event.getAttributes().getNamedItem("autolock") != null)
				{
					Integer counter = sessionData.heartBeatCounter.get();	
					//If heartBeatCounter is set increase counter and set it back
					if (counter != null)
					{
						counter ++ ;
						sessionData.heartBeatCounter.set(counter);
					}
				}
				else if(event.getAttributes().getNamedItem("reset") != null)
				{
					sessionData.heartBeatCounter.set(0);
				}
				else
				{
					events.setIsEmptyRequest(true);
				}
			}
			else if (name.equals("ticketReceived"))
			{
				if(event.getAttributes().getNamedItem("id") != null && event.getAttributes().getNamedItem("lastError") != null)
				{		
					events.add(new SmartcardTicketReceivedEvent(event.getAttributes().getNamedItem("id").getNodeValue(),event.getAttributes().getNamedItem("lastError").getNodeValue()));					
				}
			}
			else if (name.equals("uniqueID"))
			{
				if(event.getAttributes().getNamedItem("value") != null)
				{
					sessionData.uniqueClientId.set(event.getAttributes().getNamedItem("value").getNodeValue());
				}
			}
			else if (name.equals("formSizeChanged"))
			{
				// We ignore this event as the auto-resize of form (in GenForm) is not completed (missing column width for grid/dg2)
				// sessData.runtimeClientFormArea.set(new SizeInfo(Integer.parseInt(event.getAttributes().getNamedItem("width").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("height").getNodeValue())));
			}
			else if (name.equals("queryString"))
			{
				List<IAppParam> params = new ArrayList<IAppParam>();
				
				for(int x = 0; x < event.getChildNodes().getLength(); x++)
				{
					Node paramNode = event.getChildNodes().item(x);
					String paramName = paramNode.getAttributes().getNamedItem("name").getNodeValue();
					String paramValue = paramNode.getAttributes().getNamedItem("value").getNodeValue();
					
					if(paramName.toLowerCase().equals("st"))
					{
						// security token parameter
						events.add(new SecurityTokenLoginEvent(paramValue));
					}
					else
					{
						IAppParam appParam = new AppParam(paramName, paramValue); 
						params.add(appParam);
					}
				}
				
				IAppParam[] paramsArray = new IAppParam[params.size()];
				params.toArray(paramsArray);
				events.add(new AppParamEvent(paramsArray));
			}
			else if (name.equals("invalidControl"))
			{
				events.add(new InvalidControlValue(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
			}
			else if (name.equals("scannedDocument"))
			{
				events.add(new ScannedDocumentCustomEvent(event.getAttributes().getNamedItem("file").getNodeValue(), event.getAttributes().getNamedItem("absolutePath").getNodeValue()));
			}
			else if (name.equals("scannedImage"))
			{
				events.add(new ScannedImageCustomEvent(event.getAttributes().getNamedItem("image").getNodeValue(), event.getAttributes().getNamedItem("fileName").getNodeValue()));
			}
			else if (name.equals("uploadedDocument"))
			{
				events.add(new UploadedDocumentCustomEvent(event.getAttributes().getNamedItem("file").getNodeValue(), event.getAttributes().getNamedItem("absolutePath").getNodeValue()));
			}
			else if (name.equals("externalMessage"))
			{
				events.add(new ExternalMessageCustomEvent(event.getAttributes().getNamedItem("title").getNodeValue(), event.getAttributes().getNamedItem("text").getNodeValue()));
			}
			else if (name.equals("scannedImages"))
			{				
				events.add(new ExternalMessageCustomEvent(event.getAttributes().getNamedItem("title").getNodeValue(), event.getAttributes().getNamedItem("text").getNodeValue()));
			}
			else if (name.equals("onCustomUrlWindowChanged"))
			{				
				events.add(new CustomUrlWindowChangedEvent(event.getAttributes().getNamedItem("eventName").getNodeValue()));
			}		
			else if (name.equals("externalCustomEvent"))
			{
				events.add(new ExternalCustomEvent((event.getAttributes().getNamedItem("name") != null ? event.getAttributes().getNamedItem("name").getNodeValue() : null), 
						(event.getAttributes().getNamedItem("action") != null ? event.getAttributes().getNamedItem("action").getNodeValue() : null), 
							(event.getAttributes().getNamedItem("value") != null ? event.getAttributes().getNamedItem("value").getNodeValue() : null)));
			}
			else if (name.startsWith("grid"))
			{
				if (name.equals("gridselection"))
				{
					int selectedRowIndex = Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue());
					if (selectedRowIndex < 0)
						events.add(new GridRowSelectionCleared(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
					else
						events.add(new GridSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), selectedRowIndex, MouseButton.parse(event.getAttributes().getNamedItem("button").getNodeValue())));
				}
				else if (name.equals("gridcheckbox"))
					events.add(new GridCheckBoxClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), event.getAttributes().getNamedItem("value").getNodeValue().equals("true")));
				else if (name.equals("gridcombobox"))
				{
					if (event.getAttributes().getNamedItem("value") != null) // selection changed
						events.add(new GridComboBoxSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
				}
				else if (name.equals("griddatebox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue();
					Date value = tmp == "" ? null : new Date(tmp);
					events.add(new GridDateControlChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridpartialdatebox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue();
					PartialDate value = tmp == "" ? null : new PartialDate(tmp);
					events.add(new GridPartialDateControlChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("griddecimalbox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue();
					Float value = tmp == "" ? null : new Float(tmp);
					events.add(new GridDecimalBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridintbox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue();
					Integer value = tmp == "" ? null : new Integer(tmp);
					events.add(new GridIntBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridwraptext"))
				{
					String tmp = event.getChildNodes().item(0) == null ? "" : event.getChildNodes().item(0).getNodeValue();

					// String tmp = event.getAttributes().getNamedItem("value").getNodeValue();
					String value = tmp == "" ? null : tmp;
					events.add(new GridWrapTextChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridcomment"))
				{
					String tmp = event.getChildNodes().item(0) == null ? "" : event.getChildNodes().item(0).getNodeValue();
					String value = tmp == "" ? null : tmp;
					events.add(new GridCommentChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridtextbox"))
				{
					String value = event.getAttributes().getNamedItem("value").getNodeValue();
					events.add(new GridTextBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value));
				}
				else if (name.equals("gridtimebox"))
				{
					String timeValue = event.getAttributes().getNamedItem("value").getNodeValue();
					events.add(new GridTimeBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), timeValue.length() == 0 ? null : new Time(timeValue)));
				}
				else if (name.equals("gridnode"))
					events.add(new GridNodeExpanded(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), event.getAttributes().getNamedItem("expanded").getNodeValue().equals("true")));
				else if (name.equals("gridanswerbox"))
					events.add(new GridAnswerBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
				else if (name.equals("gridbutton"))
					events.add(new GridButtonClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue())));
				else if (name.equals("gridheaderclick"))
					events.add(new GridHeaderClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue())));
				else if (name.equals("gridmutablecombobox"))
				{
					if (event.getAttributes().getNamedItem("value") != null) // selection changed
						events.add(new GridMutableComboBoxSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
					if (event.getAttributes().getNamedItem("text") != null) // text submited
						events.add(new GridQueryComboBoxTextSubmited(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), event.getAttributes().getNamedItem("text").getNodeValue()));

				}
				else if (name.equals("gridmutableanswerbox"))
					events.add(new GridMutableAnswerBoxSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
			}
			else if (name.startsWith("tree"))
			{
				if (name.equals("treeview"))
				{
					if (event.getAttributes().getNamedItem("selection").getNodeValue().trim().equals("-1"))
					{
						events.add(new TreeViewUnselected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
					}
					else
					{
						events.add(new TreeViewSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("selection").getNodeValue()));
					}
				}
				else if (name.equals("treenode"))
				{
					events.add(new TreeNodeExpanded(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("node").getNodeValue(), event.getAttributes().getNamedItem("expanded").getNodeValue().equals("true")));
				}
				else if (name.equals("treenodecheckbox"))
					events.add(new TreeNodeChecked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("node").getNodeValue(), event.getAttributes().getNamedItem("checked").getNodeValue().equals("true")));
				else if (name.equals("treenodedropped"))
					events.add(new TreeNodeDropped(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("node").getNodeValue(), event.getAttributes().getNamedItem("newIndex").getNodeValue()));
				else if (name.equals("treenodeedited"))
					events.add(new TreeNodeEdited(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("node").getNodeValue(), event.getAttributes().getNamedItem("text").getNodeValue()));
			}
			else
			{
				if (name.equals("textbox"))
				{
					String value = event.getChildNodes().item(0) == null ? "" : event.getChildNodes().item(0).getNodeValue();
					String selectedText = null;
					if (event.getAttributes().getNamedItem("selectedText") != null)
						selectedText = event.getAttributes().getNamedItem("selectedText").getNodeValue();
					events.add(new TextBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value, selectedText));
				}
				else if (name.equals("combobox"))
				{
					if (event.getAttributes().getNamedItem("search") != null)
					{
						// search
						if (event.getAttributes().getNamedItem("value") != null)
						{
							// search was made on text
							events.add(new ComboBoxSearch(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("value").getNodeValue(), null));
						}
						else
						{
							// search was made on selected value
							events.add(new ComboBoxSearch(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), null, new Integer(Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue()))));
						}
					}
					else
					{
						// change
						if (event.getAttributes().getNamedItem("selection") != null)
						{
							// selection changed
							events.add(new ComboBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue())));
						}
						else
						{
							// text changed
							events.add(new ComboBoxTextChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("value").getNodeValue()));
						}
					}
				}
				else if (name.equals("checkbox"))
					events.add(new CheckBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("checked").getNodeValue().equals("true")));
				else if (name.equals("answerbox"))
					events.add(new AnswerBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue())));
				else if (name.equals("radiobox"))
					events.add(new RadioBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue())));
				else if (name.equals("htmlviewerclicked"))
					events.add(new HTMLViewerClick(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("link").getNodeValue())));
				else if (name.equals("htmlviewersize"))
				{
					events.add(new HTMLViewerResized(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("width").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("height").getNodeValue())));
				}
				else if (name.equals("datebox"))
				{
					boolean isDateTime = event.getAttributes().getNamedItem("time") != null && Boolean.valueOf(event.getAttributes().getNamedItem("time").getNodeValue()).booleanValue();
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue().trim();

					if (isDateTime)
					{
						DateTime value = tmp == "" ? null : new DateTime(tmp);
						events.add(new DateTimeControlChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
					}
					else
					{
						Date value = tmp == "" ? null : new Date(tmp);
						events.add(new DateControlChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
					}
				}
				else if (name.equals("timebox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue().trim();
					Time value = tmp == "" ? null : new Time(tmp);
					events.add(new TimeControlChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
				}
				else if (name.equals("intbox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue().trim();
					Integer value = tmp == "" ? null : new Integer(tmp);
					events.add(new IntBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
				}
				else if (name.equals("decimalbox"))
				{
					String tmp = event.getAttributes().getNamedItem("value").getNodeValue().trim();
					Float value = tmp == "" ? null : new Float(tmp);
					events.add(new DecimalBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
				}
				else if (name.equals("drawingcontrolvml"))
					events.add(new DrawingControlVMLChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), new DrawingControlShape(Integer.parseInt(event.getAttributes().getNamedItem("shape").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("targetID").getNodeValue()), event.getChildNodes().item(0).getNodeValue(), Integer.parseInt(event.getAttributes().getNamedItem("brushID").getNodeValue()))));
				else if (name.equals("drawingcontrolremove"))
					events.add(new DrawingControlShapeRemoved(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("shape").getNodeValue())));
				else if (name.equals("drawingcontroledit"))
					events.add(new DrawingControlShapeEdited(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("shape").getNodeValue())));
				else if (name.equals("drawingcontrolnote"))
					events.add(new DrawingControlNote(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("shape").getNodeValue())));
				else if (name.equals("drawingconfigcontrol"))
				{
					DrawingControlGroup root = parseDrawingControlGroup(event.getChildNodes().item(0));
					events.add(new DrawingControlConfiguratorChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), root));
				}

				// immediate post back
				else if (name.equals("button") || name.equals("imagebutton") || name.equals("link"))
					events.add(new ButtonClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
				else if (name.equals("navigation"))
				{
					if(event.getAttributes().getNamedItem("form") != null)
					{
						int tmp = Integer.parseInt(event.getAttributes().getNamedItem("form").getNodeValue());
						if (tmp != 0)
							events.add(new NavigationSelected(tmp));
					}
					if(event.getAttributes().getNamedItem("collapsed") != null)
					{
						boolean tmp = Boolean.parseBoolean((event.getAttributes().getNamedItem("collapsed").getNodeValue()));
						sessionData.navigationCollapsed.set(tmp);
					}
				}
				else if (name.equals("openform"))
				{
					int tmp = Integer.parseInt(event.getAttributes().getNamedItem("form").getNodeValue());
					if (tmp != 0)
						events.add(new OpenFormEvent(tmp));
				}
				else if (name.equals("topbutton"))
					events.add(new TopButtonClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue())));
				else if (name.equals("timeline"))
					events.add(new TimeLineClick(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("lineID").getNodeValue()), new Date(event.getAttributes().getNamedItem("date").getNodeValue(), DateFormat.RUSSIAN)));
				else if (name.equals("bookingcalendar"))
					events.add(new BookingCalendarEvent(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
				else if (name.equals("availabilitycontrolclick"))
					events.add(new AvailabilityControlClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("value").getNodeValue())));
				else if (name.equals("partialdatebox"))
				{
					Node tmp = event.getAttributes().getNamedItem("year");
					Integer year = tmp == null ? null : new Integer(tmp.getNodeValue());
					tmp = event.getAttributes().getNamedItem("month");
					Integer month = tmp == null ? null : new Integer(tmp.getNodeValue());
					tmp = event.getAttributes().getNamedItem("day");
					Integer day = tmp == null ? null : new Integer(tmp.getNodeValue());

					PartialDate value = null;
					if (year != null)
						value = new PartialDate(year, month, day);

					events.add(new PartialDateBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), value));
				}
				else if (name.equals("rangebox"))
				{
					events.add(new RangeBoxChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getAttributes().getNamedItem("minValue").getNodeValue(), event.getAttributes().getNamedItem("maxValue").getNodeValue()));
				}
				else if (name.equals("alert"))
				{
					boolean fromDialog = false;
					Node fromDialogNode = event.getAttributes().getNamedItem("fromDialog");
					if(fromDialogNode != null)
						fromDialog = Boolean.parseBoolean(fromDialogNode.getNodeValue());
					
					events.add(new AlertEvent(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue()), fromDialog));
				}
				else if (name.equals("riealert"))
				{
					events.add(new RIEAlertEvent());
				}
				else if (name.equals("autoLock"))
				{
					events.add(new AutoScreenLockEvent());
				}
				else if (name.equals("storedLocations"))
				{
					StoredLocationsEvent storedLocationsEvent = new StoredLocationsEvent(event.getAttributes().getNamedItem("selection") != null ? Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue()) : -2);

					for (int x = 0; x < event.getChildNodes().getLength(); x++)
					{
						int id = Integer.parseInt(event.getChildNodes().item(x).getAttributes().getNamedItem("id").getNodeValue());
						storedLocationsEvent.add(id, event.getChildNodes().item(x).getFirstChild().getNodeValue());
					}

					events.add(storedLocationsEvent);
				}
				// redirection events
				else if (name.equals("loginEvent"))
				{
					events.add(new LoginEvent(event.getAttributes().getNamedItem("name").getNodeValue(), event.getAttributes().getNamedItem("pass").getNodeValue()));
				}
				else if (name.equals("securityTokenLogin"))
				{
					events.add(new SecurityTokenLoginEvent(event.getAttributes().getNamedItem("st").getNodeValue()));
				}
				else if(name.equals("passwordDialog"))
				{
					events.add(new SystemPasswordClosed(Boolean.parseBoolean(event.getAttributes().getNamedItem("canceled").getNodeValue())));
				}
				else if (name.equals("newPassword"))
				{
					Node node = event.getAttributes().getNamedItem("pass");
					events.add(new NewPasswordEvent(event.getAttributes().getNamedItem("canceled").getNodeValue().equals("true"), node == null ? null : node.getNodeValue()));
				}
				else if (name.equals("unlock"))
				{
					events.add(new ScreenUnlock(event.getAttributes().getNamedItem("name").getNodeValue(), event.getAttributes().getNamedItem("pass").getNodeValue()));
				}
				else if (name.equals("selectRoleEvent"))
				{
					events.add(new SelectRoleEvent(Integer.parseInt(event.getAttributes().getNamedItem("roleID").getNodeValue())));
				}
				else if (name.equals("selectLocationEvent"))
				{
					events.add(new SelectLocationEvent(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue())));
				}
				else if (name.equals("dialogWasClosed"))
				{
					int id = Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue());
					boolean byServer = new Boolean(event.getAttributes().getNamedItem("id").getNodeValue()).booleanValue();
					events.add(new DialogWasClosed(id, byServer));
				}
				else if (name.equals("logout"))
					events.add(new LogoutEvent(event.getAttributes().getNamedItem("source").getNodeValue().equals("IE")));
				else if (name.equals("floorplannerplan"))
					events.add(new FloorPlannerPlanChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), event.getChildNodes().item(0).getNodeValue()));
				else if (name.equals("floorplannerinfo"))
					events.add(new FloorPlannerNote(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("areaID").getNodeValue())));
				else if (name.equals("floorplannernewarea"))
					events.add(new FloorPlannerNewArea(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("areaID").getNodeValue()), event.getAttributes().getNamedItem("path").getNodeValue()));
				else if (name.equals("floorplannerremovearea"))
					events.add(new FloorPlannerRemoveArea(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("areaID").getNodeValue())));
				else if (name.equals("bedplannernewbed"))
					events.add(new BedPlannerNewBed(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("bedID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("type").getNodeValue()), event.getChildNodes().item(0).getNodeValue(), Position.parse(event.getAttributes().getNamedItem("textPos").getNodeValue()), Position.parse(event.getAttributes().getNamedItem("imgPos").getNodeValue())));
				else if (name.equals("bedplannerremovebed"))
					events.add(new BedPlannerRemoveBed(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("bedID").getNodeValue())));
				else if (name.equals("bedplannerinfo"))
					events.add(new BedPlannerBedClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("bedID").getNodeValue()), true));
				else if (name.equals("bedplannerattachedimageclick"))
					events.add(new BedPlannerBedAttachedImageClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("bedID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("imageID").getNodeValue())));
				else if (name.equals("bedplanneredit"))
					events.add(new BedPlannerBedClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("bedID").getNodeValue()), false));
				else if (name.equals("graphingcontrolpointclick"))
					events.add(new GraphingControlClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("pointID").getNodeValue())));
				else if (name.equals("containertabselected"))
					events.add(new TabActivated(Integer.parseInt(event.getAttributes().getNamedItem("containerID").getNodeValue().substring(1))));
				else if (name.equals("collapsablecontainer"))
					events.add(new TabCollapsedOrExpanded(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Boolean.parseBoolean(event.getAttributes().getNamedItem("collapsed").getNodeValue())));
				else if (name.equals("menuclick"))
					events.add(new MenuItemClick(Integer.parseInt(event.getAttributes().getNamedItem("controlID").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("menuID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("menuItemID").getNodeValue())));
				else if (name.equals("richtextglossaryclick"))
					events.add(new RichTextControlGlossary(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
				else if (name.equals("wysiwygdocument"))
				{
					String data = null;
					if (event.getChildNodes() != null && event.getChildNodes().item(0) != null)
						data = event.getChildNodes().item(0).getNodeValue();
					events.add(new RichTextValueChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), data));
				}
				else if (name.equals("wysiwygspellcheck"))
				{
					String data = null;
					if (event.getChildNodes() != null && event.getChildNodes().item(0) != null)
						data = event.getChildNodes().item(0).getNodeValue();
					events.add(new RichTextSpellCheck(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), data));
				}
				else if(name.equals("wysiwygaddtodictionary"))
				{
					String data = null;
					if (event.getChildNodes() != null && event.getChildNodes().item(0) != null)
						data = event.getChildNodes().item(0).getNodeValue();
					events.add(new RichTextAddToDictionary(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), data));
				}
				else if (name.equals("dynamicgrid2selection"))
				{
					int selectedRowIndex = Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue());
					if (selectedRowIndex < 0)
						events.add(new DynamicGridRowSelectionCleared(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1))));
					else
						events.add(new DynamicGridRowSelectionChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), selectedRowIndex, Integer.parseInt(event.getAttributes().getNamedItem("selectedRowID").getNodeValue()), MouseButton.parse(event.getAttributes().getNamedItem("button").getNodeValue())));
				}
				else if (name.equals("dynamicgrid2button") || name.equals("dynamicgrid2imagebutton"))
					events.add(new DynamicGridCellButtonClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue())));
				else if (name.equals("dynamicgrid2mutablecombobox") || name.equals("dynamicgrid2mutableanswerbox") || name.equals("dynamicgrid2answerbox"))
				{
					String value = null;
					String text = null;
					if (event.getAttributes().getNamedItem("value") != null)
						value = event.getAttributes().getNamedItem("value").getNodeValue();
					if (event.getAttributes().getNamedItem("text") != null)
						text = event.getAttributes().getNamedItem("text").getNodeValue();

					if (value != null)
						events.add(new DynamicGridCellValueChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), value, text));
					else if (text != null)
						events.add(new DynamicGridCellTextSubmited(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), text));
				}
				else if (name.equals("dynamicgrid2textbox") || name.equals("dynamicgrid2datebox") || name.equals("dynamicgrid2intbox") || name.equals("dynamicgrid2timebox") || name.equals("dynamicgrid2decimalbox") || name.equals("dynamicgrid2checkbox") || name.equals("dynamicgrid2partialdatebox")  || name.equals("dynamicgrid2datetimebox"))
				{
					String text = null;
					if (event.getAttributes().getNamedItem("value") != null)
						text = event.getAttributes().getNamedItem("value").getNodeValue();

					events.add(new DynamicGridCellValueChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), null, text));
				}
				else if (name.equals("dynamicgrid2wraptext") || name.equals("dynamicgrid2comment"))
				{
					String tmp = event.getChildNodes().item(0) == null ? "" : event.getChildNodes().item(0).getNodeValue();
					String text = tmp == "" ? null : tmp;
					events.add(new DynamicGridCellValueChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), null, text));
				}
				else if (name.equals("dynamicgrid2table"))
				{
					events.add(new DynamicGridCellButtonClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("tableRowId").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("tableCellId").getNodeValue())));
				}
				else if (name.equals("dynamicgrid2headerclick"))
				{
					events.add(new DynamicGridColumnHeaderClicked(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue())));
				}				
				else if (name.equals("dynamicgrid2checkedlistbox"))
				{
					if (event.getChildNodes() != null)
					{
						int controlID = Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1));
						int row = Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue());
						int rowID = Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue());
						int column = Integer.parseInt(event.getAttributes().getNamedItem("column").getNodeValue());

						for (int x = 0; x < event.getChildNodes().getLength(); x++)
						{
							Node child = event.getChildNodes().item(x);

							if (child.getNodeName().equals("item"))
							{
								int itemIndex = Integer.parseInt(child.getAttributes().getNamedItem("index").getNodeValue());
								boolean checked = new Boolean(child.getAttributes().getNamedItem("checked").getNodeValue()).booleanValue();

								events.add(new DynamicGridCellItemChecked(controlID, row, rowID, column, itemIndex, checked));
							}
						}
					}
				}
				else if (name.equals("recordnavigator"))
				{
					events.add(new RecordBrowserChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selectedID").getNodeValue())));
				}
				else if (name.equals("dynamicgrid2node"))
				{
					boolean expanded = false;
					if (event.getAttributes().getNamedItem("expanded") != null)
						expanded = Boolean.valueOf((event.getAttributes().getNamedItem("expanded").getNodeValue())).booleanValue();

					boolean checked = false;
					if (event.getAttributes().getNamedItem("checked") != null)
						checked = Boolean.valueOf((event.getAttributes().getNamedItem("checked").getNodeValue())).booleanValue();

					events.add(new DynamicGridNodeExpandedCollapsed(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("row").getNodeValue()), Integer.parseInt(event.getAttributes().getNamedItem("rowID").getNodeValue()), expanded, checked));
				}
				else if (name.equals("uploadbox"))
				{				
					if (event.getAttributes().getNamedItem("filePosted") != null &&
							Boolean.parseBoolean(event.getAttributes().getNamedItem("filePosted").getNodeValue()) == false)
					{
						events.add(new FileSelected(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), ims.configuration.JNDI.getUploadedServlet().getUploadedFilename(event.getAttributes().getNamedItem("uniqueID").getNodeValue())));
					}
					else
					{
						events.add(new FileUploaded(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), ims.configuration.JNDI.getUploadedServlet().getUploadedFilename(event.getAttributes().getNamedItem("uniqueID").getNodeValue())));						
					}
				}
				else if (name.equals("checkedlistbox"))
				{
					int id = Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1));
					int selectedIndex = -2;
					if (event.getAttributes().getNamedItem("selectedIndex") != null)
						selectedIndex = Integer.parseInt(event.getAttributes().getNamedItem("selectedIndex").getNodeValue());

					if (event.getChildNodes() == null)
					{
						events.add(new CheckedListBoxChanged(id, selectedIndex));
					}
					else
					{
						CheckedListBoxItemChanged[] items = new CheckedListBoxItemChanged[event.getChildNodes().getLength()];

						for (int x = 0; x < event.getChildNodes().getLength(); x++)
						{
							Node itemNode = event.getChildNodes().item(x);
							items[x] = new CheckedListBoxItemChanged(Integer.parseInt(itemNode.getAttributes().getNamedItem("index").getNodeValue()), Boolean.valueOf(itemNode.getAttributes().getNamedItem("checked").getNodeValue()).booleanValue());
						}

						events.add(new CheckedListBoxChanged(id, selectedIndex, items));
					}
				}
				else if (name.equals("timer"))
				{
					events.add(new TimerEvent(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue())));
				}
				else if (name.equals("flash"))
				{	
					Object flashEvent = parseFlashEvent(event);
					if(flashEvent != null)
					{
						events.add(flashEvent);
					}
				}
				else if (name.equals("messagebox"))
				{
					events.add(new MessageBoxEvent(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue()), event.getAttributes().getNamedItem("button").getNodeValue()));
				}
				else if (name.equals("imsjourney"))					
				{
					events.add(new PatientJourneySelectionChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selection").getNodeValue())));					
				}
				else if (name.equals("diary"))					
				{
					if (event.getAttributes().getNamedItem("selectedEventID") != null)
						events.add(new DiarySelectionChanged(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), Integer.parseInt(event.getAttributes().getNamedItem("selectedEventID").getNodeValue())));					
					if (event.getAttributes().getNamedItem("button") != null)
						events.add(new DiaryButtonPressed(Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1)), DiaryButtonCommand.parse(event.getAttributes().getNamedItem("button").getNodeValue())));
				}
				else if (name.equals("search"))									
				{
					if(event.getAttributes().getNamedItem("iID") != null)
					{
						events.add(new SearchResultSelectionEvent(Integer.parseInt(event.getAttributes().getNamedItem("iID").getNodeValue())));
					}
					else
					{
						if(event.getAttributes().getNamedItem("liveText") != null)
							events.add(new SearchEvent(lightweight, event.getAttributes().getNamedItem("liveText").getNodeValue()));
						else if(event.getAttributes().getNamedItem("text") != null)
							events.add(new SearchEvent(lightweight, event.getAttributes().getNamedItem("text").getNodeValue()));
					}
				}
			}
		}
		
		return events;
	}	
	private static DrawingControlGroup parseDrawingControlGroup(Node root)
	{
		DrawingControlGroup result = new DrawingControlGroup(Integer.parseInt(root.getAttributes().getNamedItem("id").getNodeValue()), root.getAttributes().getNamedItem("name").getNodeValue());
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i)
		{
			Node child = children.item(i);
			if (child.getNodeName().equals("group"))
				result.add(parseDrawingControlGroup(child));
			else
				result.add(new DrawingControlArea(Integer.parseInt(child.getAttributes().getNamedItem("id").getNodeValue()), child.getAttributes().getNamedItem("name").getNodeValue(), child.getAttributes().getNamedItem("path").getNodeValue()));
		}
		return result;
	}	
	private static Object parseFlashEvent(Node event) throws SAXException, IOException
	{
		int id = Integer.parseInt(event.getAttributes().getNamedItem("id").getNodeValue().substring(1));
		if(event.getFirstChild() != null && event.getFirstChild().getNodeValue() != null)
		{
			String cdata = event.getFirstChild().getNodeValue();
			
			DOMParser parser = new DOMParser();
			parser.parse(new org.xml.sax.InputSource(new java.io.ByteArrayInputStream(cdata.getBytes())));
			Document doc = parser.getDocument();
			String name = doc.getDocumentElement().getNodeName();
			
			if (name.equals("removeImage"))
			{
				return new CameraImageRemoved(id);
			}		
			else if (name.equals("captureImage"))
			{
				String image = doc.getDocumentElement().getAttribute("image");
				return new CameraImageCaptured(id, image);
			}			
		}
		
		return null;
	}
}
