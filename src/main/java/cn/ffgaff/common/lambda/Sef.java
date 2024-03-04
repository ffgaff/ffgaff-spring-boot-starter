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

import cn.ffgaff.common.exception.ForEachException;
import cn.ffgaff.common.lambda.function.*;
import cn.ffgaff.common.lambda.vgs.Vgs;
import cn.ffgaff.common.lambda.vgs.Vgs2;
import cn.hutool.core.util.ArrayUtil;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Stream 扩展
 *
 * @param <T>
 */

public interface Sef<T> extends Stream<T>, Iterable<T> {
    Stream<T> stream();

    static <T> Sef<T> empty() {
        return l(Stream.empty());
    }

    static <T> Sef<T> l(Stream<? extends T> stream) {
//        return switch (stream) {
//            case IColl c -> c;
//            case Stream str -> IColl.l(str);
//            default -> IColl.empty();
//        };
        if (stream == null) return Sef.empty();
        if (stream instanceof Sef<? extends T> c) return (Sef<T>) c;
        return new SefImpl<>(stream);
    }

    static Sef<Number> range(int startInclusive, int endExclusive) {
        return Sef.l(IntStream.range(startInclusive, endExclusive).boxed());
    }

    static <T> Sef<T> of(Collection<T> coll) {
        return l(coll.stream());
    }

    static <T> Sef<T> of(Spliterator<T> spliterator) {
        return l(StreamSupport.stream(spliterator, false));
    }

    @SafeVarargs
    static <T> Sef<T> of(T... t) {
        return l(Arrays.stream(t));
    }

    static <T> Sef<T> ofArray(T[] t) {
        return l(Arrays.stream(t));
    }


//    default boolean contains(Object o) {
//        return anyMatch(Predicate.isEqual(o));
//    }
//
//    default boolean containsAll(IColl<? extends T> other) {
//        Set<? extends T> set = other.toSet(HashSet::new);
//        return set.isEmpty() ? true : filter(t -> set.remove(t)).anyMatch(t -> set.isEmpty());
//    }


    default String joining() {
        return stream().map(String::valueOf).collect(Collectors.joining());
    }

    default String joining(CharSequence delimiter) {
        return map(String::valueOf).collect(Collectors.joining(delimiter));
    }

    default String joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return map(String::valueOf).collect(Collectors.joining(delimiter, prefix, suffix));
    }

    default <U> Sef<Vgs2<T, U>> joinLeft(Sef<? extends U> r, TestFun2<? super T, ? super U> testFun2) {
        Buffer<? extends U> buff = Buffer.of(r);
        return flatMap(t -> buff.sef().filter(u -> testFun2.test(t, u))
                .map(u -> Vgs.<T, U>create(t, u)))
                .onClose(r::close);

    }

    default Aef<T> toAef() {
        return stream().collect(Collectors.toCollection(Aef::empty));
    }

    default <R, T2> Sef<R> zip(Iterator<T2> right, T defaultLeft, T2 defaultRight, FunR2<T, T2, R> funR2) {
        Iterator<T> it1 = stream().iterator();
        Iterator<T2> it2 = right;

        class z implements Iterator<R> {

            @Override
            public boolean hasNext() {
                return it1.hasNext() || it2.hasNext();
            }

            @Override
            public R next() {
                boolean ln = it1.hasNext();
                boolean rn = it2.hasNext();

                if (ln || rn) {
                    return funR2.apply(ln ? it1.next() : defaultLeft, rn ? it2.next() : defaultRight);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
        return new SefImpl<R>(new z()).onClose(() -> {
            stream().close();
        });
    }

    default <R, T2> Sef<R> zip(Iterator<T2> right, FunR2<T, T2, R> funR2) {
        return zip(right, null, null, funR2);
    }

    default <R, T2> Sef<R> zip(Stream<T2> right, FunR2<T, T2, R> funR2) {
        return zip(right.iterator(), null, null, funR2);
    }

    default <R, T2> Sef<R> zip(Stream<T2> right, T defaultLeft, T2 defaultRight, FunR2<T, T2, R> funR2) {
        return zip(right.iterator(), defaultLeft, defaultRight, funR2);
    }

    @Override
    default Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    default Sef<T> sequential() {
        return this;
    }

    @Override
    default Sef<T> parallel() {
        return this;
    }

    @Override
    default Sef<T> unordered() {
        return this;
    }

    @Override
    default Sef<T> filter(Predicate<? super T> predicate) {
        return Sef.l(stream().filter(predicate));
    }

    default Optional<T> findFirst(TestFun1<? super T> predicate) {
        return stream().filter(predicate).findFirst();
    }

    default Optional<T> findFirst(TestFun2<? super T, Integer> predicate) {
        final int[] i = {0};
        return findFirst(t -> predicate.test(t, i[0]++));
    }

    @Override
    default <R> Sef<R> map(Function<? super T, ? extends R> mapper) {
        return Sef.l(stream().map(mapper));
    }


    default <R> Sef<R> mapIndex(FunR2<? super T, Integer, ? extends R> mapper) {
        final int[] i = {0};
        return Sef.l(stream().map(o -> mapper.apply(o, i[0]++)));
    }


    default <R> Sef<R> map(Class<R> rClass) {
        return map(rClass::cast);
    }

    @Override
    default IntStream mapToInt(ToIntFunction<? super T> mapper) {
        return stream().mapToInt(mapper);
    }

    @Override
    default LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream().mapToLong(mapper);
    }

    @Override
    default DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream().mapToDouble(mapper);
    }

    @Override
    default <R> Sef<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return Sef.l(stream().flatMap(mapper));
    }


    default <R> Sef<R> flatMapIndex(FunR2<? super T, Integer, ? extends Stream<? extends R>> mapper) {
        final int[] i = {0};
        return Sef.l(stream().flatMap(o -> mapper.apply(o, i[0]++)));
    }


    @Override
    default IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
        return stream().flatMapToInt(mapper);
    }

    @Override
    default LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
        return stream().flatMapToLong(mapper);
    }

    @Override
    default DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
        return stream().flatMapToDouble(mapper);
    }

    @Override
    default Sef<T> distinct() {
        return Sef.l(stream().distinct());
    }

    @Override
    default Sef<T> sorted() {
        return Sef.l(stream().sorted());
    }

    @Override
    default Sef<T> sorted(Comparator<? super T> comparator) {
        return Sef.l(stream().sorted(comparator));
    }

    @Override
    default Sef<T> peek(Consumer<? super T> action) {
        return Sef.l(stream().peek(action));
    }

    @Override
    default Sef<T> limit(long maxSize) {
        return Sef.l(stream().limit(maxSize));
    }

    @Override
    default Sef<T> skip(long n) {
        return Sef.l(stream().skip(n));
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        stream().forEach(action);
    }

    default void forEachTry(Fun1<? super T> action) {
        ForEachException exception = new ForEachException();
        stream().forEach(o -> {
            try {
                action.call(o);
            } catch (Exception e) {
                exception.appendError(e);
            }
        });
        if (exception.isError()) throw exception;
    }


    default void forEachIndex(Fun2<? super T, Integer> action) {
        final int[] i = {0};
        stream().forEach(o -> action.accept(o, i[0]++));
    }

    @Override
    default void forEachOrdered(Consumer<? super T> action) {
        stream().forEachOrdered(action);
    }

    @Override
    default Object[] toArray() {
        return stream().toArray();
    }

    @Override
    default <A> A[] toArray(IntFunction<A[]> generator) {
        return stream().toArray(generator);
    }

    default <A> A[] toArray(Class<A> aClass) {
        return stream().toArray(i -> ArrayUtil.newArray(aClass, i));
    }


    @Override
    default T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream().reduce(identity, accumulator);
    }

    @Override
    default Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream().reduce(accumulator);
    }

    @Override
    default <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream().reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return stream().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream().collect(collector);
    }

    default <R> R convert(Class<R> c, Fun2<R, ? super T> fun) {
        return convert(FunR.of(() -> c.getDeclaredConstructor().newInstance()), fun);
    }

    default <R> R convert(FunR<R> get, Fun2<R, ? super T> fun) {
        return stream().collect(get, fun, (l, r) -> {
        });
    }

    default <R> R convert(FunR1<Sef<T>, R> fun) {
        return fun.apply(this);
    }

    @Override
    default Optional<T> min(Comparator<? super T> comparator) {
        return stream().min(comparator);
    }

    @Override
    default Optional<T> max(Comparator<? super T> comparator) {
        return stream().max(comparator);
    }

    @Override
    default long count() {
        return stream().count();
    }

    @Override
    default boolean anyMatch(Predicate<? super T> predicate) {
        return stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(Predicate<? super T> predicate) {
        return stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(Predicate<? super T> predicate) {
        return stream().noneMatch(predicate);
    }

    @Override
    default Optional<T> findFirst() {
        return stream().findFirst();
    }

    @Override
    default Optional<T> findAny() {
        return stream().findAny();
    }


    @Override
    default Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    default boolean isParallel() {
        return stream().isParallel();
    }

    @Override
    default Sef<T> onClose(Runnable closeHandler) {
        return Sef.l(stream().onClose(closeHandler));
    }

    @Override
    default void close() {
        stream().close();
    }


//    static List

    final class Buffer<T> {

        static <T> Buffer<T> of(Stream<? extends T> stream) {
            return of((Spliterator<T>) stream.spliterator());
        }

        static <T> Buffer<T> of(Spliterator<T> spliterator) {
            if (spliterator instanceof Sef.Buffer.BufferSpliterator buff) {
                return buff.parentSeqBuffer();
            } else {
                return new Buffer<>(spliterator);
            }
        }

        private final Spliterator<T> source;
        private final Aef<T> buffer = Aef.empty();
        private volatile boolean buffering = true;

        Buffer(Spliterator<T> spliterator) {
            this.source = Objects.requireNonNull(spliterator);
        }

        Sef<T> sef() {
            return Sef.of(new BufferSpliterator());
        }

        private class BufferSpliterator implements Spliterator<T> {
            private int nextIndex = 0;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                final T t;
                if (buffering) {
                    synchronized (buffer) {
                        if (!canAdvance()) return false;
                        t = getOne();
                    }
                } else {
                    if (!canAdvanceBuffer()) return false;
                    t = getOne();
                }
                action.accept(t);
                return true;
            }

            private T getOne() {
                return buffer.get(nextIndex++);
            }

            private boolean canAdvance() {
                return canAdvanceBuffer() || tryAdvanceSource() && canAdvanceBuffer();
            }

            private boolean canAdvanceBuffer() {
                return nextIndex < buffer.size();
            }

            private boolean tryAdvanceSource() {

                boolean advanceSource = buffering;
                if (!advanceSource)
                    return false;
                do
                    advanceSource = source.tryAdvance(buffer::add);
                while (advanceSource && !canAdvanceBuffer());
                buffering = advanceSource;
                return true;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            private int estmateSizeForBuffer() {
                return buffer.size() - nextIndex;
            }

            @Override
            public long estimateSize() {
                if (buffering) {
                    synchronized (buffer) {
                        int buff = estmateSizeForBuffer();
                        if (!buffering) return buff;
                        long size = buff + source.estimateSize();
                        return size >= 0 ? size : Long.MAX_VALUE;
                    }
                } else {
                    return estmateSizeForBuffer();
                }
            }

            @Override
            public int characteristics() {
                return (source.characteristics() & ~Spliterator.CONCURRENT) | Spliterator.ORDERED
                        | (buffering ? 0 : Spliterator.SIZED);
            }

            @Override
            public Comparator<? super T> getComparator() {
                return Spliterator.super.getComparator();
            }

            Buffer<T> parentSeqBuffer() {
                return Buffer.this;
            }
        }
    }
}
