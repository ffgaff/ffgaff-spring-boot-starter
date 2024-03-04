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

package cn.ffgaff.common.lambda.function;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface TestFun2<T, U> extends BiPredicate<T, U>, FunBase<TestFun2<T, U>> {

    default boolean test(T t, U u) {
        return callWithRuntimeException(t, u);
    }

    boolean call(T t, U u) throws Exception;

    default boolean callWithRuntimeException(T t, U u) {
        try {
            return call(t, u);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
