package org.starrier.imperator.content.entity.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author starrier
 * @date 2021/12/9
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long  id;

    private String username;

    private String nickname;

    private String weChatId;

    private String cityName;

    private String areaName;

    private String age;

    private String sex;

}
