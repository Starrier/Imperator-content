package org.starrier.imperator.content.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author starrier
 * @date 2020/11/28
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RedisLua {

    private List<String> testKey;

    private String testValue;

}
