package online.jf201.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.jf201.entity.User;
import online.jf201.mapper.UserMapper;
import online.jf201.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
