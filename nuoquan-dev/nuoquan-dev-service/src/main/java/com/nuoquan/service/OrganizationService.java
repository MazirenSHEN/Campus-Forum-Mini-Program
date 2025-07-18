package com.nuoquan.service;

import com.nuoquan.pojo.Organization;
import com.nuoquan.pojo.OrganizationImage;
import com.nuoquan.pojo.vo.OrganizationVO;
import com.nuoquan.utils.PagedResult;

public interface OrganizationService {
    /**
     * 列出全部可显示的组织
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    PagedResult queryOrganization(Integer page, Integer pageSize, String userId);

    /**
     * 伪删除组织，使组织status=0(Unreadable)
     * @param organizationId
     */
    void pseudoDeleteOrganization(String organizationId);

    /**
     * 伪删除组织图片，使组织status=0(Unreadable)
     * @param organizationId
     * @param order
     * @return
     */
    int pseudoDeleteOrganizationImg(String organizationId, Integer order);

    /**
     * 根据组织id查询组织
     * @param id
     * @return
     */
    OrganizationVO getOrganizationById(String id);

    /**
     * 暂未使用
     * 根据组织图片id查询组织图片
     * @param id
     * @return
     */
    OrganizationImage getOrganizationImgById(String id);

    /**
     * 将组织信息存入数据库
     * @param organization
     * @return
     */
    String saveOrganization(Organization organization);

    /**
     * 保存组织图片
     * @param organizationImage
     * @return 图片id
     */
    String saveOrganizationImage(OrganizationImage organizationImage);
}
