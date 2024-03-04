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

package cn.ffgaff.common.tools.cache;


import cn.ffgaff.common.lambda.function.Fun1;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;

import java.time.Duration;
import java.util.Optional;

public class GaffCaffeineCache<K, V> extends GaffAbstractCache<K, V> {


    private final Cache<K, GaffValue<V>> cache;

    public GaffCaffeineCache() {
        cache = Caffeine.newBuilder().expireAfter(new Exp()).build();
    }

    public GaffCaffeineCache(Caffeine<?, ?> caffeine) {
        cache = caffeine.expireAfter(new Exp()).build();
    }

    @Override
    public V getCache(K key) {
        return Optional.ofNullable(cache.getIfPresent(key)).map(GaffValue::getValue).orElse(null);
    }

    //
    @Override
    public V getCache(K key, Fun1<GaffValue<V>> fun) {
        return Optional.ofNullable(this.cache.get(key, o -> {
            GaffValue<V> gv = new GaffValue<>();
            fun.accept(gv);
            return gv;
        })).map(GaffValue::getValue).orElse(null);

//        return Optional.ofNullable(cache.getIfPresent(key)).map(GaffValue::getValue)
//                .orElseGet(() -> {
//                    GaffValue<V> gv = new GaffValue<>();
//                    fun.accept(gv);
//                    cache.put(key, gv);
//                    return gv.getValue();
//                });

    }


    @Override
    public void setCache(K key, V v, GaffCacheValueValid valid) {
        cache.put(key, new GaffValue<V>().setValue(v).setValid(valid));
    }

    @Override
    public void delete(K key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
        cache.cleanUp();
    }

    @Override
    public void recovery() {
        cache.cleanUp();
    }

    @Override
    protected GaffValue<V> getGaffValue(K key) {
        return null;
    }

    //    @Slf4j
    static class Exp<K, V> implements Expiry<K, GaffValue<V>> {
        @Override
        public long expireAfterCreate(K key, GaffValue<V> value, long currentTime) {
            if (value.getValid() instanceof TimeOutValid valid && valid.getExpiresIn() != null && valid.getExpiresIn() >= 0) {
//                nanos
                return Duration.ofSeconds(valid.getExpiresIn()).toNanos();
            } else {
                return Long.MAX_VALUE;
            }


        }

        @Override
        public long expireAfterUpdate(K key, GaffValue<V> value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }


        @Override
        public long expireAfterRead(K key, GaffValue<V> value, long currentTime, @NonNegative long currentDuration) {
//            log.debug("expireAfterRead: {}", value);
            if (Optional.ofNullable(value.getValid()).map(GaffCacheValueValid::isValid).orElse(true)) {
                return currentDuration;
            } else {
                return 0;
            }

        }
    }

}
