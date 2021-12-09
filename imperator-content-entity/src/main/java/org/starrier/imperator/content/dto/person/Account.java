package org.starrier.imperator.content.dto.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.LinkedList;

/**
 * @author starrier
 * @date 2021/11/23
 */
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {


    /**
     * 重置时间
     */
    private static final int RESET_TIME = 30;

    /**
     * 密码连续输入5次失败的持续时间
     * 10 min
     */
    private static final int DURATION = 10;

    /**
     * 最大输入失败次数
     */
    private static final int MAX_TIMES = 5;

    /**
     * 用户id
     */
    @NotBlank(message = "user id not allow blank")
    private String id;

    /**
     * 登录失败次数
     */
    private int failCount;

    /**
     * 第一次失败的时间
     */
    private long firstFailTime;

    /**
     * 登录失败的时间
     */
    private LinkedList<Long> times;

    private boolean lock;


}
