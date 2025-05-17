package org.springboot.authapi.AWSService;

import org.springboot.authapi.Request.ProductRequest;
import org.springboot.authapi.ResponseEnities.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EcommersService {
    String uploadFile(MultipartFile file);

    ProductResponse addProduct(ProductRequest productRequest, MultipartFile file);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Integer productId);

    boolean deleteFileName(String fileName);

    void deleteFileNameById(Integer id);

    ProductResponse updateProduct(Integer id,ProductRequest productRequest, MultipartFile file);
}
