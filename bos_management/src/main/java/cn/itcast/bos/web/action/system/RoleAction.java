package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
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
 * Created by ${joel} on 2017/10/14 0014.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

    @Autowired
    private RoleService roleService;

    @Action(value = "role_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        List<Role> result = roleService.findAll();
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

}
