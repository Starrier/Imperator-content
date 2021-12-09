package org.starrier.imperator.content.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.starrier.imperator.content.entity.article.Article;
import org.starrier.imperator.content.repository.dao.ArticleDao;

import java.util.List;
import java.util.Optional;

/**
 * @author Starrier
 * @date 2019/1/9.
 */
@Mapper
@Repository
public interface ArticleMapper extends ArticleDao {
    /**
     * Insert Article with Id.
     *
     * @param article Article
     * @return
     */
    @Override
    int insertArticle(@Param("article") Article article);

    /**
     * Delete Article By id.
     *
     * @param id Long
     */
    @Override
    void deleteById(@Param("id") Long id);

    /**
     * Delete Article By Author.
     *
     * @param author String
     */
    @Override
    void deleteArticleByAuthor(@Param("author") String author);

    /**
     * Delete   :   Delete Article By article.
     *
     * @param article Article
     */
    @Override
    void deleteArticle(@Param("article") Article article);

    /**
     * Update article.
     *
     * @param article Article
     */
    @Override
    void updateArticle(@Param("article") Article article);

    /**
     * 通过时间显示文章列表.
     *
     * @return the result
     */
    @Override
    List<Article> listArticle();

    /**
     * 通过文章类别id查询文章列表.
     *
     * @param categoryId int
     * @return return the result
     */
    @Override
    List<Article> getArticlesByCategoryId(@Param("categoryId") int categoryId);

    /**
     * 通过时间查询文章列表.
     *
     * @param date String
     * @return return the result
     */
    @Override
    List<Article> getArticlesByDate(String date);

    /**
     * 通过关键字搜索.
     *
     * @param keyword String
     * @return return the result
     */
    @Override
    List<Article> getArticlesByKeyword(@Param("keyword") String keyword);

    /**
     * 通过文章id显示文章详情.
     *
     * @param id Long
     * @return return the result
     */
    @Override
    Article getArticleById(Long id);

    /**
     * @param author via user
     * @return articles
     */
    @Override
    List<Article> getArticlesByAuthor(String author);

    /**
     * Get Article author
     */
    @Override
    List<Article> getAllAuthorName();

    @Override
    Optional<Article> findById(Long articleId);

    @Override
    List<Article> findAllByKey(String key);
}
