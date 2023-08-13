package ling.yang.myshop.web;

import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.service.ICartService;
import ling.yang.myshop.service.IProductService;
import ling.yang.myshop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;
    private final IProductService productService;
    private final IUserService userService;

    @GetMapping
    public List<CartVo> listCart() {
        return cartService.list()
                          .stream()
                          .map(e -> {
                              Product product = productService.getById(e.getProductId());
                              User user = userService.getById(e.getUserId());
                              return CartVo.builder()
                                           .id(e.getId())
                                           .productId(e.getProductId())
                                           .productName(product.getName())
                                           .price(product.getPrice())
                                           .amount(e.getAmount())
                                           .totalPrice(product.getPrice()
                                                              .multiply(new BigDecimal(e.getAmount())))
                                           .userId(e.getUserId())
                                           .userName(user.getName())
                                           .build();
                          })
                          .collect(Collectors.toList());
    }

    @GetMapping("/{cartId}")
    public CartVo oneCartItem(@PathVariable int cartId) {
        return cartService.getOptById(cartId)
                          .map(e -> {
                              Product product = productService.getById(e.getProductId());
                              User user = userService.getById(e.getUserId());
                              return CartVo.builder()
                                           .id(e.getId())
                                           .productId(e.getProductId())
                                           .productName(product.getName())
                                           .price(product.getPrice())
                                           .amount(e.getAmount())
                                           .totalPrice(product.getPrice()
                                                              .multiply(new BigDecimal(e.getAmount())))
                                           .userId(e.getUserId())
                                           .userName(user.getName())
                                           .build();
                          })
                          .orElse(null);
    }

    @PostMapping
    public int addCartItem(@RequestBody CartVo vo) {
        Cart entity = Cart.builder()
                          .productId(vo.getProductId())
                          .userId(vo.getUserId())
                          .amount(vo.getAmount())
                          .build();
        Cart newCart = cartService.validateSave(entity);
        return newCart.getId();
    }

    @DeleteMapping("/{cartId}")
    public void removeCartItem(@PathVariable int cartId) {
        Optional<Cart> optById = cartService.getOptById(cartId);
        if (optById.isEmpty()) {
            return;
        }
        cartService.removeById(cartId);
    }

    @PutMapping("/{cartId}")
    public void updateCartItem(@PathVariable int cartId, @RequestBody CartVo vo) {
        Optional<Cart> optById = cartService.getOptById(cartId);
        if (optById.isEmpty()) {
            return;
        }
        cartService.validateUpdate(cartId, vo);
    }
}
