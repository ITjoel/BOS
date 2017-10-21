package cn.itcast.bos.service.transit.impl;

import cn.itcast.bos.dao.transit.DeliveryInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String transitInfoId, DeliveryInfo deliveryInfo) {
        //保存配送信息
        deliveryInfoRepository.save(deliveryInfo);

        //查询出物流信息
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setDeliveryInfo(deliveryInfo);

        //更改物流状态 // 出入库中转、到达网点、开始配送、正常签收、异常
        transitInfo.setStatus("开始配送");

    }
}
