package top.abeille.basic.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.abeille.basic.profile.model.AccountInfoModel;
import top.abeille.basic.profile.service.AccountInfoService;
import top.abeille.common.basic.BasicController;

/**
 * 账户信息Controller
 *
 * @author liwenqiang 2018/12/20 9:54
 **/
@RestController
public class AccountInfoController extends BasicController {

    private final AccountInfoService accountInfoService;

    @Autowired
    public AccountInfoController(AccountInfoService accountInfoService) {
        this.accountInfoService = accountInfoService;
    }

    /**
     * 查询账号信息——根据ID
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @GetMapping("/v1/account")
    public ResponseEntity getAccount(Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        AccountInfoModel account = accountInfoService.getById(id);
        if (account == null) {
            log.info("Not found anything about account with id {}.", id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(account);
    }

    /**
     * 保存账号信息
     *
     * @param account 账户信息
     * @return ResponseEntity
     */
    @PostMapping("/v1/account")
    public ResponseEntity saveAccount(@RequestBody AccountInfoModel account) {
        try {
            accountInfoService.save(account);
        } catch (Exception e) {
            log.error("Save account occurred an error: ", e);
            return ResponseEntity.ok("error");
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    /**
     * 修改账号信息
     *
     * @param account 账户信息
     * @return ResponseEntity
     */
    @PutMapping("/v1/account")
    public ResponseEntity modifyAccount(@RequestBody AccountInfoModel account) {
        if (account.getId() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            accountInfoService.save(account);
        } catch (Exception e) {
            log.error("Modify account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.NOT_MODIFIED);
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * 删除账号信息
     *
     * @param id 主键
     * @return ResponseEntity
     */
    @DeleteMapping("/v1/account")
    public ResponseEntity removeAccount(Long id) {
        if (id == null) {
            return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            accountInfoService.removeById(id);
        } catch (Exception e) {
            log.error("Remove account occurred an error: ", e);
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(HttpStatus.MOVED_PERMANENTLY);
    }
}
