package com.nuoquan.controller;

import com.nuoquan.pojo.ArticleImage;
import com.nuoquan.pojo.FeaturedArticle;
import com.nuoquan.pojo.vo.FeaturedArticleVO;
import com.nuoquan.service.FeaturedArticleService;
import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static com.nuoquan.controller.BasicController.PAGE_SIZE;
/**
 * 文章加精相关接口
 * @author BoyuanYE
 * @date 2022.08,10
 */
@RestController
@Api(value = "文章加精相关接口", tags = { "FeaturedArticle-Controller" })
@RequestMapping("/featuredArticle")
public class FeaturedArticleController extends BasicController {

    @ApiOperation(value = "查询全部加精文章", notes = "查询全部加精文章的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/queryFeaturedArticles")
    public JSONResult queryFeaturedArticles(Integer page, Integer pageSize, String userId){
        if(page == null) {
            page = 1;
        }

        if(pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedResult result = featuredArticleService.queryFeaturedArticle(page, pageSize, userId);

        return JSONResult.ok(result);
    }


    @ApiOperation(value = "查询加精文章", notes = "查询加精文章的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "featuredArticleId", value = "文章加精id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "操作者id", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getFeaturedArticleById")
    public JSONResult getFeaturedArticleById(String featuredArticleId, String userId){

        FeaturedArticleVO result = featuredArticleService.getFeaturedArticleById(featuredArticleId, userId);

        return JSONResult.ok(result);
    }



    @ApiOperation(value = "文章加精并设置封面", notes = "文章加精及封面设置接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "原始文章id", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/setToFeaturedArticle")
    public JSONResult setToFeaturedArticle(@ApiParam(value="file", required=true) MultipartFile coverFile,
                                           String articleId) throws Exception{
        if (!featuredArticleService.isArticleFeatured(articleId)){
            return JSONResult.errorException("Already Featured");
        }
        FeaturedArticle featuredArticle = new FeaturedArticle();
        featuredArticle.setArticleId(articleId);
        featuredArticle.setCreateDate(new Date());

        //设置封面图片
        if (coverFile != null) {
            // 判断是否超出大小限制
            if (coverFile.getSize() > MAX_IMAGE_SIZE) {
                return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
            }
            String fileName = coverFile.getOriginalFilename();
            if (StringUtils.isNotBlank(fileName)) {
                // 获取文件后缀
                String[] strList = fileName.split("\\.");
                String newFileName = "coverImg." + strList[strList.length-1];	// 把顺序 order.原后缀 作为文件名
                // 保存到数据库中的相对路径
                String uploadPathDB = resourceService.uploadImg(coverFile);

                featuredArticle.setCoverPath(uploadPathDB);
            }else {
                return JSONResult.errorMsg("File name is blank");
            }
        }else {
            return JSONResult.errorMsg("Cover upload error");
        }

        String featuredArticleId = featuredArticleService.setToFeaturedArticle(featuredArticle); // 存入数据库
        return JSONResult.ok(featuredArticleId);
    }


    @ApiOperation(value = "撤销文章加精（将精选文章status改为0）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "featuredArticleId", value = "加精id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping(value="/removeFeaturedArticle")
    public JSONResult removeFeaturedArticle(String featuredArticleId) throws Exception {
        featuredArticleService.removeFeaturedArticle(featuredArticleId);
        return JSONResult.ok();
    }



}
