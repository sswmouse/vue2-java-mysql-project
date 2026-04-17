package com.example.demo.repository;

import com.example.demo.model.AmplifyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 增幅选项数据仓库
 */
@Repository
public interface AmplifyOptionRepository extends JpaRepository<AmplifyOption, Long> {

    /**
     * 根据增幅类型查询
     */
    List<AmplifyOption> findByAmplifyTypeOrderByLevelAsc(String amplifyType);

    /**
     * 根据增幅属性查询
     */
    List<AmplifyOption> findByAttributeOrderByLevelAsc(String attribute);
}
