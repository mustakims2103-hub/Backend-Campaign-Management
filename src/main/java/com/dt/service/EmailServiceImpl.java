package com.dt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private TemplateEngine templateEngine;
	

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Async
    public void sendCampaignEmail(
            String toEmail,
            String firstName,
            String campaignName,
            String status,
            Integer campaignId,
            String type) {

        try {

            Context context = new Context();
            context.setVariable("firstName", firstName);
            context.setVariable("campaignName", campaignName);
            context.setVariable("status", status);
            context.setVariable("type", type);
            context.setVariable("campaignUrl",
                    "http://localhost:5173/layout/campaign-details/" + campaignId);

            String htmlContent = templateEngine.process(
                    "email/campaign-assigned",
                    context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);

            if (type.equals("ASSIGNED")) {
                helper.setSubject("New Campaign Have Been Assigned");
            } else {
                helper.setSubject("Campaign Status Updated");
            }

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
//    @Override
//    @Async
//    public void sendCampaignAssignedEmail(String toEmail,
//                                           String campaignName,
//                                           String empName,
//                                           Integer campaignId) {
//
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            String firstName = empName.split(" ")[0]; // get first name
//
//            String campaignUrl = "http://localhost:5173/layout/campaign-details/" + campaignId;
//
//            String htmlContent = """
//                    <html>
//                    <body style="font-family: Arial, sans-serif;">
//                        <h3>Hello %s,</h3>
//
//                        <p>You have been assigned to the campaign:</p>
//
//                        <h2 style="color:#2E86C1;">%s</h2>
//
//                        <p>Please click below to view campaign details:</p>
//
//                        <a href="%s"
//                           style="
//                               display:inline-block;
//                               padding:10px 20px;
//                               background-color:#2E86C1;
//                               color:white;
//                               text-decoration:none;
//                               border-radius:5px;
//                               font-weight:bold;">
//                            View Campaign
//                        </a>
//
//                        <br><br>
//
//                        <p>Regards,<br><b>Leads Team</b></p>
//                    </body>
//                    </html>
//                    """.formatted(firstName, campaignName, campaignUrl);
//
//            helper.setTo(toEmail);
//            helper.setSubject("New Campaign Assigned");
//            helper.setText(htmlContent, true); // true = HTML
//
//            mailSender.send(message);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send email", e);
//        }
//    }
//    
//    @Override
//    @Async
//    public void sendCampaignStatusUpdateEmail(
//            String toEmail,
//            String empName,
//            String campaignName,
//            String status,
//            Integer campaignId) {
//
//        try {
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(toEmail);
//            helper.setSubject("Campaign Status Updated");
//
//            String campaignUrl = "http://localhost:5173/layout/campaign-details/" + campaignId;
//
//            String htmlContent = buildStatusEmailTemplate(empName, campaignName, status, campaignUrl);
//
//            helper.setText(htmlContent, true);
//
//            mailSender.send(message);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
