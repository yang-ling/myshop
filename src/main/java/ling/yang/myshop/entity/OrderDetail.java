package ling.yang.myshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

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
@TableName("order_detail")
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private Integer cartId;

    private Integer productId;

    private Integer amount;

    private BigDecimal price;

    @Version
    private Integer version;

    @TableLogic
    private int deleted;

    private LocalDateTime updated;
}
