package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area> {

    Area findByProvinceAndCityAndDistrict(String province, String city, String district);

    @Query("select distinct province from Area")
    public List<String> findProvince();

    @Query("select distinct city from Area where province = ?")
    public List<String> findCity(String province);

    @Query("select distinct district from Area where province = ? and city=?")
    public List<String> findDistrict(String province, String city);
}
