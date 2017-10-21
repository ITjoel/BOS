package cn.itcast.bos.dao.transit;

import cn.itcast.bos.domain.transit.DeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo, Integer> {
}
