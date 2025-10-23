package com.reactive.bootcamps.infraestructure.driven.persitence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.UUID;

@Table("bootcamp")
public class BootcampEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
    @Column("launch_date")
    private LocalDate launchDate;
    private String duration;

    public BootcampEntity() {}

    public BootcampEntity(UUID id, String name, String description, LocalDate launchDate, String duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.duration = duration;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
