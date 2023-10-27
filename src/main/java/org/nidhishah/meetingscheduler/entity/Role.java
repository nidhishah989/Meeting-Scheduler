package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name")
    private String roleName;

    // Constructors, getters, setters, and other fields as needed


    public Long getId() {
        return id;
    }

    public String getName() {
        return roleName;
    }

    public void setName(String name) {
        this.roleName = name;
    }
}
