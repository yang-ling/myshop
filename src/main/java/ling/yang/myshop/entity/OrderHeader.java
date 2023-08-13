package ling.yang.myshop.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import ling.yang.myshop.entity.enums.OrderStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
@TableName("order_header")
public class OrderHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String orderNo;

    private Integer userId;

    private String cardNo;

    private LocalDate expiryDate;

    private String cvcNo;

    @EnumValue
    private OrderStatus status;

    @Version
    private Integer version;

    @TableLogic
    private int deleted;

    private LocalDateTime updated;

}
