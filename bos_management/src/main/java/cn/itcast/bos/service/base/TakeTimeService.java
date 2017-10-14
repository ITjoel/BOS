package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.TakeTime;

import java.util.List;

/**
 * Created by ${joel} on 2017/9/23 0023.
 */
public interface TakeTimeService {
    List<TakeTime> findAll();
}
