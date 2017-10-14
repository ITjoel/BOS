package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.take_delivery.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${joel} on 2017/10/11 0011.
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {
        //判断运单号是否存在
        WayBill presistWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if (presistWayBill == null || presistWayBill.getId() == null) {
            //运单不存在
            wayBillRepository.save(wayBill);
            //保存到索引库
            wayBillIndexRepository.save(wayBill);
        } else {
            //运单存在
            try {
                Integer id = presistWayBill.getId();
                BeanUtils.copyProperties(presistWayBill, wayBill);
                presistWayBill.setId(id);
                //更新索引库
                wayBillIndexRepository.save(presistWayBill);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Page<WayBill> findPageData(WayBill wayBill, Pageable pageable) {
        //首先判断waybill中是否是无条件查询还是有条件查询
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            //无条件查询,查询数据库
            return wayBillRepository.findAll(pageable);
        } else {
            BoolQueryBuilder query = new BoolQueryBuilder();
            if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
                QueryBuilder wayBillNumQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(wayBillNumQuery);
            }
            if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
                //情况一  查询"北" 长度小于分词器词条长度
                WildcardQueryBuilder wildcardSendAddress = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");
                //情况二 查询"北京市昌平区" 长度大于分词器长度
                QueryStringQueryBuilder sendAddress = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                //两种情况取or关系
                boolQueryBuilder.should(sendAddress);
                boolQueryBuilder.should(wildcardSendAddress);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
                WildcardQueryBuilder wildcardRecAddress = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                //情况二
                QueryStringQueryBuilder recAddress = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //两种情况取or关系
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(recAddress);
                boolQueryBuilder.should(wildcardRecAddress);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
                TermQueryBuilder sendProNum = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(sendProNum);
            }
            if (wayBill.getSignStatus() != null || wayBill.getSignStatus() != 0) {
                TermQueryBuilder signStatus = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
                query.must(signStatus);
            }
            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable);
            return wayBillIndexRepository.search(searchQuery);
        }
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
