package org.starrier.imperator.content.entity.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Starrier
 * @date 2018/10/27.
 */
@Accessors(chain = true)
@Builder
@Data
@Table(name = "article")
@AllArgsConstructor
public class Article implements Serializable {

    private static final long serialVersionUID = -8372839858844609232L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateDate;

    @Column
    private String summary;

    @Column
    private String category;

    @Column(name = "author")
    private String author;

    @Column
    private Long commentCount;

    /**
     * Serialize Version
     */
    @Version
    @Column
    private Long version;

    public Article() {
    }
}
