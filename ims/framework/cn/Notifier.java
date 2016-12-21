/*
 * Created on 01-Sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ims.framework.cn;

import ims.configuration.ConfigFlag;

import java.io.Serializable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author jmacenri
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Notifier extends Thread  implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String fatalMessage = "";

	public Notifier(String fatalMessage)
	{
		this.fatalMessage = fatalMessage;
	}
	
	public void run()
	{
		try 
		{
			Properties props = new Properties();
			if (!ConfigFlag.FW.MAIL_FATAL.getValue())
			{
				return;
			}
			props.put("mail.smtp.host", ConfigFlag.FW.SMTP_SERVER.getValue());
			if (ConfigFlag.FW.SMTP_AUTH.getValue().length() > 0)
			{
				props.put("mail.smtp.auth", "true");
			}
			
			Session session = Session.getDefaultInstance(props, new ImsAuthenticator());
	
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ConfigFlag.FW.SMTP_SERVER.getValue() + ".FW" + "@" + ConfigFlag.FW.DOMAIN.getValue()));
	
			StringTokenizer st = new StringTokenizer(ConfigFlag.FW.FATAL_TO.getValue(),",;");
			//Set up array of recipient addresses
			InternetAddress[] recipients = new InternetAddress[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens()) {
				recipients[i] = new InternetAddress(st.nextToken());
				i++;
			}
	
			message.addRecipients(Message.RecipientType.TO, recipients);
	
			message.setSubject("Fatal Error: " + ConfigFlag.FW.SMTP_SERVER.getValue());
	
			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
	
			//String thisMachine = InetAddress.getLocalHost().getHostName();
			// Fill the message
			messageBodyPart.setText(this.fatalMessage);
	
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
		
			// Put parts in message
			message.setContent(multipart);
	
			Transport.send(message);	
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public class ImsAuthenticator extends Authenticator
	{
		protected PasswordAuthentication getPasswordAuthentication()
		{
			String username = "";
			String password = "";
			StringTokenizer st = new StringTokenizer(ConfigFlag.FW.SMTP_AUTH.getValue(), ":");
			if (st.countTokens() == 2)
			{
				username = st.nextToken();
				password = st.nextToken();				
			}
			return new PasswordAuthentication(username,password);
		}
	}

}
