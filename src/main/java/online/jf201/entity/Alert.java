package online.jf203.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Alert {
    @TableId
//    private Integer id;
    private String Equipment;
    private String Location;
    private String content;
    private String SampleTime;
}
