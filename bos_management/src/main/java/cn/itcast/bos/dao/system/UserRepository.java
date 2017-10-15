package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
