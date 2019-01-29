package top.abeille.basic.common.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller基础类
 *
 * @author liwenqiang 2018/7/27 23:12
 **/
@RestController
public abstract class BasicController {

    /**
     * 开启日志
     */
    protected static final Log log = LogFactory.getLog(BasicController.class);

}
