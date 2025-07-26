package com.hirepath.hirepath_backend.repository.province;

import com.hirepath.hirepath_backend.model.entity.province.Province;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends CrudRepository<Province, Long> {
}
