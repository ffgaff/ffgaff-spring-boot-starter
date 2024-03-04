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


@FunctionalInterface
public interface Fun extends Runnable, FunBase<Fun> {

    default void run() {
        callWithRuntimeException();
    }

    void call() throws Exception;

    default void callWithRuntimeException() {
        try {
            call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Fun of(Fun f) {
        return f;
    }

    static void run(Fun f) {
        f.run();
    }


}
