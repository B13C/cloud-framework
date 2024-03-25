package cn.maple.core.datasource.service;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.repository.GXMyBatisRepository;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.GXUnionTypeEnums;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.model.GXBaseModel;
import cn.maple.core.framework.service.GXBusinessService;
import cn.maple.core.framework.util.GXCommonUtils;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 业务DB基础Service
 *
 * @param <P>  仓库对象类型
 * @param <M>  Mapper类型
 * @param <T>  实体类型
 * @param <D>  DAO类型
 * @param <R>  响应对象类型
 * @param <ID> 实体的主键ID类型
 * @author britton chen <britton@126.com>
 */
@SuppressWarnings("unused")
public interface GXMyBatisBaseService<P extends GXMyBatisRepository<M, T, D, ID>, M extends GXBaseMapper<T>, T extends GXBaseModel, D extends GXMyBatisDao<M, T, ID>, R extends GXBaseDBResDto, ID extends Serializable> extends GXBusinessService, GXValidateDBExistsService {
    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition);

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    boolean checkRecordIsExists(List<GXCondition<?>> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName    表名字
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition);

    /**
     * 通过SQL更新表中的数据
     *
     * @param updateFields 需要更新的数据
     * @param condition    更新条件
     * @return Integer
     */
    Integer updateFieldByCondition(List<GXUpdateField<?>> updateFields, List<GXCondition<?>> condition);

    /**
     * 列表或者搜索(分页)
     *
     * @param searchReqDto 参数
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto searchReqDto);

    /**
     * 列表或者搜索(分页)
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return GXPagination
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 搜索条件
     * @return List
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto);

    /**
     * 通过条件查询列表信息
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return List
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 查询条件
     * @param rowMapper          映射函数
     * @return List
     */
    <E> List<E> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Function<Dict, E> rowMapper);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    List<R> findByCondition(List<GXCondition<?>> condition);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param extraData 额外数据
     * @return List
     */
    List<R> findByCondition(List<GXCondition<?>> condition, Object extraData);

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param columns   需要查询的列
     * @return List
     */
    List<R> findByCondition(List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName  表名字
     * @param condition  搜索条件
     * @param columns    需要查询的字段
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    List<R> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField);

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    List<R> findByCondition(List<GXCondition<?>> condition, Map<String, String> orderField, Set<String> groupField);

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    List<R> findByCondition(List<GXCondition<?>> condition, Map<String, String> orderField);

    /**
     * 通过条件查询列表信息
     *
     * @param tableName  表名字
     * @param condition  搜索条件
     * @param columns    需要查询的字段
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    R findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    R findOneByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition, Object extraData);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(String tableName, Set<String> columns, List<GXCondition<?>> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param queryParamInnerDto 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(GXBaseQueryParamInnerDto queryParamInnerDto);

    /**
     * 通过条件获取一条数据
     *
     * @param masterQueryParamInnerDto   外层的主查询条件
     * @param unionQueryParamInnerDtoLst union查询条件
     * @param unionTypeEnums             union的类型
     * @return 一条数据
     */
    R findOneByCondition(GXBaseQueryParamInnerDto masterQueryParamInnerDto, List<GXBaseQueryParamInnerDto> unionQueryParamInnerDtoLst, GXUnionTypeEnums unionTypeEnums);

    /**
     * 通过条件获取一条数据
     *
     * @param queryParamInnerDto 搜索条件
     * @param rowMapper          映射函数
     * @return 一条数据
     */
    <E> E findOneByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Function<Dict, E> rowMapper);

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    R findOneByCondition(List<GXCondition<?>> condition, Object extraData);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @return 一条数据
     */
    R findOneByCondition(List<GXCondition<?>> condition);

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param columns   字段集合
     * @return 一条数据
     */
    R findOneByCondition(List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    ID updateOrCreate(T entity);

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    ID updateOrCreate(T entity, List<GXCondition<?>> condition);

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param condition   条件
     * @param copyOptions 复制可选项
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req, List<GXCondition<?>> condition, CopyOptions copyOptions);

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req, CopyOptions copyOptions);

    /**
     * 创建或者更新
     *
     * @param req 请求参数
     * @return ID
     */
    <Q extends GXBaseReqDto> ID updateOrCreate(Q req);

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @param extraData     额外数据
     * @return 新数据ID
     */
    ID copyOneData(List<GXCondition<?>> copyCondition, Dict replaceData, Dict extraData);

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @return 新数据ID
     */
    ID copyOneData(List<GXCondition<?>> copyCondition, Dict replaceData);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName       表名
     * @param updateFieldList 软删除时需要同时更新的字段
     * @param condition       删除条件
     * @param extraData       额外数据
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXUpdateField<?>> updateFieldList, List<GXCondition<?>> condition, Dict extraData);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @param extraData 额外数据
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition, Dict extraData);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(List<GXCondition<?>> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(List<GXCondition<?>> condition);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findMultiFieldByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition(condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findMultiFieldByCondition(List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 查询指定字段的值
     *
     * @param queryParamInnerDto 查询参数
     * @param targetClazz        返回数据的类型
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findMultiFieldByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz);

    /**
     * 获取一条记录的指定单字段
     *
     * @param condition   条件
     * @param column      字段名字
     * @param targetClazz 返回的类型
     * @return 指定的类型
     */
    default <E> E findSingleFieldByCondition(List<GXCondition<?>> condition, String column, Class<E> targetClazz) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(getTableName()).condition(condition).columns(CollUtil.newHashSet(column)).build();
        return findSingleFieldByCondition(queryParamInnerDto, targetClazz);
    }

    /**
     * 获取一条记录的指定单字段
     *
     * @param queryParamInnerDto 查询条件
     * @param targetClazz        返回的类型
     * @return 指定的类型
     */
    <E> E findSingleFieldByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz);

    /**
     * 获取指定单字段的列表
     *
     * @param condition    条件
     * @param column       字段名字
     * @param targetClazz  返回的类型
     * @param groupByField 分组
     * @return 指定的类型
     */
    default <E> List<E> findSingleFieldLstByCondition(List<GXCondition<?>> condition, String column, Class<E> targetClazz, Set<String> groupByField) {
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(getTableName()).condition(condition).columns(CollUtil.newHashSet(column)).groupByField(groupByField).build();
        return findSingleFieldLstByCondition(queryParamInnerDto, targetClazz);
    }

    /**
     * 获取指定单字段的列表
     *
     * @param queryParamInnerDto 条件
     * @param targetClazz        目标类型
     * @return 指定的类型
     */
    <E> List<E> findSingleFieldLstByCondition(GXBaseQueryParamInnerDto queryParamInnerDto, Class<E> targetClazz);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Collection
     */
    Collection<R> findByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param params             参数
     * @return Object
     */
    Collection<R> findByCallMapperMethod(String mapperMethodMethod, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodMethod 需要调用的方法
     * @param convertMethodName  结果集转换函数名字
     * @param copyOptions        转换选项
     * @param params             参数
     * @return Object
     */
    R findOneByCallMapperMethod(String mapperMethodMethod, String convertMethodName, CopyOptions copyOptions, Object... params);

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName 需要调用的方法
     * @param params           参数
     * @return Object
     */
    R findOneByCallMapperMethod(String mapperMethodName, Object... params);

    /**
     * 根据条件统计数量
     *
     * @param conditions 查询条件
     * @return 查询到的数量
     */
    Long countByCondition(List<GXCondition<?>> conditions);

    /**
     * 根据条件统计数量
     *
     * @param queryParamInnerDto 查询条件
     * @return 查询到的数量
     */
    Long countByCondition(GXBaseQueryParamInnerDto queryParamInnerDto);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKeyName(T entity);

    /**
     * 获取表的名字
     *
     * @return String
     */
    String getTableName();

    /**
     * 获取MyBatis Plus数据表的信息
     *
     * @return TableInfo
     */
    TableInfo getTableInfo();

    /**
     * 从参数中获取 CopyOptions
     *
     * @param queryParamInnerDto 查询参数
     * @return CopyOptions
     */
    default CopyOptions getCopyOptions(GXBaseQueryParamInnerDto queryParamInnerDto) {
        return ObjectUtil.defaultIfNull(queryParamInnerDto.getCopyOptions(), GXCommonUtils::getDefaultCopyOptions);
    }
}
