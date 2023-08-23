package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.exceptions.MyShopException;
import ling.yang.myshop.mapper.CartMapper;
import ling.yang.myshop.service.ICartService;
import ling.yang.myshop.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static ling.yang.myshop.exceptions.MyShopExceptionAttributes.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final IProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart validateSave(Cart entity) {
        Optional<Product> optById = productService.getOptById(entity.getProductId());
        if (optById.isEmpty()) {
            throw new MyShopException(PRODUCT_NOT_FOUND);
        }
        Product product = optById.get();
        if (product.getAmount() < entity.getAmount()) {
            throw new MyShopException(NOT_ENOUGH_PRODUCT);
        }
        Optional<Cart> cart = this.lambdaQuery()
                                  .eq(Cart::getUserId, entity.getUserId())
                                  .eq(Cart::getProductId, entity.getProductId())
                                  .oneOpt();
        if (cart.isEmpty()) {
            this.save(entity);
            return entity;
        }
        Cart newCart = cart.get()
                           .withAmount(cart.get()
                                           .getAmount() + entity.getAmount());
        if (product.getAmount() < newCart.getAmount()) {
            throw new MyShopException(NOT_ENOUGH_PRODUCT);
        }
        this.updateById(newCart);
        return newCart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validateUpdate(int cartId, CartVo vo) {
        Optional<Product> optById = productService.getOptById(vo.getProductId());
        if (optById.isEmpty()) {
            throw new MyShopException(PRODUCT_NOT_FOUND);
        }
        Product product = optById.get();
        if (product.getAmount() < vo.getAmount()) {
            throw new MyShopException(NOT_ENOUGH_PRODUCT);
        }
        Cart entity = this.getById(cartId);
        entity = entity.withAmount(vo.getAmount())
                       .withProductId(vo.getProductId())
                       .withUserId(vo.getUserId());

        return this.updateById(entity);
    }
}
