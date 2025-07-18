//package com.nuoquan.admin.controller;
//
//import com.nuoquan.controller.BasicController;
//import com.nuoquan.pojo.admin.Tablepar;
//import com.nuoquan.pojo.vo.TitleVo;
//import com.nuoquan.service.LongArticleMgtService;
//import com.nuoquan.utils.JSONResult;
//import com.nuoquan.utils.PagedResult;
//import io.swagger.annotations.Api;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 对长文章进行后台管理操作:
// */
//@Api(value = "长文章人工操作")
//@Controller
//@RequestMapping("/CheckLongArticle")
//public class LongArticleMGTController extends BasicController {
//
//    private String prefix = "admin/hotArticle";
//
//    @Autowired
//    private LongArticleMgtService longArticleMgtService;
//
//    /**
//     * 展示跳转页面
//     *
//     * @param modelMap
//     * @return
//     */
//    @GetMapping("/view")
//    @RequiresPermissions("system:longArticle:view")
//    public String view(ModelMap modelMap) {
//        String str = "长文章";
//        setTitle(modelMap, new TitleVo("列表", str + "管理", true, "欢迎进入" + str + "页面", true, false));
//        return prefix + "/list";
//    }
//
//    /**
//     * 查询全部hotArticle
//     * @param tablepar
//     * @param searchText
//     * @return
//     */
//    @PostMapping("/list")
//    @RequiresPermissions("system:longArticle:list")
//    @ResponseBody
//    public Object list(Tablepar tablepar, String searchText) {
//        PagedResult result = longArticleMgtService.list(tablepar.getPageNum(), tablepar.getPageSize());
//        return result;
//    }
//
//    /**
//     * TODO:是否需要queryList方法？
//     * 新增跳转
//     * @param modelMap
//     * @return
//     */
//    @GetMapping("/add")
//    public String add(ModelMap modelMap) {
//        // 添加头条长文章列表
//        List<HotArticleMgt> articleList = longArticleMgtService.queryList();
//        modelMap.put("hotArticleList", articleList);
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存
//     * @param article
//     * @param model
//     * @param
//     * @return
//     */
//    @PostMapping("/add")
//    @RequiresPermissions("system:longArticle:list")
//    @ResponseBody
//    public JSONResult add(HotArticleMgt article, Model model){
//        int flag = longArticleMgtService.insertHotArticle(article);
//        if (flag > 0) {
//            return JSONResult.ok();
//        } else {
//            return JSONResult.errorMsg("添加失败");
//        }
//    }
//
//    @PostMapping("/remove")
//    @RequiresPermissions("system:longArticle:remove")
//    @ResponseBody
//    public JSONResult remove(String ids) {
//        int flag = longArticleMgtService.deleteHotArticle(ids);
//        if (flag > 0) {
//            return JSONResult.ok();
//        } else {
//            return JSONResult.errorMsg("删除失败");
//        }
//    }
//}
