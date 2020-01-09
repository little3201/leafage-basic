package top.abeille.basic.hypervisor.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用户关键信息类
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserAuthority extends User {

    private static final long serialVersionUID = 4870999263949230176L;

    public UserAuthority(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
