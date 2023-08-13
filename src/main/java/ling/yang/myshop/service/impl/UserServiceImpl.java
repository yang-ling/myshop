package ling.yang.myshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ling.yang.myshop.entity.User;
import ling.yang.myshop.mapper.UserMapper;
import ling.yang.myshop.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
