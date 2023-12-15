package online.jf203.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.jf203.entity.User;
import online.jf203.mapper.UserMapper;
import online.jf203.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
