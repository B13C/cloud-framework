package cn.maple.core.framework.ddd.repository;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.dto.inner.GXBaseQueryParamInnerDto;
import cn.maple.core.framework.dto.inner.condition.GXCondition;
import cn.maple.core.framework.dto.inner.field.GXUpdateField;
import cn.maple.core.framework.dto.res.GXBaseDBResDto;
import cn.maple.core.framework.dto.res.GXPaginationResDto;

import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface GXBaseRepository<T, R extends GXBaseDBResDto, ID extends Serializable> {
    /**
     * 保存或者更新数据
     *
     * @param entity    需要更新或者保存的数据
     * @param condition 附加条件,用于一些特殊场景
     * @return ID
     */
    ID updateOrCreate(T entity, List<GXCondition<?>> condition);

    /**
     * 创建或者更新
     *
     * @param entity 数据实体
     * @return ID
     */
    ID updateOrCreate(T entity);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param targetClazz     目标对象类型
     * @param extraData       额外数据
     * @return 列表
     */
    <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryInnerDto 查询对象
     * @param targetClazz     目标对象类型
     * @return 列表
     */
    <E> List<E> findByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     查询列
     * @param targetClazz 结果数据类型
     * @return 列表
     */
    <E> List<E> findByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 根据条件获取所有数据
     *
     * @param dbQueryParamInnerDto 查询条件
     * @return 列表
     */
    List<R> findByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取所有数据
     *
     * @param tableName 表名字
     * @param condition 条件
     * @return 列表
     */
    List<R> findByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件获取所有数据
     *
     * @param condition 条件
     * @return 列表
     */
    default List<R> findByCondition(List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        return findByCondition(getTableName(), condition);
    }

    /**
     * 获取所有数据
     *
     * @return 列表
     */
    default List<R> findByCondition() {
        return findByCondition(getTableName(), Collections.emptyList());
    }

    /**
     * 根据条件获取数据
     *
     * @param dbQueryParamInnerDto 查询参数
     * @return R 返回数据
     */
    R findOneByCondition(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return R 返回数据
     */
    R findOneByCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件获取数据
     *
     * @param condition 查询条件
     * @return R 返回数据
     */
    default R findOneByCondition(List<GXCondition<?>> condition) {
        Assert.notNull(condition, "条件不能为null");
        return findOneByCondition(getTableName(), condition);
    }

    /**
     * 根据条件获取数据
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @param columns   需要查询的列
     * @return R 返回数据
     */
    R findOneByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @param columns   需要返回的列
     * @return 返回数据
     */
    R findOneById(String tableName, ID id, Set<String> columns);

    /**
     * 通过ID获取一条记录
     *
     * @param tableName 表名字
     * @param id        ID值
     * @return 返回数据
     */
    R findOneById(String tableName, ID id);

    /**
     * 根据条件获取分页数据
     *
     * @param dbQueryParamInnerDto 条件查询
     * @return 分页数据
     */
    GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto dbQueryParamInnerDto);

    /**
     * 根据条件获取分页数据
     *
     * @param tableName 表名字
     * @param page      当前页
     * @param pageSize  每页大小
     * @param condition 查询条件
     * @param columns   需要的数据列
     * @return 分页对象
     */
    GXPaginationResDto<R> paginate(String tableName, Integer page, Integer pageSize, List<GXCondition<?>> condition, Set<String> columns);

    /**
     * 根据条件软(逻辑)删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteSoftCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 根据条件删除
     *
     * @param tableName 表名
     * @param condition 删除条件
     * @return 影响行数
     */
    Integer deleteCondition(String tableName, List<GXCondition<?>> condition);

    /**
     * 检测数据是否存在
     *
     * @param tableName 表名字
     * @param condition 查询条件
     * @return 1 存在 0 不存在
     */
    boolean checkRecordIsExists(String tableName, List<GXCondition<?>> condition);

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
    boolean validateExists(Object value, String tableName, String fieldName, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException;

    /**
     * 通过条件更新数据
     *
     * @param tableName 需要更新的表名
     * @param data      需要更新的数据
     * @param condition 更新条件
     * @return 影响的行数
     */
    Integer updateFieldByCondition(String tableName, List<GXUpdateField<?>> data, List<GXCondition<?>> condition);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code
     *     findFieldByCondition("s_admin", condition, CollUtil.newHashSet("nickname", "username"), Dict.class);
     *     }
     * </pre>
     *
     * @param tableName   表名字
     * @param condition   查询条件
     * @param columns     字段名字集合
     * @param targetClazz 值的类型
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findFieldByCondition(String tableName, List<GXCondition<?>> condition, Set<String> columns, Class<E> targetClazz);

    /**
     * 查询指定字段的值
     * <pre>
     *     {@code
     *     GXBaseQueryParamInnerDto paramInnerDto = GXBaseQueryParamInnerDto.builder()
     *                       .tableName("s_admin")
     *                       .columns(CollUtil.newHashSet("nickname", "username"))
     *                       .condition(condition)
     *                       .build();
     *     findFieldByCondition(paramInnerDto, Dict.class);
     *     }
     * </pre>
     *
     * @param dbQueryInnerDto 查询数据
     * @param targetClazz     值的类型
     * @param extraData       额外参数
     * @return 返回指定的类型的值对象
     */
    <E> List<E> findFieldByCondition(GXBaseQueryParamInnerDto dbQueryInnerDto, Class<E> targetClazz, Dict extraData);

    /**
     * 通过条件获取单字段的数据
     * <pre>
     * {@code
     * HashBasedTable<String, String, Object> condition = HashBasedTable.create();
     * String username = getSingleField("s_admin" ,condition , "username" , String.class);
     * }
     * </pre>
     *
     * @param tableName   表名
     * @param condition   查询条件
     * @param fieldName   需要的字段名字
     * @param targetClazz 返回的字段类型
     * @return 目标类型的值
     */
    <E> E findOneSingleFieldByCondition(String tableName, List<GXCondition<?>> condition, String fieldName, Class<E> targetClazz);

    /**
     * 查询一条数据
     *
     * @param baseQueryParamInnerDto 查询条件
     * @param rowMapper              转换函数
     * @return R
     */
    default R findOneByCondition(GXBaseQueryParamInnerDto baseQueryParamInnerDto, UnaryOperator<R> rowMapper) {
        return rowMapper.apply(findOneByCondition(baseQueryParamInnerDto));
    }

    /**
     * 查询多条数据
     *
     * @param baseQueryParamInnerDto 查询条件
     * @param rowMapper              转换函数
     * @return List
     */
    default List<R> findByCondition(GXBaseQueryParamInnerDto baseQueryParamInnerDto, UnaryOperator<R> rowMapper) {
        return findByCondition(baseQueryParamInnerDto).stream().map(rowMapper).collect(Collectors.toList());
    }

    /**
     * 查询分页数据
     *
     * @param baseQueryParamInnerDto 查询条件
     * @param rowMapper              转换函数
     * @return 分页对象
     */
    default GXPaginationResDto<R> paginate(GXBaseQueryParamInnerDto baseQueryParamInnerDto, UnaryOperator<R> rowMapper) {
        GXPaginationResDto<R> paginate = paginate(baseQueryParamInnerDto);
        List<R> collect = paginate.getRecords().stream().map(rowMapper).collect(Collectors.toList());
        paginate.setRecords(collect);
        return paginate;
    }

    /**
     * 获取 Primary Key
     *
     * @param entity 实体对象
     * @return String
     */
    String getPrimaryKeyName(T entity);

    /**
     * 获取 Primary Key
     *
     * @return String
     */
    String getPrimaryKeyName();

    /**
     * 获取实体的表名字
     *
     * @param entity 实体对象
     * @return 实体表名字
     */
    String getTableName(T entity);

    /**
     * 通过泛型标识获取实体的表名字
     *
     * @return 数据库表名字
     */
    String getTableName();
}
