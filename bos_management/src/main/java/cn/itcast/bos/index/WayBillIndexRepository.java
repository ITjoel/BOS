package cn.itcast.bos.index;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by ${joel} on 2017/10/12 0012.
 */
public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill, Integer> {
}
