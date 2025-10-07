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
            AND pm.is_deleted = 0
            AND pm.company_id = companyId
            """, nativeQuery = true)
    List<PaymentMethodListProjection> findAllPaymentMethodsAdminPanel(@Param("companyId") Long companyId);
}
