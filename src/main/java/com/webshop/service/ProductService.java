package com.webshop.service;

import com.webshop.configuration.FileUploadUtil;
import com.webshop.model.Category;
import com.webshop.model.Image;
import com.webshop.model.Product;
import com.webshop.model.dto.ProductsTotalPages;
import com.webshop.model.exceptions.ProductNotFoundException;
import com.webshop.projections.ProductProjection;
import com.webshop.repository.ImageRepository;
import com.webshop.repository.ParentCategoryRepository;
import com.webshop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    private final ImageRepository imageRepository;

    public ProductService(CategoryService categoryService, ProductRepository productRepository, ParentCategoryRepository parentCategoryRepository, ImageRepository imageRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.parentCategoryRepository = parentCategoryRepository;
        this.imageRepository = imageRepository;
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public List<Product> findAllWithImages() {
        return this.productRepository.findAllWithImages();
    }

    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<ProductProjection> findAllValidProductProjection() {
        return this.productRepository.findAllValidProductProjection();
    }

    public List<Product> findAllByCategoryId(Long categoryId) {
        return this.productRepository.findAllByCategoryId(categoryId);
    }

    public List<Product> findAllByIsOnDiscount() {
        return this.productRepository.findAllByIsOnDiscount(true);
    }

    public List<Product> findAllDealOfTheDayProducts() {
        return this.productRepository.findAllByIsDealOfTheDay(true);
    }

    private boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg");
    }


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
        if (!hoverPhoto.isEmpty())
            this.imageRepository.save(hoverImage);
        this.imageRepository.saveAll(imagesList);


        Product product = new Product(name, code, description, stocks,
                price, discountPrice, isNew, isOnDiscount, isDealOfTheDay,
                initialImage, !hoverPhoto.isEmpty() ? hoverImage : null, imagesList, category);

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

    public void deleteProduct(Long id) {
        Product product = this.findById(id);
        if (product != null) {
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

    public List<Product> findAllByCategoryUrlAndParentCategoryUrl(String parentCategoryUrl, String categoryUrl, String sort, Integer page, Integer pageSize, String priceInterval) {

        Pageable pageable;
        if (sort.equals("cena-najvisoka-prvo")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("price").descending());
        } else if (sort.equals("cena-najniska-prvo")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("price").ascending());
        } else {
            pageable = PageRequest.of(page, pageSize);
        }

        priceInterval = priceInterval.replaceAll(" ", "");
        Integer from;
        Integer to;
        try {
            from = Integer.parseInt(priceInterval.split("-")[0]);
        } catch (Exception ex) {
            from = 0;
        }
        try {
            to = Integer.parseInt(priceInterval.split("-")[1]);
        } catch (Exception ex) {
            to = findBiggestProductPriceByCategoryUrlAndParentCategoryUrl(categoryUrl, parentCategoryUrl);
        }

        List<Product> productList = this.productRepository.findAllByCategoryUrlNameAndCategoryParentCategoryUrlAndDiscountPriceBetween(pageable, categoryUrl, parentCategoryUrl, from, to).toList();

        return productList;
    }

    public Integer findTotalPagesByCategoryUrlAndParentCategoryUrl(String categoryUrl, String parentCategoryUrl, Integer pagesize, String priceInterval) {
        priceInterval = priceInterval.replaceAll(" ", "");
        Integer from;
        Integer to;
        try {
            from = Integer.parseInt(priceInterval.split("-")[0]);
        } catch (Exception ex) {
            from = 0;
        }
        try {
            to = Integer.parseInt(priceInterval.split("-")[1]);
        } catch (Exception ex) {
            to = findBiggestProductPriceByCategoryUrlAndParentCategoryUrl(categoryUrl, parentCategoryUrl);
        }

        return (int) Math.ceil(1.0d * this.productRepository.findTotalPagesByCategoryUrlNameAndCategoryParentCategoryUrl(categoryUrl, parentCategoryUrl, from, to) / pagesize);
    }

    public Integer findBiggestProductPriceByCategoryUrlAndParentCategoryUrl(String categoryUrl, String parentCategoryUrl) {
        return this.productRepository.findAllByCategoryUrlNameAndCategoryParentCategoryUrl(categoryUrl, parentCategoryUrl)
                .stream()
                .mapToInt(Product::calculateDiscountPrice)
                .max()
                .getAsInt();
    }


    public List<Product> findRelatedProducts(Long productId, Long categoryId) {
        return this.productRepository.findTop10ByIdNotAndCategoryId(productId, categoryId);
    }

    public List<Product> findAllByParentCategoryUrl(String parentCategoryUrl, String sort, Integer page, Integer pagesize) {
        return this.productRepository.findAllByCategoryParentCategoryUrl(parentCategoryUrl);
    }
}
