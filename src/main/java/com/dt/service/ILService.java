package com.dt.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dt.entity.AddCampaignForm;
import com.dt.entity.StatusCountDTO;
import com.dt.payloads.AddCampaignDTO;
import com.dt.payloads.AssigneeDTO;
import com.dt.payloads.UsersDTO;


public interface ILService {

	
	public AddCampaignDTO AddNewCampaign(AddCampaignDTO course);
	
	Page<AddCampaignDTO> getAllCampaigns(Pageable pageable);
	
	public AddCampaignDTO getCampaign(Integer id);

	
	public AssigneeDTO addNewAssignee(AssigneeDTO dto);
	
	
	
	public List<AssigneeDTO>  getAllAss();
	
	public void updateStatus(Integer id, String status);
	
	public String addNewUser(UsersDTO user);
	
	public String updateCampaign(Integer id, AddCampaignDTO form);
	
//	public Page<AddCampaignForm> filterCampaigns(CampaignFilterRequest request);
//	
	public List<String>  getAllStatuses();
	
	public List<String>  getAllTypes();
	
    public List<StatusCountDTO> getStatusCounts();
    
    
    public String deleteCampaign(Integer Id);
	
	
}

