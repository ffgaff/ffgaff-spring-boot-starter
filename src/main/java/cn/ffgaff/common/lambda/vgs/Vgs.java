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

package cn.ffgaff.common.lambda.vgs;

import cn.ffgaff.common.lambda.Aef;

import java.util.Objects;

public interface Vgs {


    Object[] toArray();

    default Aef<Object> toAef() {
        return Aef.of(toArray());
    }

    /**
     * 创建一个包含一个参数的变量组
     *
     * @param v1   参数1
     * @param <V1> 参数1的类型
     * @return 一个参数的变量组实例
     */
    static <V1> Vgs1<V1> create(V1 v1) {
        return new Vgs1<>(v1);
    }

    /**
     * 创建一个包含两个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @return 一个包含两个参数的变量组实例
     */
    static <V1, V2> Vgs2<V1, V2> create(V1 v1, V2 v2) {
        return new Vgs2<>(v1, v2);
    }


    /**
     * 创建一个包含三个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @return 一个包含三个参数的变量组实例
     */
    static <V1, V2, V3> Vgs3<V1, V2, V3> create(V1 v1, V2 v2, V3 v3) {
        return new Vgs3<>(v1, v2, v3);
    }


    /**
     * 创建一个包含四个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @return 一个包含四个参数的变量组实例
     */
    static <V1, V2, V3, V4> Vgs4<V1, V2, V3, V4> create(V1 v1, V2 v2, V3 v3, V4 v4) {
        return new Vgs4<>(v1, v2, v3, v4);
    }

    /**
     * 创建一个包含五个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param v5   参数5
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @param <V5> 参数5的类型
     * @return 一个包含五个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5> Vgs5<V1, V2, V3, V4, V5> create(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5) {
        return new Vgs5<>(v1, v2, v3, v4, v5);
    }

    /**
     * 创建一个包含六个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param v5   参数5
     * @param v6   参数6
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @param <V5> 参数5的类型
     * @param <V6> 参数6的类型
     * @return 一个包含六个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6> Vgs6<V1, V2, V3, V4, V5, V6> create(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6) {
        return new Vgs6<>(v1, v2, v3, v4, v5, v6);
    }


    /**
     * 创建一个包含七个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param v5   参数5
     * @param v6   参数6
     * @param v7   参数7
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @param <V5> 参数5的类型
     * @param <V6> 参数6的类型
     * @param <V7> 参数7的类型
     * @return 一个包含七个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7> Vgs7<V1, V2, V3, V4, V5, V6, V7> create(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) {
        return new Vgs7<>(v1, v2, v3, v4, v5, v6, v7);
    }

    /**
     * 创建一个包含八个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param v5   参数5
     * @param v6   参数6
     * @param v7   参数7
     * @param v8   参数8
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @param <V5> 参数5的类型
     * @param <V6> 参数6的类型
     * @param <V7> 参数7的类型
     * @param <V8> 参数8的类型
     * @return 一个包含八个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8> Vgs8<V1, V2, V3, V4, V5, V6, V7, V8> create(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8) {
        return new Vgs8<>(v1, v2, v3, v4, v5, v6, v7, v8);
    }

    /**
     * 创建一个包含九个参数的变量组
     *
     * @param v1   参数1
     * @param v2   参数2
     * @param v3   参数3
     * @param v4   参数4
     * @param v5   参数5
     * @param v6   参数6
     * @param v7   参数7
     * @param v8   参数8
     * @param v9   参数9
     * @param <V1> 参数1的类型
     * @param <V2> 参数2的类型
     * @param <V3> 参数3的类型
     * @param <V4> 参数4的类型
     * @param <V5> 参数5的类型
     * @param <V6> 参数6的类型
     * @param <V7> 参数7的类型
     * @param <V8> 参数8的类型
     * @param <V9> 参数9的类型
     * @return 一个包含九个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9> Vgs9<V1, V2, V3, V4, V5, V6, V7, V8, V9> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9) {
        return new Vgs9<>(v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }

    /**
     * 创建一个包含十个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @return 一个包含十个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> Vgs10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10) {
        return new Vgs10<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
    }

    /**
     * 创建一个包含十一个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @return 一个包含十一个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> Vgs11<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11) {
        return new Vgs11<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11);
    }

    /**
     * 创建一个包含十二个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param v12   参数12
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @param <V12> 参数12的类型
     * @return 一个包含十二个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> Vgs12<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12) {
        return new Vgs12<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12);
    }

    /**
     * 创建一个包含十三个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param v12   参数12
     * @param v13   参数13
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @param <V12> 参数12的类型
     * @param <V13> 参数13的类型
     * @return 一个包含十三个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> Vgs13<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9,
            V10 v10, V11 v11, V12 v12, V13 v13) {
        return new Vgs13<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13);
    }


    /**
     * 创建一个包含十四个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param v12   参数12
     * @param v13   参数13
     * @param v14   参数14
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @param <V12> 参数12的类型
     * @param <V13> 参数13的类型
     * @param <V14> 参数14的类型
     * @return 一个包含十四个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> Vgs14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9,
            V10 v10, V11 v11, V12 v12, V13 v13, V14 v14) {
        return new Vgs14<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14);
    }

    /**
     * 创建一个包含十五个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param v12   参数12
     * @param v13   参数13
     * @param v14   参数14
     * @param v15   参数15
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @param <V12> 参数12的类型
     * @param <V13> 参数13的类型
     * @param <V14> 参数14的类型
     * @param <V15> 参数15的类型
     * @return 一个包含十五个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> Vgs15<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9,
            V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15) {
        return new Vgs15<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);
    }


    /**
     * 创建一个包含十六个参数的变量组
     *
     * @param v1    参数1
     * @param v2    参数2
     * @param v3    参数3
     * @param v4    参数4
     * @param v5    参数5
     * @param v6    参数6
     * @param v7    参数7
     * @param v8    参数8
     * @param v9    参数9
     * @param v10   参数10
     * @param v11   参数11
     * @param v12   参数12
     * @param v13   参数13
     * @param v14   参数14
     * @param v15   参数15
     * @param v16   参数16
     * @param <V1>  参数1的类型
     * @param <V2>  参数2的类型
     * @param <V3>  参数3的类型
     * @param <V4>  参数4的类型
     * @param <V5>  参数5的类型
     * @param <V6>  参数6的类型
     * @param <V7>  参数7的类型
     * @param <V8>  参数8的类型
     * @param <V9>  参数9的类型
     * @param <V10> 参数10的类型
     * @param <V11> 参数11的类型
     * @param <V12> 参数12的类型
     * @param <V13> 参数13的类型
     * @param <V14> 参数14的类型
     * @param <V15> 参数15的类型
     * @param <V16> 参数16的类型
     * @return 一个包含十六个参数的变量组实例
     */
    static <V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16> Vgs16<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15, V16> create(
            V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9,
            V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15, V16 v16) {
        return new Vgs16<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16);
    }
}
