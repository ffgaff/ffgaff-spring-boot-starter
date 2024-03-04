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

import java.util.Optional;
import java.util.function.Function;


@FunctionalInterface
public interface FunR1<P, R> extends Function<P, R>, FunBase<FunR1<P, R>> {

    @Override
    default R apply(P t) {
        return callWithRuntimeException(t);
    }

    R call(P t) throws Exception;

    default R callWithRuntimeException(P parameter) {
        try {
            return call(parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default R call(P p, R def) {
        return Optional.ofNullable(p).map(this::callWithRuntimeException).orElse(def);
    }

    static <P, R> FunR1<P, R> of(FunR1<P, R> f) {
        return f;
    }
}
