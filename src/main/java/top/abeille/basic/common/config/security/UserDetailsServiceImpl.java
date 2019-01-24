package top.abeille.basic.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.abeille.basic.authority.model.RoleInfoModel;
import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.basic.authority.model.UserRoleModel;
import top.abeille.basic.authority.service.IRoleInfoService;
import top.abeille.basic.authority.service.IUserInfoService;
import top.abeille.basic.authority.service.IUserRoleService;

import java.util.LinkedList;
import java.util.List;

/**
 * 用户认证service实现
 *
 * @author liwenqiang
 * @date 2018/10/18 21:18
 **/
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleInfoService roleInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setUsername(username);//账号
        /*根据登录名查找用户信息*/
        UserInfoModel infoModel = userInfoService.getByExample(userInfo);
        if (infoModel == null) {
            throw new UsernameNotFoundException("username does not exist");
        }
        List<GrantedAuthority> authorityList = new LinkedList<>();
        /*添加角色组*/
        this.addAuthorities(infoModel, authorityList);
        /*检查角色是否配置*/
        if (infoModel.getUserRoleId() == null || authorityList.size() == 0) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        return new User(infoModel.getUsername(), infoModel.getPassword(), infoModel.getEnabled(), infoModel.getAccountNonExpired(),
                infoModel.getCredentialsNonExpired(), infoModel.getAccountNonLocked(), authorityList);
    }

    /**
     * 查询所有角色并添加到权限组中
     *
     * @param userInfo
     * @param authorityList
     */
    private void addAuthorities(UserInfoModel userInfo, List<GrantedAuthority> authorityList) {
        List<UserRoleModel> roleList = userRoleService.findAllByUserId(userInfo.getId());
        roleList.forEach(userRoleInfo -> {
            RoleInfoModel roleInfo = roleInfoService.getById(userRoleInfo.getId());
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + roleInfo.getRoleName().toUpperCase()));
        });
    }
}
