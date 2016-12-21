package ims.framework.cn;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpSession;

import ims.domain.SessionData;
import ims.framework.FormAccessLogic;
import ims.framework.FormName;
import ims.framework.SessionConstants;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IFormAccessValidator;

public final class FormAccessLoader extends ims.framework.FormAccessLoader implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final class ConcreteFormName extends FormName
	{
		private static final long serialVersionUID = 1L;
		
		public ConcreteFormName(int value)
		{
			super(value);
		}
		public ConcreteFormName(int value, String name)
		{
			super(value, name);
		}
	}
	
	public FormAccessLoader(HttpSession session)
	{
		this.session = session;
	}
		
	public final IFormAccessValidator getFormAccessValidator(int value) throws Exception
	{
		SessionData sessData = (SessionData) this.session.getAttribute(SessionConstants.SESSION_DATA);
		Map map = sessData.configurationModule.get().getRegisteredForms();		
		return getFormAccessValidator((IAppForm) map.get(new Integer(value)));
	}
	public final IFormAccessValidator getFormAccessValidator(IAppForm form) throws Exception
	{
		if(form == null)
			return null;
		
		String accessLogicName = form.getGenFormPackageName().concat(".AccessLogic");
		FormAccessLogic logic = UILogicFactory.getInstance().createFormAccessLogic(accessLogicName, form.getFormId());				
		
		if(logic == null)
			return null;
		
		logic.setContext(new Context(this.session), new ConcreteFormName(form.getFormId(), form.getName()));
		
		return logic;
	}
	
	private transient HttpSession session;
}
