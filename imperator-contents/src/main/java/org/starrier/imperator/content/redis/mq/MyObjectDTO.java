package org.starrier.imperator.content.redis.mq;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MyObjectDTO implements Serializable {
    private Long id;
    private String content;
}
