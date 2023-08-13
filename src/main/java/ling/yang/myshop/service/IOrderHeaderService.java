package ling.yang.myshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ling.yang.myshop.entity.OrderHeader;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
public interface IOrderHeaderService extends IService<OrderHeader> {

    String validateSave(int userId, List<Integer> cartIds);

    void validatePay(int userId, String orderNo);

    void cancelOrder(int userId, String orderNo);
}
