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

import cn.hutool.core.util.ReflectUtil;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;


public interface FunBase<F extends FunBase<F>> extends Serializable {

//    default F getFun() {
//        return (F) FunBase.this;
//    }

    default SerializedLambda getSerializedLambda() {
        return ReflectUtil.invoke(FunBase.this, "writeReplace");
    }

}
