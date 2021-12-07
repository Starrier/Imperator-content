package org.starrier.imperator.content.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author starrier
 * @date 2021/11/30
 */
public class RedisConstant {

    public static final String CHECK_IN_PRE_KEY = "USER_CHECK_IN::DAY::";

    public static final String CONTINUOUS_CHECK_IN_COUNT_PRE_KEY = "USER_CHECK_IN::CONTINUOUS_COUNT::";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

}
