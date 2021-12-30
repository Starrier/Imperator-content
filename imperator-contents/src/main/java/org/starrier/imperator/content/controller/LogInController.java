package org.starrier.imperator.content.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starrier.common.result.Result;
import org.starrier.imperator.content.dto.person.Account;
import org.starrier.imperator.content.entity.account.AccountRepresentService;
import org.starrier.imperator.content.entity.account.AccountServiceApplication;

/**
 * @author starrier
 * @date 2021/11/23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/login")
public class LogInController {

    private final AccountRepresentService accountRepresentService;
    private final AccountServiceApplication accountServiceApplication;


    @PostMapping("/check-in")
    public Result checkIn(@RequestBody Account account) {

        Boolean checkIn = accountRepresentService.checkIn(Long.valueOf(account.getId()));

        return Result.success(checkIn);
    }

    @PostMapping
    public Result login(@RequestBody Account account){
        return Result.success(accountServiceApplication.login(account));
    }


}
