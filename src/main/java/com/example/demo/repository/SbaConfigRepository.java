package com.example.demo.repository;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Department;
import com.example.demo.entities.SBAConfig;
import com.example.demo.enum_entities.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SbaConfigRepository extends CrudRepository<SBAConfig, Long> {
    @Query("SELECT sbc FROM SBAConfig sbc WHERE sbc.department = :department AND sbc.academic_year=:year AND sbc.status = :status")
    SBAConfig findSBAByDepartment(@Param("department")Department department, @Param("year")Academic_Year year, @Param("status")Status status);

    @Query("SELECT sbc FROM SBAConfig sbc WHERE sbc.department = :department AND sbc.academic_year=:year")
    SBAConfig findSBAByDepartmentAndYear(@Param("department")Department department, @Param("year")Academic_Year year);

    @Query("SELECT sbc FROM SBAConfig sbc WHERE sbc.department = :department AND sbc.status =:status")
    SBAConfig findSBAByDepartmentAndStatus(@Param("department")Department department, @Param("status")Status status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE SBAConfig sc SET sc.status = :status,sc.academic_year = sc.academic_year," +
            "sc.department = sc.department WHERE sc.sba_config_id = :configId")
    void updateSbaConfig(@Param("status")Status status, @Param("configId") Long configId);
}
