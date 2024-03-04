/*
 * Copyright 2024  ffgaff.cn
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import cn.ffgaff.common.lambda.Sef;
import cn.ffgaff.common.tools.cache.GaffCache;
import cn.ffgaff.common.tools.cache.TimeOutValid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CacheTest.Main.class)
//@Slf4j


public class CacheTest {

    @SpringBootApplication
    public static class Main {

    }

//    @Autowired
//    RedisTemplate<String,Integer> redisTemplate;
//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;

//    @Autowired
//    GaffCache<String, Integer> cache;


    @SneakyThrows
    void testCache(GaffCache<String, Integer> cache) {

        Sef.range(0, 999)
                .forEach(o -> {
                    new Thread(() -> {
                        Integer a = cache.getCache("key", v -> v.setValue(o.intValue()).setValid(new TimeOutValid().setExpiresIn(100L)));
                        Integer b = cache.getCache("key", v -> v.setValue(o.intValue() + 1).setValid(new TimeOutValid().setExpiresIn(10L)));
//                        log.info("{}:{}", a, b);
//                        assertEquals(a + b, a * 2);

                    }).start();
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                });

//        log.info("test" + cache.getCache("key-test", v -> v.setValue(111)));
//        log.info("test2" + cache.getCache("key-test", v -> v.setValue(222)));
    }

//    @SneakyThrows
//    @Test
//    public void db() {
//
//        new Thread(() -> {
//
//            redisTemplate.opsForValue().set("test111", 11);
//
//            log.info("ops:{}", redisTemplate.opsForValue().get("test111"));
//
//            redisTemplate.delete("test111");
//        }).start();
//
//        Thread.sleep(10000);
//    }

    @SneakyThrows
    @Test
    public void cacheTest() {
        System.out.println("asdfs");
    }

}
