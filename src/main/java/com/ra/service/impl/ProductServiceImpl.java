package com.ra.service.impl;

import com.ra.dto.request.ProductRequest;
import com.ra.model.Product;
import com.ra.repository.CategoryRepository;
import com.ra.repository.ProductRepository;
import com.ra.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Value("${path-upload}")
    private String pathUpload;

    @Value("${server.port}")
    private Long port;
    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }



    @Override
    public Product save(ProductRequest productRequest) throws IOException {
        Product product=new Product();
        product.setProductName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setStatus(productRequest.getStatus());
        product.setStock(productRequest.getStock());
        product.setCategory(categoryRepository.findById(productRequest.getCategoryId()).get());

       String fileName=productRequest.getImage().getOriginalFilename();
       try {
           FileCopyUtils.copy(productRequest.getImage().getBytes(),new File(pathUpload+fileName));
       }catch (IOException e){
           throw  new RuntimeException(e.getMessage());
       }

       product.setImage("http://localhost:" + port + "/" + fileName);
        return productRepository.save(product);
    }



    @Override
    public Product update(ProductRequest productRequest, Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        System.out.println(productOptional);
        if (!productOptional.isPresent()) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        Product product = productOptional.get();
        product.setId(id);
        product.setProductName(productRequest.getProductName());
        MultipartFile image = productRequest.getImage();
        if (image != null) {
            product.setImage(image.getOriginalFilename());
        }
//        existingProduct.setImage(productRequest.getImage().getOriginalFilename());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setStatus(productRequest.getStatus());
        product.setStock(productRequest.getStock());
        product.setCategory(categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + productRequest.getCategoryId())));
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
       productRepository.deleteById(id);
    }

    @Override
    public List<Product> sortBasedUponSomeField(String field) {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC,field));
    }

    @Override
    public Page<Product> findAllWithPagination(int offset, int pageSize) {
        return productRepository.findAll(PageRequest.of(offset,pageSize));
    }

    @Override
    public Page<Product> findAllWithPaginationAndSorting(int offset, int pageSize,String field) {
        return productRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(Sort.Direction.ASC,field)));
    }

    @Override
    public Page<Product> findAllByProductNameContainingIgnoreCase(String search, Pageable pageable) {
        return productRepository.findAllByProductNameContainingIgnoreCase(search,pageable);
    }

    @Override
    public Page<Product> findAllByPriceBetween(Double minProduct, Double maxProduct, Pageable pageable) {
        return productRepository.findAllByPriceBetween(minProduct,maxProduct,pageable);
    }



}
