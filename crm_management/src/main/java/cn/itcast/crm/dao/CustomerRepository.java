package cn.itcast.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ${joel} on 2017/9/22 0022.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);

    @Query("update Customer set fixedAreaId = ? where id = ?")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, int id);

    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void clearFixedAreaId(String fixedAreaId);


    @Query("update Customer set type = 1 where telephone = ?")
    @Modifying
    public void updateType(String telephone);

    Customer findByTelephone(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);

    @Query("select fixedAreaId from Customer where address=?")
    public String findFixedAreaIdByAddress(String address);
}
