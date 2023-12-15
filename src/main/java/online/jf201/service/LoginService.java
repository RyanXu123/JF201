package online.jf201.service;

import online.jf201.dto.LoginDto;
import online.jf201.entity.ResultMassage;

public interface LoginService {
    public ResultMassage login(LoginDto loginDto);

}
