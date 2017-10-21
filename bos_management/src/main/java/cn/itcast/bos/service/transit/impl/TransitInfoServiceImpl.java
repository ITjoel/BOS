package cn.itcast.bos.service.transit.impl;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.TransitInfoServie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by ${joel} on 2017/10/16 0016.
 */
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoServie {

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void createTransit(String wayBillIds) {
        if (StringUtils.isNotBlank(wayBillIds)) {
            for (String wayBillId : wayBillIds.split(",")) {
                //根据wayBillIds 查出运单
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
                //判断运单状态是否为待发货
                if (wayBill.getSignStatus() == 1) {
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出入库中转");
                    transitInfoRepository.save(transitInfo);
                    //更改运单状态为派送中
                    wayBill.setSignStatus(2);
                    //更新索引库
                    wayBillIndexRepository.save(wayBill);
                }
            }

        }
    }

    @Override
    public Page<TransitInfo> findPageData(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }
}
