package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
public interface InOutStorageInfoService {
    void save(String transitInfoId, InOutStorageInfo inOutStorageInfo);
}
