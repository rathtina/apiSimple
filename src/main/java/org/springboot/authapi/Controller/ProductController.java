package org.springboot.authapi.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springboot.authapi.AWSService.EcommersService;
import org.springboot.authapi.Request.ProductRequest;
import org.springboot.authapi.ResponseEnities.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ProductController {

    @Autowired private EcommersService ecommersService;

    @PostMapping("/createProduct")
    public ResponseEntity<ProductResponse> addProduct(@RequestPart("product") String productString,
                                      @RequestPart("file") MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = null;
        try {
            productRequest=objectMapper.readValue(productString,ProductRequest.class);
        }catch (JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid product data");
        }
        ProductResponse productResponse = ecommersService.addProduct(productRequest,file);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(ecommersService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(ecommersService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Integer id) {
        ecommersService.deleteFileNameById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(
            @PathVariable Integer id,
            @RequestPart("product") String productString,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest;

        try {
            productRequest = objectMapper.readValue(productString, ProductRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product data");
        }

        ProductResponse updatedProduct = ecommersService.updateProduct(id, productRequest, file);
        return ResponseEntity.ok(updatedProduct);
    }

}

//ObjectMapper :
//  1-Deserialize JSON string → Java object using readValue()
//  2-Serialize Java object → JSON string using writeValueAsString()