package com.hirepath.hirepath_backend.repository.paymentmethod;

import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Long> {
}
