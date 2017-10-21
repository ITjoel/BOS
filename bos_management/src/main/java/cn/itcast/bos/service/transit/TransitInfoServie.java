package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by ${joel} on 2017/10/16 0016.
 */
public interface TransitInfoServie {
    void createTransit(String wayBillIds);

    Page<TransitInfo> findPageData(Pageable pageable);
}
