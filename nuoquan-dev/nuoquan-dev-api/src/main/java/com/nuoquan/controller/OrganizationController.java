package com.nuoquan.controller;

import com.nuoquan.enums.StatusEnum;
import com.nuoquan.mapper.nq1.OrganizationMapper;
import com.nuoquan.pojo.Organization;
import com.nuoquan.pojo.OrganizationImage;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.JsonUtils;
import com.nuoquan.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "组织相关接口", tags = { "OrganizationController" })
@RequestMapping("/organization")
public class OrganizationController extends BasicController {
    @Autowired
    OrganizationMapper organizationMapper;

    @ApiOperation(value = "查询全部可显示的组织", notes = "查询全部可显示的组织的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "操作者ID", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryOrganizations")
    public JSONResult queryOrganizations(Integer page, Integer pageSize, String userId){
        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = PAGE_SIZE;
        }
        PagedResult result = organizationService.queryOrganization(page, pageSize, userId);

        return JSONResult.ok(result);
    }

    @ApiOperation(value = "更改组织状态为unreadable", notes = "更改组织状态为unreadable的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationId", value = "组织id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value="/pseudoDeleteOrganization")
    public JSONResult pseudoDeleteOrganization(String organizationId) throws Exception {
        organizationService.pseudoDeleteOrganization(organizationId);
        return JSONResult.ok();
    }

    @ApiOperation(value = "根据组织id查询", notes = "根据组织id查询的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "操作者ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "organizationId", value = "组织id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value="/getOrganizationById")
    public JSONResult getOrganizationById(String userId, String organizationId) throws Exception {
        return JSONResult.ok(organizationService.getOrganizationById(organizationId));
    }

    @ApiOperation(value = "上传或修改组织信息", notes = "上传或修改组织信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value="组织名称", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="intro", value="组织简介", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="division", value="组织部门组成", required=false, dataType="String", paramType="form"),
            @ApiImplicitParam(name="activityIntro", value="组织活动介绍", required=false, dataType="String", paramType="form"),
            @ApiImplicitParam(name="officialAccountLink", value="组织公众号或推文链接", required=false, dataType="String", paramType="form"),
            @ApiImplicitParam(name="status", value="状态/类别", required=true, dataType="Integer", paramType="form")
    })
    @PostMapping(value="/uploadOrganization")
    public JSONResult uploadOrganization(@ApiParam(value = "file", required = true) MultipartFile logo,
                                         String name, String intro, String division,
                                         String activityIntro,
                                         String officialAccountLink, Integer status) throws Exception {
        boolean isLegal = false;
        Organization organization = new Organization();

        // 上传组织logo
        if (logo != null && (status==1 || status == 2)) {
            // 判断是否超出大小限制
            if (logo.getSize() > MAX_IMAGE_SIZE) {
                return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
            }
            String logoName = logo.getOriginalFilename();
            if (StringUtils.isNotBlank(logoName)) {
                // 保存到数据库中的相对路径
                String uploadPathDB = resourceService.uploadImg(logo);
                organization.setLogoPath(uploadPathDB);
            }else {
                return JSONResult.errorMsg("File name is blank");
            }
        }else {
            return JSONResult.errorMsg("Upload error");
        }

        // 保存组织信息到数据库
        organization.setName(name);
        organization.setIntro(intro);
        organization.setDivision(division);
        organization.setActivityIntro(activityIntro);
        organization.setOfficialAccountLink(officialAccountLink);

        // 检测内容是否非法
        if (weChatService.msgSecCheck(name)
                && weChatService.msgSecCheck(intro)
                && weChatService.msgSecCheck(division)
                && weChatService.msgSecCheck(activityIntro)
                && weChatService.msgSecCheck(officialAccountLink)) {
            // 合法
            isLegal = true;
            organization.setStatus(status);
//            if (resourceConfig.getAutoCheckArticle()) { //查看是否设置自动过审
//                organization.setStatus(StatusEnum.READABLE.type);
//            }else {
//                organization.setStatus(StatusEnum.CHECKING.type);
//            }
        } else {
            // 非法，尽管非法也保存到数据库
            organization.setStatus(StatusEnum.DELETED.type);
        }
        String organizationId = organizationService.saveOrganization(organization); // 存入数据库

        if (isLegal) {
            return JSONResult.ok(organizationId);
        }else {
            return JSONResult.errorMsg("发布内容涉嫌违规");
        }
    }

    @ApiOperation(value = "上传组织图片", notes = "上传组织图片的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId", value="组织Id", required=true, dataType="String", paramType="form"),
            @ApiImplicitParam(name="order", value="图片顺序", required=true, dataType="int", paramType="form")
    })
    @PostMapping(value="/uploadOrganizationImg")
    public JSONResult uploadOrganizationImg(String organizationId,
                                            Integer order,
                                            @ApiParam(value="file", required=true) MultipartFile file) throws Exception {

        if (org.apache.commons.lang3.StringUtils.isNoneBlank(organizationId) && file != null) {
            // 判断是否超出大小限制
            if (file.getSize() > MAX_IMAGE_SIZE) {
                return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
            }
            String fileName = file.getOriginalFilename();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
                // 获取文件后缀
                String[] strList = fileName.split("\\.");
                String newFileName = order + "." + strList[strList.length-1];	// 把顺序 order.原后缀 作为文件名
                // 保存到数据库中的相对路径
                String uploadPathDB = resourceService.uploadImg(file);

                Integer imageOrder = Integer.valueOf(order);

                OrganizationImage organizationImage = new OrganizationImage();
                organizationImage.setImagePath(uploadPathDB);
                organizationImage.setOrganizationId(organizationId);
                organizationImage.setImageOrder(imageOrder);
                String imgId = organizationService.saveOrganizationImage(organizationImage);

                return JSONResult.ok(imgId);
            }else {
                return JSONResult.errorMsg("File name is blank");
            }
        }else {
            return JSONResult.errorMsg("Upload error");
        }
    }

    @ApiOperation(value = "更改组织图片为unreadable", notes = "更改组织图片为unreadable的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationId", value = "组织id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "order", value = "图片顺序", required = true, dataType = "int", paramType = "form")
    })
    @PostMapping(value="/pseudoDeleteOrganizationImg")
    public JSONResult pseudoDeleteOrganizationImg(String organizationId, Integer order) throws Exception {
        // 如果返回值为1，表示有此图片
        if (organizationService.pseudoDeleteOrganizationImg(organizationId, order) == 1)
            return JSONResult.ok();
            // 如果返回值为0，表示无此图片
        else
            return JSONResult.errorMsg("无此图片");
    }


}
