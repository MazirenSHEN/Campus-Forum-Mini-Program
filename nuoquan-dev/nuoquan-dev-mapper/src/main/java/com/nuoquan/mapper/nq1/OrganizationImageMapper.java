package com.nuoquan.mapper.nq1;

import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.OrganizationImage;
import com.nuoquan.utils.MyMapper;

import java.util.List;

public interface OrganizationImageMapper extends MyMapper<OrganizationImage> {

    // 获取此organization全部图片
    public List<OrganizationImage> getOrganizationImage(String organizationId);

    // 根据organizationId和imageOrder获得某张图片
    public OrganizationImage getOrganizationImageByIdAndOrder(String organizationId, Integer imageOrder);

    // 将此图片改为unreadable
    public void updateStatusToUnreadable(String organizationId, Integer imageOrder);
}