package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/hash")
@Api(tags = "Hash类型")
public class HashController {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, String> getHashOperations() {
        return redisTemplate.opsForHash();
    }

    @PostMapping("/put")
    @ApiOperation(value = "往hash类型key中添加数据")
    public void put(@RequestParam String hashKey, @RequestParam String key, @RequestParam String value) {
        getHashOperations().put(hashKey, key, value);
    }

    @PostMapping("/get")
    @ApiOperation(value = "根据key获取value")
    public String get(@RequestParam String hashKey, @RequestParam String key) {
        return getHashOperations().get(hashKey, key);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据key批量删除value，返回删除数量")
    public Long delete(@RequestParam String hashKey, @RequestParam String... key) {
        return getHashOperations().delete(hashKey, (Object[]) key);
    }

    @PostMapping("/size")
    @ApiOperation(value = "查询hash中的元素数量")
    public Long size(@RequestParam String hashKey) {
        return getHashOperations().size(hashKey);
    }

    @PostMapping("/lengthOfValue")
    @ApiOperation(value = "查询hash中的value的长度")
    public Long lengthOfValue(@RequestParam String hashKey, @RequestParam String key) {
        return getHashOperations().lengthOfValue(hashKey, key);
    }

    @PostMapping("/entries")
    @ApiOperation(value = "获取整个hash集合")
    public Map<String, String> entries(@RequestParam String hashKey) {
        return getHashOperations().entries(hashKey);
    }

    @PostMapping("/hasKey")
    @ApiOperation(value = "判断hash中是否存在指定的key")
    public Boolean hasKey(@RequestParam String hashKey, @RequestParam String key) {
        return getHashOperations().hasKey(hashKey, key);
    }

    @PostMapping("/putIfAbsent")
    @ApiOperation(value = "创建hash类型的数据，当hash中不存在该key-value时才成功")
    public Boolean putIfAbsent(@RequestParam String hashKey, @RequestParam String key, @RequestParam String value) {
        return getHashOperations().putIfAbsent(hashKey, key, value);
    }

    @PostMapping("/keys")
    @ApiOperation(value = "获取key的集合")
    public Set<String> keys(@RequestParam String hashKey) {
        return getHashOperations().keys(hashKey);
    }

    @PostMapping("/values")
    @ApiOperation(value = "获取value的集合")
    public List<String> values(@RequestParam String hashKey) {
        return getHashOperations().values(hashKey);
    }

    @PostMapping("/multiGet")
    @ApiOperation(value = "批量获取value，不存在的key返回null，即使所有key都不存在也会返回一个全是null的集合")
    public List<String> multiGet(@RequestParam String hashKey, @RequestParam List<String> keyList) {
        return getHashOperations().multiGet(hashKey, keyList);
    }

    @PostMapping("/putAll")
    @ApiOperation(value = "往hash类型key中批量添加数据")
    public void putAll(@RequestParam String hashKey, @RequestBody Map<String, String> map) {
        getHashOperations().putAll(hashKey, map);
    }

    @PostMapping("/increment")
    @ApiOperation(value = "根据key对value进行自增，返回自增后的结果")
    public Long increment(@RequestParam String hashKey, @RequestParam String key, @RequestParam Long nums) {
        return getHashOperations().increment(hashKey, key, nums);
    }

    @PostMapping("/scan")
    @ApiOperation(value = "遍历hash")
    public void scan(@RequestParam String hashKey) {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<Map.Entry<String, String>> cursor = getHashOperations().scan(hashKey, scanOptions);
        cursor.forEachRemaining(e -> {
            System.out.println(e.getKey() + ":" + e.getValue());
        });
    }
}
