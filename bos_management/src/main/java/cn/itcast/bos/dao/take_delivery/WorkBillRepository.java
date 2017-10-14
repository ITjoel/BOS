package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/9 0009.
 */
public interface WorkBillRepository extends JpaRepository<WorkBill, Integer> {
}
