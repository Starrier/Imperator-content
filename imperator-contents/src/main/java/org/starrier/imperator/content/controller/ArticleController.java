package org.starrier.imperator.content.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.starrier.common.result.Result;
import org.starrier.common.result.ResultCode;
import org.starrier.imperator.content.annotation.RequestLimit;
import org.starrier.imperator.content.entity.article.Article;
import org.starrier.imperator.content.service.ArticleService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Article Controller.
 *
 * @author Starrier
 * @date 2018/10/27.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    private final RedisTemplate redisTemplate;

    public ArticleController(ArticleService articleService, RedisTemplate redisTemplate) {
        this.articleService = articleService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * <p>Add article </p>
     * <p>Insert Article with id</p>
     * <p>
     * Insert new Article by id
     * <p>
     * 1.发送消息到rabbitmq服务端
     * 2.写入数据库
     * 3.异步消息，邮件通知
     *
     * @param article Article, this is the real
     * @return return the result whether the method process success or fail or not.
     */
    @PostMapping
    @ResponseBody
    public Result insertArticleById(@RequestBody Article article) {
        try {
            articleService.insertArticle(article);
            return Result.builder().code(200).message("Article have been inserted!").build();
        } catch (Exception e) {
            log.error("Article have got error:[{}]", e.getMessage());
            return Result.builder().code(404).message("Article inserted has been failed! !").build();
        }
    }

    /**
     * <p> Delete  /p>
     * <p> Delete Article By ArticleId<</p>
     * 1. Determine whether the user is the current user or is the administrator
     * 2. User who meet the current appeal conditions can only,otherwise an exception is returned
     * 3. Check the current article''s id exists,if so,delete,otherwise an exception is retured
     * 4. Return the result with {@link ResponseEntity}
     *
     * @param articleId delete by id
     * @return status code. return 200 while operation have been done successful
     * ,and return error code depends on  exception.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public Result deleteArticleById(final @PathVariable("id") Long articleId) {
        Optional<Article> articleOptional = articleService.findById(articleId);
        if (articleOptional.isPresent()) {
            log.info("Thr article would have been deleted has been checked out:[{}]", articleOptional);
            articleService.deleteById(articleId);
            return Result.builder().code(200).message("DELETE SUCCESS").build();
        } else {
            return Result.builder().code(404).message("Article is not exist").build();
        }
    }

    /**
     * <p>Delete</p>
     * <p>Delete Article By Author</p>
     * 1. Determine
     *
     * @param author which is article's own
     * @return status code. return 200 while operation have been done successful
     * ,and return error code depends on  exception.
     */
    @CacheEvict(value = "authorArticles")
    @DeleteMapping("/author/{author}")
    public Result deleteArticleByAuthor(final @PathVariable("author") String author) {
        articleService.deleteArticleByAuthor(author);
        return Result.success(HttpStatus.OK);
    }

    /**
     * <p>update article for detail</p>
     * 1. Determine whether the current article that needs to
     * be modified exist with article's di unique.{@link Optional}
     * 2. If the article exists,modify it and surround it with
     * the try-catch statement
     * 3. return the result whether successful or not
     *
     * @param article Deliver content that user needs to modify
     * @return {@link ResponseEntity}
     */
    @CachePut(value = "articleId")
    @ResponseBody
    @PutMapping
    @SneakyThrows(Exception.class)
    public Result updateArticle(final @RequestBody Article article) {
        Optional<Article> articleOptional = articleService.findById(article.getId());
        if (!articleOptional.isPresent()) {
            log.info("Update  Article is  not exit:{}", article);
            return Result.error(ResultCode.RESULE_DATA_NONE, "Update Article is not exist");
        }
        article.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        articleService.updateArticle(article);
        return Result.success(200);
    }

    @PutMapping("/article/id")
    public Result updateArticleById(@RequestBody Article article) {
        articleService.updateArticleById(article.getId());
        return Result.builder().code(ResultCode.SUCCESS.code()).build();
    }

    /**
     * <p>Fetch Article via article's id</p>
     *
     * @param id which is article's identify
     * @return status code. return 200 while operation have been done successful
     * ,and return error code depends on  exception.
     */
    @Cacheable(value = "articleId")
    @SneakyThrows(Exception.class)
    @GetMapping("/{id}")
    public Result getArticleById(final @PathVariable(value = "id") Long id) {

        Article articleOptional = articleService.getArticleById(id);
        log.info("Optional find the information:[{}]", articleOptional);
        return articleOptional != null ? Result.success(articleOptional) : Result.error(ResultCode.RESULE_DATA_NONE, "error");
    }

    /**
     * <p>Fetch</p>Find   :   Get All  Articles
     * <p>Get All Articles</p>
     * 1. List
     *
     * @return return the result {@link List}
     */
    @ResponseBody
    @GetMapping
    @Cacheable(value = "articles")
    public Result getAllArticles(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "limit", defaultValue = "15") int limit) {
        return Result.builder().code(200).message(ResultCode.SUCCESS.message()).data(articleService.listArticle(page, limit)).build();
    }

    /**
     * Fetch  Comment By Id With Feign.
     *
     * @param id Long
     * @return return the result {@link Result}
     */
    @ResponseBody
    @GetMapping(value = "{id}/comment")
    public Result getCommentById(final @PathVariable("id") Long id) {
        log.info("This  is  ArticleController  Find Comment with id:{}", id);
        return Result.success();
    }

    /**
     * <p>Get Articles By Author</p>
     * 1.
     *
     * @param author String
     * @return return the result
     * @see Exception {@link Exception} to catch the exception information
     */
    @ResponseBody
    @GetMapping(value = "/author/{author}",
            consumes = "application/json",
            produces = "application/json")
    @Cacheable("authorArticles")
    @SneakyThrows(Exception.class)
    public List<Article> getArticleByAuthor(final @PathVariable("author") String author) {
        List<Article> listByAuthor = articleService.getArticlesByAuthor(author);
        log.info("listByAuthor:{}", listByAuthor);
        return listByAuthor;
    }

    /**
     * <p>Find all the articles which include the word given</p>
     * 1.
     *
     * @param keyword String
     * @return {@link Result}
     */
    @GetMapping(value = "/search/{keyword}", consumes = "application/json", produces = "application/json")
    public Result search(final @PathVariable(value = "keyword") String keyword) {
        return Result.success(articleService.getArticlesByKeyword(keyword));
    }

    /**
     * <p>Get All the article for the specified category with categoryId given</p>
     * 1.
     *
     * @param categoryId article's category id
     * @return ResponseEntity<List < Article>>
     */
    @GetMapping("/category")
    public ResponseEntity<?> getArticleCategory(final @RequestParam(value = "categoryId") int categoryId) {
        Optional<List<Article>> articles = articleService.getArticlesByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    @RequestLimit(maxCount = 1, second = 10)
    @GetMapping("/test")
    public String test() {
        return articleService.test();
    }

}