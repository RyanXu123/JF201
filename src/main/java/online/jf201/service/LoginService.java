package online.jf203.service;

import online.jf203.dto.LoginDto;
import online.jf203.entity.ResultMassage;

public interface LoginService {
    public ResultMassage login(LoginDto loginDto);

}
