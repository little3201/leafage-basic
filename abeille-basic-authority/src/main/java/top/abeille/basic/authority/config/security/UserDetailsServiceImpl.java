package top.abeille.basic.authority.config.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.abeille.basic.authority.model.RoleInfoModel;
import top.abeille.basic.authority.model.UserInfoModel;
import top.abeille.basic.authority.model.UserRoleModel;
import top.abeille.basic.authority.service.RoleInfoService;
import top.abeille.basic.authority.service.UserInfoService;
import top.abeille.basic.authority.service.UserRoleService;

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

    public UserDetailsServiceImpl(UserInfoService userInfoService, UserRoleService userRoleService, RoleInfoService roleInfoService) {
        this.userInfoService = userInfoService;
        this.userRoleService = userRoleService;
        this.roleInfoService = roleInfoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据登录名查找用户信息
        UserInfoModel userInfo = userInfoService.getByUsername(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("username does not exist");
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        // 添加角色组
        this.addAuthorities(userInfo.getId(), authorityList);
        // 检查角色是否配置
        if (CollectionUtils.isEmpty(authorityList)) {
            throw new InsufficientAuthenticationException("permission denied");
        }
        return new User(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEnabled(), userInfo.getAccountNonExpired(),
                userInfo.getCredentialsNonExpired(), userInfo.getAccountNonLocked(), authorityList);
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
