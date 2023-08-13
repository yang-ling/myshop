package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.entity.Product;
import ling.yang.myshop.mapper.ProductMapper;
import ling.yang.myshop.service.IProductService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
