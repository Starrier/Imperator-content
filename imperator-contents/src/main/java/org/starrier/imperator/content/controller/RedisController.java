package org.starrier.imperator.content.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.ResponseEntity;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starrier.imperator.content.test.RedisLua;

import java.util.Arrays;
import java.util.List;

/**
 * @author starrier
 * @date 2020/11/28
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("/lua")
    public ResponseEntity<Object> redisLuaTest(@RequestBody RedisLua redisLua) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/Test.lua")));
        redisScript.setResultType(Boolean.class);
        List<String> keys = redisLua.getTestKey();
        Object execute = redisTemplate.execute(redisScript, keys, "100");
        assert execute != null;
        return ResponseEntity.ok(execute);
    }

    @GetMapping("/lua")
    public ResponseEntity<Object> redisLuaTest() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/Test.lua")));
        redisScript.setResultType(Boolean.class);
        List<String> keys = Arrays.asList("testLua", "hello lua");
        Object execute = redisTemplate.execute(redisScript, keys, "100");
        assert execute != null;
        return ResponseEntity.ok(execute);
    }
}
