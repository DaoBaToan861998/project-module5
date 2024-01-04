package com.ra.controller;

import com.ra.model.Category;
import com.ra.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/v1")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/categories/list")
    public ResponseEntity<Iterable<Category>> getAll(){
        List<Category> categories= (List<Category>) categoryService.findAll();
        if(categories.isEmpty()){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @GetMapping("/admin/category/{id}")
    public  ResponseEntity<Category> findById(@PathVariable Long id){
        Optional<Category> category=categoryService.findById(id);
        if(!category.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
            return new ResponseEntity<>(category.get(),HttpStatus.OK);
    }
    @PostMapping("/admin/category/add")
    public  ResponseEntity<Category> add(@RequestBody Category category){
            return  new ResponseEntity<>(categoryService.save(category),HttpStatus.OK);
    }
    @DeleteMapping("/admin/category/{id}")
    public  ResponseEntity<Category> delete(@PathVariable Long id){
           Optional<Category> categoryOptional=categoryService.findById(id);
           if(!categoryOptional.isPresent()){
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }
           categoryService.deleteById(id);
           return  new ResponseEntity<>(categoryOptional.get(),HttpStatus.OK);
    }
    @PutMapping("/admin/category/{id}")
    public  ResponseEntity<Category> update(@PathVariable Long id,@RequestBody Category category){
         Optional<Category> categoryOptional=categoryService.findById(id);
         if(!categoryOptional.isPresent()){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         category.setCategoryId(categoryOptional.get().getCategoryId());
         categoryService.save(category);
         return new ResponseEntity<>(categoryOptional.get(),HttpStatus.OK);
    }
}
