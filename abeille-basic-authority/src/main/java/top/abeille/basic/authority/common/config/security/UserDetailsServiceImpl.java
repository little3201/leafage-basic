package top.abeille.basic.authority.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.abeille.basic.authority.server.model.RoleInfoModel;
import top.abeille.basic.authority.server.model.UserInfoModel;
import top.abeille.basic.authority.server.model.UserRoleModel;
import top.abeille.basic.authority.server.service.RoleInfoService;
import top.abeille.basic.authority.server.service.UserInfoService;
import top.abeille.basic.authority.server.service.UserRoleService;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户认证service实现
 *
 * @author liwenqiang 2018/10/18 21:18
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoService userInfoService;

    private final UserRoleService userRoleService;

    private final RoleInfoService roleInfoService;

    @Autowired
    public UserDetailsServiceImpl(UserInfoService userInfoService, UserRoleService userRoleService, RoleInfoService roleInfoService) {
        this.userInfoService = userInfoService;
        this.userRoleService = userRoleService;
        this.roleInfoService = roleInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoModel userInfo = new UserInfoModel();
        userInfo.setUsername(username);//账号
        /*根据登录名查找用户信息*/
        UserInfoModel infoModel = userInfoService.getByExample(userInfo);
        if (infoModel == null) {
            throw new UsernameNotFoundException("username does not exist");
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        /*添加角色组*/
        this.addAuthorities(infoModel.getId(), authorityList);
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
     * @param userId        用户ID
     * @param authorityList 权限列表
     */
    private void addAuthorities(Long userId, List<GrantedAuthority> authorityList) {
        List<UserRoleModel> roleList = userRoleService.findAllByUserId(userId);
        roleList.forEach(userRoleInfo -> {
            RoleInfoModel roleInfo = roleInfoService.getById(userRoleInfo.getId());
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + roleInfo.getRoleName().toUpperCase()));
        });
    }
}
