package com.dt.cotroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dt.entity.StatusCountDTO;
import com.dt.payloads.AddCampaignDTO;
import com.dt.payloads.AssigneeDTO;
import com.dt.payloads.UpdateStatusDto;
import com.dt.payloads.UsersDTO;
import com.dt.service.ILService;

@RestController
public class ILController {

	@Autowired
	private ILService service;
	
	
//	@GetMapping("/")
//	public String getHome() {
//		return "This is home page";
//	}
	
	 @GetMapping("/status-counts")
	    public ResponseEntity<List<StatusCountDTO>> getStatusCounts() {
	        return ResponseEntity.ok(service.getStatusCounts());
	    }
	
	@GetMapping("/getalltypes")
    public List<String> getTypes(){
		return service.getAllTypes();
	}
	
	
	@GetMapping("/getallstatus")
    public List<String> getStatuses(){
		return service.getAllStatuses();
	}
	
	
//	@PostMapping("/filter-campaigns")
//	public Page<AddCampaignForm> filterCampaigns(@RequestBody CampaignFilterRequest filters) {
//	    return service.filterCampaigns(filters);
//	}
	
	
	
	@PostMapping("/addcamp")	
	public AddCampaignDTO addNewCamp(@RequestBody AddCampaignDTO course){
		
		return service.AddNewCampaign(course);
	}
	
	
	
	@PostMapping("/addAssignee")	
	public AssigneeDTO addAssignee(@RequestBody AssigneeDTO dto){
		
		return service.addNewAssignee(dto);
	}
	
	
	 @GetMapping("/getallcamp")
	    public Page<AddCampaignDTO> getCampaigns(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "20") int size) {
	        
	        PageRequest pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("id").descending());
	        return service.getAllCampaigns(pageable);
	    }
	
	
	
	
		
	@GetMapping("/getCamp/{id}")
    public AddCampaignDTO getCamp(@PathVariable Integer id) {
       
        return service.getCampaign(id);
    }
	
	
	
	
	@GetMapping("/getAllAss")
	public List<AssigneeDTO> getAsss(){
		      
			
		List<AssigneeDTO> ass=service.getAllAss()	;	
		 	
		return ass;
	}
	
	
	
	@PatchMapping("/status/{id}")
	public String setStatus( @PathVariable Integer id,
			                 @RequestBody UpdateStatusDto status){
		        
		
		service.updateStatus(id, status.getStatus());
		
		return "Status updated...!";
		   
			
		
	}
	
	

	
	
	@PutMapping("/update/{id}")
	public String updateCamp(@PathVariable Integer id, @RequestBody AddCampaignDTO dto) {
		
		return service.updateCampaign(id, dto);
	}
	
	
	@DeleteMapping("/campaigns/{id}")
	public String delCamp(@PathVariable Integer id) {
		  
		return service.deleteCampaign(id);
	}
	
	
	 @PostMapping("/AddUserLogin")	
		public String addNewUsers(@RequestBody UsersDTO user){
			        
			return service.addNewUser(user);
		}
	
	
	
	
	

}
