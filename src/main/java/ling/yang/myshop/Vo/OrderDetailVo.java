package ling.yang.myshop.Vo;

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
}
