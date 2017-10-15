package cn.itcast.bos.dao.realm;

import cn.itcast.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/14 0014.
 */
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
