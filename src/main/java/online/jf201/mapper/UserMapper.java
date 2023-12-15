package online.jf203.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.jf203.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}