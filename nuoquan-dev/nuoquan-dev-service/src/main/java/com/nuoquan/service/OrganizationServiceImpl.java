package com.nuoquan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.OrganizationImageMapper;
import com.nuoquan.mapper.nq1.OrganizationMapper;
import com.nuoquan.pojo.Organization;
import com.nuoquan.pojo.OrganizationImage;
import com.nuoquan.pojo.vo.ArticleVO;
import com.nuoquan.pojo.vo.OrganizationVO;
import com.nuoquan.utils.PageUtils;
import com.nuoquan.utils.PagedResult;
import com.nuoquan.utils.SensitiveFilterUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService{
    @Autowired
    private Sid sid;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private OrganizationImageMapper organizationImageMapper;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;

    /**
     * 将Organization转换为OrganizationVO 并为组织添加VO属性
     * @param organization
     * @return
     */
    private OrganizationVO composeOrganizationVO(Organization organization) {
        OrganizationVO organizationVO = new OrganizationVO();
        BeanUtils.copyProperties(organization, organizationVO);
        return composeOrganizationVO(organizationVO);
    }

    /**
     * 为组织添加VO属性
     * @param organizationVO
     * @return
     */
    private OrganizationVO composeOrganizationVO(OrganizationVO organizationVO) {
        // 添加图片列表
        organizationVO = addOrganizationImage(organizationVO);
        organizationVO.setLogoPath(resourceService.composeUrl(organizationVO.getLogoPath()));

        // 敏感词检测
        organizationVO.setIntro(sensitiveFilterUtil.filter(organizationVO.getIntro()));
        organizationVO.setName(sensitiveFilterUtil.filter(organizationVO.getName()));
        organizationVO.setActivityIntro(sensitiveFilterUtil.filter(organizationVO.getActivityIntro()));
        organizationVO.setDivision(sensitiveFilterUtil.filter(organizationVO.getDivision()));
        return organizationVO;
    }

    /**
     * 为组织添加图片
     * @param organizationVO
     * @return
     */
    public OrganizationVO addOrganizationImage(OrganizationVO organizationVO) {
        List<OrganizationImage> images = organizationImageMapper.getOrganizationImage(organizationVO.getId());
        //为图片url添加前缀
        int flag = 1;   // 图片order序号flag
        int noImgIndex[] = new int[6-images.size()];    // 空缺图片order list
        int i = 0;
        for (OrganizationImage image : images) {
            while (image.getImageOrder() != flag){
                noImgIndex[i]=flag-1;
                flag++;
                i++;
            }
            image.setImagePath(resourceService.composeUrl(image.getImagePath()));
            flag++;
        }
        while (flag <= 6){
            noImgIndex[i]=flag-1;
            flag++;
            i++;
        }

        for (int j = 0; j < noImgIndex.length; j++) {
            images.add(noImgIndex[j], null);
        }
        // 添加图片列表
        organizationVO.setImgList(images);
        return organizationVO;
    }

    /**
     * 列出全部可显示的组织
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public PagedResult queryOrganization(Integer page, Integer pageSize, String userId){
        Example organizationExample = new Example(Organization.class);
        // 在这些组织中找到状态为可读的组织
        Example.Criteria statusCriteria = organizationExample.createCriteria();
        statusCriteria.andNotEqualTo("status", StatusEnum.DELETED.type);
        organizationExample.and(statusCriteria);
        organizationExample.setOrderByClause("create_date asc");

        PageHelper.startPage(page, pageSize);
        List<Organization> list = organizationMapper.selectByExample(organizationExample);
        PageInfo<Organization> pageInfo = new PageInfo<>(list);
        PageInfo<OrganizationVO> pageInfoVO = PageUtils.PageInfo2PageInfoVo(pageInfo);

        // 组装VO
        List<OrganizationVO> listVO = new ArrayList<>();
        for (Organization c : list) {
            listVO.add(composeOrganizationVO(c));
        }
        pageInfoVO.setList(listVO);

        //为最终返回对象 pagedResult 添加属性
        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(pageInfoVO.getPageNum());
        pagedResult.setTotal(pageInfoVO.getPages());
        pagedResult.setRows(pageInfoVO.getList());
        pagedResult.setRecords(pageInfoVO.getTotal());

        return pagedResult;
    }

    /**
     * 伪删除组织，使组织status=0(Unreadable)
     * @param organizationId
     */
    @Override
    public void	pseudoDeleteOrganization(String organizationId) {
        Example example = new Example(Organization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", organizationId);
        Organization c = new Organization();
        c.setStatus(StatusEnum.DELETED.type);
        organizationMapper.updateByExampleSelective(c, example);
    }

    /**
     * 伪删除组织，使组织status=0(Unreadable)
     * @param organizationId
     * @param imageOrder
     * @return
     */
    @Override
    public int pseudoDeleteOrganizationImg(String organizationId, Integer imageOrder){
        int flag = 0;

        OrganizationImage j = organizationImageMapper.getOrganizationImageByIdAndOrder(organizationId, imageOrder);
        if (j != null) {
            if (j.getStatus() != 0) {
                j.setStatus(StatusEnum.DELETED.type);
                organizationImageMapper.updateStatusToUnreadable(organizationId, imageOrder);
                flag = 1;
            }
        }
        return flag;
    }

    /**
     * 根据组织id查询组织
     * @param id
     * @return
     */
    @Override
    public OrganizationVO getOrganizationById(String id){
        Organization organization = organizationMapper.selectByPrimaryKey(id);
        OrganizationVO organizationVO = composeOrganizationVO(organization);
        return organizationVO;
    }

    /**
     * 暂时没有用上
     * 根据组织图片id查询组织图片
     * @param id
     * @return
     */
    @Override
    public OrganizationImage getOrganizationImgById(String id){
       return null;
    }

    /**
     * 将组织信息存入数据库
     * @param organization
     * @return
     */
    @Override
    public String saveOrganization(Organization organization) {
        String id = sid.nextShort();
        organization.setId(id);
        organizationMapper.insertSelective(organization);
        return id;
    }

    /**
     * 将图片存入数据库
     * @param organizationImage
     * @return 图片id
     */
    @Override
    public String saveOrganizationImage(OrganizationImage organizationImage) {
        String id = sid.nextShort();
        organizationImage.setId(id);
        organizationImageMapper.insertSelective(organizationImage);
        return id;
    }


}
