package online.jf203.control_203;

import online.jf203.entity.Sitecold;
import online.jf203.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Controller
public class AlertDesign_203_Controller {


    @Autowired
    private JdbcTemplate jdbc;

    Boolean data_abnormal_alert=true;//数据异常报警
    Boolean real_alert=false;//热点报警
    Boolean coldsite_alert=false;//冷通道波动报警

    Integer time_limit=6;
    @CrossOrigin
    @PostMapping("/getData/203/realdata/alert_design")
    @ResponseBody
    public List<Boolean> alert_design(@RequestBody List<Boolean>data ){
//        List<Double> ret=new ArrayList<>();
//        real_alert=data.get(0);
//        data_abnormal_alert=data.get(1);
        coldsite_alert=data.get(0);
        return data;
    }

    @CrossOrigin
    @RequestMapping("/getData/203/realdata/alert_design")
    @ResponseBody
    public List<Boolean> alert_design0(){
//        List<Double> ret=new ArrayList<>();
//        real_alert,data_abnormal_alert,
        return Arrays.asList(coldsite_alert);
    }


//    Integer time_limit=6;
    //    Integer time_limit=6;
    @CrossOrigin
    @PostMapping("/getData/203/dataStatus_time_limit_design")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public Integer time_limit_design(@RequestBody List<String> data) {
//        Integer TIME_design=time_limit*30;
        if (data.isEmpty()){
            return  time_limit/2;
        }
        Integer TIME_design=Integer.parseInt(data.get(0).toString())*2;
        time_limit=TIME_design;
        return  time_limit/2;
    }

    Map<Integer,String> alert_content = new HashMap<>();

    Integer cnt=0;
    @CrossOrigin
    @RequestMapping("/getData/203/alert")
    @ResponseBody
    public Map<String,Object> alert2(){

        Map<String,Object> b= new HashMap<>();
        List<List<String>> real= new ArrayList<>();
        List<List<String>> data_abnormal_detail= new ArrayList<>();
        List<List<String>> cold_list= new ArrayList<>();

//        <sitecold> find_list = new LambdaQueryWrapper<>();
//        find_list.allEq(null);

        String sql2="select * from predata where PointName='冷通道最大温度' ORDER BY id DESC limit 0,7"; //预测警告
        String sql3="select * from preshow where PointName='冷通道最大温度' ORDER BY id DESC limit 0,7"; //实时警告
        String sql_abnormal=" select * from  abnormal_detail where time=( select MAX(time) from abnormal_detail )";

        List <Map<String,Object>> list2=jdbc.queryForList(sql_abnormal);
        for (Map<String,Object> m:list2){
            data_abnormal_detail.add(Arrays.asList(m.get("time").toString(),"数据异常",m.get("Detail").toString()));
        }
        List <Map<String,Object>> list3=jdbc.queryForList(sql3);
        for (Map<String,Object> m:list3){
            if(Double.parseDouble(m.get("Value0").toString())>= 26.8){
                real.add(Arrays.asList(m.get("time").toString(),m.get("Equipment").toString().substring(3),m.get("PointName").toString()+"为"+String.format("%.2f",m.get("Value0"))+"°C"));
            }
        }

        List<List<String>> temp= new ArrayList<>();
        if(real_alert==true){//实时报警
            b.put("real_hot",real);
        }else{
            b.put("real_hot",temp);
        }
        if (data_abnormal_alert==true){//数据异常报警
            b.put("data_abnormal_detail",data_abnormal_detail);
        }else{
            b.put("data_abnormal_detail",temp);
        }

        if(coldsite_alert==true){//波动报警
            b.put("cold_change",cold_list);
//            cold_list.clear();
        }else{
            b.put("cold_change",temp);

        }


        String sql_data_alert="select * from data_alert ORDER BY id DESC limit 0,1"; //实时警告

        String sql_data_reasonable="select * from data_reasonable order by Value0 desc limit 6" ;
        sql_data_reasonable.replace("6",time_limit.toString());
//        String sql2="select * from aicmd where CommandType='保底控制' " ;
        List <Map<String,Object>> list_data_reasonable=jdbc.queryForList(sql_data_reasonable);
        Integer cnt=0;
        Integer data_alert=0;
        for(Map<String,Object> c : list_data_reasonable){
            cnt+=Integer.parseInt(c.get("Value0").toString());
        }
        if(cnt>=time_limit){
            data_alert=1;

        }
        b.put("data_alert",data_alert);
        return b;
    }

}
