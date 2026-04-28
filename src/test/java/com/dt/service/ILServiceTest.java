package com.dt.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dt.entity.AddCampaignForm;
import com.dt.entity.Assignee;
import com.dt.entity.StatusCountDTO;
import com.dt.entity.Users;
import com.dt.payloads.AddCampaignDTO;
import com.dt.payloads.AssigneeDTO;
import com.dt.payloads.UsersDTO;
import com.dt.repo.AssigneeRepo;
import com.dt.repo.ILRepo;
import com.dt.repo.UserRepository;



@ExtendWith(MockitoExtension.class)
public class ILServiceTest {

	@Mock
    private ILRepo repo;

    @Mock
    private AssigneeRepo assRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ILServiceImpl service;
	    
	    
	    
	    
	    @Test
	    public void testAddNewCampaign() {
	    	
	    	
	    	AddCampaignDTO dto= new AddCampaignDTO();
	    	AddCampaignForm form=new AddCampaignForm();
	    	
	    	Assignee ass=new Assignee();
	    	ass.setId(1L);
	    	
	    	form.setAssignees(Set.of(ass));
	    	
	    	
	    	when(mapper.map(dto, AddCampaignForm.class)).thenReturn(form);
	    	when(assRepo.findAllById(Set.of(1L))).thenReturn(List.of(ass));
	    	when(repo.save(form)).thenReturn(form);
	    	when(mapper.map(form, AddCampaignDTO.class)).thenReturn(dto);
	    	
	    	AddCampaignDTO result=	service.AddNewCampaign(dto);
	    	
	    	Assertions.assertNotNull(result);
	    	
	    	verify(repo).save(form);
	    	
	    	
	    	
	    }
	    
	
	    @Test
	    public void testupdateCampaign() {
	    	
	    	
	    	
	    	 Integer campaignId = 1;
	    	 
	    	 AddCampaignDTO dto = new AddCampaignDTO();
	    	    dto.setCampaignName("Updated Campaign");
	    	    dto.setStatus("Closed");
	    	    
	    	    
	    	    AssigneeDTO assigneeDto = new AssigneeDTO();
	    	    
	    	    assigneeDto.setId(1L);
	    	    
	    	    dto.setAssignees(Set.of(assigneeDto));
	    	    
	    	    AddCampaignForm existingCampaign = new AddCampaignForm();

	    	    when(repo.findById(campaignId))
	    	            .thenReturn(Optional.of(existingCampaign));

	    	    Assignee ass=new Assignee();
	    	 
	    	    ass.setId(1L);
	    	  
	    	    when(assRepo.findAllById(Set.of(1L)))
	    	            .thenReturn(List.of(ass));

	    	    String result = service.updateCampaign(campaignId, dto);
	    	    
	    	    Assertions.assertEquals("Updated successfully...", result);
	    	    verify(repo).save(existingCampaign);
	    	    
	      
	      
	    
	    }
	    
	    
	    @Test
	    public void testDeleteCampaign() {
	    	
	    	
	    	AddCampaignForm form = new AddCampaignForm();
	    	
	    	when(repo.findById(1)).thenReturn(Optional.of(form));
	    	
	    	   String result= service.deleteCampaign(1);
	    	
	    	   Assertions.assertEquals("campaign delete success fully.", result);
	    	    verify(repo).delete(form);
	    }
	    
	    
	    @Test
	    void testGetAllCampaigns() {

	        AddCampaignForm form = new AddCampaignForm();
	        AddCampaignDTO dto = new AddCampaignDTO();

	        Page<AddCampaignForm> page =
	                new PageImpl<>(List.of(form));

	        when(repo.findAll(any(Pageable.class))).thenReturn(page);
	        when(mapper.map(form, AddCampaignDTO.class)).thenReturn(dto);

	        Page<AddCampaignDTO> result =
	                service.getAllCampaigns(PageRequest.of(0, 10));

	        Assertions.assertEquals(1, result.getTotalElements());
	    }
	    
	    @Test
	    public void testGetCampaign() {
	    	AddCampaignForm form=new AddCampaignForm();
	    	
	    	AddCampaignDTO dto = new AddCampaignDTO();
	    	
	    	when(repo.findById(1)).thenReturn(Optional.of(form));
	    	when(mapper.map(form, AddCampaignDTO.class)).thenReturn(dto);
	    	
	    	AddCampaignDTO result= service.getCampaign(1);
	    	
	    	Assertions.assertNotNull(result);
	    	
	    		    	
	    }
	    
	    @Test
	    public void testAddNewAssignee() {
	     
	    	Assignee ass=new Assignee();
	    	AssigneeDTO dto = new AssigneeDTO();
	    	
	    	
	    	
	    	when(mapper.map(dto, Assignee.class)).thenReturn(ass);
	    	when(assRepo.save(ass)).thenReturn(ass);
	    	when(mapper.map(ass, AssigneeDTO.class)).thenReturn(dto);
	    	
	    	AssigneeDTO result= service.addNewAssignee(dto);
	    	
	    	Assertions.assertNotNull(result);
	    	
	    	
	    }
	    
	    @Test
	    public void testUpdateStatus() {
	    
	    	
	    	AddCampaignForm form=new AddCampaignForm();
	    	
	    	when(repo.findById(1)).thenReturn(Optional.of(form));
	    	
	    	  service.updateStatus(1, "Closed");
	    	
	    	  Assertions.assertEquals("Closed", form.getStatus());
	    	
	    }
	    
	    @Test
	    void testGetStatusCounts() {

	        List<Object[]> mockData = new ArrayList<>();
	        mockData.add(new Object[]{"Active", 5L});
	        mockData.add(new Object[]{"Inactive", 2L});

	        when(repo.countCampaignsByStatus()).thenReturn(mockData);

	        List<StatusCountDTO> result = service.getStatusCounts();

	        Assertions.assertEquals(2, result.size());
	        Assertions.assertEquals("Active", result.get(0).getStatus());
	    }
	    
	    @Test
	    void testGetAllTypes() {

	        when(repo.findDistinctTypes()).thenReturn(List.of("Email", "SMS"));

	        List<String> result = service.getAllTypes();

	        Assertions.assertEquals(2, result.size());
	    }
	    
	    @Test
	    void testGetAllStatuses() {

	        when(repo.findDistinctStatuses()).thenReturn(List.of("Active", "Closed"));

	        List<String> result = service.getAllStatuses();

	        Assertions.assertEquals(2, result.size());
	    }
	    
	    void testGetAllAss() {
	    	Assignee ass=new Assignee();
	    	AssigneeDTO dto = new AssigneeDTO();
	    	
	    	when(assRepo.findAll()).thenReturn(List.of(ass));
	    	when(mapper.map(ass, AssigneeDTO.class)).thenReturn(dto);
	    	
	    	List<AssigneeDTO> result = service.getAllAss();

	    	Assertions.assertEquals(1, result.size());
	    	
	    	
	    	
	    }
	    
	    void testAddNewUser() {
	    	
	    	  UsersDTO dto = new UsersDTO();
	    	    dto.setPassword("123");
	    	
	    	
	    	    when(passwordEncoder.encode("123")).thenReturn("encoded");
	    	    
	    	    
	    	    String result = service.addNewUser(dto);

	    	    Assertions.assertEquals("User saved...!", result);
	    	    verify(userRepo).save(any(Users.class));
	 
	    	
	    	
	    }
	    
}
