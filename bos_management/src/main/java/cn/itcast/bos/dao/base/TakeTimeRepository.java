package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/9/23 0023.
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer> {
}
