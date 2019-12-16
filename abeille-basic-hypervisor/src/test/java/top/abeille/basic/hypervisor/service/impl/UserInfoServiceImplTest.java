/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.hypervisor.service.impl;

import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.ApiDocBuilder;
import com.power.doc.model.ApiConfig;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.abeille.basic.hypervisor.dto.UserDTO;
import top.abeille.basic.hypervisor.repository.UserInfoRepository;
import top.abeille.common.mock.AbstractServiceMock;

/**
 * 用户信息service测试
 *
 * @author liwenqiang 2019/1/29 17:10
 **/
public class UserInfoServiceImplTest extends AbstractServiceMock {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    /**
     * 测试修改用户信息
     * 如果使用jpa的getOne(),必须加@Transactional，否则会曝出 hibernate lazyXXXXX - no session
     */
    @Test
    public void save() {
        UserDTO user = new UserDTO();
        user.setNickname("管理员");
        String pwd = new BCryptPasswordEncoder().encode("abeille");
        userInfoService.create(user);
        Mockito.verify(userInfoRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void testBuilderControllersApi() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://localhost:8763");
        //true会严格要求注释，推荐设置true
        config.setStrict(true);
        //true会将文档合并导出到一个markdown
        //config.setAllInOne(true);
        //生成html时加密文档名不暴露controller的名称
        config.setMd5EncryptedHtmlName(true);

        //指定文档输出路径
        //@since 1.7 版本开始，选择生成静态html doc文档可使用该路径：DocGlobalConstants.HTML_DOC_OUT_PATH;
        config.setOutPath("D:\\doc");
        // @since 1.2,如果不配置该选项，则默认匹配全部的controller,
        // 如果需要配置有多个controller可以使用逗号隔开
        config.setPackageFilters("top.abeille.basic.hypervisor.controller");
        config.setProjectName("文档系统");

        long start = System.currentTimeMillis();
        //获取接口数据后自行处理
        ApiDocBuilder.builderControllersApi(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }
}