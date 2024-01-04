package com.ra.repository;

import com.ra.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findAllByProductNameContainingIgnoreCase(String search, Pageable pageable);
    Page<Product> findAllByPriceBetween(Double minProduct,Double maxProduct,Pageable pageable);
}
