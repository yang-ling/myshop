package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.OrderDetail;
import ling.yang.myshop.entity.OrderHeader;
import ling.yang.myshop.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailVo {
    private Integer id;

    private Integer orderId;

    private Integer cartId;

    private Integer productId;

    private String productName;

    private Integer amount;

    private BigDecimal price;

    private BigDecimal totalPrice;

    public static OrderDetailVo of(OrderDetail d, OrderHeader h, Product product) {
        return OrderDetailVo.builder()
                            .id(d.getId())
                            .orderId(h.getId())
                            .cartId(d.getCartId())
                            .productName(product.getName())
                            .productId(d.getProductId())
                            .amount(d.getAmount())
                            .price(d.getPrice())
                            .totalPrice(d.getPrice()
                                         .multiply(new BigDecimal(d.getAmount())))
                            .build();
    }
}
