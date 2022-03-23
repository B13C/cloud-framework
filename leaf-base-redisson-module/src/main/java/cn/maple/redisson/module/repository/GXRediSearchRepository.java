package cn.maple.redisson.module.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.util.GXCommonUtils;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.dto.req.GXRediSearchSchemaReqDto;
import io.github.dengliming.redismodule.redisearch.RediSearch;
import io.github.dengliming.redismodule.redisearch.client.RediSearchClient;
import io.github.dengliming.redismodule.redisearch.index.Document;
import io.github.dengliming.redismodule.redisearch.index.IndexDefinition;
import io.github.dengliming.redismodule.redisearch.index.IndexOptions;
import io.github.dengliming.redismodule.redisearch.index.RSLanguage;
import io.github.dengliming.redismodule.redisearch.index.schema.Field;
import io.github.dengliming.redismodule.redisearch.index.schema.Schema;
import io.github.dengliming.redismodule.redisearch.search.SearchOptions;
import io.github.dengliming.redismodule.redisearch.search.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Repository
public class GXRediSearchRepository {
    @Resource
    private RediSearchClient rediSearchClient;

    /**
     * 通过条件获取数据
     *
     * @param dataIndexesParamInnerDto 索引数据
     * @param targetClass              目标类型
     * @param customerData             额外数据
     * @return 列表
     */
    public <R> List<R> search(GXRediSearchQueryParamReqDto dataIndexesParamInnerDto, Class<R> targetClass, Object... customerData) {
        String indexName = dataIndexesParamInnerDto.getIndexName();
        RediSearch rediSearch = getRediSearch(indexName);
        SearchOptions searchOptions = Optional.ofNullable(dataIndexesParamInnerDto.getSearchOptions()).orElse(new SearchOptions());
        String query = dataIndexesParamInnerDto.getQuery();

        if (CharSequenceUtil.isBlank(query)) {
            return Collections.emptyList();
        }

        SearchResult search = rediSearch.search(query, searchOptions);
        List<Document> documents = search.getDocuments();
        List<R> rLst = new ArrayList<>();
        documents.forEach(doc -> {
            Dict data = Dict.create();
            data.put("id", doc.getId());
            doc.getFields().forEach((k, v) -> {
                if (CharSequenceUtil.equals("$", k) && JSONUtil.isTypeJSON(v.toString())) {
                    data.putAll(JSONUtil.toBean(v.toString(), Dict.class));
                }
            });
            R r = GXCommonUtils.convertSourceToTarget(data, targetClass, dataIndexesParamInnerDto.getConvertMethodName(), dataIndexesParamInnerDto.getCopyOptions(), customerData);
            rLst.add(r);
        });
        return rLst;
    }

    /**
     * 创建或者修改索引结构
     *
     * @param schemaReqDto 索引数据
     * @return 新增是否成功
     */
    @SuppressWarnings("all")
    public boolean createIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        List<Field> schemaFields = schemaReqDto.getSchemaFieldLst();
        if (Objects.isNull(schemaFields) || schemaFields.isEmpty()) {
            log.info(CharSequenceUtil.format("修改索引失败"));
            return false;
        }
        String separator = Optional.ofNullable(schemaReqDto.getSeparator()).orElse(",");
        RSLanguage language = Optional.ofNullable(schemaReqDto.getLanguage()).orElse(RSLanguage.CHINESE);
        IndexDefinition.DataType dataType = Optional.ofNullable(schemaReqDto.getDataType()).orElse(IndexDefinition.DataType.JSON);
        Schema schema = new Schema(schemaFields);
        List<String> prefixes = schemaReqDto.getPrefixes();
        IndexDefinition indexDefinition = new IndexDefinition(dataType);
        indexDefinition.setPrefixes(prefixes);
        indexDefinition.setLanguage(language);
        if (CharSequenceUtil.isNotBlank(schemaReqDto.getFilter())) {
            indexDefinition.setFilter(schemaReqDto.getFilter());
        }
        if (Objects.nonNull(schemaReqDto.getLanguageField())) {
            indexDefinition.setLanguageField(schemaReqDto.getLanguageField());
        }
        if (Objects.nonNull(schemaReqDto.getScore())) {
            indexDefinition.setScore(schemaReqDto.getScore());
        }
        if (CharSequenceUtil.isNotBlank(schemaReqDto.getScoreFiled())) {
            indexDefinition.setScoreFiled(schemaReqDto.getScoreFiled());
        }
        if (CharSequenceUtil.isNotBlank(schemaReqDto.getPayloadField())) {
            indexDefinition.setPayloadField(schemaReqDto.getPayloadField());
        }
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.definition(indexDefinition);
        if (Objects.nonNull(schemaReqDto.getStopWords()) && CollUtil.isNotEmpty(schemaReqDto.getStopWords())) {
            indexOptions.stopwords(schemaReqDto.getStopWords());
        }
        if (Objects.nonNull(schemaReqDto.getMaxTextFields()) && schemaReqDto.getMaxTextFields()) {
            indexOptions.maxTextFields();
        }
        if (Objects.nonNull(schemaReqDto.getNoFields()) && schemaReqDto.getNoFields()) {
            indexOptions.noFields();
        }
        return getRediSearch(indexName).createIndex(schema, indexOptions);
    }

    /**
     * 修改索引结构
     *
     * @param schemaReqDto 索引信息
     * @return 修改是是否成功
     */
    @SuppressWarnings("all")
    public boolean alertIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        List<Field> schemaFields = schemaReqDto.getSchemaFieldLst();
        if (Objects.isNull(schemaFields) || schemaFields.isEmpty()) {
            log.info(CharSequenceUtil.format("修改索引失败"));
            return false;
        }
        String separator = Optional.ofNullable(schemaReqDto.getSeparator()).orElse(",");
        return getRediSearch(indexName).alterIndex(schemaFields.toArray(new Field[0]));
    }

    /**
     * 删除索引
     *
     * @param schemaReqDto 索引数据
     * @return 删除索引是否成功
     */
    @SuppressWarnings("all")
    public boolean dropIndexSchema(GXRediSearchSchemaReqDto schemaReqDto) {
        String indexName = schemaReqDto.getIndexName();
        RediSearch rediSearch = getRediSearch(indexName);
        List<String> indexNameLst = rediSearch.listIndexes();
        if (CollUtil.contains(indexNameLst, indexName)) {
            return rediSearch.dropIndex(true);
        }
        log.info(CharSequenceUtil.format("索引{}不存在,没有做删除操作", indexName));
        return true;
    }

    /**
     * 获取RediSearch对象
     *
     * @param indexName 索引名字
     * @return RediSearch对象
     */
    public RediSearch getRediSearch(String indexName) {
        return rediSearchClient.getRediSearch(indexName);
    }
}
