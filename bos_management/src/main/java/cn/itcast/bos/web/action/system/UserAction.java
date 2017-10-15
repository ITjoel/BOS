package cn.itcast.bos.web.action.system;


import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by ${joel} on 2017/10/13 0013.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

    @Autowired
    private UserService userService;

    @Action(value = "user_login", results = {
            @Result(name = "success", type = "redirect", location = "index.html"),
            @Result(name = "login", type = "redirect", location = "login.html")})
    public String login() {
        //基于shiro用户登录
        Subject subject = SecurityUtils.getSubject();

        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
        try {

            subject.login(token);

            return SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return LOGIN;
        }
    }

    @Action(value = "user_logout", results = {@Result(name = "success", type = "redirect", location = "login.html")})
    public String logout() {
        //基于shiro退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }


    @Action(value = "user_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        List<User> result = userService.findAll();
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    @Action(value = "user_save", results = {@Result(name = "success", type = "redirect", location = "/pages/system/userlist.html")})
    public String save() {
        userService.save(model, roleIds);
        return SUCCESS;
    }
}
