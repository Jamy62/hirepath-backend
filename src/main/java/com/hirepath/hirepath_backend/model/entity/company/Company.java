package com.hirepath.hirepath_backend.model.entity.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
public class Company {

    public enum VerificationStatus {
        PENDING, TRUE, FALSE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "logo")
    private String logo;

    @Column(name = "banner")
    private String banner;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;


    // verification details
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;

    @Column(name = "legal_business_name")
    private String legalBusinessName;

    @Column(name = "public_name")
    private String publicName;

    @Column(name = "website")
    private String website;

    @Column(name = "industry")
    private String industry;

    @Column(name = "founded_date")
    private ZonedDateTime foundedDate;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "verified_at")
    private ZonedDateTime verified_at;

    @Column(name = "verified_by")
    private Long verifiedBy;


    @Column(name = "guid")
    private String guid;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "amount")
    private BigDecimal amount;

}