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

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SefImpl<T> implements Sef<T> {

    private final Stream<? extends T> stream;

    //    private Object[]                  buffered;
    SefImpl(Stream<? extends T> stream) {
        this.stream = stream.sequential();
    }

    SefImpl(Iterator<? extends T> iterator) {
        stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    @Override
    public Stream<T> stream() {
        return (Stream<T>) stream;
    }

}
