package com.webshop.service;

import com.webshop.model.OrderCart;
import com.webshop.model.Product;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.dto.OrderCartProductsDto;
import com.webshop.model.dto.ProductQuantityDto;
import com.webshop.repository.OrderCartRepository;
import com.webshop.repository.ProductInOrderCartRepository;
import com.webshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductInOrderCartService {

    private final ProductInOrderCartRepository productInOrderCartRepository;
    private final ProductRepository productRepository;
    private final OrderCartRepository orderCartRepository;

    public ProductInOrderCartService(ProductInOrderCartRepository productInOrderCartRepository, ProductRepository productRepository, OrderCartRepository orderCartRepository) {
        this.productInOrderCartRepository = productInOrderCartRepository;
        this.productRepository = productRepository;
        this.orderCartRepository = orderCartRepository;
    }


    public ProductInOrderCart create(OrderCart orderCart, Product product, int quantity) {
        return this.productInOrderCartRepository.save(new ProductInOrderCart(orderCart, product, quantity));
    }

    public List<ProductInOrderCart> findAllProductsInOrderCart(OrderCart orderCart) {
        return this.productInOrderCartRepository.findAllByOrderCart(orderCart);
    }

    public Optional<ProductInOrderCart> findByProductAndOrderCart(Product product, OrderCart orderCart) {
        return this.productInOrderCartRepository.findProductInOrderCartByProductAndOrderCart(product, orderCart);
    }

    public int deleteProduct(Long productId, Long orderCartId) {
        Product product = this.productRepository.findById(productId).get();
        OrderCart orderCart = this.orderCartRepository.findById(orderCartId).get();
        ProductInOrderCart p = this.productInOrderCartRepository.findProductInOrderCartByProductAndOrderCart(product, orderCart).get();
        this.productInOrderCartRepository.delete(p);
        return this.productInOrderCartRepository.findAllByOrderCart(orderCart).size();
    }

    public void updateQuantity(ProductInOrderCart productInOrderCart, int quantity) {
        int currentQuantity = productInOrderCart.getQuantity();
        if (currentQuantity + quantity > productInOrderCart.getProduct().getStocks())
            productInOrderCart.setQuantity(productInOrderCart.getProduct().getStocks());
        else
            productInOrderCart.setQuantity(currentQuantity + quantity);
        this.productInOrderCartRepository.save(productInOrderCart);
    }

    public void updateQuantityOrderCart(ProductInOrderCart productInOrderCart, int quantity) {
        if (quantity > productInOrderCart.getProduct().getStocks())
            productInOrderCart.setQuantity(productInOrderCart.getProduct().getStocks());
        else
            productInOrderCart.setQuantity(quantity);
        this.productInOrderCartRepository.save(productInOrderCart);
    }

    public void updateAllProductsInOrderCart(OrderCartProductsDto orderCartProductsDto, Long orderCartId) {
        OrderCart orderCart = this.orderCartRepository.findById(orderCartId).get();
        List<Long> productIds = orderCartProductsDto.getProductIds().stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        List<Integer> quantities = orderCartProductsDto.getProductQuantities().stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        List<ProductQuantityDto> productsWithQuantity = new ArrayList<>();
        List<Product> products = this.productRepository.findAllById(productIds);

        products.forEach(
                product -> {
                    int index = productIds.indexOf(productIds.stream().filter(id -> id == product.getId()).findFirst().get());
                    int productQuantity = quantities.get(index);
                    productsWithQuantity.add(new ProductQuantityDto(product, productQuantity));
                }
        );

        List<ProductInOrderCart> productsInOrderCart = this.productInOrderCartRepository.findAllByOrderCart(orderCart);

        productsInOrderCart.forEach(
                productInOrderCart -> {
                    int quantity = productsWithQuantity.stream()
                            .filter(productQuantityDto ->
                                    productQuantityDto.getProduct().getId() == productInOrderCart.getProduct().getId())
                            .findFirst().get().getQuantity();
                    this.updateQuantityOrderCart(productInOrderCart, quantity);
                }
        );

    }

    @Transactional
    public void deleteOrderCart(OrderCart orderCart){
        this.productInOrderCartRepository.deleteAllByOrderCart(orderCart);
    }
}
