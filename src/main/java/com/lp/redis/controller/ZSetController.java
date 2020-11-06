package com.lp.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/zset")
@Api(tags = "ZSet类型")
public class ZSetController {

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    private ZSetOperations<String, String> getZSetOperations() {
        return redisTemplate.opsForZSet();
    }

    @PostMapping("/add")
    @ApiOperation(value = "往ZSet集合中添加元素，并设置次数，如果存在则更新次数，不存在则新增，只有新增操作时才返回true")
    public Boolean add(@RequestParam String key, @RequestParam String value, @RequestParam Double score) {
        return getZSetOperations().add(key, value, score);
    }

    @PostMapping("/count")
    @ApiOperation(value = "根据次数区间统计元素数量")
    public Long count(@RequestParam String key, @RequestParam Double min,@RequestParam Double max) {
        return getZSetOperations().count(key,min,max);
    }

    @PostMapping("/score")
    @ApiOperation(value = "获取指定元素的次数")
    public Double score(@RequestParam String key, @RequestParam String value) {
        return getZSetOperations().score(key,value);
    }

    @PostMapping("/scan")
    @ApiOperation(value = "遍历ZSet集合")
    public void scan(@RequestParam String key) {
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        Cursor<ZSetOperations.TypedTuple<String>> cursor = getZSetOperations().scan(key, scanOptions);
        cursor.forEachRemaining(e -> {
            System.out.println(e.getValue() + ":" + e.getScore());
        });
    }

    @PostMapping("/incrementScore")
    @ApiOperation(value = "递增元素的次数，返回递增后的结果")
    public Double incrementScore(@RequestParam String key, @RequestParam String value, @RequestParam Double score) {
        return getZSetOperations().incrementScore(key,value,score);
    }

    @PostMapping("/size")
    @ApiOperation(value = "获取当前集合大小")
    public Long size(@RequestParam String key) {
        return getZSetOperations().size(key);
    }

    @PostMapping("/zCard")
    @ApiOperation(value = "获取当前集合key的数量")
    public Long zCard(@RequestParam String key) {
        return getZSetOperations().zCard(key);
    }

    @PostMapping("/reverseRank")
    @ApiOperation(value = "根据元素的降序升序位置获得当前元素的索引")
    public Long reverseRank(@RequestParam String key, @RequestParam String value) {
        return getZSetOperations().reverseRank(key,value);
    }

    @PostMapping("/unionAndStore")
    @ApiOperation(value = "求两个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量")
    public Long unionAndStore(@RequestParam String key1, @RequestParam String key2, @RequestParam String newKey) {
        return getZSetOperations().unionAndStore(key1,key2,newKey);
    }

    @PostMapping("/unionAndStore2")
    @ApiOperation(value = "求多个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量，支持聚合函数设置")
    public Long unionAndStore2(@RequestParam String key, @RequestParam List<String> list, @RequestParam String newKey) {
        // list参数是存放key的，如果合并多个key则都把key存放在list中
        // RedisZSetCommands.Aggregate 是指定合并的时候对相同元素的合并方式，MIN表示取最小值，MAX取最大值，SUM求和
        return getZSetOperations().unionAndStore(key,list,newKey, RedisZSetCommands.Aggregate.MIN);
    }

    @PostMapping("/unionAndStore3")
    @ApiOperation(value = "求多个集合的并集，并且存入一个新的集合中，将相同元素的次数进行相加，返回并集数量，支持乘法系数和聚合函数设置")
    public Long unionAndStore3(@RequestParam String key, @RequestParam List<String> list, @RequestParam String newKey) {
        // list参数是存放key的，如果合并多个key则都把key存放在list中
        // RedisZSetCommands.Aggregate 是指定合并的时候对相同元素的合并方式，MIN表示取最小值，MAX取最大值，SUM求和
        // RedisZSetCommands.Weights 是乘以的系数，每个元素都会乘以这个系数，需要注意的是list中有多少个集合，就必须要有几个系数
        return getZSetOperations().unionAndStore(key,list,newKey, RedisZSetCommands.Aggregate.SUM, RedisZSetCommands.Weights.of(3,2));
    }

    @PostMapping("/unionAndStore4")
    @ApiOperation(value = "求两个集合的交集，并把结果存入新的集合，返回心机和的数量，此类方法有和unionAndStore一样的重载方法")
    public Long intersectAndStore(@RequestParam String key1, @RequestParam String key2, @RequestParam String newKey) {
        return getZSetOperations().intersectAndStore(key1,key2,newKey);
    }

    @PostMapping("/rank")
    @ApiOperation(value = "获取指定元素在ZSet集合中的索引位置，按value升序顺序，reverseRank按value降序顺序")
    public Long rank(@RequestParam String key, @RequestParam String value) {
        return getZSetOperations().rank(key,value);
    }

    @PostMapping("/range")
    @ApiOperation(value = "根据value升序顺序进行区间筛选，reverseRange方法是根据降序顺序进行筛选")
    public Set<String> range(@RequestParam String key, @RequestParam Long start, @RequestParam Long end) {
        return getZSetOperations().range(key,start,end);
    }

    @PostMapping("/rangeByLex")
    @ApiOperation(value = "按字典顺序获取集合所有的key值，reverseRangeByLex是按照反序筛选")
    public Set<String> rangeByLex(@RequestParam String key) {
        // 默认范围是整个集合最小值和最大值
        // RedisZSetCommands.Range.range().lte(10).gte(5)，也可以使用连缀方式限定范围
        // gt: greater than 大于
        // gte: greater than or equal 大于等于
        // lt: less than 小于
        // lte: less than or equal 小于等于
        return getZSetOperations().rangeByLex(key,RedisZSetCommands.Range.range());
    }

    @PostMapping("/rangeByLex2")
    @ApiOperation(value = "按字典获取集合所有的key值，设置取值的个数，以及开始取值的索引位置，reverseRangeByLex是按照反序筛选")
    public Set<String> rangeByLex2(@RequestParam String key) {
        // RedisZSetCommands.Limit.limit(); 这是默认方式，不做任何限制
        // RedisZSetCommands.Limit.limit().count(1).offset(1); 使用连缀方式设置限制，count表示取一个，offset表示从索引1开始取
        return getZSetOperations().rangeByLex(key,RedisZSetCommands.Range.range(),RedisZSetCommands.Limit.limit().count(1).offset(1));
    }

    @PostMapping("/rangeByScore")
    @ApiOperation(value = "按照次数升序顺序的区间范围获取元素，reverseRangeByScore是按照降序")
    public Set<String> rangeByScore(@RequestParam String key,@RequestParam Double start,@RequestParam Double end) {
        return getZSetOperations().rangeByScore(key,start,end);
    }

    @PostMapping("/rangeByScore2")
    @ApiOperation(value = "按照次数区间范围获取元素，可以设置取值个数(count)，开始取值的索引(offset)，reverseRangeByScore是按照降序")
    public Set<String> rangeByScore2(@RequestParam String key,@RequestParam Double start,@RequestParam Double end
            ,@RequestParam Long offset,@RequestParam Long count) {
        return getZSetOperations().rangeByScore(key,start,end,offset,count);
    }

    @PostMapping("/rangeByScoreWithScores")
    @ApiOperation(value = "根据次数升序顺序进行区间筛选，返回一个Tuple类型的集合，也就是把value和score都返回，reverseRangeByScoreWithScores是降序")
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores(@RequestParam String key, @RequestParam Long start, @RequestParam Long end) {
        return getZSetOperations().rangeByScoreWithScores(key,start,end);
    }

    @PostMapping("/rangeByScoreWithScores2")
    @ApiOperation(value = "根据次数升序顺序进行区间筛选，返回一个Tuple类型的集合，也就是把value和score都返回，" +
            "可以设置取值个数(count)，开始取值的索引(offset)，reverseRangeByScoreWithScores是降序")
    public Set<ZSetOperations.TypedTuple<String>> rangeByScoreWithScores2(@RequestParam String key, @RequestParam Long start, @RequestParam Long end
            ,@RequestParam Long offset,@RequestParam Long count) {
        return getZSetOperations().rangeByScoreWithScores(key,start,end,offset,count);
    }
    
}
