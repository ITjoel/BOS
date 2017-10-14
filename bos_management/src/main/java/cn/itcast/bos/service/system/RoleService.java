package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
public interface RoleService {
    List<Role> findByUser(User user);
}
