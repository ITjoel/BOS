package cn.itcast.bos.web.action.transit;

import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.service.transit.SignInfoService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by ${joel} on 2017/10/17 0017.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SignInfoAction extends BaseAction<SignInfo> {

    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    @Autowired
    private SignInfoService signInfoService;

    @Action(value = "sign_save", results = {@Result(name = "success", type = "redirect", location = "pages/transit/transitinfo.html")})
    public String save() {
        //调用业务层保存签收信息
        signInfoService.save(model, transitInfoId);
        return SUCCESS;
    }
}
