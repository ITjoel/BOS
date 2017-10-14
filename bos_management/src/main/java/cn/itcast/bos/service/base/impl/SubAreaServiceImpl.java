package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubAreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;

import org.springframework.data.domain.Pageable;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${joel} on 2017/9/24 0024.
 */

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;


    @Override
    public void saveSubAreaBach(List<SubArea> list) {
        subAreaRepository.save(list);
    }

    @Override
    public Page<SubArea> pageQuery(final SubArea subArea, Pageable pageable) {
        System.out.println("222222222222222222");
        Specification<SubArea> specification = new Specification<SubArea>() {

            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(subArea.getAssistKeyWords())) {
                    Predicate p1 = cb.like(root.get("assistKeyWords").as(String.class), "%" + subArea.getAssistKeyWords() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(subArea.getKeyWords())) {
                    Predicate p2 = cb.like(root.get("keyWords").as(String.class), "%" + subArea.getKeyWords() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(subArea.getStartNum())) {
                    Predicate p3 = cb.like(root.get("startNum").as(String.class), "%" + subArea.getStartNum() + "%");
                    list.add(p3);
                }
                if (StringUtils.isNotBlank(subArea.getEndNum())) {
                    Predicate p4 = cb.like(root.get("endNum").as(String.class), "%" + subArea.getEndNum() + "%");
                    list.add(p4);
                }

                Join<Object, Object> areaRoot = root.join("area", JoinType.INNER);
                if (subArea.getArea() != null && StringUtils.isNotBlank(subArea.getArea().getProvince())) {
                    Predicate p6 = cb.like(areaRoot.get("province").as(String.class), "%" + subArea.getArea().getProvince() + "%");
                    list.add(p6);
                }

                if (subArea.getArea() != null && StringUtils.isNotBlank(subArea.getArea().getCity())) {
                    Predicate p7 = cb.like(areaRoot.get("city").as(String.class), "%" + subArea.getArea().getCity() + "%");
                    list.add(p7);
                }

                if (subArea.getArea() != null && StringUtils.isNotBlank(subArea.getArea().getDistrict())) {
                    Predicate p8 = cb.like(areaRoot.get("district").as(String.class), "%" + subArea.getArea().getDistrict() + "%");
                    list.add(p8);
                }

                Join<Object, Object> fixedIdRoot = root.join("fixedArea", JoinType.INNER);
                if (subArea.getFixedArea() != null && StringUtils.isNotBlank(subArea.getFixedArea().getId())) {
                    Predicate p9 = cb.like(fixedIdRoot.get("id").as(String.class), "%" + subArea.getFixedArea().getId() + "%");
                    list.add(p9);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        return subAreaRepository.findAll(specification, pageable);
    }

    @Override
    public List<SubArea> findAll() {
        return subAreaRepository.findAll();
    }

    @Override
    public void save(SubArea subArea) {
        subAreaRepository.save(subArea);
    }


}
