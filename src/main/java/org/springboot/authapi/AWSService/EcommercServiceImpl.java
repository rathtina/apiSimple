package org.springboot.authapi.AWSService;
import org.springboot.authapi.Enities.Product;
import org.springboot.authapi.Repository.ProductRepository;
import org.springboot.authapi.Request.ProductRequest;
import org.springboot.authapi.ResponseEnities.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EcommercServiceImpl implements EcommersService{

    private final S3Client s3Client;
    private final String bucketName;

    @Autowired
    public EcommercServiceImpl(S3Client s3Client,
                               @Value("${aws.s3.bucketname}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Autowired private ProductRepository productRepository;

    @Override
    public String uploadFile(MultipartFile file) {
        String filenameExtension=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key= UUID.randomUUID().toString()+"."+filenameExtension;
        try {
            PutObjectRequest putObjectRequest=PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse putObjectResponse=s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(putObjectResponse.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
            }
        }catch (IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An error occoured while uploading the file");
        }

    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest, MultipartFile file) {
        if (productRepository.existsByName(productRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product with the same name already exists");
        }

        String imageUrl = uploadFile(file);

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(imageUrl);
        product.setCategory(productRequest.getCategory());
        product.setStock(productRequest.getStock());

        productRepository.save(product);

        return new ProductResponse(product);
    }


    @Override
    public List<ProductResponse> getAllProducts() {
       return productRepository.findAll()
               .stream()
               .map(ProductResponse::new)
               .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Integer productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + productId));

        return new ProductResponse(existingProduct);
    }

    @Override
    public boolean deleteFileName(String fileName) {
        DeleteObjectRequest deleteObjectRequest=DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFileNameById(Integer id) {
        ProductResponse product = getProductById(id);

        if (product.getImageUrl() == null || !product.getImageUrl().contains("/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image URL format.");
        }

        String fileName = product.getImageUrl().substring(product.getImageUrl().lastIndexOf("/") + 1);

        boolean isFileDeleted = deleteFileName(fileName);
        if (!isFileDeleted) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image from S3.");
        }

        productRepository.deleteById(product.getId());
    }

    @Override
    public ProductResponse updateProduct(Integer id, ProductRequest productRequest, MultipartFile file) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: " + id));
        if (file !=null && !file.isEmpty()){
            if (existingProduct.getImageUrl() == null || !existingProduct.getImageUrl().contains("/")) {
                String oldFileName=existingProduct.getImageUrl().substring(existingProduct.getImageUrl().lastIndexOf("/") + 1);
                deleteFileName(oldFileName);
            }
            String newFileName=uploadFile(file);
            existingProduct.setImageUrl(newFileName);
        }
        //update Fields
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setStock(productRequest.getStock());
        productRepository.save(existingProduct);
        return new ProductResponse(existingProduct);
    }


}

//1-PutObjectRequest 	Request to upload an object to S3
//2-PutObjectResponse Response received after the upload

//stream perform some operation such as
//1-map Transforms each element. Example: convert Product to ProductResponse.
//2-filter()	Filters elements by a condition.
//3-collect()	Gathers the results into a collection like List, Set, etc.
//4-forEach()	Performs an action for each element.
//5-sorted()	Sorts the stream elements.