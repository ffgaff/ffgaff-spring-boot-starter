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

public abstract class GaffAbstractCache<K, V> implements GaffCache<K, V> {


    protected abstract GaffValue<V> getGaffValue(K key);

    protected GaffValue<V> getValueAndValid(K key) {

        GaffValue<V> gv = getGaffValue(key);
        if (gv == null) return remove(key);
        if (gv.getValid() == null) return gv;
        else
            return gv.getValid().isValid() ? gv : remove(key);
    }

    private GaffValue<V> remove(K key) {
        delete(key);
        return null;
    }

}
