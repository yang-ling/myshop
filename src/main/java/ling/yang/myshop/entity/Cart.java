package ling.yang.myshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  Cart
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Data
public class Cart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer amount;

    @Version
    private Integer version;

    @TableLogic
    private int deleted;

    private LocalDateTime updated;

}