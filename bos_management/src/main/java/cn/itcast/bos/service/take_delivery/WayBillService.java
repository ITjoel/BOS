package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by ${joel} on 2017/10/11 0011.
 */
public interface WayBillService {

    void save(WayBill wayBill);

    Page<WayBill> findPageData(WayBill WayBill, Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);
}
