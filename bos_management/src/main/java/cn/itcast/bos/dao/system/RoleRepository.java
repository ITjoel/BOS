package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("from Role r inner join fetch r.users u where u.id = ?")
    List<Role> findByUser(Integer id);
}
