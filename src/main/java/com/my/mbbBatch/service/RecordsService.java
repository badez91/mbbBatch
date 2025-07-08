package com.my.mbbBatch.service;

import com.my.mbbBatch.commonUtils.FileParser;
import com.my.mbbBatch.dto.RecordsUpdateDto;
import com.my.mbbBatch.entity.Records;
import com.my.mbbBatch.repository.RecordsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordsService {

    private final RecordsRepository recordsRepository;

    public void processUploadFile(MultipartFile file) throws Exception {

        List<Records> list = FileParser.parse(file);
        recordsRepository.saveAll(list);
    }

    public Page<Records> searchRecords(Optional<Long> customerId, Optional<String> accountNumber, Optional<String> description, int page, int size){

        Specification<Records> specification = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (customerId.isPresent()){
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerId"), customerId.get()));
        }

        if (accountNumber.isPresent()){
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("accountNumber"),"%" + accountNumber.get() + "%"));
        }

        if (description.isPresent()){
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + description.get() + "%"));
        }

        Pageable pageable = PageRequest.of(page, size);
        return recordsRepository.findAll(specification,pageable);
    }

    @Transactional
    public Records updateRecords(RecordsUpdateDto dto) {
        Records records = recordsRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Transaction records not found"));

        // ✅ Manually check optimistic lock version
        if (!records.getVersion().equals(dto.getVersion())) {
            throw new OptimisticLockException("Record has been modified by another transaction.");
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()){
            records.setDescription(dto.getDescription());
        }
        if (dto.getTrxAmount() != null && dto.getTrxAmount().compareTo(BigDecimal.ZERO) > 0){
            records.setTrxAmount(dto.getTrxAmount());
        }
        return recordsRepository.save(records);
    }
}
