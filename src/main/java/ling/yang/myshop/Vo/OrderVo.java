package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderVo {
    private Integer id;

    private String orderNo;

    private Integer userId;

    private String userName;

    private LocalDate expiryDate;

    private String cvcNo;

    private OrderStatus status;

    private BigDecimal grandTotalPrice;

    List<OrderDetailVo> details;
}
