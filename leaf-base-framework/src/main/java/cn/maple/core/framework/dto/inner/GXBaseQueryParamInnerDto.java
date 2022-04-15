package cn.maple.core.framework.dto.inner;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.maple.core.framework.dto.GXBaseDto;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
@Data
@Builder
public class GXBaseQueryParamInnerDto extends GXBaseDto {
    /**
     * 需要查询的主表名字(在有join查询时,需要有主表、次表的区分)
     */
    private String tableName;

    /**
     * 需要查询的主表名字的别名(在有join查询时,需要有主表、次表的区分)
     */
    private String tableNameAlias;

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页数据条数
     */
    private Integer pageSize;

    /**
     * 搜索条件
     */
    private Table<String, String, Object> condition;

    /**
     * 需要查询的数据列
     */
    private Set<String> columns;

    /**
     * 排序字段
     * eg:
     * <code>
     * Map&lt;String ,String&gt; orderByField = new HashMap<>();
     * orderByField.put("created_at" , "desc");
     * orderByField.put("username" , "asc");
     * </code>
     */
    private Map<String, String> orderByField;

    /**
     * 分组字段
     */
    private Set<String> groupByField;

    /**
     * GXBaseData及其子类中的方法名字
     * 通常用于在需要额外处理一些逻辑时自动调用
     */
    private String methodName;

    /**
     * SQL中的having条件
     * <pre>
     * {@code
     * Set<String> having = CollUtil.newHashSet("SUM(area)>1000000" , "SUM(price) >= 1000");
     * }
     * </pre>
     */
    private Set<String> having;

    /**
     * 对象复制时的一些额外配置
     */
    private CopyOptions copyOptions;

    /**
     * 限制条数
     */
    private Integer limit;

    /**
     * JOIN链接信息
     */
    private List<GXJoinDto> joins;

    /**
     * 额外参数
     * <p>
     * 配合methodName一起使用
     */
    private Object extraData;
}
