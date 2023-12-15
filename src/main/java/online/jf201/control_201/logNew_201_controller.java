package online.jf201.control_201;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import online.jf201.entity.log;
import online.jf201.service.logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class logNew_201_controller {
    @Autowired
    private logService logservice;
    @CrossOrigin
    @RequestMapping("/getData/201/logs/out-1")
    @ResponseBody
    public List<log> LogsSelect(){
        LambdaQueryWrapper<log> andWrapper = new LambdaQueryWrapper<>();
        andWrapper.last("limit 1000");
        List<log> list = logservice.list(andWrapper);
        return list;
    }

    /***************/

    @CrossOrigin
    @PostMapping("/getData/201/logs-1")
    @ResponseBody
    public List<log> logsSelectOne(@RequestBody List<String> data) {
        String start_time = data.get(0);
        String end_time = data.get(1);

        LambdaQueryWrapper<log> andWrapper = new LambdaQueryWrapper<>();
        andWrapper.between(log::getTime, start_time, end_time);
        List<log> list = logservice.list(andWrapper);
        return list;
    }

}

