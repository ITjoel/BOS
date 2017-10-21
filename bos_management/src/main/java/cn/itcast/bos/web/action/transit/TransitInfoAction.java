package cn.itcast.bos.web.action.transit;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoServie;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${joel} on 2017/10/16 0016.
 */

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {

    private String wayBillIds;

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    @Autowired
    private TransitInfoServie transitInfoServie;

    @Action(value = "transit_create", results = {@Result(name = "success", type = "json")})
    public String create() {
        Map<String, Object> result = new HashMap<>();
        //调用业务层,保存transitInFo信息
        try {
            transitInfoServie.createTransit(wayBillIds);
            result.put("success", true);
            result.put("msg", "开启中转配送成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "开启中转配送失败,异常:" + e.getMessage());
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    @Action(value = "transit_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        //调用业务层查找数据
        Page<TransitInfo> pageData = transitInfoServie.findPageData(pageable);
        pushPageDatakToValueStack(pageData);
        return SUCCESS;
    }
}
