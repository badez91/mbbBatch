package com.my.mbbBatch.repository;

import com.my.mbbBatch.entity.Records;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long>, JpaSpecificationExecutor<Records> {

}
