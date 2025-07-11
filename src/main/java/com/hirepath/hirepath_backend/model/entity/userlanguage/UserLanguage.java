package com.hirepath.hirepath_backend.model.entity.userlanguage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hirepath.hirepath_backend.model.entity.language.Language;
import com.hirepath.hirepath_backend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "user_language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "proficiency")
    private String proficiency;

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
