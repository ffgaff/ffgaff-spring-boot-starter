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
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GaffThreadLocalCache<K, V> extends GaffAbstractCache<K, V> {

    //    private final Map<K, G2Value<V>> map;
    private static final ThreadLocal<Map> caches =
            new NamedThreadLocal<>("ThreadLocalCache");


    private Map<K, GaffValue<V>> getMap() {
        return Optional.ofNullable(
                        caches.get())
                .orElseGet(() ->
                {

                    log.debug("new ThreadLocalCache");
                    caches.set(new HashMap<>());
                    return caches.get();
                });
    }

    public GaffThreadLocalCache() {

    }

    @Override
    protected GaffValue<V> getGaffValue(K key) {
        return getMap().get(key);
    }

    @Override
    public V getCache(K key) {
        return Optional.ofNullable(getValueAndValid(key)).map(GaffValue::getValue).orElse(null);
    }

    @Override
    public V getCache(K key, Fun1<GaffValue<V>> fun) {
        return Optional.ofNullable(getValueAndValid(key)).map(GaffValue::getValue).orElseGet(() -> {
            GaffValue<V> gv = new GaffValue<>();
            fun.accept(gv);
            getMap().put(key, gv);
            return gv.getValue();
        });
    }

    @Override
    public void setCache(K key, V v, GaffCacheValueValid valid) {
        getMap().put(key, new GaffValue<V>().setValue(v).setValid(valid));
    }

    @Override
    public void delete(K key) {
        getMap().remove(key);
    }

    @Override
    public void clear() {
        getMap().clear();
    }

}
