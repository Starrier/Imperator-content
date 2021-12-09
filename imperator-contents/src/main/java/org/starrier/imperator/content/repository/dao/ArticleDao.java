package org.starrier.imperator.content.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.starrier.imperator.content.entity.article.Article;

import java.util.List;
import java.util.Optional;

/**
 * Article  CRUD.
 *
 * @author Starrier
 * @date 2018/10/27.
 */
public interface ArticleDao {

    /**
     * Insert Article with Id.
     *
     * @param article Article
     * @return
     */
    int insertArticle(Article article);

    /**
     * Delete Article By id.
     *
     * @param id Long
     */
    void deleteById(@Param("id") Long id);

    /**
     * Delete Article By Author.
     *
     * @param author String
     */
    void deleteArticleByAuthor(String author);

    /**
     * Delete   :   Delete Article By article.
     *
     * @param article Article
     */
    void deleteArticle(Article article);

    /**
     * 更新文章.
     *
     * @param article Article
     */
    void updateArticle(Article article);

    /**
     * 通过时间显示文章列表.
     *
     * @return the result
     */
    List<Article> listArticle();

    /**
     * 通过文章类别id查询文章列表.
     *
     * @param categoryId int
     * @return return the result
     */
    @Select("select id from article where id = #{id}")
    List<Article> getArticlesByCategoryId(int categoryId);

    /**
     * 通过时间查询文章列表.
     *
     * @param date String
     * @return return the result
     */
    List<Article> getArticlesByDate(String date);

    /**
     * 通过关键字搜索.
     *
     * @param keyword String
     * @return return the result
     */
    List<Article> getArticlesByKeyword(@Param("keyword") String keyword);

    /**
     * 通过文章id显示文章详情.
     *
     * @param id Long
     * @return return the result
     */
    Article getArticleById(@Param("id") Long id);

    /**
     * Fetch all the articles by {@code author}.
     *
     * @param author via user.
     * @return articles with list.
     */
    List<Article> getArticlesByAuthor(@Param("author") String author);

    /**
     * Fetch all the author.
     *
     * @return the find result with list.
     */
    List<Article> getAllAuthorName();

    /**
     * Find Article by id.
     *
     * @param articleId
     * @return
     */
    Optional<Article> findById(@Param("articleId") Long articleId);

    List<Article> findAllByKey(@Param("key") String key);

    List<Article> findAllByIds(@Param("articleIds") List<Integer> articleIds);

    void updateArticleById(@Param("id") Long id);
}
