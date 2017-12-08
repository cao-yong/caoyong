package com.caoyong.core.dao.product;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.caoyong.core.bean.product.Brand;
import com.caoyong.core.bean.product.BrandQuery;

/**
 * 品牌dao
 * 
 * @author yong.cao
 * @time 2017年6月7日下午9:30:31
 */
public interface BrandDao {

    /**
     * 插入品牌
     * 
     * @param brand
     * @return
     */
    Integer insertSelective(Brand brand) throws DataAccessException;

    /**
     * 查询列表
     * 
     * @param query
     * @return
     */
    List<Brand> selectBrandListByQuery(BrandQuery query) throws DataAccessException;

    /**
     * 查询总条数
     * 
     * @param query
     * @return
     */
    Integer selectCount(BrandQuery query) throws DataAccessException;

    /**
     * 根据id查询品牌
     * 
     * @param id
     * @return
     * @throws DataAccessException
     */
    Brand selectBrandById(Long id) throws DataAccessException;

    /**
     * 根据id修改品牌
     * 
     * @param brand
     * @return
     */
    void updateBrandById(Brand brand) throws DataAccessException;

    /**
     * 批量删除
     * 
     * @param ids
     * @throws DataAccessException
     */
    Integer deletes(Long[] ids) throws DataAccessException;

    /**
     * 根据id删除品牌
     * 
     * @param id
     * @return
     * @throws DataAccessException
     */
    Integer deleteBrandById(Long id) throws DataAccessException;
}
