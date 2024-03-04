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

import java.util.function.BiConsumer;


@FunctionalInterface
public interface Fun2<P1, P2> extends BiConsumer<P1, P2>, FunBase<Fun2<P1, P2>> {

    @Override
    default void accept(P1 p1, P2 p2) {
        callWithRuntimeException(p1, p2);
    }

    void call(P1 p1, P2 p2) throws Exception;

    default void callWithRuntimeException(P1 p1, P2 p2) {
        try {
            call(p1, p2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
