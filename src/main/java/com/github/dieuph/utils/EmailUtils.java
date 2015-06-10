package com.github.dieuph.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The Class EmailUtils.
 *
 * @author dieuph
 */
public class EmailUtils {
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    // private static final String SMTP_AUTH_USER = "email";
    // private static final String SMTP_AUTH_PWD  = "password";

    /**
     * Send email.
     *
     * @param fromAddress the from address
     * @param toAddress the to address
     * @param password the password
     * @param subject the subject of email
     * @param content the content of email
     * @throws Exception the exception
     */
    public static void send(String fromAddress, String toAddress, String password,
            String subject, String content) throws Exception {
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        // props.put("mail.smtps.quitwait", "false");

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject);
        message.setContent(content, "text/plain");

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

        transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, fromAddress, password);

        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}
