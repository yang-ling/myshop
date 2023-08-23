package ling.yang.myshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ling.yang.myshop.Vo.CartVo;
import ling.yang.myshop.entity.Cart;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ling Yang
 * @since 2023-08-13
 */
public interface ICartService extends IService<Cart> {

    Cart validateSave(Cart entity);

    boolean validateUpdate(int cartId, CartVo vo);
}
