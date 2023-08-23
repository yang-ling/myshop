package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.OrderHeader;
import ling.yang.myshop.entity.User;
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

    public static OrderVo of(OrderHeader h, User user, List<OrderDetailVo> detailVos) {
        BigDecimal grandTotal = detailVos.stream()
                                         .map(OrderDetailVo::getTotalPrice)
                                         .reduce(BigDecimal::add)
                                         .orElse(BigDecimal.ZERO);
        return OrderVo.builder()
                      .id(h.getId())
                      .orderNo(h.getOrderNo())
                      .userId(h.getUserId())
                      .userName(user.getName())
                      .expiryDate(h.getExpiryDate())
                      .cvcNo(h.getCvcNo())
                      .status(h.getStatus())
                      .grandTotalPrice(grandTotal)
                      .details(detailVos)
                      .build();
    }
}
