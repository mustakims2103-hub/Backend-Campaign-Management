package com.dt.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dt.entity.AddCampaignForm;
import com.dt.entity.Assignee;
import com.dt.entity.StatusCountDTO;
import com.dt.entity.Users;
import com.dt.exception.CampaignNotFoundException;
import com.dt.payloads.AddCampaignDTO;
import com.dt.payloads.AssigneeDTO;
import com.dt.payloads.UsersDTO;
import com.dt.repo.AssigneeRepo;
import com.dt.repo.ILRepo;
import com.dt.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ILServiceImpl implements ILService {
	
	  private final EmailService emailService;
	

	 private final ILRepo repo;
	    private final AssigneeRepo assRepo;
	    private final UserRepository userRepo;
	    private final ModelMapper mapper;
	    private final PasswordEncoder passwordEncoder;

	    public ILServiceImpl(ILRepo repo,
	                         AssigneeRepo assRepo,
	                         UserRepository userRepo,
	                         ModelMapper mapper,
	                         PasswordEncoder passwordEncoder,
	                         EmailService emailService) {

	        this.repo = repo;
	        this.assRepo = assRepo;
	        this.userRepo = userRepo;
	        this.mapper = mapper;
	        this.passwordEncoder = passwordEncoder;
	        this.emailService = emailService;
	    }
		

	
	
	@Override
	@Transactional
	public AddCampaignDTO AddNewCampaign(AddCampaignDTO campaign) {

		AddCampaignForm campobj = this.mapper.map(campaign, AddCampaignForm.class);

		Set<Long> ids = campobj.getAssignees()
				      .stream()
				      .map(Assignee::getId)
				      .collect(Collectors.toSet());

		List<Assignee> assigneesFromDb = assRepo.findAllById(ids);

		if (assigneesFromDb.size() != ids.size()) {
			throw new RuntimeException("Some assigness not found");
		}

		
		for (Assignee ass : assigneesFromDb) {

			ass.getCampaigns().add(campobj);

		}

		campobj.setAssignees(new HashSet<>(assigneesFromDb));
		campobj.setCreatedAt(LocalDateTime.now());

		repo.save(campobj);

		 // ✅ SEND EMAIL TO ALL ASSIGNEES
	    for (Assignee assignee : assigneesFromDb) {
	    	emailService.sendCampaignEmail(
	    	        assignee.getAssigneeEmail(),
	    	        assignee.getAssigneeName(),
	    	        campobj.getCampaignName(),
	    	        null,
	    	        campobj.getId(),
	    	        "ASSIGNED"
	    	);
	    }
	    
		
		return this.mapper.map(campobj, AddCampaignDTO.class);

	}

//	@Override
//	@Transactional
//	public AddCampaignDTO AddNewCampaign(AddCampaignDTO campaign) {
//	    
//		Set<Assignee> attachedAssignees = new HashSet<>();
//	    
//	    
//	    AddCampaignForm campobj = this.mapper.map(campaign, AddCampaignForm.class);
//	    
//	    
//	    for (Assignee selectedAssignee : campobj.getAssignees()) {
//	        if (selectedAssignee.getId() != null) {
//	            Assignee existingAssignee = assRepo.findById(selectedAssignee.getId())
//	                .orElseThrow(() -> new RuntimeException("Assignee not found with id " + selectedAssignee.getId()));
//
//	            // Add this campaign to the assignee's list of campaigns
//	            existingAssignee.getCampaigns().add(campobj);
//
//	            attachedAssignees.add(existingAssignee);
//	        } else {
//	            throw new RuntimeException("Invalid Assignee provided");
//	        }
//	    }
//
//	    // Set the attached assignees to the campaign
//	    campobj.setAssignees(attachedAssignees);
//	    
//	    campobj.setCreatedAt(LocalDateTime.now());
//
//	    // Save the campaign
//	   	    repo.save(campobj);
//	   AddCampaignDTO dto=  this.mapper.map(campobj, AddCampaignDTO.class);
//	    		 
//	   return dto;
//	}

	@Override
	public String updateCampaign(Integer id, AddCampaignDTO form) {
		AddCampaignForm camp = repo.findById(id)
				.orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));

		
		
		
		camp.setCampaignName(form.getCampaignName());
		camp.setCampaignSeries(form.getCampaignSeries());
		camp.setType(form.getType());
		camp.setStatus(form.getStatus());
		camp.setStartDate(form.getStartDate());

		camp.setDueDate(form.getDueDate());
		camp.setLeads(form.getLeads());
		camp.setNotes(form.getNotes());
		camp.setAssets(form.getAssets());
		camp.setIndustry(form.getIndustry());

	

		Set<Long> ids = form.getAssignees()
				 .stream()
				 .map(ass -> ass.getId())
				 .collect(Collectors.toSet());

		
		Set<Assignee> attached = new HashSet<>(assRepo.findAllById(ids));

		camp.setAssignees(attached);

		// Save updated campaign
		repo.save(camp);

		
		 
	    
		
		return "Updated successfully...";
		
		
		// Re-attach existing Assignees from DB
//		Set<Assignee> attachedAssignees = new HashSet<>();
//		for (Assignee selectedAssignee : form.getAssignees()) {
//			if (selectedAssignee.getId() != null) {
//				Assignee existingAssignee = assRepo.findById(selectedAssignee.getId()).orElseThrow(
//						() -> new RuntimeException("Assignee not found with id " + selectedAssignee.getId()));
//
//				// Add this campaign to the assignee’s campaigns
//				existingAssignee.getCampaigns().add(camp);
//				attachedAssignees.add(existingAssignee);
//			} else {
//				throw new RuntimeException("Invalid Assignee provided");
//			}
//		}
//
//		camp.setAssignees(attachedAssignees);

//		Set<Long> ids = form.getAssignees().stream()
//				.map(Assignee::getId)
//				.collect(Collectors.toSet());
	}

	public String deleteCampaign(Integer Id) {

		AddCampaignForm camp = repo.findById(Id)
				.orElseThrow(() -> new RuntimeException("Campaign not found with id: " + Id));

		repo.delete(camp);

		return "campaign delete success fully.";
	}

	@Override
	public Page<AddCampaignDTO> getAllCampaigns(Pageable pageable) {

		return repo.findAll(pageable).map(campaign -> mapper.map(campaign, AddCampaignDTO.class));
	}

//	@Override
//	public Page<AddCampaignDTO> getAllCampaigns(Pageable pageable) {
//		// Fetch page of entities
//		Page<AddCampaignForm> campaignsPage = repo.findAll(pageable);
//
//		// Convert each entity to DTO using ModelMapper
//		Page<AddCampaignDTO> dtoPage = campaignsPage.map(campaign -> {
//			AddCampaignDTO dto = mapper.map(campaign, AddCampaignDTO.class);
//			// Force initialization of lazy collections if needed
//			dto.getAssignees().size();
//			return dto;
//		});
//
//		return dtoPage;
//	}

	@Override
	public AddCampaignDTO getCampaign(Integer id) {

		AddCampaignForm campaign = repo.findById(id)
				.orElseThrow(() -> new CampaignNotFoundException("Campaign not found with campaign id--" + id));

		return this.mapper.map(campaign, AddCampaignDTO.class);
	}

	@Override
	public AssigneeDTO addNewAssignee(AssigneeDTO dto) {

		Assignee map = this.mapper.map(dto, Assignee.class);

		Assignee savedAssignee = assRepo.save(map);

		AssigneeDTO map2 = this.mapper.map(savedAssignee, AssigneeDTO.class);

		return map2;

	}

	@Override
	@Transactional
	public void updateStatus(Integer id, String newStatus) {

	    AddCampaignForm campaign = repo.findById(id)
	            .orElseThrow(() ->
	                    new CampaignNotFoundException("Campaign not found with id: " + id));

	    
	    String oldStatus = campaign.getStatus();

	   
	    if (Objects.equals(oldStatus, newStatus)) {
	        return; // Exit method
	    }

	  
	    campaign.setStatus(newStatus);

	   
	    repo.save(campaign);

	    
	    Set<Assignee> assignees = campaign.getAssignees();

	    //  Send email notification
	    for (Assignee assignee : assignees) {

	        emailService.sendCampaignEmail(
	                assignee.getAssigneeEmail(),
	                assignee.getAssigneeName(),   // better if you have firstName field
	                campaign.getCampaignName(),
	                newStatus,
	                campaign.getId(),
	                "STATUS"
	        );
	    }
	}

	public List<StatusCountDTO> getStatusCounts() {
		List<Object[]> results = repo.countCampaignsByStatus();

		return results.stream().map(obj -> new StatusCountDTO((String) obj[0], (Long) obj[1]))
				.collect(Collectors.toList());
	}

	public List<String> getAllTypes() {
		return repo.findDistinctTypes();
	}

	public List<String> getAllStatuses() {
		return repo.findDistinctStatuses();
	}

	@Override
	public List<AssigneeDTO> getAllAss() {
		List<Assignee> allAsss = assRepo.findAll();

		List<AssigneeDTO> assDTO = allAsss.stream().map(assignee -> mapper.map(assignee, AssigneeDTO.class))
				.collect(Collectors.toList());

		return assDTO;
	}
	
	
	 public String addNewUser(UsersDTO user) {

			Users usermain = new Users();

			usermain.setUsername(user.getUsername());
			usermain.setPassword(passwordEncoder.encode(user.getPassword()));
			usermain.setEmpName(user.getEmpName());
//			   usermain.setId(user.getId());
			usermain.setRoles(user.getRoles());

			userRepo.save(usermain);

			return "User saved...!";
		}

//	public Page<AddCampaignForm> filterCampaigns(CampaignFilterRequest request) {
//	Specification<AddCampaignForm> spec = Specification.where(null);
//
//	if (request.getSearchText() != null && !request.getSearchText().isEmpty()) {
//		spec = spec.and((root, query, cb) -> cb.or(
//				cb.like(cb.lower(root.get("campaignName")), "%" + request.getSearchText().toLowerCase() + "%"),
//				cb.like(cb.lower(root.get("client")), "%" + request.getSearchText().toLowerCase() + "%")));
//	}
//
//	if (request.getClient() != null && !request.getClient().isEmpty()) {
//		spec = spec.and((root, query, cb) -> cb.equal(root.get("client"), request.getClient()));
//	}
//
//	// Add more conditions similarly...
//
//	Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
//	return repo.findAll(spec, pageable);
//
//}

}
