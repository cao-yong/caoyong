package com.caoyong.core.service.product;

import java.util.List;

import com.caoyong.core.bean.base.Page;
import com.caoyong.core.bean.base.ResultBase;
import com.caoyong.core.bean.product.Color;
import com.caoyong.core.bean.product.Product;
import com.caoyong.core.bean.product.ProductIsShowVO;
import com.caoyong.core.bean.product.ProductQueryDTO;
import com.caoyong.exception.BizException;

/**
 * 产品
 * 
 * @author yong.cao
 * @time 2017年7月30日 下午9:33:09
 */
public interface ProductService {
    /**
     * 查询产品分页
     * 
     * @param query
     * @return
     * @throws BizException
     */
    Page<Product> selectPageByQuery(ProductQueryDTO query) throws BizException;

    /**
     * 查询颜色结果集
     * 
     * @return
     */
    ResultBase<List<Color>> selectColorList() throws BizException;

    /**
     * 保存商品信息
     * 
     * @param product
     * @return
     */
    ResultBase<Integer> saveProduct(Product product) throws BizException;

    /**
     * 上架
     * 
     * @param isShowVO
     * @return
     * @throws BizException
     */
    ResultBase<Integer> isShow(ProductIsShowVO isShowVO) throws BizException;

    /**
     * 根据 productId修改产品信息
     * 
     * @param product
     * @return
     * @throws BizException
     */
    ResultBase<Integer> updateProductById(Product product) throws BizException;

    /**
     * 根据 productId删除产品
     * 
     * @param id
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteProductById(Long id) throws BizException;

    /**
     * 根据 ids删除产品
     * 
     * @param ids
     * @return
     * @throws BizException
     */
    ResultBase<Integer> deleteProductByIds(Long ids[]) throws BizException;

}
