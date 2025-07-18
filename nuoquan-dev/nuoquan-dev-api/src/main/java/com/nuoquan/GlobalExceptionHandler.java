package com.nuoquan;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.nuoquan.utils.JSONResult;

/**
 * @author jerrio
 * 全局异常统一处理，返回值 和Controller的返回规则一样
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 全局捕获 Fdfs 找不到文件的异常 (这个异常好像捕获不到，已经被处理，应捕获 NoSuchMethodError)
	 * @param t
	 */
    @ExceptionHandler(FdfsServerException.class)
    public void fdfsException(Throwable t){
    	System.out.println("错误码：2，错误信息：找不到节点或文件");
    }
    /**
     * 全局捕获上传文件超出限制的异常 
     * (有返回给前端的值的时候最好不要使用该全局捕获，该异常会继续往下执行代码，造成
     * 多次返回的情况，会报错。 @author jerrio)
     * @param t
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    public JSONResult handleAll(Throwable t){
        System.out.println("Uploaded file size exceed server's limit");
        return JSONResult.errorException("Uploaded file size exceed server's limit");
    }
}