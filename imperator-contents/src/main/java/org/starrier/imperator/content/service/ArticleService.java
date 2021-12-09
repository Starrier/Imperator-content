package org.starrier.imperator.content.service;

import org.starrier.imperator.content.entity.article.Article;
import org.starrier.imperator.content.entity.article.ArticleVote;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @author Starrier
 * @date 2018/10/27.
 */
public interface ArticleService {

    /**
     * Insert Article with Id
     *
     * @param article
     */
    Boolean insertArticle(Article article);

    /**
     * Delete Article via id
     *
     * @param id
     */
    void deleteById(final Long id);

    /**
     * Delete Article By Author
     *
     * @param author
     */
    void deleteArticleByAuthor(final String author);

    /**
     * Delete Article By Article
     *
     * @param article
     */
    void deleteArticle(final Article article);

    /**
     * Fetch All Articles
     *
     * @param page
     * @param limit
     * @return
     */
    List<Article> listArticle(int page, int limit);

    /**
     * 点赞
     *
     * @param articleVote
     * @return
     */
    Article createVote(ArticleVote articleVote);

    /**
     * 取消点赞
     *
     * @param articleId
     * @param likeId
     * @return
     */
    void removeVote(int articleId, int likeId);

    /**
     * @param id
     * @return
     */
    Optional<List<Article>> getArticlesByCategoryId(int id);

    /**
     * @param keyword
     * @return
     */
    List<Article> getArticlesByKeyword(String keyword);

    /**
     * @param articleId
     * @return
     */
    Article getArticleById(Long articleId);

    /**
     * @param author
     * @return
     */
    List<Article> getArticlesByAuthor(final String author);

    /**
     * <p>Update Article.</p>
     *
     * @param article
     */
    void updateArticle(Article article);

    /**
     * Asynchronous method.
     */
    void executeAsynchronous() throws InterruptedException;

    /**
     *
     */
    Future<Article> collapsingGlobal(Long id);

    List<String> listAllAuthorName();

    Optional<Article> findById(Long articleId);

    List<Article> searchArticles(String key);

    List<Article> findAllByIds(List<Integer> articleIds);

    void updateArticleById(Long id);

    String test();
}
