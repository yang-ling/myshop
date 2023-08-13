package ling.yang.myshop.Vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartVo {

    private Integer id;

    private Integer productId;

    private String productName;
    private BigDecimal price;
    private BigDecimal totalPrice;

    private Integer userId;
    private String userName;

    private Integer amount;
}
