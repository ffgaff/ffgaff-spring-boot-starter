# GaffCache

GaffCache 封装caffeine和redis等多种缓存，可配置切换或者公用多种缓存方式。

## 特性

- 基于 Java 17 开发。
- 可以独立控制每个缓存项的有效逻辑。
- 可封装多套缓存方案，实现轻松切换。

## 创建

可以通过`GaffCacheUtil`工具类创建缓存。

### 创建默认缓存

缓存默认会创建caffeine缓存。

```java
class demo {
    GaffCache<Object, Object> cache = GaffCacheUtil.newBuilder().builder();
}

```

### 创建指定类型缓存

可指定缓存类型，内置类型包括`GaffThreadLocalCache`,`GaffCaffeineCache`,`GaffRedisCache`，也可以通过继承`GaffAbstractCache`
创建自己的缓存。

```java
class demo {
    GaffCache<Object, Object> cache = GaffCacheUtil.newBuilder()
            .setCacheClass(GaffThreadLocalCache.class)
            .builder();
}

```

## 使用缓存

可以简单的使用getCache 方法获取和设置缓存。该方法新通过key获取缓存。如果缓存项无效则通过参数2创建一个新的缓存项。

```java
class demo {
    void use() {
        cache.getCache("key", value -> value.setValue("value"));
    }
}

```

### 缓存项的有效性验证

默认情况下缓存项目是长期有效的，我们可以定义一套验证来让缓存达成条件后失效。内置了`TimeOutValid`
类用于配置过期时间。可以让缓存项过期。也可以通过继承
`GaffCacheValueValid`接口来定义自己的验证逻辑。

#### 例：20秒后过期的缓存项

```java
class demo {
    void use() {
        cache.getCache("key", value -> value.setValue("value")
                .setValid(new TimeOutValid().setExpiresIn(20L))
        );
    }
}

```

### API 列表

- getCache 获取指定缓存项
- setCache 设置指定缓存项
- delete 删除指定缓存项
- clear 清空缓存数据
- recovery 整理缓存回收资源

## Spring Boot 中的配置

当我们引入gaffCache后，会根据spring配置默认生成一个GaffCache的Bean.如果我们配置了Redis数据库，
gaffCache会默认使用redis。否则默认使用Caffeine作为缓存。这将会应用到`@ExpiredCache`注解。当然也可以通过注解的属性指定
缓存Bean的名称。

### 使用`@ExpiredCache`注解

**第一步:**  添加 `@EnableAutoGaffCache` 注解，来启用`@ExpiredCache`注解能力。

#### 注：`@EnableAutoGaffCache` 注解需要spring aop支持。请引入依赖库

```java

@Configuration
@EnableAutoGaffCache
public class CacheConfiguration {

}

```

**第二步:** 服务类方法中添加`@ExpiredCache`注解使用缓存。

```java

@Service
@Slf4j
public class CacheDemoService {

    @ExpiredCache(cacheName = "t2", key = "#a")
    public int getInt(int a, int b) {
        return a + b;
    }

    @ExpiredCache(cacheName = "t2", key = "#a", type = CacheOPType.update)
    public int upInt(int a, int b) {
        return a * b;
    }

    @ExpiredCache(cacheName = "t2", key = "#a", type = CacheOPType.delete)
    public void delInt(int a) {

    }


    //引用类型

    @ExpiredCache(cacheName = "tObject", key = "#id")
    public Object getObject(int id, Object value) {
        return value;
    }

    @ExpiredCache(cacheName = "tObject", key = "#id", type = CacheOPType.update)
    public Object upObject(int id, Object value) {
        return value;
    }

    @ExpiredCache(cacheName = "tObject", key = "#id", type = CacheOPType.delete)
    public void delObject(int id) {

    }
}
```

### 自定义GaffCache Bean

我们可以根据自己需求来配置缓存的Bean对象。

#### 例：配置一个缓存bean对象

```java

@Configuration
@EnableAutoGaffCache
public class CacheConfiguration {

    @Bean(name = "default")
    public GaffCache<?, ?> gaffCache() {
        return GaffCacheUtil.newBuilder().builder();
    }
}

```

#### 例：配置一个redis缓存，并自定义序列化方案 （需要自行引入jackson相关库）

```java

@Configuration
@EnableAutoGaffCache
public class CacheConfiguration {

    @Bean(name = "redis")
    public GaffCache<?, ?> gaffCache(RedisConnectionFactory redisConnectionFactory) {
        return new GaffRedisCache<>("gaff", redisConnectionFactory, cache -> {
            RedisSerializer stringSerializer = new StringRedisSerializer();
            cache.setKeySerializer(stringSerializer);
            cache.setHashKeySerializer(stringSerializer);
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
            cache.setValueSerializer(serializer);
            cache.setHashValueSerializer(serializer);
        });
    }
}

```

#### 例：配置多个缓存共用 建议加`@Primary`注解来标识默认的缓存

```java

@Configuration
@EnableAutoGaffCache
public class CacheConfiguration {

    @Bean(name = "default")
    public GaffCache<?, ?> gaffCache() {
        return GaffCacheUtil.newBuilder().builder();
    }

    @Bean(name = "redis")
    @Primary
    public GaffCache<?, ?> gaffCacheForRedis(RedisConnectionFactory redisConnectionFactory) {
        return new GaffRedisCache<>("gaff", redisConnectionFactory, cache -> {
            RedisSerializer stringSerializer = new StringRedisSerializer();
            cache.setKeySerializer(stringSerializer);
            cache.setHashKeySerializer(stringSerializer);
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
            cache.setValueSerializer(serializer);
            cache.setHashValueSerializer(serializer);
        });
    }
}


```




