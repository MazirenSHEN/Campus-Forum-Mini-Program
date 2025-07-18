package com.nuoquan.shiro.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import com.nuoquan.utils.StringUtils;
import com.nuoquan.pojo.AdminUser;
import com.nuoquan.shiro.service.MyShiroRealm;


/**
 * shiro 工具类
 *	
 * @author fuce
 */
public class ShiroUtils {

    private ShiroUtils(){}
    
    /**
     * 获取shiro subject
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午10:00:55
     */
    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }
    
    /**
     * 获取登录session
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午10:00:41
     */
    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 退出登录
     * @author fuce
     * @Date 2019年11月21日 上午10:00:24
     */
    public static void logout()
    {
        getSubject().logout();
    }
    
    /**
     * 获取登录用户model
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午10:00:10
     */
    public static AdminUser getUser()
    {
    	AdminUser user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj))
        {
            user = new AdminUser();
            BeanUtils.copyProperties(obj, user);
        }
        return user;
    }
    
    /**
     * set用户
     * @param user
     * @author fuce
     * @Date 2019年11月21日 上午9:59:52
     */
    public static void setUser(AdminUser user)
    {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }
    
    /**
     * 清除授权信息
     * @author fuce
     * @Date 2019年11月21日 上午9:59:37
     */
    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyShiroRealm realm = (MyShiroRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }
    
    /**
     * 获取登录用户id
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午9:58:55
     */
    public static String getUserId()
    {
    	AdminUser adminUser = getUser();
        if (adminUser == null || adminUser.getId() == null){
            throw new RuntimeException("用户不存在！");
        }
        return adminUser.getId().trim();
    }

    /**
     * 获取登录用户name
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午9:58:48
     */
    public static String getLoginName()
    {
    	AdminUser adminUser = getUser();
        if (adminUser == null){
            throw new RuntimeException("用户不存在！");
        }
        return adminUser.getUsername();
    }
    
    /**
     * 获取登录用户ip
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午9:58:26
     */
    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }
    
    /**
     * 获取登录用户sessionid
     * @return
     * @author fuce
     * @Date 2019年11月21日 上午9:58:37
     */
    public static String getSessionId()
    {
        return String.valueOf(getSubject().getSession().getId());
    }
}
