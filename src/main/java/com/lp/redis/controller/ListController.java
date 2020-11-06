package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/list")
@Api(tags="List类型")
public class ListController {

    @Autowired
    public RedisTemplate<String,String> redisTemplate;

    private ListOperations<String,String> getListOperations(){
        return redisTemplate.opsForList();
    }

    @PostMapping("/leftPush")
    @ApiOperation(value = "从集合头部插入元素，自动创建集合，返回集合长度")
    public Long leftPush(@RequestParam String key,@RequestParam String value){
        return getListOperations().leftPush(key,value);
    }

    @PostMapping("/rightPush")
    @ApiOperation(value = "从集合尾部插入元素，自动创建集合，返回集合长度")
    public Long rightPush(@RequestParam String key,@RequestParam String value){
        return getListOperations().rightPush(key,value);
    }

    @PostMapping("/leftPushAll")
    @ApiOperation(value = "从集合头部批量插入元素，自动创建集合，返回集合长度")
    public Long leftPushAll(@RequestParam String key,@RequestParam String... values){
        return getListOperations().leftPushAll(key,values);
    }

    @PostMapping("/leftPushIfPresent")
    @ApiOperation(value = "从集合头部批量元素，不自动创建集合，返回集合长度，集合不存在返回0")
    public Long leftPushIfPresent(@RequestParam String key,@RequestParam String value){
        return getListOperations().leftPushIfPresent(key,value);
    }

    @PostMapping("/range")
    @ApiOperation(value = "获取集合指定区间元素，集合不存在返回空集合，超出集合范围不返回任何元素")
    public List<String> range(@RequestParam String key,@RequestParam Long start,@RequestParam Long end){
        return getListOperations().range(key,start,end);
    }

    @PostMapping("/leftPop")
    @ApiOperation(value = "从指定集合的头部弹出元素，集合不存在则返回null")
    public String leftPop(@RequestParam String key){
        return getListOperations().leftPop(key);
    }

    @PostMapping("/leftPop2")
    @ApiOperation(value = "从指定集合的头部弹出元素，集合不存在会一直阻塞等待，超时则返回null")
    public String leftPop2(@RequestParam String key,@RequestParam Long time){
        TimeUnit timeUnit = TimeUnit.SECONDS;
        return getListOperations().leftPop(key,time,timeUnit);
    }

    @PostMapping("/index")
    @ApiOperation(value = "从指定集合的指定索引获取元素，集合不存在则返回null，索引可以输入负数，从尾部开始计算")
    public String index(@RequestParam String key,@RequestParam Long index){
        return getListOperations().index(key,index);
    }

    @PostMapping("/set")
    @ApiOperation(value = "从指定集合的指定索引位置插入元素，集合不存在或索引越界会抛出异常，索引可以输入负数，从尾部开始计算")
    public void set(@RequestParam String key,@RequestParam Long index,@RequestParam String value){
        getListOperations().set(key,index,value);
    }

    @PostMapping("/remove")
    @ApiOperation(value = "从指定集合的指定索引位置移除指定元素，集合不存在或索引越界会返回0，value值匹配上才会删除，索引可以输入负数，从尾部开始计算")
    public Long remove(@RequestParam String key,@RequestParam Long index,@RequestParam String value){
       return getListOperations().remove(key,index,value);
    }

    @PostMapping("/trim")
    @ApiOperation(value = "从指定集合移除指定区间元素")
    public void trim(@RequestParam String key,@RequestParam Long start,@RequestParam Long end){
        getListOperations().trim(key,start,end);
    }

    @PostMapping("/size")
    @ApiOperation(value = "获取指定集合长度")
    public Long size(@RequestParam String key){
       return getListOperations().size(key);
    }

    @PostMapping("/rightPopAndLeftPush")
    @ApiOperation(value = "从sourceKey尾部集合弹出一个元素，放入destinationKey集合的头部，如果目标集合不存在则会创建一个")
    public String rightPopAndLeftPush(@RequestParam String sourceKey,@RequestParam String destinationKey){
        return getListOperations().rightPopAndLeftPush(sourceKey,destinationKey);
    }

    @PostMapping("/rightPopAndLeftPush2")
    @ApiOperation(value = "从sourceKey尾部集合弹出一个元素，放入destinationKey集合的头部，如果目标集合不存在则会创建一个，如果源集合不存在则会一直阻塞等待，直到超时为止")
    public String rightPopAndLeftPush2(@RequestParam String sourceKey,@RequestParam String destinationKey,@RequestParam Long time){
        return getListOperations().rightPopAndLeftPush(sourceKey,destinationKey,time,TimeUnit.SECONDS);
    }
}
