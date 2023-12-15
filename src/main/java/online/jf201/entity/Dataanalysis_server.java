package online.jf203.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Dataanalysis_server {
    @TableId
    private String Location;
    private String Equipment;
    private double Power;
    private String SampleTime;
    private Integer id;
}
