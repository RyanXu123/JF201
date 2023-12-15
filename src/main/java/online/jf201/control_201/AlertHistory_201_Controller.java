package online.jf201.control_201;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import online.jf201.entity.Alert;
import online.jf201.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class AlertHistory_201_Controller {
    @Autowired
    private AlertService alertservice;
    @CrossOrigin
    @RequestMapping("/getData/201/alert_history")
    @ResponseBody
    public List<Alert> alert_history(){
        LambdaQueryWrapper<Alert> andWrapper = new LambdaQueryWrapper<>();
        andWrapper.last("limit 1000");
        List <Alert> list =alertservice.list(andWrapper);
//        return new HashMap<>();
        return list;
    }

    @CrossOrigin
    @PostMapping("/getData/201/alert_history")
    @ResponseBody
    public List<Alert> alert_history(@RequestBody List<String> data){
        String start_time=data.get(0);
        String end_time=data.get(1);
        LambdaQueryWrapper<Alert> andWrapper = new LambdaQueryWrapper<>();
        andWrapper.ge(Alert::getSampleTime,start_time).lt(Alert::getSampleTime,end_time);
        List <Alert> list =alertservice.list(andWrapper);
//        return new HashMap<>();
        return list;
    }

}
