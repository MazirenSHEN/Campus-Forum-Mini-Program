package com.nuoquan.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.DeleteObjectsRequest;
import com.qcloud.cos.model.DeleteObjectsResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nuoquan.config.COSProperties;
import com.nuoquan.utils.StringUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;

/**
 * @Description: 资源服务类，提供文件储存等服务
 * @Author: jerrio
 * @Date: 2020.8.13
 */
@Service
public class ResourceService {
	@Autowired
	private COSClient cosClient;
	@Autowired
	private COSProperties cosProperties;
	
	public void cosClientTest() {
		cosClient.putObject("nqbucket-1258460770", "test/key2", "test-content");
	}
	
	/**
	 * 以流形式上传图片
	 * @param file
	 * @param path
	 * @throws Exception
	 */
	private String uploadFile(MultipartFile file, String path) throws Exception {
		String filename = Sid.next();
		// 获取文件后缀
		String[] strList = file.getOriginalFilename().split("\\.");
		String key = path + filename + "." + strList[strList.length-1];
		// 上传到COS，方法2 从输入流上传(需提前告知输入流的长度, 否则可能导致 oom)
		InputStream inputStream = file.getInputStream();
		ObjectMetadata objectMetadata = new ObjectMetadata();
		// 设置输入流长度
		objectMetadata.setContentLength(file.getSize());
		// 设置 Content type, 默认是 application/octet-stream
		objectMetadata.setContentType(file.getContentType());
		PutObjectResult putObjectResult = cosClient.putObject(cosProperties.getBucketName(), key, inputStream, objectMetadata);
//		String etag = putObjectResult.getETag();
		// 关闭输入流...
		inputStream.close();
		return key;
	}

	/**
	 * 以流形式上传图片到预定地址
	 * @param file
	 * @throws Exception
	 */
	public String uploadImg(MultipartFile file) throws Exception {
		String path = cosProperties.getRootDir() + "/img/";
		return uploadFile(file, path);
	}

	/**
	 * 以流形式上传视频到预定地址
	 * @param file
	 * @throws Exception
	 */
	public String uploadVideo(MultipartFile file) throws Exception {
		String path = cosProperties.getRootDir() + "/video/";
		return uploadFile(file, path);
	}

	/**
	 * 批量删除文件
	 * @param keys
	 * @throws Exception
	 */
	public void deleteFile(String[] keys) throws Exception{
		DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(cosProperties.getBucketName());
		// 设置要删除的key列表, 最多一次删除1000个
		ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();
		// 传入要删除的文件名
		for (String key : keys){
			String finalKey;
			if (key.startsWith("/")){
				//去除头部"/"，不然接口找不到文件
				finalKey = (String) key.subSequence(1, key.length());
			}else {
				finalKey = key;
			}
			keyList.add(new DeleteObjectsRequest.KeyVersion(finalKey));
		}
		deleteObjectsRequest.setKeys(keyList);
		// 批量删除文件
		try {
			DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
			List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
		} catch (MultiObjectDeleteException mde) { // 如果部分删除成功部分失败, 返回MultiObjectDeleteException
			List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
			List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
		} catch (CosServiceException e) { // 如果是其他错误，例如参数错误， 身份验证不过等会抛出 CosServiceException
			e.printStackTrace();
			throw e;
		} catch (CosClientException e) { // 如果是客户端错误，例如连接不上COS
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 为url加上域名和根目录返回给前端
	 * @param url
	 * @return
	 */
	public String composeUrl(String url) {
		if (!StringUtils.isBlank(url) && !url.startsWith("http")) {
//			url = cosProperties.getDomain() + "/" + url;
			url = "/" + url;
		}
		return url;
	}
	
}
