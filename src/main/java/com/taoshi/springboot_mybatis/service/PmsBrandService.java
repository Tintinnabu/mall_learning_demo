package com.taoshi.springboot_mybatis.service;

import com.taoshi.springboot_mybatis.mbg.model.PmsBrand;

import java.util.List;

/**
 * @author Tintinnabu
 * @description
 * @data 2020/2/12
 */

public interface PmsBrandService {

    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrand brand);

    int updateBrand(Long id, PmsBrand brand);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);


}
