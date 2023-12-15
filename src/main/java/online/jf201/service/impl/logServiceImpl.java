package online.jf201.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.jf201.entity.log;
import online.jf201.mapper.logMapper;
import online.jf201.service.logService;
import org.springframework.stereotype.Service;

@Service
public class logServiceImpl extends ServiceImpl <logMapper, log> implements logService {
}
