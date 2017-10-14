package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by ${joel} on 2017/9/22 0022.
 */
public interface CustomerService {
    //查询所有未关联客户列表
    @Path("/noassociationcustomer")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findNoAssociationCustomer();

    //已经关联到指定区域的客户列表
    @Path("/hasassociationsfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findHasAssociationsFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);

    //将客户关联到区上
    @Path("/associationscustomerstofixedarea")
    @PUT
    public void associationsCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr, @QueryParam("fixedAreaId") String fixedAreaId);


    @Path("/customer")
    @POST
    @Consumes({"application/json", "application/xml"})
    public void regist(Customer customer);

    @Path("/customer/telephone/{telephone}")
    @GET
    @Consumes({"application/json", "application/xml"})
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    @Path("/customer/updatetype/{telephone}")
    @GET
    public void updateType(@PathParam("telephone") String telephone);

    @Path("/customer/login")
    @GET
    @Consumes({"application/json", "application/xml"})
    public Customer login(@QueryParam("telephone") String telephone, @QueryParam("password") String password);

    //根据发件人地址查找客户表中定区id
    @Path("/customer/findFixedAreaIdByAddress")
    @GET
    @Consumes({"application/xml", "application/json"})
    public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
