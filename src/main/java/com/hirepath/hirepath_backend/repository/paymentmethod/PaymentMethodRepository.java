package com.hirepath.hirepath_backend.repository.paymentmethod;

import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListProjection;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByGuid(String guid);

    @Query(value = """
            SELECT
                pm.guid AS guid,
                pm.card_code AS cardCode,
                pt.name AS paymentTypeName,
                pm.created_at AS createdAt,
                pm.updated_at AS updatedAt
            FROM payment_methods pm
            JOIN payment_types pt ON pm.payment_type_id = pt.id
            WHERE (LOWER(pm.card_code) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND pm.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN pm.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN pm.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<PaymentMethodListProjection> findAllPaymentMethodsAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
