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

package cn.ffgaff.common.lambda;

import cn.hutool.core.util.ArrayUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 数组操作扩展
 *
 * @param <T>
 */
public interface Aef<T> extends List<T>, AutoCloseable {

    /**
     * 获取Stream扩展
     *
     * @return
     */
    Sef<T> sef();

    default Aef<T> reverse() {
        Collections.reverse(Aef.this);
        return Aef.this;
    }

    default Aef<T> append(T t) {
        add(t);
        return Aef.this;
    }

    default void close() {
        Aef.this.reverse().sef().forEachTry(o -> {
            if (o instanceof AutoCloseable autoCloseable) {
                autoCloseable.close();
            }
        });
        Aef.this.clear();
    }

    @SafeVarargs
    static <T> Aef<T> of(T... t) {
        if (ArrayUtil.isEmpty(t)) return empty();
        AefImpl<T> tem = new AefImpl<>();
        Collections.addAll(tem, t);
        return tem;
    }

    static <T> Aef<T> of(Collection<T> t) {
        return new AefImpl<>(t);
    }

    static <T> Aef<T> empty() {
        return new AefImpl<>();
    }


}
