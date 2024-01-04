package com.ra.service;

import com.ra.dto.request.ProductRequest;
import com.ra.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService  {
    Iterable<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(ProductRequest productRequest) throws IOException;
    Product update(ProductRequest productRequest,Long id);
    void deleteById(Long id);
    List<Product> sortBasedUponSomeField(String field);
    Page<Product> findAllWithPagination(int offset, int pageSize);
    Page<Product> findAllWithPaginationAndSorting(int offset, int pageSize,String field);
    Page<Product> findAllByProductNameContainingIgnoreCase(String search, Pageable pageable);
    Page<Product> findAllByPriceBetween(Double minProduct,Double maxProduct,Pageable pageable);


}
