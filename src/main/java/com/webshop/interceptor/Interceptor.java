package com.webshop.interceptor;

import com.webshop.model.Category;
import com.webshop.model.OrderCart;
import com.webshop.model.ParentCategory;
import com.webshop.model.ProductInOrderCart;
import com.webshop.model.exceptions.OrderCartNotFoundException;
import com.webshop.service.CategoryService;
import com.webshop.service.OrderCartService;
import com.webshop.service.ParentCategoryService;
import com.webshop.service.ProductInOrderCartService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class Interceptor implements HandlerInterceptor {
    private final OrderCartService orderCartService;
    private final ParentCategoryService parentCategoryService;
    private final CategoryService categoryService;
    private final ProductInOrderCartService productInOrderCartService;

    Interceptor(OrderCartService orderCartService, ParentCategoryService parentCategoryService, CategoryService categoryService, ProductInOrderCartService productInOrderCartService) {
        this.orderCartService = orderCartService;
        this.parentCategoryService = parentCategoryService;
        this.categoryService = categoryService;
        this.productInOrderCartService = productInOrderCartService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        Cookie cookie;
        if (cookies == null || Arrays.stream(cookies).noneMatch(t -> t.getName().equals("CART"))) {
            cookie = generateCookie();
//            cookie.setDomain("z-ani.herokuapp.com");
            cookie.setPath("/");
            response.addCookie(cookie);
            response.addHeader("COOKIE", "" + cookie.getValue());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !request.getRequestURI().contains("/admin")) {
            Map<ParentCategory, List<Category>> map = new HashMap<>();
            this.parentCategoryService.findAll().forEach(parentCategory -> {
                map.put(parentCategory, this.categoryService.findAllCategoryNamesByParentCategory(parentCategory.getId()));
            });
            modelAndView.getModelMap().addAttribute("categoriesMap", map);
            if (response.getHeader("COOKIE") != null) {
                Long orderCartId = Long.parseLong(response.getHeader("COOKIE"));
                HandleCookie(modelAndView, orderCartId);
            } else if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(t -> t.getName().equals("CART"))) {
                Long orderCartId = Long.parseLong(Arrays.stream(request.getCookies()).filter(t -> t.getName().equals("CART")).findFirst().get().getValue());
                HandleCookie(modelAndView, orderCartId);
            }
        }
    }

    private void HandleCookie(@Nullable ModelAndView modelAndView, Long orderCartId) {
        OrderCart orderCart = this.orderCartService.findOrderCartById(orderCartId);
        modelAndView.getModelMap().addAttribute("isCookieAccepted", orderCart.getIsAccepted());
        List<ProductInOrderCart> productInOrderCarts = productInOrderCartService.findAllProductsInOrderCart(orderCart);

        modelAndView.getModelMap().addAttribute("productsInCart", productInOrderCarts);
        modelAndView.getModelMap().addAttribute("totalPrice",getTotal(productInOrderCarts));
        modelAndView.getModelMap().addAttribute("productsInCartSize", productInOrderCarts.size());
    }

    private long getTotal(List<ProductInOrderCart> productsInOrderCart) {
        return productsInOrderCart.stream()
                .mapToInt(p -> p.getProduct().calculateDiscountPrice() * p.getQuantity())
                .sum();
    }

    private Cookie generateCookie() {
        OrderCart orderCart = this.orderCartService.saveOrderCart(new OrderCart());
        Cookie cookie = new Cookie("CART", orderCart.getId().toString());
        cookie.setMaxAge(2 * 24 * 60 * 60);
        return cookie;
    }
}