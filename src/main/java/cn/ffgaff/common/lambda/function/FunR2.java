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

import java.util.function.BiFunction;


@FunctionalInterface
public interface FunR2<P1, P2, R> extends BiFunction<P1, P2, R>, FunBase<FunR2<P1, P2, R>> {

    @Override
    default R apply(P1 p1, P2 p2) {
        return callWithRuntimeException(p1, p2);
    }

    R call(P1 p1, P2 p2) throws Exception;

    default R callWithRuntimeException(P1 p1, P2 p2) {
        try {
            return call(p1, p2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <P1, P2, R> FunR2<P1, P2, R> of(FunR2<P1, P2, R> f) {
        return f;
    }
}
