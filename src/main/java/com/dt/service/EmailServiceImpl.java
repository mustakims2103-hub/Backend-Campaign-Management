package com.dt.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private TemplateEngine templateEngine;
	

	@Value("${brevo.api.key}")
    private String brevoApiKey;
	
	 private final RestTemplate restTemplate = new RestTemplate();
	 
	
//    private final JavaMailSender mailSender;
//
//    public EmailServiceImpl(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
    
	 @Async
	 public void sendCampaignEmail(
	         String toEmail,
	         String firstName,
	         String campaignName,
	         String status,
	         Integer campaignId,
	         String type) {

	     try {

	         // 1️⃣ Prepare Thymeleaf HTML
	         Context context = new Context();
	         context.setVariable("firstName", firstName);
	         context.setVariable("campaignName", campaignName);
	         context.setVariable("status", status);
	         context.setVariable("type", type);
	         context.setVariable("campaignUrl",
	                 "https://distinctcampaign.vercel.app/layout/campaign-details/" + campaignId);

	         String htmlContent = templateEngine.process(
	                 "email/campaign-assigned",
	                 context);

	         String subject = type.equals("ASSIGNED")
	                 ? "New Campaign Has Been Assigned"
	                 : "Campaign Status Updated";

	         // 2️⃣ Create Request Body using Map (SAFE JSON)
	         Map<String, Object> requestBody = new HashMap<>();

	         Map<String, String> sender = new HashMap<>();
	         sender.put("email", "mustakims2103@gmail.com");

	         Map<String, String> to = new HashMap<>();
	         to.put("email", toEmail);

	         requestBody.put("sender", sender);
	         requestBody.put("to", List.of(to));
	         requestBody.put("subject", subject);
	         requestBody.put("htmlContent", htmlContent);

	         // 3️⃣ Headers
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         headers.set("api-key", brevoApiKey);

	         HttpEntity<Map<String, Object>> entity =
	                 new HttpEntity<>(requestBody, headers);

	         // 4️⃣ Send
	         restTemplate.postForObject(
	                 "https://api.brevo.com/v3/smtp/email",
	                 entity,
	                 String.class
	         );

	         System.out.println("Email Sent Successfully ✅");

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
