package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.entity.OrderDetail;
import ling.yang.myshop.mapper.OrderDetailMapper;
import ling.yang.myshop.service.IOrderDetailService;
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
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
