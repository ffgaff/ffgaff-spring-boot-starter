# ffgaff

ffgaff 致力于提供一套封装的工具库。用于简化代码，增加效率。

## 主要功能

* **缓存能力**：封装caffeine和redis等多种缓存，可配置切换或者公用多种缓存方式。详情请移步[缓存能力]()。

## 快速开始

### 引入依赖

* Apache Maven

```xml

<dependency>
    <groupId>cn.ffgaff</groupId>
    <artifactId>ffgaff-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

```

* Gradle DSL

```groovy
    implementation 'cn.ffgaff:ffgaff-spring-boot-starter:1.0-SNAPSHOT'

```

### 缓存能力

下面是我们的缓存能力基础应用。更多请参考[缓存能力]()

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

# License

[Apache2.0](https://www.apache.org/licenses/LICENSE-2.0.html)



