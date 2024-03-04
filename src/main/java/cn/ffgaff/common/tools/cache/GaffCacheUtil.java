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

import lombok.SneakyThrows;

public class GaffCacheUtil {


    public static GaffCacheBuilder newBuilder() {
        return new GaffCacheBuilder();
    }

    public static class GaffCacheBuilder {

        private Class<?> cacheClass;

        public <T extends GaffCache> GaffCacheBuilder setCacheClass(Class<T> cacheClass) {
            this.cacheClass = cacheClass;
            return this;
        }

        @SneakyThrows
        public <K, V> GaffCache<K, V> builder() {
            if (cacheClass == null) cacheClass = GaffCaffeineCache.class;
            return (GaffCache<K, V>) cacheClass.getDeclaredConstructor().newInstance();

        }
    }

}
