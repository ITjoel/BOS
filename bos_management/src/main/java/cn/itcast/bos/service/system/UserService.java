package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
public interface UserService {
    User findByUsername(String username);
}
