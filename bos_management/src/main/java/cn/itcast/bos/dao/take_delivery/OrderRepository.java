package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/9 0009.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByOrderNum(String orderNum);
}
