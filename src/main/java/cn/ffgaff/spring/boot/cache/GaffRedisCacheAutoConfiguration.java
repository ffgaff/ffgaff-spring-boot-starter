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

package cn.ffgaff.spring.boot.cache;

import cn.ffgaff.common.tools.cache.GaffCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

@AutoConfiguration(after = {RedisAutoConfiguration.class})
@ConditionalOnClass(RedisOperations.class)
public class GaffRedisCacheAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(value = GaffCache.class)
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public GaffCache<?, ?> gaffRedisCache(RedisConnectionFactory redisConnectionFactory) {
        return new GaffRedisCache<>(redisConnectionFactory);
    }
}
