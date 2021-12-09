package org.starrier.imperator.content.entity.user.service;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.starrier.common.utils.json.FastJsonUtils;
import org.starrier.imperator.content.dto.person.Account;

import java.util.Objects;

/**
 * @author starrier
 * @date 2021/11/23
 */
@Service
public class AccountServiceApplication {

    private final StringRedisTemplate stringRedisTemplate;

    public AccountServiceApplication(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * judge current account login or not
     * <p></p>
     *
     * @param account current user info
     * @return account login or not
     */
    public Boolean login(Account account) {

        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();

        Boolean hasCurLoginAccount = operations.getOperations().hasKey(account.getId());
        if (BooleanUtils.isFalse(hasCurLoginAccount)) {
            return false;
        }

        String curAccountStringInfo = operations.get(account.getId());
        if (StringUtils.isBlank(curAccountStringInfo)) {
            return false;
        }

        Account curAccount = FastJsonUtils.getJsonToBean(curAccountStringInfo, Account.class);
        if (Objects.isNull(curAccount)) {
            return false;
        }
        return true;
    }


}



