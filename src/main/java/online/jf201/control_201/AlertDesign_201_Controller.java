package online.jf201.control_201;

import online.jf201.entity.Alert;
import online.jf201.service.AlertService;
import online.jf201.entity.log;
import online.jf201.mapper.logMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Controller
public class AlertDesign_201_Controller {


    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private logMapper logMapper;
    @Autowired
    private AlertService alertservice;

    

    Boolean data_abnormal_alert=true;//数据异常报警
    Boolean real_alert=false;//热点报警
    Boolean coldsite_alert=false;//冷通道波动报警
    

    Integer time_limit=6;
    Double cold_unstable_fixed_time = 10.0;
    Double cold_unstable_fixed_range = 3.0;
    
    @CrossOrigin
    @PostMapping("/getData/201/realdata/alert_design")
    @ResponseBody
    public List<Object> alertDesign(@RequestBody List<Object> data) {
        String coldsiteAlertLog = "";

        Boolean coldsiteAlert = (Boolean) data.get(0);
        String userName = (String) data.get(1);
        String userRole = (String) data.get(2);
        String time_operate = (String) data.get(3);

        if (coldsiteAlert.equals(true)) {
            coldsiteAlertLog = "冷通道报警";
            if (!coldsiteAlert.equals(coldsite_alert)) {

                log logAlert = new log();
                logAlert.setDatacenter_room("JF201");
                logAlert.setContent(coldsiteAlertLog);
                logAlert.setUserName(userName);
                logAlert.setUserRole(userRole);
                logAlert.setTime(time_operate);

                logMapper.insert(logAlert);
            }
        } else if (coldsiteAlert.equals(false)) {
            coldsiteAlertLog = "冷通道未报警";
            if (!coldsiteAlert.equals(coldsite_alert)) {
                log logAlert = new log();
                logAlert.setDatacenter_room("JF201");
                logAlert.setContent(coldsiteAlertLog);
                logAlert.setUserName(userName);
                logAlert.setUserRole(userRole);
                logAlert.setTime(time_operate);

                logMapper.insert(logAlert);
            }

        }
        coldsite_alert=coldsiteAlert;
        return data;
    }

    @CrossOrigin
    @RequestMapping("/getData/201/realdata/alert_design")
    @ResponseBody
    public List<Boolean> alert_design0(){
//        List<Double> ret=new ArrayList<>();
//        real_alert,data_abnormal_alert,
        return Arrays.asList(coldsite_alert);
    }


//    Integer time_limit=6;
    //    Integer time_limit=6;
    @CrossOrigin
    @PostMapping("/getData/201/dataStatus_time_limit_design")
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
    @RequestMapping("/getData/201/alert")
    @ResponseBody
    public Map<String,Object> alert2(){

        Map<String,Object> b= new HashMap<>();
        List<List<String>> real= new ArrayList<>();
        List<List<String>> data_abnormal_detail= new ArrayList<>();
        List<List<String>> cold_list= new ArrayList<>();
        List<Map<String, Object>> list_data = new ArrayList<>();

//        <sitecold> find_list = new LambdaQueryWrapper<>();
//        find_list.allEq(null);
        
        Double cold_unstable_fixed_time_real = cold_unstable_fixed_time * 2;

        String sql2="select * from predata where PointName='冷通道最大温度' ORDER BY id DESC limit 0,7"; //预测警告
        String sql3="select * from preshow where PointName='冷通道最大温度' ORDER BY id DESC limit 0,7"; //实时警告
        String sql_abnormal=" select * from  abnormal_detail where time=( select MAX(time) from abnormal_detail )";
        String sql_penultimate = "select * from realdata_once where Location='JF201' and Equipment='服务器' and PointName='冷通道温度'  and time = ( SELECT time FROM realdata_once order by time desc limit 1 OFFSET 2000)"; //60数据间隔
        String sql_last = "select * from realdata_once where Location='JF201' and Equipment='服务器' and PointName='冷通道温度' and time = ( SELECT time FROM realdata_once order by time desc limit 1)"; //19测点x3+功率
        sql_penultimate.replace("60", cold_unstable_fixed_time_real.toString());
        Integer siteNum = 19;//测点个数

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
        List<String> server = Arrays.asList("A","B","C","D","E","F","G","H","J","K","L","M","N","P");
        Collections.reverse(server);//从K开始排序

        for (String c : server) {  // 遍历服务器 c 为（"A","B","C","D" ...）

            sql_penultimate = sql_penultimate.replace("'服务器'", "'服务器" + c + "'"); //某服务器所有测点
            sql_last = sql_last.replace("'服务器'", "'服务器" + c + "'");
            List<Map<String, Object>> list_penultimate = jdbc.queryForList(sql_penultimate);
            List<Map<String, Object>> list_last = jdbc.queryForList(sql_last);

            List<Double> server_site_cold_up = new ArrayList<>(); //某列服务器冷通道上测点
            List<Double> server_site_cold_down = new ArrayList<>();  //某列服务器冷通道下测点



            for (int i = 0; i < list_penultimate.size(); i++) {
                // 获取当前测点的上一次和最新一次的温度值
                Double penultimateValue = (double) list_penultimate.get(i).get("Value0");
                Double lastValue = (double) list_last.get(i).get("Value0");
                Double gap = Math.abs(penultimateValue - lastValue);

                if (i % 2 == 0) {
                    // 处理上测点
                    if (lastValue < 1.0) {
                        server_site_cold_up.add(-1.0);
                    } else {
                        server_site_cold_up.add(Math.round(gap * 100.0) / 100.0);
                        if (gap > cold_unstable_fixed_range) {
                            cold_list.add(Arrays.asList(list_last.get(i).get("time").toString(), list_last.get(i).get("Equipment").toString(), "冷通道测点温度波动为" + String.format("%.2f", gap) + "°C"));
                        }
                    }
                } else {
                    // 处理下测点
                    if (lastValue < 0.1) {
                        server_site_cold_down.add(-1.0);
                    } else {
                        server_site_cold_down.add(Math.round(gap * 100.0) / 100.0);
                        if (gap > cold_unstable_fixed_range) {
                            cold_list.add(Arrays.asList(list_last.get(i).get("time").toString(), list_last.get(i).get("Equipment").toString(), "冷通道测点温度波动为" + String.format("%.2f", gap) + "°C"));
                        }
                    }
                }
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

    /*****************************************coldchange***************************************************************/
    @CrossOrigin
    @RequestMapping("/getData/201/realdata/coldsite_change")
    @ResponseBody
    public List<Map<String,Object>> coldsite_change(){

        Double cold_unstable_fixed_time_real=cold_unstable_fixed_time*2;

        List <Map<String,Object>> list_data= new ArrayList<>();  //储存返回的json
        List<String> server = Arrays.asList("A","B","C","D","E","F","G","H");
        Collections.reverse(server);//从P开始排序
        String sql_penultimate="select * from realdata_once where Location='JF201' and Equipment='服务器' and PointName='冷通道温度'  and time = ( SELECT time FROM realdata_once order by time desc limit 1 OFFSET 60)"; //60数据间隔
        String sql_last="select * from realdata_once where Location='JF201' and Equipment='服务器' and PointName='冷通道温度' and time = ( SELECT MAX(time) FROM realdata_once)"; //19测点x3+功率
        sql_penultimate.replace("60",cold_unstable_fixed_time_real.toString());
        Map<String, Object> servers_cold= new TreeMap<>();  //所有列列服务器冷通道
        Integer siteNum=19;//测点个数

//        Integer id=0;
        for (String c:server) {  //遍历服务器 c为（"A","B","C","D" ...）
            Map<String, Object> server_temp_cold = new TreeMap<>();  //某列服务器冷通道

            sql_penultimate=sql_penultimate.replace("'服务器'", "'服务器" + c + "'"); //某服务器所有测点
            sql_last=sql_last.replace("'服务器'", "'服务器" + c + "'");

            List<Map<String, Object>> list_penultimate = jdbc.queryForList(sql_penultimate);
            List<Map<String, Object>> list_last = jdbc.queryForList(sql_last);

            List<Double> server_site_cold_up = new ArrayList<>(); //某列服务器冷通道上测点
            List<Double> server_site_cold_down = new ArrayList<>();  //某列服务器冷通道下测点


            for(int i=0; i< list_penultimate.size();i++){
//                Map<String,Object> penultimateMap= list_penultimate.get(i);
//                Map<String,Object> lastMap= list_last.get(i);
                Double penultimateValue=(double)list_penultimate.get(i).get("Value0");
                Double lastValue=(double)list_last.get(i).get("Value0");
                Double gap=Math.abs(penultimateValue-lastValue);

                if(i%2==0){
                    if(lastValue<0.1){
                        server_site_cold_up.add(-1.0);
                    }else{
                        server_site_cold_up.add(Math.round(gap*100.0)/100.0);
                        if(gap>cold_unstable_fixed_range){
                            Alert alert0 = new Alert();
                            alert0.setContent(list_penultimate.get(i).get("SiteName").toString()+"波动"+String.format("%.2f",Math.abs(gap))+"度");
                            alert0.setEquipment(list_penultimate.get(i).get("Equipment").toString().substring(3));
                            alert0.setLocation("FT201");
                            alert0.setSampleTime(list_penultimate.get(i).get("time").toString());
                            alertservice.save(alert0);
                        }
                    }
                }else{
                    if(lastValue<0.1){
                        server_site_cold_down.add(-1.0);
                    }else{
                        server_site_cold_down.add(Math.round(gap*100.0)/100.0);
                        if(gap>cold_unstable_fixed_range){
                            Alert alert0 = new Alert();
                            alert0.setContent(list_penultimate.get(i).get("SiteName").toString()+"波动"+String.format("%.2f",Math.abs(gap))+"度");
                            alert0.setEquipment(list_penultimate.get(i).get("Equipment").toString().substring(3));
                            alert0.setLocation("FT201");
                            alert0.setSampleTime(list_penultimate.get(i).get("time").toString());
                            alertservice.save(alert0);
                        }
                    }
                }
                Map<String, Object> site_cold = new TreeMap<>(); //冷通道
                site_cold.put("up", server_site_cold_up); //某列服务器所有上测点  （up，{服务器所有测点（1，22）（2，22）..}）
                site_cold.put("down", server_site_cold_down);//某列服务器所有下测点  （down，{服务器所有测点（1，22）（2，22）..}）
                servers_cold.put(c, site_cold); //冷通道（A，{(avg,xx),(sitedetail,xx)}）
            }
        }
        list_data.add(servers_cold);
        return list_data;
    }
    @CrossOrigin
    @RequestMapping("/getData/201/realdata/cold_detect_design")
    @ResponseBody
    public List<Double> cold_detect_design(){
        List<Double> ret=new ArrayList<>();
        ret.add(cold_unstable_fixed_range);
        ret.add(cold_unstable_fixed_time);
        return ret;
    }

    @CrossOrigin
    @PostMapping("/getData/201/realdata/cold_detect_design")
    @ResponseBody
    public List<Object> cold_detect_design2(@RequestBody List<Object> data){
        String coldfluctuationtimeLog = "";
        String coldfluctuationRangeLog = "";

        Double coldfluctuationRange = (Double) data.get(0);
        Double coldfluctuationTime = (Double) data.get(1);

        String userName = data.get(2).toString();
        String userRole = data.get(3).toString();
        String time_operate = data.get(4).toString();

        if (coldfluctuationRange.equals("3.0")) {
            coldfluctuationRangeLog = "冷通道波动范围阈值未改变";
            if (!coldfluctuationRange.equals(cold_unstable_fixed_range)){
                log log1 = new log();
                log1.setDatacenter_room("JF201");
                log1.setContent( coldfluctuationRangeLog);
                log1.setUserName(userName);
                log1.setUserRole(userRole);
                log1.setTime(time_operate);

                logMapper.insert(log1);
            }

        } else if (!coldfluctuationRange.equals("3.0")) {
            coldfluctuationRangeLog ="冷通道波动范围阈值改变为";
            if (!coldfluctuationRange.equals(cold_unstable_fixed_range)){
                log log1 = new log();
                log1.setDatacenter_room("JF201");
                log1.setContent(coldfluctuationRangeLog + "为" + coldfluctuationRange);
                log1.setUserName(userName);
                log1.setUserRole(userRole);
                log1.setTime(time_operate);

                logMapper.insert(log1);
            }
        }
        cold_unstable_fixed_range = coldfluctuationRange;

        if (coldfluctuationTime.equals("10.0")) {
            coldfluctuationtimeLog = "冷通道波动时间阈值未改变";
            if (!coldfluctuationTime.equals(cold_unstable_fixed_time)){
                log log1 = new log();
                log1.setDatacenter_room("JF201");
                log1.setContent(coldfluctuationtimeLog);
                log1.setUserName(userName);
                log1.setUserRole(userRole);
                log1.setTime(time_operate);

                logMapper.insert(log1);
            }

        } else if (!coldfluctuationTime.equals("10.0")) {
            coldfluctuationtimeLog ="冷通道波动时间阈值改变";
            if (!coldfluctuationTime.equals(cold_unstable_fixed_time)){
                log log1 = new log();
                log1.setDatacenter_room("JF201");
                log1.setContent(coldfluctuationtimeLog+"为"+ coldfluctuationTime);
                log1.setUserName(userName);
                log1.setUserRole(userRole);
                log1.setTime(time_operate);

                logMapper.insert(log1);
            }
        }
        cold_unstable_fixed_time = coldfluctuationTime;
        return data;

    }


}
