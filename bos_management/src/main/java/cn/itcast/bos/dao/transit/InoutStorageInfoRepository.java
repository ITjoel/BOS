package cn.itcast.bos.dao.transit;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
public interface InoutStorageInfoRepository extends JpaRepository<InOutStorageInfo, Integer> {
}
