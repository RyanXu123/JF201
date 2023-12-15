package online.jf203.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Sitecold {
    @TableId
//    private String Equipment;
    private String Location;
    private String PointName;
    private Double GapValue;
//    private Double NOUSE;
}
