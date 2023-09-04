package ling.yang.myshop.Vo;

import ling.yang.myshop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
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
