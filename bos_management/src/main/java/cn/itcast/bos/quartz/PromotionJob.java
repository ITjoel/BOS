package cn.itcast.bos.quartz;

import cn.itcast.bos.service.take_delivery.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by ${joel} on 2017/10/8 0008.
 */
public class PromotionJob implements Job {

    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时过期清理程序");
        promotionService.updateStatus(new Date());
    }
}
