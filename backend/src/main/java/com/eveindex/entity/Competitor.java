package com.eveindex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "competitors")
public class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competitorId;

    private String competitorName;
    private String productType;
    private Boolean isMedicalRegistered;
    private String registrationInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // getter/setter 省略
}