package com.ra.controller;

import com.ra.dto.request.ProductRequest;
import com.ra.model.Product;
import com.ra.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class ProductController {
    @Autowired
    private IProductService productService;
    @GetMapping("/products/list")
    public ResponseEntity<Iterable<Product>> getAll(){
        List<Product> products= (List<Product>) productService.findAll();
        if(products.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity<>(products,HttpStatus.OK);
    }

    @PostMapping("/admin/product/add")
    public ResponseEntity<Product> addProduct(@ModelAttribute ProductRequest productRequest) {
        try {
            productService.save(productRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PutMapping("/admin/product/{id}")
//    public  ResponseEntity<Product> updateById(@PathVariable Long id,@RequestBody ProductRequest productRequest){
//        Optional<Product> productOptional=productService.findById(id);
//        if(!productOptional.isPresent()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(productService.update(productRequest,id),HttpStatus.OK);
//    }
@PutMapping("/admin/product/{id}")
public ResponseEntity<Product> updateById(@PathVariable Long id, @ModelAttribute ProductRequest productRequest) {
    try {
        Product updatedProduct = productService.update(productRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } catch (EntityNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
    @GetMapping("/admin/product/{id}")
    public  ResponseEntity<Product> getById(@PathVariable Long id){
        Optional<Product> productOptional=productService.findById(id);
        if(!productOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(productOptional.get(),HttpStatus.OK);
    }
    @DeleteMapping("/admin/product/{id}")
    public  ResponseEntity<Product> deleteById(@PathVariable Long id){
        Optional<Product> productOptional=productService.findById(id);
        if(!productOptional.isPresent()){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
              productService.deleteById(productOptional.get().getId());
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/products/sort/{field}")
    public  ResponseEntity<List<Product>> sortProduct(@PathVariable String field){
        return new ResponseEntity<>(productService.sortBasedUponSomeField(field),HttpStatus.OK);
    }

    @GetMapping("/products/pagination/{offset}/{pageSize}")
    public  ResponseEntity<Page<Product>> findAllWithPagination(@PathVariable int offset,@PathVariable int pageSize){
        return new ResponseEntity<>(productService.findAllWithPagination(offset,pageSize),HttpStatus.OK);
    }
    @GetMapping("/products/paginationAndSorting/{offset}/{pageSize}/{field}")
    public  ResponseEntity<Page<Product>> findAllPaginationAndSorting(@PathVariable int offset,@PathVariable int pageSize,@PathVariable String field){
        return new ResponseEntity<>(productService.findAllWithPaginationAndSorting(offset,pageSize,field),HttpStatus.OK);
    }
    @PostMapping("/products/pagination/search")
   public ResponseEntity<Page<Product>> findAllByProductName(@RequestParam String keyword, @PageableDefault(size = 4,page = 0)Pageable pageable){
       Page<Product> productPage=productService.findAllByProductNameContainingIgnoreCase(keyword,pageable);
       return new ResponseEntity<>(productPage,HttpStatus.OK);
    }
    @GetMapping("/products/pagination/productBetween")
    public  ResponseEntity<Page<Product>> productBettwen(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam int page,
            @RequestParam int size){
        Page<Product> products=productService.findAllByPriceBetween(minPrice,maxPrice, PageRequest.of(page,size));
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


}
