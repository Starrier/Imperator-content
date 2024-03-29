package org.starrier.imperator.content.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.starrier.common.result.Result;
import org.starrier.imperator.content.entity.article.Article;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * es控制器
 * index - 数据库
 * type - table
 * field/row - 字段
 * 2020/9/2 17:58.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/es")
public class ElasticSearchController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @param request
     * @param index
     * @return
     */
    @PostMapping("/index")
    public Result createIndex(HttpServletRequest request, @RequestParam("index") String index) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        Result result = isExistsIndex(index);
        if (Objects.equals(Boolean.TRUE, result.getData())) {
            return Result.error("索引已存在...");
        }
        // 创建index
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            log.debug("创建index索引成功: {}", createIndexResponse.index());
            return Result.success(createIndexResponse.index());
        } catch (IOException e) {
            log.debug("创建index索引异常...");
        }
        return Result.error("创建index索引失败...");
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    @GetMapping("/exists/index")
    public Result isExistsIndex(String index) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            log.debug("获取是否存在索引成功: {}", exists);
            return Result.success(exists);
        } catch (IOException e) {
            log.debug("创建index索引异常...");
        }
        log.debug("获取是否存在索引失败...");
        return Result.error("获取是否存在索引失败...");
    }

    /**
     * 删除索引
     *
     * @param request
     * @param index
     * @return
     */
    @PostMapping("/delete/index")
    public Result deleteIndex(HttpServletRequest request, String index) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(index);
        try {
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteRequest, RequestOptions.DEFAULT);
            log.debug("删除索引成功: {}", delete);
            return Result.success(delete);
        } catch (IOException e) {
            log.debug("删除索引异常...");
        }
        log.debug("删除索引失败");
        return Result.error("删除索引失败");
    }

    /**
     * 索引中添加类型(type),使用json格式存入,另外存入方式可参考: mapAdd() 或者 xContentBuilderAdd()
     *
     * @param request
     * @param index
     * @return
     */
    @PostMapping("/add/type")
    public Result addType(HttpServletRequest request, @RequestParam String index, @RequestParam String type, @RequestBody Article article) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        if (Objects.isNull(article)) {
            return Result.error("数据有误...");
        }
        Long id = article.getId();
        if (Objects.isNull(id)) {
            id = 1L;
        }
        IndexRequest indexRequest = new IndexRequest(index, type);
        indexRequest.id(String.valueOf(id));
        indexRequest.source(JSON.toJSONString(article), XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.debug("往索引中添加类型成功: {}", indexResponse);
            return Result.success(indexResponse);
        } catch (IOException e) {
            log.error("error:[{}]", e.getMessage(), e);
        }
        log.debug("往索引中添加类型失败");
        return Result.error("往索引中添加类型失败");
    }

    /**
     * map方式插入
     */
    public void mapAdd() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("Articles").id("1").source(jsonMap);
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {

        }

    }

    /**
     * 利用XContentBuilder 方式往索引中插入类型
     *
     * @throws IOException
     */
    public void xContentBuilderAdd() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        IndexRequest indexRequest = new IndexRequest("Articles")
                .id("3").source(builder);
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {

        }
    }

    /**
     * 更新索引中的类型
     *
     * @param request
     * @param index
     * @param article
     * @return
     */
    @PostMapping("/update/type")
    public Result updateType(HttpServletRequest request, @RequestParam String index, @RequestParam String type, @RequestBody Article article) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        if (Objects.isNull(article)) {
            return Result.error("数据有误...");
        }
        Long id = article.getId();
        if (Objects.isNull(id)) {
            return Result.error("更新类型id不存在...");
        }
        UpdateRequest updateRequest = new UpdateRequest(index, String.valueOf(id)).doc(JSON.toJSONString(article), XContentType.JSON);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            log.debug("往索引中更新类型成功: {}", updateResponse);
            return Result.success(updateResponse);
        } catch (IOException e) {

        }
        log.debug("往索引中更新类型失败");
        return Result.error("往索引中更新类型失败");
    }

    /**
     * 删除索引中的某个类型
     *
     * @param request
     * @param index
     * @param id
     * @return
     */
    @PostMapping("/delete/type")
    public Result deleteType(HttpServletRequest request, String index, @RequestParam String type, String id) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        if (StringUtils.isEmpty(id)) {
            return Result.error("类型不存在...");
        }
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        try {
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.debug("删除索引中的类型成功: {}", delete);
            return Result.success(delete);
        } catch (IOException e) {

            log.debug("删除索引中的类型异常...");
        }
        log.debug("删除索引中的类型失败");
        return Result.error("删除索引中的类型失败");
    }

    /**
     * 根据索引和类型id查询数据
     *
     * @param request
     * @param index
     * @param id
     * @return
     */
    @GetMapping("/get/type")
    public Result getType(HttpServletRequest request, String index, String type, String id) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        if (StringUtils.isEmpty(id)) {
            return Result.error("类型不存在...");
        }
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (response.isExists()) {
                Map<String, Object> source = response.getSourceAsMap();
                Article data = JSON.parseObject(JSON.toJSONString(source), Article.class);
                log.debug("根据id查询成功: {}", data);
                return Result.success(data);
            }
            return Result.success("数据不存在");
        } catch (IOException e) {

            log.debug("根据id查询异常...");
        }
        log.debug("根据id查询失败");
        return Result.error("根据id查询失败");
    }

    /**
     * 条件/分页查询
     * QueryBuilders.termQuery(“key”, “vaule”); // 完全匹配
     * QueryBuilders.termsQuery(“key”, “vaule1”, “vaule2”) ; //一次匹配多个值
     * QueryBuilders.matchQuery(“key”, “vaule”) //单个匹配, field不支持通配符, 前缀具高级特性
     * QueryBuilders.multiMatchQuery(“text”, “field1”, “field2”); //匹配多个字段, field有通配符忒行
     * QueryBuilders.matchAllQuery(); // 匹配所有文件
     * 组合查询
     * // Bool Query 用于组合多个叶子或复合查询子句的默认查询
     * // must 相当于 与 & =
     * // must not 相当于 非 ~ ！=
     * // should 相当于 或 | or
     * // filter 过滤
     * QueryBuilders.boolQuery()
     * .must(QueryBuilders.termQuery(“key”, “value1”))
     * .must(QueryBuilders.termQuery(“key”, “value2”))
     * .mustNot(QueryBuilders.termQuery(“key”, “value3”))
     * .should(QueryBuilders.termQuery(“key”, “value4”))
     * .filter(QueryBuilders.termQuery(“key”, “value5”));
     *
     * @param request
     * @param Article
     * @param index
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/get/data")
    public Result conditionSearch(HttpServletRequest request, @RequestBody Article Article, @RequestParam String index, @RequestParam String type, int page, int size) {
        if (StringUtils.isEmpty(index)) {
            return Result.error("索引不存在...");
        }
        if (Objects.isNull(Article)) {
            return Result.error("类型不存在...");
        }
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 默认分词查询
        //QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "你好企业")
        //.fuzziness(Fuzziness.AUTO) //模糊查询
        //.prefixLength(3) // 在匹配查询上设置前缀长度选项,指明区分词项的共同前缀长度，默认是0
        //.maxExpansions(10); //设置最大扩展选项以控制查询的模糊过程
        //sourceBuilder.query(matchQueryBuilder);

        // 精确匹配
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("user", Article.getUser());
        // 模糊匹配 %s的意思是,字符串类型替换,用后面的Article.getUser()替换%s
        WildcardQueryBuilder termQueryBuilder = QueryBuilders.wildcardQuery("user.keyword", String.format("*%s*", Article.getAuthor()));
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.should(termQueryBuilder);
        sourceBuilder.query(boolBuilder);

        // 查询开始-结束 。可以用来分页使用
        sourceBuilder.from(page);
        sourceBuilder.size(size);

        // 设置一个可选的超时，控制允许搜索的时间。
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 排序
        sourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));

        // builder存入request中
        searchRequest.types(type);
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 处理搜索结果
            RestStatus restStatus = searchResponse.status();
            if (restStatus != RestStatus.OK) {
                log.debug("查询有误...请重试");
                return Result.error("查询有误...请重试");
            }
            List<Article> list = new ArrayList<>();
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit hit : hits) {
                list.add(JSON.parseObject(hit.getSourceAsString(), Article.class));
            }
            log.debug("查询成功: {}", list);
            // 返回
            return Result.success(list);
        } catch (IOException e) {

            log.debug("查询异常");
        }
        log.debug("查询失败");
        return Result.error("查询失败");
    }

}
