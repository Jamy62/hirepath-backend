package com.hirepath.hirepath_backend.model.entity.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import com.hirepath.hirepath_backend.model.entity.jobtype.JobType;
import com.hirepath.hirepath_backend.model.entity.township.Township;
import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.Position;
import java.time.ZonedDateTime;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_user_id")
    private CompanyUser companyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_plan_id")
    private CompanyPlan companyPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "township_id")
    private Township township;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_level_id")
    private ExperienceLevel experienceLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_type_id")
    private JobType jobType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_function_id")
    private JobFunction jobFunction;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "min_salary")
    private Double minSalary;

    @Column(name = "max_salary")
    private Double maxSalary;

    @Column(name = "posted_date")
    private ZonedDateTime postedDate;

    @Column(name = "expire_date")
    private ZonedDateTime expireDate;

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

}
