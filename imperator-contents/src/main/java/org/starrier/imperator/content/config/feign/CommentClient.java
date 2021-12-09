package org.starrier.imperator.content.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.starrier.imperator.content.dto.CommentDTO;
import org.starrier.imperator.content.entity.article.Article;

import java.util.List;


/**
 * @author Imperater
 * @date 2019/08/31
 */

@FeignClient(value = "api/v1/comments", url = "https://jsonplaceholder.typicode.com/")
public interface CommentClient {


    /**
     * Get all comments for current Article.
     *
     * @param articleId current {@link Article#getId()}
     * @return {@link CommentDTO}
     */

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    List<CommentDTO> getComments(Long articleId);
}
