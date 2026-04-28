package com.dt.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
	public class Assignee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignee_id")
    private Long id;

    @Column(name = "assignee_name", nullable = false, length = 100)
    private String assigneeName;

    @Column(name = "email", nullable = false, unique = true)
    private String assigneeEmail;

    @ManyToMany(mappedBy = "assignees", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<AddCampaignForm> campaigns = new HashSet<>();
   
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignee assignee = (Assignee) o;
        return id != null && id.equals(assignee.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
