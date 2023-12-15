package online.jf201.control_201;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PreShow_201_controller {


    @Autowired
    private JdbcTemplate jdbc;

    @CrossOrigin
    @PostMapping("/getData/201/preshownew")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public Map<String,Object> preshownew2(@RequestBody List<List<String>> data) {//一个时刻68个数据


        String start_data= data.get(0).get(0).toString();
        String end_data= data.get(0).get(1).toString();
        System.out.println(end_data);


        String sql_power_future="select * from predata where EquipmentType='Server'  ORDER BY id DESC limit 0,10080";
        String sql_temperature_future="select * from predata where EquipmentType='Outside'  ORDER BY id DESC limit 0,10080";

        String sql_power_now="select * from preshow where EquipmentType='Server'  ORDER BY id DESC limit 0,10080";
        String sql_temperature_now="select * from preshow where EquipmentType='Outside'  ORDER BY id DESC limit 0,10080";

        if(!end_data.equals("-1")){
            sql_power_future=sql_power_future.replace("ORDER BY id DESC limit 0,10080","and time > '"+start_data.toString()+"' and time < '"+end_data.toString()+"'");
            sql_temperature_future=sql_temperature_future.replace("ORDER BY id DESC limit 0,10080","and time > '"+start_data.toString()+"' and time < '"+end_data.toString()+"'");
            sql_power_now=sql_power_now.replace("ORDER BY id DESC limit 0,10080","and time > '"+start_data.toString()+"' and time < '"+end_data.toString()+"'");
            sql_temperature_now=sql_temperature_now.replace("ORDER BY id DESC limit 0,10080","and time > '"+start_data.toString()+"' and time < '"+end_data.toString()+"'");
        }


        LinkedList <String>timeline_arr= new LinkedList<>();
        LinkedList <String> temperature_arr_now = new LinkedList<>();
        LinkedList <String> power_arr_now = new LinkedList<>();
        LinkedList <String> temperature_arr_future = new LinkedList<>();
        LinkedList <String> power_arr_future = new LinkedList<>();


        List<Map<String, Object>> list_power_future = jdbc.queryForList(sql_power_future);
        List<Map<String, Object>> list_power_now = jdbc.queryForList(sql_power_now);
        List<Map<String, Object>> list_temperature_future = jdbc.queryForList(sql_temperature_future);
        List<Map<String, Object>> list_temperature_now = jdbc.queryForList(sql_temperature_now);




        for(Map<String, Object> c:list_power_now) {
            String time0 = c.get("time").toString();
            timeline_arr.add(time0);
        }
        Collections.reverse(timeline_arr);

        for(Map<String, Object> c:list_power_now) {
            String Value0 = c.get("Value0").toString();
            power_arr_now.add(Value0);
        }
        Collections.reverse(power_arr_now);

        for(Map<String, Object> c:list_power_future) {
            String Value0 = c.get("Value0").toString();
            power_arr_future.add(Value0);
        }
        Collections.reverse(power_arr_future);

        for(Map<String, Object> c:list_temperature_now) {
            String Value0 = c.get("Value0").toString();
            temperature_arr_now.add(Value0);
        }
        Collections.reverse(temperature_arr_now);

        for(Map<String, Object> c:list_temperature_future) {
            String Value0 = c.get("Value0").toString();
            temperature_arr_future.add(Value0);
        }
        Collections.reverse(temperature_arr_now);


        Map<String,Object> ret= new HashMap<>();

        ret.put("future_temperature",temperature_arr_future);
        ret.put("now_temperature",temperature_arr_now);
        ret.put("future_power",power_arr_future);
        ret.put("now_power",power_arr_now);
        ret.put("timeline",timeline_arr);
        return ret;
    }


    @CrossOrigin
    @RequestMapping("/getData/201/preshownew")
    @ResponseBody
//    @Scheduled(fixedRate = 30000)
    public Map<String,Object> preshownew() {//一个时刻68个数据

        String sql_power_future="select * from predata where EquipmentType='Server'  ORDER BY id DESC limit 0,10080";
        String sql_temperature_future="select * from predata where EquipmentType='Outside'  ORDER BY id DESC limit 0,10080";

        String sql_power_now="select * from preshow where EquipmentType='Server'  ORDER BY id DESC limit 0,10080";
        String sql_temperature_now="select * from preshow where EquipmentType='Outside'  ORDER BY id DESC limit 0,10080";

        LinkedList <String>timeline_arr= new LinkedList<>();
        LinkedList <String> temperature_arr_now = new LinkedList<>();
        LinkedList <String> power_arr_now = new LinkedList<>();
        LinkedList <String> temperature_arr_future = new LinkedList<>();
        LinkedList <String> power_arr_future = new LinkedList<>();


        List<Map<String, Object>> list_power_future = jdbc.queryForList(sql_power_future);
        List<Map<String, Object>> list_power_now = jdbc.queryForList(sql_power_now);
        List<Map<String, Object>> list_temperature_future = jdbc.queryForList(sql_temperature_future);
        List<Map<String, Object>> list_temperature_now = jdbc.queryForList(sql_temperature_now);




        for(Map<String, Object> c:list_power_now) {
            String time0 = c.get("time").toString();
            timeline_arr.add(time0);
        }
        Collections.reverse(timeline_arr);

        for(Map<String, Object> c:list_power_now) {
            String Value0 = c.get("Value0").toString();
            power_arr_now.add(Value0);
        }
        Collections.reverse(power_arr_now);

        for(Map<String, Object> c:list_power_future) {
            String Value0 = c.get("Value0").toString();
            power_arr_future.add(Value0);
        }
        Collections.reverse(power_arr_future);

        for(Map<String, Object> c:list_temperature_now) {
            String Value0 = c.get("Value0").toString();
            temperature_arr_now.add(Value0);
        }
        Collections.reverse(temperature_arr_now);

        for(Map<String, Object> c:list_temperature_future) {
            String Value0 = c.get("Value0").toString();
            temperature_arr_future.add(Value0);
        }
        Collections.reverse(temperature_arr_now);


        Map<String,Object> ret= new HashMap<>();

        ret.put("future_temperature",temperature_arr_future);
        ret.put("now_temperature",temperature_arr_now);
        ret.put("future_power",power_arr_future);
        ret.put("now_power",power_arr_now);
        ret.put("timeline",timeline_arr);
        return ret;
    }
}
