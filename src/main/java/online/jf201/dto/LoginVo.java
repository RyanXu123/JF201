package online.jf203.dto;

import online.jf203.entity.User;
import lombok.Data;

@Data
public class LoginVo {
    private Integer id;
    private String token;
    private User user;

}
