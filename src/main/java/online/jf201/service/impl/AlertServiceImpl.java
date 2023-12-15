package online.jf203.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.jf203.entity.Alert;
import online.jf203.mapper.AlertMapper;
import online.jf203.service.AlertService;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl extends ServiceImpl<AlertMapper, Alert> implements AlertService {
}
