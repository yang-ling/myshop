package ling.yang.myshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ling.yang.myshop.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {

}
