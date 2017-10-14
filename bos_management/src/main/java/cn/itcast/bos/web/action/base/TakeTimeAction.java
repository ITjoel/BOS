package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Created by ${joel} on 2017/9/23 0023.
 */

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {

    @Autowired
    private TakeTimeService takeTimeService;

    //显示收派时间
    @Action(value = "taketime_findAll", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        //调用业务层,查找所有taketimes
        List<TakeTime> takeTimes = takeTimeService.findAll();
        //压入值栈
        ActionContext.getContext().getValueStack().push(takeTimes);
        return SUCCESS;
    }
}
