package com.nuoquan.controller;

import com.nuoquan.utils.JSONResult;
import com.nuoquan.utils.StringUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @Description: 资源相关接口
 * @Author: Jerrio
 * @Date: 8/6/2020
 */
@RestController
@Api(value="资源相关接口", tags= {"Resource-Controller"})
@RequestMapping("/resource")
public class ResourceController extends BasicController{

    @ApiOperation(value = "Upload file to COS")
    @ApiImplicitParams({})
    @PostMapping("/uploadFile")
    public JSONResult uploadFile(@ApiParam(value = "face image", required = true) MultipartFile file)
            throws Exception {

        if (file != null) {
            // Determine the file format.
            String[] strList = Objects.requireNonNull(file.getContentType()).split("/");
            String type = strList[0];
            String uploadPathDB = "";
            // 判断是否超出大小限制
            if (type.equalsIgnoreCase("image")){
                if (file.getSize() > MAX_IMAGE_SIZE) {
                    return JSONResult.errorException("Uploaded file size exceed server's limit (10MB)");
                } else {
                    uploadPathDB = resourceService.uploadImg(file);
                }
            }else if (type.equalsIgnoreCase("video")){
                if (file.getSize() > MAX_Video_SIZE) {
                    return JSONResult.errorException("Uploaded file size exceed server's limit (10GB)");
                } else {
                    uploadPathDB = resourceService.uploadVideo(file);
                }
            } else {
                return JSONResult.errorMsg("Upload error! Can only upload video or image now.s");
            }
            return JSONResult.ok(uploadPathDB);
        }else {
            return JSONResult.errorMsg("Upload error! The file is null.");
        }
    }

    @ApiOperation(value = "Delete file in COS", notes = "This api supports batch delete.")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keys", value = "COS储存路径", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/deleteFile")
    public JSONResult deleteFile(String keys) throws Exception {

        if (StringUtils.isBlank(keys)) {
            return JSONResult.errorMsg("Keys can not be blank");
        } else {
            String[] keyList = keys.split(",\\s*");
            resourceService.deleteFile(keyList);
        }
        return JSONResult.ok();
    }
}
