package com.dt.service;

public interface EmailService {

	void sendCampaignEmail(
            String toEmail,
            String firstName,
            String campaignName,
            String status,
            Integer campaignId,
            String type);
//    void sendCampaignAssignedEmail(String toEmail,
//            String campaignName,
//            String empName,
//            Integer campaignId);
//
//    void sendCampaignStatusUpdateEmail(
//            String toEmail,
//            String empName,
//            String campaignName,
//            String status,
//            Integer campaignId);
    
}
