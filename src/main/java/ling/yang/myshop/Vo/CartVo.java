package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.Cart;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVo {

    private Integer id;

    private Integer productId;

    private String productName;
    private BigDecimal price;
    private BigDecimal totalPrice;

    private Integer userId;
    private String userName;

    private Integer amount;

    public static CartVo of(Cart e, Product product, User user) {
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
    }
}
