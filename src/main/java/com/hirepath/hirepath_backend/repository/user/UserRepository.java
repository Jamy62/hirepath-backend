package com.hirepath.hirepath_backend.repository.user;

import com.hirepath.hirepath_backend.model.dto.user.UserListProjection;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGuid(String guid);

    @Query(value = """
            SELECT
                u.name AS name,
                u.full_name AS fullName,
                u.email AS email,
                u.mobile AS mobile,
                u.profile AS profile,
                r.name AS roleName,
                u.is_active AS isActive,
                u.is_blocked AS isBlocked,
                u.guid AS guid,
                u.created_at AS createdAt,
                u.updated_at AS updatedAt,
                u.last_login_at AS lastLoginAt
            FROM users u
            JOIN roles r on u.role_id = r.id
            WHERE (LOWER(u.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND u.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN u.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN u.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<UserListProjection> findAllUsersAdminPanal(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
