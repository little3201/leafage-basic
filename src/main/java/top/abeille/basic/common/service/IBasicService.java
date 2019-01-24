package top.abeille.basic.common.service;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * service基础接口
 *
 * @author liwenqiang 2018/7/27 23:14
 **/
public interface IBasicService<T> {

    /**
     * 根据id获取entity
     *
     * @param id
     * @return T
     */
    default T getById(Long id) {
        return null;
    }

    /**
     * 根据条件模版获取entity
     *
     * @param t
     * @return T
     */
    default T getByExample(T t) {
        return null;
    }

    /**
     * 获取所有的entity
     *
     * @return List<T>
     */
    default List<T> findAll() {
        return null;
    }

    /**
     * 获取所有entities并排序
     *
     * @param sort
     * @return List<T>
     */
    default List<T> findAll(Sort sort) {
        return null;
    }

    /**
     * 根据条件查询所有——设置匹配条件，如例所示：
     * Type one: ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("oneVar","twoVar");
     * Type two: ExampleMatcher exampleMatcher = ExampleMatcher.matching()
     *                      .withMatcher(roleInfoModel.getRoleName(), startsWith().ignoreCase())
     *                      .withMatcher(String.valueOf(roleInfoModel.getRoleId()), ExampleMatcher.GenericPropertyMatchers.contains());
     *
     * @param t
     * @return List<T>
     */
    default List<T> findAllByExample(T t, ExampleMatcher exampleMatcher) {
        return null;
    }

    /**
     * 分页获取所有entities
     *
     * @param pageable
     * @return Page<T>
     */
    default Page<T> findAllByPage(Pageable pageable) {
        return null;
    }

    /**
     * 获取行
     *
     * @return long type result
     */
    default long getCount() {
        return 0;
    }


    /**
     * 根据pkId删除entity
     *
     * @param id
     * @return
     */
    default int removeById(Long id) {
        return 0;
    }

    /**
     * 批量删除
     *
     * @param entities
     * @return
     */
    default int removeInBatch(List<T> entities) {
        return 0;
    }

    /**
     * 保存entity
     *
     * @param entity
     * @return
     */
    default T save(T entity) {
        return null;
    }

    /**
     * 批量保存
     *
     * @param entities
     * @return
     */
    default List<T> saveAll(List<T> entities) {
        return null;
    }

    /**
     * 更新entity
     *
     * @param entity
     * @return
     */
    default T update(T entity) {
        return null;
    }

    /**
     * 批量更新
     *
     * @param entities
     * @return
     */
    default List<T> updateAll(List<T> entities) {
        return null;
    }

}
