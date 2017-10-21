package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.SignInfo;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
public interface SignInfoService {
    void save(SignInfo model, String transitInfoId);
}
