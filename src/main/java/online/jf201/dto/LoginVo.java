package online.jf201.dto;

import lombok.Data;
import online.jf201.entity.User;

@Data
public class LoginVo {
    private Integer id;
    private String token;
    private User user;

}
