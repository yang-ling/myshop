package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.mapper.CartMapper;
import ling.yang.myshop.service.ICartService;
import ling.yang.myshop.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
            throw new RuntimeException("No Product Found!");
        }
        Product product = optById.get();
        if (product.getAmount() < entity.getAmount()) {
            throw new RuntimeException("Not Enough Products!");
        }
        this.save(entity);
        return entity;
    }

    @Override
    public void validateUpdate(int cartId, CartVo vo) {
        Optional<Product> optById = productService.getOptById(vo.getProductId());
        if (optById.isEmpty()) {
            throw new RuntimeException("No Product Found!");
        }
        Product product = optById.get();
        if (product.getAmount() < vo.getAmount()) {
            throw new RuntimeException("Not Enough Products!");
        }

        Cart entity = Cart.builder()
                          .id(cartId)
                          .productId(vo.getProductId())
                          .userId(vo.getUserId())
                          .amount(vo.getAmount())
                          .build();
        this.updateById(entity);
    }
}
