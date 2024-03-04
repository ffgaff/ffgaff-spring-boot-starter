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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Vgs14<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14> implements Vgs {

    private V1 v1;
    private V2 v2;
    private V3 v3;
    private V4 v4;
    private V5 v5;
    private V6 v6;
    private V7 v7;
    private V8 v8;
    private V9 v9;
    private V10 v10;
    private V11 v11;
    private V12 v12;
    private V13 v13;
    private V14 v14;

    @Override
    public Object[] toArray() {
        return new Object[]{v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14};
    }
}