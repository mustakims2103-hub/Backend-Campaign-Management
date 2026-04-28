package com.dt.payloads;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import com.dt.entity.Assignee;

import lombok.Data;


@Data
public class AddCampaignDTO {

	
	 
	    private Integer id;

	    private String campaignName;

	    private String campaignSeries;

	    private String type;

	    private String status;
	    
	    private LocalDateTime createdAt;

	    private Date startDate;

	    private Date dueDate;

	   
	    private String leads;

	    
	    private String notes;

	   
	    private String assets;

	   
	    private String industry;
	    
	    private Set<AssigneeDTO> assignees;
	    
}
