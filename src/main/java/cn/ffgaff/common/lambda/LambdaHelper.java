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

import cn.ffgaff.common.lambda.function.FunBase;
import cn.ffgaff.common.tools.cache.GaffCache;
import cn.ffgaff.common.tools.cache.GaffCacheUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.util.ClassUtil;

import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;

public class LambdaHelper {
    private static final GaffCache<String, SerializedLambda> cache = GaffCacheUtil.newBuilder().builder();


    public static <R> Class<R> getRealClass(FunBase<?> func) {
        final SerializedLambda lambda = resolve(func);
        checkLambdaTypeCanGetClass(lambda.getImplMethodKind());
        return ClassUtil.loadClass(lambda.getImplClass());
    }


    public static String getMethodName(FunBase<?> func) {
        return resolve(func).getImplMethodName();
    }

    public static String getFieldName(FunBase<?> func) throws IllegalArgumentException {
        return BeanUtil.getFieldName(getMethodName(func));
    }

    public static SerializedLambda resolve(FunBase<?> func) {
        return _resolve(func);
    }


    private static SerializedLambda _resolve(FunBase<?> func) {
        return cache.getCache(func.getClass().getName(), (key) -> key.setValue(func.getSerializedLambda()));
    }

    private static void checkLambdaTypeCanGetClass(int implMethodKind) {
        if (implMethodKind != MethodHandleInfo.REF_invokeVirtual &&
                implMethodKind != MethodHandleInfo.REF_invokeStatic) {
            throw new IllegalArgumentException("该lambda不是合适的方法引用");
        }
    }
}
