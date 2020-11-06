package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/string")
@Api(tags="String类型")
public class StringController {

    @Autowired
    public RedisTemplate<String,String> redisTemplate;

    protected ValueOperations<String,String> getValueOperations(){
        return redisTemplate.opsForValue();
    }

    @PostMapping("/get")
    @ApiOperation(value = "使用key查询value")
    public String get(@RequestParam String key){
        Object object = getValueOperations().get(key);
        return Optional.ofNullable(object).map(Object::toString).orElse("key is not exist.");
    }

    @PostMapping("/set")
    @ApiOperation(value = "添加key-value")
    public void set(@RequestParam String key,@RequestParam String value){
        getValueOperations().set(key,value);
    }

    @PostMapping("/append")
    @ApiOperation(value = "追加添加key-value")
    public Integer append(@RequestParam String key,@RequestParam String value){
        return getValueOperations().append(key,value);
    }

    @PostMapping("/getAndSet")
    @ApiOperation(value = "使用key查询value并修改value值，redis会保证原子性")
    public String getAndSet(@RequestParam String key,@RequestParam String value){
        return getValueOperations().getAndSet(key,value);
    }

    @PostMapping("/setIfPresent")
    @ApiOperation(value = "添加key-value，当key存在的时候才生效，会覆盖旧值")
    public Boolean setIfPresent(@RequestParam String key,@RequestParam String value){
        return getValueOperations().setIfPresent(key,value);
    }

    @PostMapping("/setIfAbsent")
    @ApiOperation(value = "添加key-value，当key不存在的时候才生效")
    public Boolean setIfAbsent(@RequestParam String key,@RequestParam String value){
        return getValueOperations().setIfAbsent(key,value);
    }

    @PostMapping("/size")
    @ApiOperation(value = "使用key查询value的长度")
    public Long size(@RequestParam String key){
        return getValueOperations().size(key);
    }

    @PostMapping("/multiGet")
    @ApiOperation(value = "查询多个key，根据key的顺序返回值，没查询到的值会返回null，不去重")
    public List<String> multiGet(@RequestParam List<String> keys){
        return getValueOperations().multiGet(keys);
    }

    @PostMapping("/multiSet")
    @ApiOperation(value = "批量添加多个key-value")
    public void multiSet(@RequestBody Map<String,String> map){
        getValueOperations().multiSet(map);
    }

    @PostMapping("/multiSetIfAbsent")
    @ApiOperation(value = "批量添加多个key-value，key不存在的时候才生效，注意必须所有key都不存在才成功，否则全部失败")
    public Boolean multiSetIfAbsent(@RequestBody Map<String,String> map){
        return getValueOperations().multiSetIfAbsent(map);
    }

    @PostMapping("/increment")
    @ApiOperation(value = "原子递增，如果key不存在则直接创建一个")
    public Long increment(@RequestParam String key,@RequestParam Long value){
        return getValueOperations().increment(key,value);
    }
}
