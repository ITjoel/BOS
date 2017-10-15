package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/14 0014.
 */
public interface MenuService {
    List<Menu> findAll();


    void save(Menu menu);

    List<Menu> findByUser(User user);
}
