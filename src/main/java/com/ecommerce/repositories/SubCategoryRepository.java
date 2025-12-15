package com.ecommerce.repositories;

import com.ecommerce.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategoryId(Long categoryId);

    List<SubCategory> findByCategoryIdAndIsActiveTrue(Long categoryId);

    Optional<SubCategory> findByNameAndCategoryId(String name, Long categoryId);
}



