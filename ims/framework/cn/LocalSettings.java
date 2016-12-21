package ims.framework.cn;

import javax.servlet.http.HttpSession;

import ims.domain.SessionData;
import ims.domain.factory.LocalSettingsFactory;
import ims.framework.enumerations.LocalSettingsType;
import ims.framework.interfaces.ILocalSettingsProvider;

public final class LocalSettings extends ims.framework.LocalSettings
{
	private ILocalSettingsProvider provider;
	private String uniqueId;
	
	public LocalSettings(HttpSession session, SessionData sessionData)
	{
		uniqueId = sessionData.uniqueClientId.get();
		
		LocalSettingsFactory factory = new LocalSettingsFactory(ims.domain.DomainSession.getSession(session));
		provider = factory.getProvider();
	}
	
	@Override
	public String getWordEditorPath()
	{
		if(provider != null && uniqueId != null)
		{
			return provider.getLocalSetting(uniqueId, LocalSettingsType.WORD_EDITOR_PATH);
		}
		
		return null;
	}
	@Override
	public void setWordEditorPath(String value)
	{
		if(provider != null && uniqueId != null)
		{
			provider.setLocalSetting(uniqueId, LocalSettingsType.WORD_EDITOR_PATH, value);
		}
	}
}
