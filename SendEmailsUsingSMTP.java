/**
 * 
 */
package com.plash.testkamleshWork;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * The Class OtherEmailIntegrationUtils.
 *
 * @author Kamlesh
 */
public class SendEmailsUsingSMTP { 
	
	static Properties props = System.getProperties();
    static Session l_session = null;
	
	/**
	 * Connect and send smtp.
	 *
	 * @param serverName
	 *            the server name / host name
	 * @param portNo
	 *            the port number 
	 * @param secureConnection
	 *            the secure connection i.e, STARTTLS or STARTSSL settings keep true or false or never
	 * @param userName
	 *            the user name / User Email id use for login
	 * @param password
	 *            the password of email account
	 * @param toEmail
	 *            the to email / Email address of receiver
	 * @param subject
	 *            the subject of email
	 * @param msg
	 *            the message body of email
	 * @return the string which gives response for email is send or not
	 */
	public String connectAndSendSmtp(String serverName, String portNo, String secureConnection, String userName, String password, String toEmail, String subject, String msg, String[] attachFiles){
		
		System.out.println("Inside connectAndSendSmtp method: ");
	       emailSettings(serverName, portNo, secureConnection);
	       createSession(userName, password);
	       String issend = sendMessage(userName, toEmail,subject,msg, attachFiles);
	        return issend;
	}

	public void emailSettings(String host, String  port, String secureCon) {
	    	System.out.println("Inside emailSettings: ");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.debug", "false");
	        props.put("mail.smtp.port", port);
	        if(secureCon.equalsIgnoreCase("tls")){
	        	props.put("mail.smtp.starttls.enable", "true");	
	        }else if(secureCon.equalsIgnoreCase("ssl")){
	        	props.put("mail.smtp.startssl.enable", "true");  // make this true if port=465
	        }else{
	        	props.put("mail.smtp.starttls.enable", "false");
	        	props.put("mail.smtp.startssl.enable", "false");
	        }
	        props.put("mail.smtp.socketFactory.port", port);
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.put("mail.smtp.socketFactory.fallback", "false");

	    }
	 
	public void createSession(final String username, final String pass) {

		 System.out.println("Inside createSession Method: ");
	        l_session = Session.getInstance(props,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, pass);
	                    }
	                });
	        l_session.setDebug(true); // Enable the debug mode
	    }
	
	public String sendMessage(String fromEmail, String toEmail, String subject, String msg, String[] attachFiles) {
	        System.out.println("Inside sendMessage Method: ");
	        String msgsendresponse="";
	        try {
	           
	        	MimeMessage message = new MimeMessage(l_session);
	            System.out.println("Sending E-mail From: " + fromEmail);
	            //message.setFrom(new InternetAddress(fromEmail));
	            message.setFrom(new InternetAddress(fromEmail));

	            System.out.println("Sending E-mail To: "+toEmail);
	            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
	            
	            //message.addRecipient(Message.RecipientType.BCC, new InternetAddress(AppConstants.fromEmail)); 
	            message.setSubject(subject);
	            
	            //-----------* Send mail with attachments code Starts here *---------------------
	            Multipart multipart = new MimeMultipart();
	            MimeBodyPart messageBodyPart = new MimeBodyPart();
	            
	            // Set TEXT/HTML message here
	            messageBodyPart.setContent(msg, "text/html");
	            multipart.addBodyPart(messageBodyPart);
	    	        
	            // adds attachments
	            MimeBodyPart attachPart = new MimeBodyPart();
	            if (attachFiles != null && attachFiles.length > 0) {
	                for (String filePath : attachFiles) {
	                	try {
	                      	attachPart.attachFile(filePath);
	                    	
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }
	     
	                    multipart.addBodyPart(attachPart);
	                }
	            }
	          message.setContent(multipart);
	          //------------* Send mail with attachments code Ends here *------------------------
	            try {
	            	Transport.send(message);
	            	msgsendresponse="Message_Sent";		//Don't change this message String
	                System.out.println(msgsendresponse);
	                } catch (Exception ex) {
	                	msgsendresponse="Email sending failed due to: " +ex.getLocalizedMessage();
					System.out.println(msgsendresponse);		//gives error message for failure
					
				}
	            
	        } catch (MessagingException mex) {
	        	msgsendresponse = "Error occured in creation of MIME message due to:  "+mex.getLocalizedMessage();
	            System.out.println(msgsendresponse);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }//end catch block
	        return msgsendresponse;
	    }
  	
  	/** The main method.
	 *
	 * @param args
	 *            the arguments
	 */
  	public static void main(String[] args) {
		
		  String serverName = "smtp.mail.yahoo.com";		// smtp.zoho.com , smtp.mail.yahoo.com , smtp.rediffmail.com
		  String portNo = "465";							// 465 , 587 , 25
		  String secureConnection = "ssl";					// ssl , tls , never
		  String userName = "youremail@test.com";
		  String password = "password";
		  String toEmail = "receiveremail@test.com";
		  String subject = "New Assessment mail";
		  String msg = "<h1> This is test mail please ignore... </h1>";
		  
		// attachments
	        String[] attachFiles = new String[3];
	        attachFiles[0] = "D:/test1.html";
	        attachFiles[1] = "D:/test2.txt";
	        attachFiles[2] = "D:/test3.txt";
		  
		  SendEmailsUsingSMTP oe = new SendEmailsUsingSMTP();
		  oe.connectAndSendSmtp(serverName, portNo, secureConnection, userName, password, toEmail, subject, msg, attachFiles);
		  // Uncomment below methods to check stepwise or as per your use
		  //oe.createSession(userName, password);
		  //oe.emailSettings(serverName, portNo, secureConnection);
		  //oe.sendMessage(userName, toEmail, subject, msg);
		  
	}
}
