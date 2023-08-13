package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.entity.OrderHeader;
import ling.yang.myshop.mapper.OrderHeaderMapper;
import ling.yang.myshop.service.IOrderHeaderService;
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
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements IOrderHeaderService {

}
