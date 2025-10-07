package com.hirepath.hirepath_backend.repository.paymenttype;

import com.hirepath.hirepath_backend.model.dto.paymenttype.PaymentTypeListProjection;
import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends CrudRepository<PaymentType, Long> {
    Optional<PaymentType> findByGuid(String guid);

    @Query(value = """
            SELECT
                pt.name AS name,
                pt.description AS description,
                pt.guid AS guid,
                pt.is_deleted AS isDeleted,
                pt.created_at AS createdAt,
                pt.updated_at AS updatedAt
            FROM payment_types pt
            WHERE (LOWER(pt.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND pt.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN pt.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN pt.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<PaymentTypeListProjection> findAllPaymentTypesAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
