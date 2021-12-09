package org.starrier.imperator.content.entity.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author starrier
 * @date 2020/11/18
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVote {

    @NotBlank(message = "")
    private Long articleId;

    @NotBlank(message = "userId not allow blank")
    private Long userId;
}
