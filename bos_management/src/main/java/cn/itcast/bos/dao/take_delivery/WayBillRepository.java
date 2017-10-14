package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/11 0011.
 */
public interface WayBillRepository extends JpaRepository<WayBill, Integer> {
    WayBill findByWayBillNum(String wayBillNum);
}
