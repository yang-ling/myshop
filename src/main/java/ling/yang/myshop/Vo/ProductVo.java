package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductVo {
    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer amount;

    public static ProductVo of(Product product) {
        return ProductVo.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .amount(product.getAmount())
                        .build();
    }
}
