package cn.maple.core.datasource.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.maple.core.datasource.dao.GXMyBatisDao;
import cn.maple.core.datasource.mapper.GXBaseMapper;
import cn.maple.core.datasource.model.GXMyBatisModel;
import cn.maple.core.datasource.repository.GXMyBatisRepository;
import cn.maple.core.datasource.service.GXMyBatisBaseService;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.exception.GXDBNotExistsException;
import cn.maple.core.framework.service.impl.GXBusinessServiceImpl;
import cn.maple.core.framework.util.GXCommonUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 业务基础Service
 *
 * @param <P>  仓库对象类型
 * @param <M>  Mapper类型
 * @param <T>  实体类型
 * @param <D>  DAO类型
 * @param <R>  响应对象类型
 * @param <ID> 实体的主键ID类型
 */
public class GXMyBatisBaseServiceImpl<P extends GXMyBatisRepository<M, T, D, R, ID>, M extends GXBaseMapper<T, R>, T extends GXMyBatisModel, D extends GXMyBatisDao<M, T, R, ID>, R extends GXBaseDBResDto, ID extends Serializable> extends GXBusinessServiceImpl implements GXMyBatisBaseService<P, M, T, D, R, ID> {
    /**
     * 日志对象
     */
    @SuppressWarnings("all")
    private static final Logger LOGGER = GXCommonUtils.getLogger(GXMyBatisBaseServiceImpl.class);

    /**
     * 仓库类型
     */
    @Autowired
    @SuppressWarnings("all")
    protected P repository;

    /**
     * 基础Mapper
     */
    @Autowired
    @SuppressWarnings("all")
    private M baseMapper;

    /**
     * 检测给定条件的记录是否存在
     *
     * @param tableName 数据库表名字
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(String tableName, Table<String, String, Object> condition) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        return repository.checkRecordIsExists(tableName, conditionList);
    }

    /**
     * 检测给定条件的记录是否存在
     *
     * @param condition 条件
     * @return int
     */
    @Override
    public boolean checkRecordIsExists(Table<String, String, Object> condition) {
        return checkRecordIsExists(repository.getTableName(), condition);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param tableName 表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(String tableName, Dict data, Table<String, String, Object> condition) {
        List<GXUpdateField<?>> updateFields = convertDictToUpdateField(tableName, data);
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        return repository.updateFieldByCondition(tableName, updateFields, conditionList);
    }

    /**
     * 通过SQL更新表中的数据
     *
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return Integer
     */
    @Override
    public Integer updateFieldByCondition(Dict data, Table<String, String, Object> condition) {
        return updateFieldByCondition(repository.getTableName(), data, condition);
    }

    /**
     * 列表或者搜索(分页)
     *
     * @param queryParamReqDto 参数
     * @return GXPagination
     */
    @Override
    public GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto queryParamReqDto) {
        if (CharSequenceUtil.isEmpty(queryParamReqDto.getTableName())) {
            queryParamReqDto.setTableName(repository.getTableName());
        }
        if (Objects.isNull(queryParamReqDto.getColumns())) {
            queryParamReqDto.setColumns(CollUtil.newHashSet("*"));
        }
        return repository.paginate(queryParamReqDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param queryParamInnerDto 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(GXBaseQueryParamInnerDto queryParamInnerDto) {
        String tableName = queryParamInnerDto.getTableName();
        if (CharSequenceUtil.isBlank(tableName)) {
            tableName = repository.getTableName();
            queryParamInnerDto.setTableName(tableName);
        }
        return repository.findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition) {
        return findByCondition(tableName, condition, columns, null, null);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition) {
        return findByCondition(tableName, CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition) {
        return findByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param extraData 额外数据
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Object extraData) {
        String tableName = repository.getTableName();
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        HashSet<String> columns = CollUtil.newHashSet("*");
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(conditionList).extraData(extraData).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition 搜索条件
     * @param columns   需要查询的列
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findByCondition(repository.getTableName(), columns, condition);
    }

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
    @Override
    public List<R> findByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Map<String, String> orderField, Set<String> groupField) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(conditionList).orderByField(orderField).groupByField(groupField).build();
        return findByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @param groupField 分组字段
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField, Set<String> groupField) {
        return findByCondition(repository.getTableName(), condition, CollUtil.newHashSet("*"), orderField, groupField);
    }

    /**
     * 通过条件查询列表信息
     *
     * @param condition  搜索条件
     * @param orderField 排序字段
     * @return List
     */
    @Override
    public List<R> findByCondition(Table<String, String, Object> condition, Map<String, String> orderField) {
        return findByCondition(condition, orderField, null);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param searchReqDto 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(GXBaseQueryParamInnerDto searchReqDto) {
        return repository.findOneByCondition(searchReqDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition, Object extraData) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        GXBaseQueryParamInnerDto queryParamInnerDto = GXBaseQueryParamInnerDto.builder().tableName(tableName).columns(columns).condition(conditionList).extraData(extraData).build();
        return findOneByCondition(queryParamInnerDto);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param columns   需要查询的字段
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Set<String> columns, Table<String, String, Object> condition) {
        return findOneByCondition(tableName, columns, condition, Dict.create());
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param extraData 额外参数
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition, Object extraData) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition, extraData);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param tableName 表名字
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(String tableName, Table<String, String, Object> condition) {
        return findOneByCondition(tableName, CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition) {
        return findOneByCondition(repository.getTableName(), CollUtil.newHashSet("*"), condition);
    }

    /**
     * 通过条件获取一条数据
     *
     * @param condition 搜索条件
     * @param columns   字段集合
     * @return 一条数据
     */
    @Override
    public R findOneByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findOneByCondition(repository.getTableName(), columns, condition);
    }

    /**
     * 获取一条记录的指定字段
     *
     * @param condition   条件
     * @param fieldName   字段名字
     * @param targetClazz 返回的类型
     * @return 指定的类型
     */
    @Override
    public <E> E findOneSingleFieldByCondition(List<GXCondition<?>> condition, String fieldName, Class<E> targetClazz) {
        return repository.findOneSingleFieldByCondition(getTableName(), condition, fieldName, targetClazz);
    }

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID updateOrCreate(T entity) {
        return updateOrCreate(entity, HashBasedTable.create());
    }

    /**
     * 创建或者更新
     *
     * @param entity    数据实体
     * @param condition 更新条件
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID updateOrCreate(T entity, Table<String, String, Object> condition) {
        List<GXCondition<?>> conditionList = convertTableToCondition(getTableName(), condition);
        return repository.updateOrCreate(entity, conditionList);
    }

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param condition   条件
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req, Table<String, String, Object> condition, CopyOptions copyOptions) {
        Class<T> targetClazz = GXCommonUtils.getGenericClassType(getClass(), 2);
        T entity = convertSourceToTarget(req, targetClazz, "customizeProcess", copyOptions);
        return updateOrCreate(entity, condition);
    }

    /**
     * 创建或者更新
     *
     * @param req         请求参数
     * @param copyOptions 复制可选项
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req, CopyOptions copyOptions) {
        return updateOrCreate(req, HashBasedTable.create(), copyOptions);
    }

    /**
     * 创建或者更新
     *
     * @param req 请求参数
     * @return ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <Q extends GXBaseReqDto> ID updateOrCreate(Q req) {
        return updateOrCreate(req, CopyOptions.create());
    }

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @param extraData     额外数据
     * @return 新数据ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID copyOneData(Table<String, String, Object> copyCondition, Dict replaceData, Dict extraData) {
        R oneData = findOneByCondition(repository.getTableName(), copyCondition);
        if (Objects.isNull(oneData)) {
            throw new GXDBNotExistsException("待拷贝的数据不存在!!");
        }
        T entity = GXCommonUtils.convertSourceToTarget(oneData, GXCommonUtils.getGenericClassType(getClass(), 2), null, null, extraData);
        assert entity != null;
        String setPrimaryKeyMethodName = CharSequenceUtil.format("set{}", CharSequenceUtil.upperFirst(getPrimaryKeyName(entity)));
        Method method = ReflectUtil.getMethod(entity.getClass(), setPrimaryKeyMethodName, GXCommonUtils.getGenericClassType(getClass(), 5));
        if (Objects.isNull(method)) {
            throw new GXBusinessException(CharSequenceUtil.format("方法{}不存在", setPrimaryKeyMethodName));
        }
        ReflectUtil.invoke(entity, method, (Object) null);
        replaceData.forEach((k, v) -> GXCommonUtils.reflectCallObjectMethod(entity, CharSequenceUtil.format("set{}", CharSequenceUtil.upperFirst(CharSequenceUtil.toCamelCase(k))), v));

        return updateOrCreate(entity);
    }

    /**
     * 复制一条数据
     *
     * @param copyCondition 复制的条件
     * @param replaceData   需要替换的数据
     * @return 新数据ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ID copyOneData(Table<String, String, Object> copyCondition, Dict replaceData) {
        return copyOneData(copyCondition, replaceData, Dict.create());
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(String tableName, Table<String, String, Object> condition) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        return repository.deleteSoftCondition(tableName, conditionList);
    }

    /**
     * 根据条件软(逻辑)删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteSoftCondition(Table<String, String, Object> condition) {
        return deleteSoftCondition(repository.getTableName(), condition);
    }

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(String tableName, Table<String, String, Object> condition) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        return repository.deleteCondition(tableName, conditionList);
    }

    /**
     * 根据条件删除
     *
     * @param condition 删除条件
     * @return 影响行数
     */
    @Override
    public Integer deleteCondition(Table<String, String, Object> condition) {
        return deleteCondition(repository.getTableName(), condition);
    }

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
    @Override
    public <E> List<E> findFieldByCondition(String tableName, Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        List<GXCondition<?>> conditionList = convertTableToCondition(tableName, condition);
        return repository.findFieldByCondition(tableName, conditionList, columns, targetClazz);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    @Override
    public <E> List<E> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns, Class<E> targetClazz) {
        return findFieldByCondition(repository.getTableName(), condition, columns, targetClazz);
    }

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code findFieldByCondition("s_admin", condition1, CollUtil.newHashSet("nickname", "username"), Dict.class);}
     * </pre>
     *
     * @param condition 查询条件
     * @param columns   字段名字集合
     * @return 返回指定的类型的值对象
     */
    @Override
    public List<R> findFieldByCondition(Table<String, String, Object> condition, Set<String> columns) {
        return findFieldByCondition(repository.getTableName(), condition, columns, GXCommonUtils.getGenericClassType(getClass(), 3));
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName  需要调用的方法
     * @param convertMethodName 结果集转换函数名字
     * @param copyOptions       转换选项
     * @param params            参数
     * @return Collection
     */
    @Override
    public Collection<R> findByCallMapperMethod(String mapperMethodName, String convertMethodName, CopyOptions copyOptions, Object... params) {
        Object o = callMethod(baseMapper, mapperMethodName, params);
        if (Objects.isNull(o)) {
            return Collections.emptyList();
        }
        return GXCommonUtils.convertSourceListToTargetList((Collection<?>) o, GXCommonUtils.getGenericClassType(getClass(), 4), convertMethodName, copyOptions, Dict.create());
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName 需要调用的方法
     * @param params           参数
     * @return Object
     */
    @Override
    public Collection<R> findByCallMapperMethod(String mapperMethodName, Object... params) {
        return findByCallMapperMethod(mapperMethodName, null, null, params);
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName  需要调用的方法
     * @param convertMethodName 结果集转换函数名字
     * @param copyOptions       转换选项
     * @param params            参数
     * @return Object
     */
    @Override
    public R findOneByCallMapperMethod(String mapperMethodName, String convertMethodName, CopyOptions copyOptions, Object... params) {
        Object o = callMethod(baseMapper, mapperMethodName, params);
        if (Objects.isNull(o)) {
            return null;
        }
        return GXCommonUtils.convertSourceToTarget(o, GXCommonUtils.getGenericClassType(getClass(), 4), convertMethodName, copyOptions, Dict.create());
    }

    /**
     * 动态调用指定的指定Class中的方法
     *
     * @param mapperMethodName 需要调用的方法
     * @param params           参数
     * @return Object
     */
    @Override
    public R findOneByCallMapperMethod(String mapperMethodName, Object... params) {
        return findOneByCallMapperMethod(mapperMethodName, null, null, params);
    }

    /**
     * 实现验证注解(返回true表示数据已经存在)
     *
     * @param value                      The value to check for
     * @param tableName                  database table name
     * @param fieldName                  The name of the field for which to check if the value exists
     * @param constraintValidatorContext The ValidatorContext
     * @param param                      param
     * @return boolean
     */
    @Override
    public boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        if (CharSequenceUtil.isBlank(tableName)) {
            tableName = repository.getTableName();
        }
        if (CharSequenceUtil.isBlank(fieldName)) {
            fieldName = repository.getPrimaryKeyName();
        }
        return repository.validateExists(value, tableName, fieldName, constraintValidatorContext, param);
    }

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    @Override
    public String getPrimaryKeyName(T entity) {
        return repository.getPrimaryKeyName(entity);
    }

    /**
     * 获取表的名字
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return repository.getTableName();
    }
}
