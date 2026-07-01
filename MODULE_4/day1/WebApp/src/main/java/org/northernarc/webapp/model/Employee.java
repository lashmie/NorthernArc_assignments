package org.northernarc.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@Getter@Setter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
@Data // lambok will generate getter, setter, toString, noArgsConstructor, allArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//if wenot give this it created sequrence
    private Long id;
    private String name;
    private String email;
    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private java.util.List<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public java.util.List<Project> getProjects() {
        return projects;
    }

    public void setProjects(java.util.List<Project> projects) {
        this.projects = projects;
    }
}
