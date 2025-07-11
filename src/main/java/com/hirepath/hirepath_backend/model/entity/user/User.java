package com.hirepath.hirepath_backend.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_language_id")
    private PreferredLanguage preferredLanguage;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "profile")
    private String profile;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "last_login_at")
    private ZonedDateTime lastLoginAt;

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
