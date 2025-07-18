package com.nuoquan.shiro.service;

import java.util.List;

import com.nuoquan.pojo.AdminPermission;
import com.nuoquan.pojo.AdminRole;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.service.AdminPermissionService;
import com.nuoquan.service.AdminRoleService;
import com.nuoquan.service.AdminService;
import com.nuoquan.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 身份校验核心类
 * 
 * @ClassName: MyShiroRealm
 * @author fuce
 * @date 2018年8月25日
 *
 */
@Service
public class MyShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	@Autowired
	private AdminPermissionService adminPermissionService;
	
	/**
	 * 认证登陆
	 */
	@SuppressWarnings("unused")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		 //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) {
            return null;
        }
		String username = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());
		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		AdminUser userInfo = adminService.queryAdminUserName(username);
//		System.out.println(userInfo);
//		System.out.println("----->>userInfo=" + userInfo.getUsername() + "---"+ userInfo.getPassword());
		if (userInfo == null)
			return null;
		else{
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
					userInfo, // 用户名
					userInfo.getPassword(), // 密码
					getName() // realm name
			);
			//此处盐值写死，可在注册时产生随机盐值一并储存验证，增加安全性。@Jerrio
			authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("jumbox")); 
			return authenticationInfo;
		}
		
	}
	
	 /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
		if(principals == null){  
	       throw new AuthorizationException("principals should not be null");
	    }
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		AdminUser userinfo  = (AdminUser)principals.getPrimaryPrincipal();
		String uid=userinfo.getId();
		List<AdminRole> adminRoles= adminRoleService.queryAdminUserRoles(uid); // 一个用户可能多个角色
		for(AdminRole userRole:adminRoles){
			//System.out.println("角色名字:"+gson.toJson(userrole));
			String rolid=userRole.getId();//角色id
			authorizationInfo.addRole(userRole.getName());//添加角色名字
			List<AdminPermission> permissions=adminPermissionService.queryPermissionByRoleId(rolid);
			for(AdminPermission p:permissions){
				//System.out.println("角色下面的权限:"+p.getPerms());
				if(StringUtils.isNotEmpty(p.getPerms())){
					authorizationInfo.addStringPermission(p.getPerms());
				}
				
			}
		}
		
		return authorizationInfo;
	}
	
	 /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
    

    /**
     * md5字符串测试类
     */
    @Test
    public void md5HashGenerator() {
    	Md5Hash md5Hash = new Md5Hash("admin", "jumbox", 1); //加盐
    	System.out.println(md5Hash.toString());
    }
}
