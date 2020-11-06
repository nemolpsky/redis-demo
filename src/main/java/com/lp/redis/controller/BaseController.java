package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/base")
@Api(tags = "高级用法")
public class BaseController {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;


    @PostMapping("/executePipelined")
    @ApiOperation(value = "流水线执行命令，将多个命令包装在一个管道内执行，避免多次连接，提升执行效率")
    public void executePipelined(@RequestParam String key, @RequestParam String value) {
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) redisConnection -> {
            StringRedisConnection connection = (StringRedisConnection) redisConnection;
            connection.set(key, value);
            connection.incr(key);
            connection.get(key);
            return null;
        });
        System.out.println(list.toString());
    }

    @PostMapping("/execute")
    @ApiOperation(value = "流水线执行命令，将多个命令包装在一个管道内执行，避免多次连接，提升执行效率")
    public void execute(@RequestParam String key, @RequestParam String value) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                redisOperations.boundValueOps((K) key).set((V) value);
                return null;
            }
        });
    }

}
