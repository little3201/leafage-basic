/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.service.impl;

import io.leafage.basic.hypervisor.domain.TreeNode;
import io.leafage.basic.hypervisor.dto.AuthorityDTO;
import io.leafage.basic.hypervisor.entity.Authority;
import io.leafage.basic.hypervisor.repository.AuthorityRepository;
import io.leafage.basic.hypervisor.service.AuthorityService;
import io.leafage.basic.hypervisor.vo.AuthorityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.leafage.common.basic.AbstractBasicService;
import java.util.*;

/**
 * 权限信息Service实现
 *
 * @author liwenqiang 2018/12/17 19:36
 **/
@Service
public class AuthorityServiceImpl extends AbstractBasicService implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Page<AuthorityVO> retrieve(int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(StringUtils.hasText(order) ? order : "modifyTime"));
        return authorityRepository.findAll(pageable).map(this::convertOuter);
    }

    @Override
    public List<TreeNode> tree() {
        List<Authority> authorities = authorityRepository.findByTypeAndEnabledTrue('M');
        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        List<TreeNode> authorityVOList = new ArrayList<>(authorities.size());
        authorities.stream().filter(a -> a.getSuperior() == null).forEach(a -> {
            TreeNode treeNode = this.constructNode(a.getCode(), a);
            treeNode.setChildren(this.addChildren(a, authorities));
            authorityVOList.add(treeNode);
        });
        return authorityVOList;
    }

    @Override
    public AuthorityVO create(AuthorityDTO authorityDTO) {
        Authority authority = new Authority();
        BeanUtils.copyProperties(authorityDTO, authority);
        authority.setCode(this.generateCode());
        authority = authorityRepository.save(authority);
        return this.convertOuter(authority);
    }

    @Override
    public void remove(String code) {
        Authority authority = authorityRepository.getByCodeAndEnabledTrue(code);
        authorityRepository.deleteById(authority.getId());
    }

    /**
     * 转换对象
     *
     * @param info 基础对象
     * @return 结果对象
     */
    private AuthorityVO convertOuter(Authority info) {
        AuthorityVO authorityVO = new AuthorityVO();
        BeanUtils.copyProperties(info, authorityVO);
        return authorityVO;
    }

    /**
     * add child node
     *
     * @param superior    superior node
     * @param authorities to be build source data
     * @return tree node
     */
    private List<TreeNode> addChildren(Authority superior, List<Authority> authorities) {
        List<TreeNode> voList = new ArrayList<>();
        authorities.stream().filter(authority -> superior.getId().equals(authority.getSuperior()))
                .forEach(authority -> {
                    TreeNode treeNode = this.constructNode(superior.getCode(), authority);
                    treeNode.setChildren(this.addChildren(authority, authorities));
                    voList.add(treeNode);
                });
        return voList;
    }

    /**
     * construct tree node
     *
     * @param superior  superior code
     * @param authority data
     * @return tree node
     */
    private TreeNode constructNode(String superior, Authority authority) {
        TreeNode treeNode = new TreeNode();
        treeNode.setCode(authority.getCode());
        treeNode.setName(authority.getName());
        treeNode.setSuperior(superior);

        Map<String, String> expand = new HashMap<>();
        expand.put("icon", authority.getIcon());
        expand.put("path", authority.getPath());

        treeNode.setExpand(expand);
        return treeNode;
    }

}
