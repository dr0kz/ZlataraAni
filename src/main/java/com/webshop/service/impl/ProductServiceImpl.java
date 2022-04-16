package com.webshop.service.impl;

import com.webshop.configuration.FileUploadUtil;
import com.webshop.model.Category;
import com.webshop.model.Image;
import com.webshop.model.ParentCategory;
import com.webshop.model.Product;
import com.webshop.model.exceptions.ProductNotFoundException;
import com.webshop.projections.ProductProjection;
import com.webshop.repository.ImageRepository;
import com.webshop.repository.ParentCategoryRepository;
import com.webshop.repository.ProductRepository;
import com.webshop.service.CategoryService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    private final ImageRepository imageRepository;

    public ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository, ParentCategoryRepository parentCategoryRepository, ImageRepository imageRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.parentCategoryRepository = parentCategoryRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> findAllWithImages() {
        return this.productRepository.findAllWithImages();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<ProductProjection> findAllValidProductProjection() {
        return this.productRepository.findAllValidProductProjection();
    }

    @Override
    public List<Product> findAllByCategoryId(Long categoryId) {
        return this.productRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Product> findAllByIsOnDiscount() {
        return this.productRepository.findAllByIsOnDiscount(true);
    }

    @Override
    public List<Product> findAllDealOfTheDayProducts() {
        return this.productRepository.findAllByIsDealOfTheDay(true);
    }

    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }


    @Override
    public Product createProduct(String name,
                                 String code,
                                 String description,
                                 Integer stocks,
                                 Integer price,
                                 Integer discountPrice,
                                 boolean isNew,
                                 boolean isOnDiscount,
                                 boolean isDealOfTheDay,
                                 Long categoryId,
                                 MultipartFile initialPhoto,
                                 MultipartFile hoverPhoto,
                                 MultipartFile[] images) throws IOException {

        Category category = this.categoryService.findById(categoryId);


        byte[] initialPhotoBytes = initialPhoto.getBytes();
        byte[] hoverPhotoBytes = hoverPhoto.getBytes();
        List<byte[]> imagesBytes = new ArrayList<>();
        for (MultipartFile multipartFile : images) {
            imagesBytes.add(multipartFile.getBytes());
        }
        Image initialImage = new Image(initialPhotoBytes);
        Image hoverImage = new Image(hoverPhotoBytes);
        List<Image> imagesList = new ArrayList<>();
        imagesBytes.forEach(t -> imagesList.add(new Image(t)));

        this.imageRepository.save(initialImage);
        this.imageRepository.save(hoverImage);
        this.imageRepository.saveAll(imagesList);


        List<String> fileNames = Arrays.stream(images).map(image -> getFileWithSizeAdded(image,"600x600")).collect(Collectors.toList());

        Product product = new Product(name, code, description, stocks,
                price, discountPrice, isNew, isOnDiscount, isDealOfTheDay,
                initialImage, hoverImage, imagesList, category);

        this.productRepository.save(product);

        return product;
    }

    private String getFileWithSizeAdded(MultipartFile image, String size) {

        StringBuilder sb = new StringBuilder();

        String[] parts = StringUtils
                .cleanPath(Objects.requireNonNull(image.getOriginalFilename()))
                .replaceAll(" ", "")
                .split("\\.");

        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1)
                sb.append(size).append(".");
            sb.append(parts[i]);
        }

        return sb.toString();
    }

    private List<MultipartFile> filterFiles(MultipartFile[] files, Product product) {
        return Arrays.stream(files)
                .filter(t -> t.getOriginalFilename() != null)
                .filter(t -> isImage(t.getOriginalFilename()))
                .filter(t -> product.getImages()
                        .stream()
                        .noneMatch(k -> k.equals(t.getOriginalFilename())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = this.findById(id);
        if(product!=null){
            this.productRepository.delete(product);
        }
    }

    private void saveFiles(String uploadDir, List<String> fileNames, List<MultipartFile> images) {
        IntStream.range(0, fileNames.size()).forEach(t -> {
            try {
                FileUploadUtil.saveFile(uploadDir, fileNames.get(t), images.get(t));
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

    @Override
    public Product editProduct(Long id,
                               String name,
                               String code,
                               String description,
                               Integer stocks,
                               Integer price,
                               Integer discountPrice,
                               boolean isNew,
                               boolean isOnDiscount,
                               boolean isDealOfTheDay,
                               Long categoryId,
                               MultipartFile initialPhoto,
                               MultipartFile hoverPhoto,
                               MultipartFile[] images) {

        Product product = this.findById(id);
        Category category = this.categoryService.findById(categoryId);
        product.setName(name);
        product.setCode(code);
        product.setDescription(description);
        product.setStocks(stocks);
        product.setPrice(price);
        product.setDiscountPercentage(discountPrice);
        product.setIsNew(isNew);
        product.setIsOnDiscount(isOnDiscount);
        product.setIsDealOfTheDay(isDealOfTheDay);
        product.setCategory(category);

//        if(!images[0].isEmpty()){
//            List<MultipartFile> multipartFiles = filterFiles(images, product);
//            product.getImages().stream().map(t ->Product.UPLOAD_DIR + "/" + id+"/"+t).forEach(FileUploadUtil::deleteFile);
//            List<String> fileNames = multipartFiles.stream().map(image -> getFileWithSizeAdded(image,"600x600")).collect(Collectors.toList()); //get filenames for the newly added images
//            product.setImages(fileNames);
//            saveFiles(Product.UPLOAD_DIR + "/" + id, fileNames,  multipartFiles);
//        }
//        if(!initialPhoto.isEmpty()){
//            FileUploadUtil.deleteFile(Product.UPLOAD_DIR +"/"+id+"/"+product.getInitialPhoto());
//            String fileNameInitialPhoto = getFileWithSizeAdded(initialPhoto, "300x300");
//            product.setInitialPhoto(fileNameInitialPhoto);
//            saveFiles(Product.UPLOAD_DIR + "/" + id, List.of(fileNameInitialPhoto), List.of(initialPhoto));
//        }
//        if(!hoverPhoto.isEmpty()){
//            if(product.getHoverPhoto()!=null){
//                FileUploadUtil.deleteFile(Product.UPLOAD_DIR +"/"+id+"/"+product.getHoverPhoto());
//            }
//            String fileNameHoverPhoto = getFileWithSizeAdded(hoverPhoto, "300x300");
//            product.setHoverPhoto(fileNameHoverPhoto);
//            saveFiles(Product.UPLOAD_DIR + "/" + id, List.of(fileNameHoverPhoto), List.of(hoverPhoto));
//        }

        this.productRepository.save(product);

        return product;
    }

    @Override
    public List<Product> findAllByCategoryUrlAndParentCategoryUrl(String parentCategoryUrl, String categoryUrl) {
        ParentCategory parentCategory = this.parentCategoryRepository.findByUrl(parentCategoryUrl).get();
        return this.productRepository.findAllByCategoryUrlNameAndCategoryParentCategory(categoryUrl, parentCategory);
    }

    @Override
    public List<Product> findRelatedProducts(Long productId, Long categoryId) {
        return this.productRepository.findTop10ByIdNotAndCategoryId(productId, categoryId);
    }

    @Override
    public List<Product> findAllByParentCategoryUrl(String parentCategoryUrl) {
        return this.productRepository.findAllByCategoryParentCategoryUrl(parentCategoryUrl);
    }
}
