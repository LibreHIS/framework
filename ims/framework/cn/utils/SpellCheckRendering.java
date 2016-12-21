package ims.framework.cn.utils;

import ims.framework.interfaces.ISpellCheckError;
import ims.framework.interfaces.ISpellCheckResult;

public class SpellCheckRendering
{
	public static StringBuffer render(StringBuffer sb, ISpellCheckResult spellCheckResult)
	{
		if(spellCheckResult == null)
			throw new RuntimeException("Invalid spell checker result");
		if(sb == null)
			throw new RuntimeException("Invalid spell checker destination string buffer");
		
		sb.append("<spellcheck>");
		
		ISpellCheckError[] errors = spellCheckResult.getErrors();
		for (int i = 0; i < errors.length; i++) 
		{
			sb.append("<word value=\"" + ims.framework.utils.StringUtils.encodeXML(errors[i].getErrorWord())+"\">");
			int sugestions = errors[i].getSuggestedWords().length;
			for (int j = 0; j < sugestions; j++) 
			{
				sb.append("<s>" + ims.framework.utils.StringUtils.encodeXML(errors[i].getSuggestedWords()[j])+"</s>");
			}
			sb.append("</word>");
		}
			
		sb.append("</spellcheck>");
		
		return sb;
	}	
}
