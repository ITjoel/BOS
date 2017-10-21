package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.DeliveryInfo;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
public interface DeliveryInfoService {
    void save(String transitInfoId, DeliveryInfo deliveryInfo);
}
