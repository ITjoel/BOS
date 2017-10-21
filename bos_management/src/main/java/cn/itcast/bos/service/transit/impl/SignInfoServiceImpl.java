package cn.itcast.bos.service.transit.impl;

import cn.itcast.bos.dao.transit.SignInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {

    @Autowired
    private SignInfoRepository signInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(SignInfo signInfo, String transitInfoId) {
        //先保存签收信息
        signInfoRepository.save(signInfo);
        //查出运输物流信息,并关联运输物流信息
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setSignInfo(signInfo);

        if (signInfo.getSignType().equals("正常")) {
            //正常接收
            //设置运输物流信息状态为正常签收
            transitInfo.setStatus("正常签收");
            //通过物流信息获取运单信息再获取运单签收状态修运单状态：
            // 待发货、派送中、已签收、异常
            transitInfo.getWayBill().setSignStatus(3);
            //同步索引库
            transitInfoRepository.save(transitInfo);
        } else {
            transitInfo.setStatus("异常");
            transitInfo.getWayBill().setSignStatus(4);
            //同步索引库
            transitInfoRepository.save(transitInfo);
        }

    }
}
