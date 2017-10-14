package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by ${joel} on 2017/9/24 0024.
 */
public interface SubAreaService {

    void saveSubAreaBach(List<SubArea> list);


    Page<SubArea> pageQuery(SubArea subArea, Pageable pageable);

    List<SubArea> findAll();

    void save(SubArea model);
}