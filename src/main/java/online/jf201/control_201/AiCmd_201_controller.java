package online.jf201.control_201;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AiCmd_201_controller {
    @Autowired
    private JdbcTemplate jdbc;


    List<Map<String,Object>> list_cmd = new ArrayList<>();//AI指令

    @CrossOrigin
    @RequestMapping("/getData/201/aicmd_history")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public List<Map<String,Object>> getdata201_aicmd_history(){
        String sql = "select * from aicmd where CommandType='群控控制' OR CommandType='保底控制' OR CommandType='预控控制' ";
//        String sql2 = "select * from aicmd where CommandType='保底控制' ";
        List<Map<String, Object>> list = jdbc.queryForList(sql);

        return list;

    }
    @CrossOrigin
    @PostMapping("/getData/201/aicmd_history")
    @ResponseBody
    public List<Map<String, Object>> getdata201_aicmd_history(@RequestBody List<String> data) {
        String start_time = data.get(0);
        String end_time = data.get(1);

        String sql = "SELECT * FROM aicmd WHERE (CommandType='群控控制' OR CommandType='保底控制' OR CommandType='预控控制') AND time BETWEEN ? AND ?";
        //     String sql = "SELECT * FROM aicmd WHERE (CommandType='群控控制' OR CommandType='保底控制' OR CommandType='预控控制') AND time BETWEEN '" + start_time + "' AND '" + end_time + "'";
        List<Map<String, Object>> list = jdbc.queryForList(sql, start_time, end_time);

        return list;
    }

    @CrossOrigin
    @PostMapping("/getData/201/aicmd_select")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public List<Map<String,Object>> getdata201_aicmd_select(@RequestBody Map<String,String> data){
        String sql = "select * from aicmd where CommandType='群控控制' OR CommandType='保底控制' OR CommandType='预控控制' ";
//        String sql2 = "select * from aicmd where CommandType='保底控制' ";
        List<Map<String, Object>> list = jdbc.queryForList(sql);

        return list;

    }
    @CrossOrigin
    @RequestMapping("/getData/201/aicmd")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public List<Map<String,Object>> getdata201_aicmd(){
        String sql="select * from aicmd where CommandType <> '心跳控制' and time = ( select MAX(time) from aicmd where CommandType <> '心跳控制')" ;
//        String sql2="select * from aicmd where CommandType='保底控制' " ;
        List <Map<String,Object>> list=jdbc.queryForList(sql);
//        List <Map<String,Object>> list2=jdbc.queryForList(sql2);
//        Map<String,Object> ret= new HashMap<>();
//        Integer cnt_beat=0;
//        Integer cnt_null=0;
//        List<Map<String,Object>> list_temp_cmd = new ArrayList<>();  //AI指令寄存器
        return list;
    }

}
