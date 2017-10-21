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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            //运单不存在 设置运单状态为待发货
            wayBill.setSignStatus(1);
            wayBillRepository.save(wayBill);
            //保存到索引库
            wayBillIndexRepository.save(wayBill);
        } else {
            //运单存在
            try {
                if (presistWayBill.getSignStatus() == 1) {
                    Integer id = presistWayBill.getId();
                    BeanUtils.copyProperties(presistWayBill, wayBill);
                    presistWayBill.setId(id);
                    //再设置运单状态为待发货
                    presistWayBill.setSignStatus(1);
                    //更新索引库
                    wayBillIndexRepository.save(presistWayBill);
                }
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
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                QueryBuilder wayBillNumQuery = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(wayBillNumQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
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
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                WildcardQueryBuilder wildcardRecAddress = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");
                //情况二
                QueryStringQueryBuilder recAddress = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //两种情况取or关系
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(recAddress);
                boolQueryBuilder.should(wildcardRecAddress);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                TermQueryBuilder sendProNum = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(sendProNum);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
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

    @Override
    public List<WayBill> findWayBills(WayBill wayBill) {
        // 判断WayBill 中条件是否存在
        if (StringUtils.isBlank(wayBill.getWayBillNum())
                && StringUtils.isBlank(wayBill.getSendAddress())
                && StringUtils.isBlank(wayBill.getRecAddress())
                && StringUtils.isBlank(wayBill.getSendProNum())
                && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            // 无条件查询 、查询数据库
            return wayBillRepository.findAll();
        } else {
            // 查询条件
            // must 条件必须成立 and
            // must not 条件必须不成立 not
            // should 条件可以成立 or
            BoolQueryBuilder query = new BoolQueryBuilder(); // 布尔查询 ，多条件组合查询
            // 向组合查询对象添加条件
            if (StringUtils.isNoneBlank(wayBill.getWayBillNum())) {
                // 运单号查询
                QueryBuilder tempQuery = new TermQueryBuilder("wayBillNum",
                        wayBill.getWayBillNum());
                query.must(tempQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendAddress())) {
                // 发货地 模糊查询
                // 情况一： 输入"北" 是查询词条一部分， 使用模糊匹配词条查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder(
                        "sendAddress", "*" + wayBill.getSendAddress() + "*");

                // 情况二： 输入"北京市海淀区" 是多个词条组合，进行分词后 每个词条匹配查询
                QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
                        wayBill.getSendAddress()).field("sendAddress")
                        .defaultOperator(QueryStringQueryBuilder.Operator.AND);

                // 两种情况取or关系
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQuery);
                boolQueryBuilder.should(queryStringQueryBuilder);

                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                // 收货地 模糊查询
                QueryBuilder wildcardQuery = new WildcardQueryBuilder(
                        "recAddress", "*" + wayBill.getRecAddress() + "*");
                query.must(wildcardQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                // 速运类型 等值查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
                        wayBill.getSendProNum());
                query.must(termQuery);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                // 速运类型 等值查询
                QueryBuilder termQuery = new TermQueryBuilder("sendProNum",
                        wayBill.getSendProNum());
                query.must(termQuery);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                // 签收状态查询
                QueryBuilder termQuery = new TermQueryBuilder("signStatus",
                        wayBill.getSignStatus());
                query.must(termQuery);
            }

            SearchQuery searchQuery = new NativeSearchQuery(query);
            // ElasticSearch 允许搜索分页查询，最大数据条数10000
            Pageable pageable = new PageRequest(0, 10000);
            searchQuery.setPageable(pageable); // 分页效果

            // 有条件查询 、查询索引库
            return wayBillIndexRepository.search(searchQuery).getContent();
        }
    }
}
