package ling.yang.myshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import ling.yang.myshop.Vo.ProductVo;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Data
@With
@Builder
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private BigDecimal price;

    private Integer amount;

    @Version
    private int version;

    @TableLogic
    private int deleted;

    private LocalDateTime updated;

    public static Product of(ProductVo vo) {
        return Product.builder()
                      .id(vo.getId())
                      .name(vo.getName())
                      .price(vo.getPrice())
                      .amount(vo.getAmount())
                      .build();
    }
}
