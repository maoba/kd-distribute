package com.kd.distribute.dao;

import com.kd.distribute.model.KdProduct;
import com.kd.distribute.model.KdProductExample;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KdProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    long countByExample(KdProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int deleteByExample(KdProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int insert(KdProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int insertSelective(KdProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    List<KdProduct> selectByExample(KdProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    KdProduct selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int updateByExampleSelective(@Param("record") KdProduct record, @Param("example") KdProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int updateByExample(@Param("record") KdProduct record, @Param("example") KdProductExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int updateByPrimaryKeySelective(KdProduct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table kd_product
     *
     * @mbg.generated Fri Dec 25 22:56:30 CST 2020
     */
    int updateByPrimaryKey(KdProduct record);

    void updateProductCount(@Param("purchaseProductNum") int purchaseProductNum,
                            @Param("updateUser") String xxx, @Param("updateTime") Date date,
                            @Param("id") Integer id);
}