package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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

import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {

    @Autowired
    private FixedAreaService fixedAreaservice;

    @Action(value = "fixed_save", results = {@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html")})
    public String save() {
        fixedAreaservice.save(model);
        return SUCCESS;
    }

    @Action(value = "fixed_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        // 创建pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);

        // 调用service层查询数据
        Page<FixedArea> page = fixedAreaservice.pageQuery(model, pageable);

        // 将数据压栈
        pushPageDatakToValueStack(page);
        return SUCCESS;
    }

    //查询未关联的列表
    @Action(value = "fixed_Area_findNoAssociationCustomer", results = {@Result(name = "success", type = "json")})
    public String findNoAssociationCustomer() {
        //使用webClient调用webService接口
        Collection<? extends Customer> collection = WebClient.create("http://localhost:9998/crm_management/services/customerService/noassociationcustomer")
                .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        //压入值栈栈顶
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    @Action(value = "fixeAarea_findAll", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        List<FixedArea> list = fixedAreaservice.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    //查询已经关联的列表
    @Action(value = "fixed_Area_findHasAssociationsFixedAreaCustomers", results = {@Result(name = "success", type = "json")})
    public String findHasAssociationsFixedAreaCustomers() {
        Collection<? extends Customer> collection = WebClient.create("http://localhost:9998/crm_management/services/customerService/hasassociationsfixedareacustomers/" + model.getId())
                .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
        //压入值栈栈顶
    }

    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    //关联客户到定区
    @Action(value = "fixedArea_associationscustomerstofixedarea", results = {@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html")})
    public String associationCustomerToFixedArea() {
        String customerStr = StringUtils.join(customerIds, ",");
        WebClient.create("http://localhost:9998/crm_management/services/customerService/associationscustomerstofixedarea" +
                "?customerIdStr=" + customerStr + "&fixedAreaId=" + model.getId()).put(null);
        return SUCCESS;
    }

    //关联快递员到定区
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    @Action(value = "fixedArea_associationCourierToFixedArea",
            results = {@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html")})
    public String associationCourierToFixedArea() {
        fixedAreaservice.associationCourierToFixedArea(model, courierId, takeTimeId);
        return SUCCESS;
    }

    @Action(value = "fixed_findAllFixedArea", results = {@Result(name = "success", type = "json")})
    public String findAllFixedArea() {
        List<FixedArea> fixedAreas = fixedAreaservice.findAllFixedArea();

        ActionContext.getContext().getValueStack().push(fixedAreas);
        return SUCCESS;
    }
}
