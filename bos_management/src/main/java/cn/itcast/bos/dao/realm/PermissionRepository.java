package cn.itcast.bos.dao.realm;

import cn.itcast.bos.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    @Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id =?  ")
    List<Permission> findByUser(Integer id);
}
