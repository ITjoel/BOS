package cn.itcast.bos.web.action.base;


import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

import cn.itcast.bos.web.action.common.BaseAction;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
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

import java.io.IOException;
import java.util.List;

import static com.opensymphony.xwork2.Action.SUCCESS;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier> {

    @Autowired
    private CourierService courierService;

    @Action(value = "courier_save", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String save() {

        courierService.save(model);
        return SUCCESS;
    }


    @Action(value = "courier_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        // 创建pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Courier> page = courierService.findByPageable(model, pageable);

        pushPageDatakToValueStack(page);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Action(value = "courier_delBacth", results = {@Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String delBacth() {
        // 切割
        String[] idArray = ids.split(",");
        courierService.delBacth(idArray);

        return SUCCESS;
    }

    @Action(value = "courier_updateBacth", results = {@Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String updateBacth() {
        // 切割
        String[] idArray = ids.split(",");
        courierService.updateBacth(idArray);
        return SUCCESS;
    }

    private int id;

    @Action(value = "courier_findById", results = {@Result(name = "success", type = "json")})
    public String findById() {

        Courier courier = courierService.findById(id);
        System.out.println(courier);
        //ActionContext.getContext().getValueStack().push(courier);
        String json = JSONObject.toJSONString(courier);
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    //关联定区快递员
    @Action(value = "courier_findnoassociation", results = {@Result(name = "success", type = "json")})
    public String findnoassociation() {
        List<Courier> couriers = courierService.findNoassociation();
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }


}
