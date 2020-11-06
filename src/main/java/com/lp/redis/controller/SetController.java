package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/set")
@Api(tags = "Set类型")
public class SetController {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    private SetOperations<String, String> getSetOperations() {
        return redisTemplate.opsForSet();
    }

    @PostMapping("/add")
    @ApiOperation(value = "批量插入元素，自动创建集合，返回集合长度")
    public Long add(@RequestParam String key, @RequestParam String... values) {
        return getSetOperations().add(key, values);
    }

    @PostMapping("/members")
    @ApiOperation(value = "获取集合中的全部元素，集合不存在返回空集合")
    public Set<String> members(@RequestParam String key) {
        return getSetOperations().members(key);
    }

    @PostMapping("/pop")
    @ApiOperation(value = "随机获取集合中的指定数量的元素，集合不存在返回null")
    public String pop(@RequestParam String key) {
        return getSetOperations().pop(key);
    }

    @PostMapping("/pop2")
    @ApiOperation(value = "随机获取集合中的指定数量的元素，集合不存在返回空集合")
    public List<String> pop2(@RequestParam String key, @RequestParam Long nums) {
        return getSetOperations().pop(key, nums);
    }

    @PostMapping("/isMember")
    @ApiOperation(value = "判断集合中是否包含指定元素，集合不存在也返回false")
    public Boolean isMember(@RequestParam String key, @RequestParam String value) {
        return getSetOperations().isMember(key, value);
    }

    @PostMapping("/remove")
    @ApiOperation(value = "移除集合中的指定元素集，返回移除数量，集合不存在则返回0")
    public Long remove(@RequestParam String key, @RequestParam String... values) {
        return getSetOperations().remove(key, (Object) values);
    }

    @PostMapping("/randomMember")
    @ApiOperation(value = "随机获取集合中的一个元素，但是元素不会被移除，集合为空返回null")
    public String randomMember(@RequestParam String key) {
        return getSetOperations().randomMember(key);
    }

    @PostMapping("/intersect")
    @ApiOperation(value = "求两个集合的交集")
    public Set<String> intersect(@RequestParam String key1, @RequestParam String key2) {
        return getSetOperations().intersect(key1, key2);
    }

    @PostMapping("/intersectAndStore")
    @ApiOperation(value = "求两个集合的交集，并将结果存储在一个新key之中")
    public Long intersectAndStore(@RequestParam String key1, @RequestParam String key2, @RequestParam String newKey) {
        return getSetOperations().intersectAndStore(key1, key2, newKey);
    }

    @PostMapping("/difference")
    @ApiOperation(value = "求两个集合的差集")
    public Set<String> difference(@RequestParam String key1, @RequestParam String key2) {
        return getSetOperations().difference(key1, key2);
    }

    @PostMapping("/union")
    @ApiOperation(value = "求两个集合的并集")
    public Set<String> union(@RequestParam String key1, @RequestParam String key2) {
        return getSetOperations().union(key1, key2);
    }

    @PostMapping("/distinctRandomMembers")
    @ApiOperation(value = "从集合中获取指定数量的随机元素，并保证元素不重复")
    public Set<String> distinctRandomMembers(@RequestParam String key, @RequestParam Long nums) {
        return getSetOperations().distinctRandomMembers(key, nums);
    }

    @PostMapping("/scan")
    @ApiOperation(value = "遍历Set集合")
    public void scan(@RequestParam String key) throws IOException {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        try (Cursor<String> cursor = getSetOperations().scan(key, scanOptions)) {
            cursor.forEachRemaining(System.out::println);
        }
    }
}
