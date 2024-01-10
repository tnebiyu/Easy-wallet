package com.nebiyu.Kelal.services;
import com.nebiyu.Kelal.repositories.UserRepository;
import com.nebiyu.Kelal.dto.PingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PingService  {
@Autowired
private UserRepository userRepository;
    public PingResponse ping() {
try{
    var data =   PingResponse.Data.builder().statusMessage("the api is working").build();
    return PingResponse.builder().data(data).error(false).error_msg("").build();
}
catch (Exception e) {
    return PingResponse.builder().error(true).error_msg("error " + e ).build();
}


    }

}
