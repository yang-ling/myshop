package ling.yang.myshop.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.exceptions.MyShopException;
import ling.yang.myshop.service.ICartService;
import ling.yang.myshop.service.IProductService;
import ling.yang.myshop.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;

/**
 * Cart Controller
 *
 * @author yangling
 */

@Tag(name = "Cart")
@RestController
@RequestMapping(value = "/api/v1/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CartController {
    private final ICartService cartService;
    private final IProductService productService;
    private final IUserService userService;

    @Operation(summary = "List all cart items", description = "List all cart items")
    @GetMapping("/{userId}")
    public List<CartVo> listCart(@PathVariable int userId) {
        return cartService.lambdaQuery()
                          .eq(Cart::getUserId, userId)
                          .list()
                          .stream()
                          .map(e -> {
                              Product product = productService.getById(e.getProductId());
                              User user = userService.getById(e.getUserId());
                              return CartVo.of(e, product, user);
                          })
                          .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/{cartId}")
    public CartVo oneCartItem(@PathVariable int userId, @PathVariable int cartId) {
        return cartService.getOptById(cartId)
                          .map(e -> {
                              if (e.getUserId() != userId) {
                                  return null;
                              }
                              Product product = productService.getById(e.getProductId());
                              User user = userService.getById(e.getUserId());
                              return CartVo.of(e, product, user);
                          })
                          .orElseThrow(() -> new MyShopException(CART_ITEM_NOT_FOUND));
    }

    @PostMapping
    public CartVo addCartItem(@RequestBody CartVo vo) {
        Cart entity = Cart.builder()
                          .productId(vo.getProductId())
                          .userId(vo.getUserId())
                          .amount(vo.getAmount())
                          .build();
        Cart newCart = cartService.validateSave(entity);
        Product product = productService.getById(newCart.getProductId());
        User user = userService.getById(newCart.getUserId());
        return CartVo.of(newCart, product, user);
    }

    @DeleteMapping("/{userId}/{cartId}")
    public boolean removeCartItem(@PathVariable int userId, @PathVariable int cartId) {
        Optional<Cart> optById = cartService.getOptById(cartId);
        if (optById.isEmpty()) {
            return false;
        }
        if (optById.get()
                   .getUserId() != userId) {
            return false;
        }
        return cartService.removeById(cartId);
    }

    @PutMapping("/{userId}/{cartId}")
    public boolean updateCartItem(@PathVariable int userId, @PathVariable int cartId, @RequestBody CartVo vo) {
        Optional<Cart> optById = cartService.getOptById(cartId);
        if (optById.isEmpty()) {
            return false;
        }
        if (optById.get()
                   .getUserId() != userId) {
            return false;
        }
        return cartService.validateUpdate(cartId, vo.withUserId(userId));
    }
}
