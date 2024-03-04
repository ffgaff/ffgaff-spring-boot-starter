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


import cn.ffgaff.common.lambda.function.Fun1;
import cn.ffgaff.common.tools.cache.*;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GaffRedisCache<K, V> extends GaffAbstractCache<K, V> {


    private final RedisTemplate<String, Object> cache;
    private final String PREFIX;

    private final static GaffCache<Object, String> kmap = GaffCacheUtil.newBuilder().builder();

    private static final String SEPARATOR = ":";

    public GaffRedisCache(String prefix, RedisConnectionFactory redisConnectionFactory, Fun1<RedisTemplate<String, Object>> initRedis) {

        cache = new RedisTemplate<>();
        cache.setConnectionFactory(redisConnectionFactory);
        initRedis.accept(cache);
        cache.afterPropertiesSet();
        if (StrUtil.isBlank(prefix))
            this.PREFIX = "giffCache" + SEPARATOR;
        else
            this.PREFIX = prefix + SEPARATOR;
    }

    public GaffRedisCache(String prefix, RedisConnectionFactory redisConnectionFactory) {
        this(prefix, redisConnectionFactory, cache -> {
            RedisSerializer stringSerializer = new StringRedisSerializer();
            cache.setKeySerializer(stringSerializer);
            cache.setHashKeySerializer(stringSerializer);
        });
    }

    public GaffRedisCache(RedisConnectionFactory redisTemplate) {
        this(null, redisTemplate);
    }

    public GaffRedisCache() {
        this(null, SpringUtil.getBean(RedisConnectionFactory.class));
    }


    private String getKey(Object key) {
        return kmap.getCache(key, o -> o.setValue(PREFIX + createKey(key)));
    }

    private String createKey(Object key) {
        log.debug("create key {}", key);

        if (key instanceof String stringKey) {
            return stringKey;
        }
        TypeDescriptor source = TypeDescriptor.forObject(key);
        ConversionService conversionService = DefaultConversionService.getSharedInstance();

        if (conversionService.canConvert(source, TypeDescriptor.valueOf(String.class))) {
            try {
                return conversionService.convert(key, String.class);
            } catch (ConversionFailedException ex) {
                if (source.isArray() || source.isCollection() || source.isMap()) {
                    return convertCollectionLikeOrMapKey(key, source);
                }
                throw ex;
            }
        }
        if (hasToStringMethod(key.getClass())) {
            return key.toString();
        }
        String message = String.format("Cannot convert cache key %s to String; Please register a suitable Converter"
                        + " via 'RedisCacheConfiguration.configureKeyConverters(...)' or override '%s.toString()'",
                source, key.getClass().getName());
        throw new IllegalStateException(message);
    }


    private String convertCollectionLikeOrMapKey(Object key, TypeDescriptor source) {

        if (source.isMap()) {

            int count = 0;

            StringBuilder target = new StringBuilder("{");

            for (Map.Entry<?, ?> entry : ((Map<?, ?>) key).entrySet()) {
                target.append(createKey(entry.getKey())).append("=").append(createKey(entry.getValue()));
                target.append(++count > 1 ? ", " : "");
            }

            target.append("}");

            return target.toString();

        } else if (source.isCollection() || source.isArray()) {

            StringJoiner stringJoiner = new StringJoiner(",");

            Collection<?> collection = source.isCollection() ? (Collection<?>) key
                    : Arrays.asList(ObjectUtils.toObjectArray(key));

            for (Object collectedKey : collection) {
                stringJoiner.add(createKey(collectedKey));
            }

            return "[" + stringJoiner + "]";
        }

        throw new IllegalArgumentException(String.format("Cannot convert cache key [%s] to String", key));
    }

    private boolean hasToStringMethod(Class<?> type) {

        Method toString = ReflectionUtils.findMethod(type, "toString");
        return toString != null && !Object.class.equals(toString.getDeclaringClass());
    }

    @Override
    public V getCache(K key) {
        return Optional.ofNullable(getValueAndValid(key))
                .map(GaffValue::getValue).orElse(null);
    }

    //
    @Override
    public V getCache(K key, Fun1<GaffValue<V>> fun) {
        GaffValue<V> tem = getValueAndValid(key);
        if (tem == null) {
            return loopUpCache(key, fun).getValue();
        } else {
            return tem.getValue();
        }
    }


    @Override
    public void setCache(K key, V v, GaffCacheValueValid valid) {
        String k = getKey(key);
        synchronized (k) {
            _setCache(k, new GaffValue<V>().setValue(v).setValid(valid));
        }
    }

//    private final static Map<String, Object> kmap = new ConcurrentHashMap<>();

//    private Object getLock(String k) {
//        Object obj = kmap.getCache(k);
//    }

    private GaffValue<V> loopUpCache(K key, Fun1<GaffValue<V>> fun) {
        String k = getKey(key);
        synchronized (k) {
            GaffValue<V> tem = getValueAndValid(key);
            if (tem == null) {
                GaffValue<V> gv = new GaffValue<>();
                fun.accept(gv);
                _setCache(k, gv);
                return gv;
            } else return tem;
        }
    }

    private void _setCache(String key, GaffValue<V> v) {
        if (v.getValid() instanceof TimeOutValid timeOutValid) {
            if (timeOutValid.getExpiresIn() != null && timeOutValid.getExpiresIn() >= 0) {
                if (v.getValue() == null) {
                    cache.opsForValue().set(key, v, timeOutValid.getExpiresIn(), TimeUnit.SECONDS);
                } else {
                    cache.opsForValue().set(key, v.getValue(), timeOutValid.getExpiresIn(), TimeUnit.SECONDS);
                }
            } else {
                cache.opsForValue().set(key, v.getValue());
            }
        } else {
            cache.opsForValue().set(key, v);
        }
    }

    @Override
    public void delete(K key) {
        cache.delete(getKey(key));
    }

    @Override
    public void clear() {
        Set<String> keys = cache.keys(PREFIX + "*");
        assert keys != null;
        cache.delete(keys);
    }

    @Override
    protected GaffValue<V> getValueAndValid(K key) {

        GaffValue<V> gv = getGaffValue(key);
        if (gv == null) return null;
        if (gv.getValid() == null) return gv;
        else if (gv.getValid().isValid())
            return gv;
        else {
            delete(key);
            return null;
        }
    }

    @Override
    public void recovery() {
        Set<String> keys = cache.keys(PREFIX + "*");
        assert keys != null;
        keys.forEach(key -> {
            try {
                if (cache.opsForValue().get(key) instanceof GaffValue<?> tem) {
                    if (!Optional.ofNullable(tem.getValid()).map(GaffCacheValueValid::isValid).orElse(true)
                    ) {
                        cache.delete(key);
                    }
                }
            } catch (Exception ignored) {

            }
        });
    }

    @Override
    protected GaffValue<V> getGaffValue(K key) {
        Object tem = cache.opsForValue().get(getKey(key));
        if (tem == null) return null;
        if (tem instanceof GaffValue gv)
            return gv;
        else
            return new GaffValue<V>().setValue((V) tem);
    }


}
