package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findByUser(User user) {
        if (user.getUsername().equals("admin")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void save(Role role, String[] permissionIds, String menuIds) {

        //保存权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(Integer.parseInt(permissionId));
                role.getPermissions().add(permission);
            }
        }
        //保存菜单
        if (menuIds != null) {
            Menu menu = new Menu();
            menu.setId(Integer.parseInt(menuIds));
            role.getMenus().add(menu);
        }

        //保存角色
        roleRepository.save(role);
    }


}
