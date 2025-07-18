package com.nuoquan.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



public class MyInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)throws Exception {
		// System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)throws Exception {
		// System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//设置前端的全局 地址，如果前端网页错乱请修改这儿
		request.setAttribute("rootPath", request.getContextPath());
		//yml里面得演示模式true 为开启 就会拉取数据拦截表进行判断符合得就拦截
//		if(V2Config.getDemoEnabled().equals("true")) {
//			Boolean b = ifurl(request, response);
//			if(b) {
//				throw new DemoModeException();
//			}
//		}
//		System.out.print(request);
		return true;// 只有返回true才会继续向下执行，返回false取消当前请求
	}
	
	/** 
	 * 判断是否需要拦截的用于展示演示模式
	 * @param request
	 * @param response
	 * @return
	 * @author fuce
	 * @Date 2019年11月11日 下午5:17:30
	 */
//	public Boolean ifurl(HttpServletRequest request, HttpServletResponse response) {
//	
//		//当前请求
//		String requesturl=request.getRequestURI();
//		if(request.getMethod().equals("POST")) {
//			
//			for (SysInterUrl sysInterUrl : posturllist) {
//				if(StrUtil.containsAnyIgnoreCase(requesturl, sysInterUrl.getUrl())) {
//					return true;
//				}
//			}
//		}else {
//			for (SysInterUrl sysInterUrl : geturllist) {
//				if(StrUtil.containsAnyIgnoreCase(requesturl, sysInterUrl.getUrl())) {
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}

}
