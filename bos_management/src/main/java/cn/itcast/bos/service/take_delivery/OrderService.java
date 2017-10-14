package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by ${joel} on 2017/10/8 0008.
 */
public interface OrderService {

    @Path("/order")
    @POST
    @Consumes({"application/json", "application/xml"})
    public void saveOrder(Order order);


    Order findByOrderNum(String orderNum);
}
