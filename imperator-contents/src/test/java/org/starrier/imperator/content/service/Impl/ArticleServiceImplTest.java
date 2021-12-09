package org.starrier.imperator.content.service.Impl;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.starrier.imperator.content.entity.article.Article;
import org.starrier.imperator.content.service.ArticleService;

/**
 * @author starrier
 * @date 2020/11/23
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate()
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
@PrepareForTest({
        ArticleService.class,
        ArticleServiceImpl.class
})
public class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl articleService;


    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ArticleService.class);
        PowerMockito.mockStatic(ArticleServiceImpl.class);
    }

    @Test
    void insertArticle() {
        Article article = new Article();
        article.setAuthor("test");
        article.setTitle("test-title");

        //Boolean insertResult = articleService.insertArticle(article);
        //Assert.assertTrue(insertResult);
    }

    @Test
    void listArticle() {
    }

    @Test
    void createVote() {
    }

    @Test
    void removeVote() {
    }

    @Test
    void getArticlesByCategoryId() {
    }

    @Test
    void getArticlesByKeyword() {
    }

    @Test
    void getArticleById() {
    }

    @Test
    void getArticleByIdRecovery() {
    }

    @Test
    void updateArticle() {
    }

    @Test
    void executeAsynchronous() {
    }

    @Test
    void collapsingGlobal() {
    }

    @Test
    void listAllAuthorName() {
    }

    @Test
    void findById() {
    }

    @Test
    void searchArticles() {
    }

    @Test
    void findAllByIds() {
    }

    @Test
    void updateArticleById() {
    }

    @Test
    void test1() {
    }

    @Test
    void testGetArticleById() {
    }

    @Test
    void getArticleByIdError() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteArticleByAuthor() {
    }

    @Test
    void deleteArticle() {
    }

    @Test
    void getArticlesByAuthor() {
    }
}