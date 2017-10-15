package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.realm.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/14 0014.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void save(Menu menu) {
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() == 0) {
            menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }
}
