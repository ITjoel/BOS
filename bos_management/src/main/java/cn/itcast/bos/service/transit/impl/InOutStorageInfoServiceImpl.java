package cn.itcast.bos.service.transit.impl;

import cn.itcast.bos.dao.transit.InoutStorageInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.InOutStorageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {
    @Autowired
    private InoutStorageInfoRepository inoutStorageInfoRepository;

    @Autowired
    private TransitInfoRepository transitInfoRepository;

    @Override
    public void save(String transitInfoId, InOutStorageInfo inOutStorageInfo) {
        //保存出入库信息
        inoutStorageInfoRepository.save(inOutStorageInfo);

        //查询物流运输信息
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));

        //关联出入库信息到物流运输信息
        transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
        if (inOutStorageInfo.getOperation().equals("到达网点")) {
            transitInfo.setStatus("到达网点");
            //更新网点,显示配送路径
            transitInfo.setOutletAddress(inOutStorageInfo.getAddress());
        }
    }
}
