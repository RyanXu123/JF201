package online.jf201.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.jf201.entity.Alert;
import online.jf201.mapper.AlertMapper;
import online.jf201.service.AlertService;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl extends ServiceImpl<AlertMapper, Alert> implements AlertService {
}
