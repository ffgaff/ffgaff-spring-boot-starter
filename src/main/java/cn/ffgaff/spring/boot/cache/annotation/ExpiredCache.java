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

package cn.ffgaff.spring.boot.cache.annotation;


import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExpiredCache {

    /**
     * SpEL表达式,用于动态计算密钥表达式，默认为方法所有参数组合
     * <ul>
     * <li> 基础用法
     * <pre>
     *         &#064;EasyCache(key = "#par")
     *         &#064;EasyCache(key = "#par.url")
     *     </pre>
     * </li>
     * <li> 进阶用法<br>
     *   {@code #root.target}, and {@code #root.methodSignature}
     * 目标对象和方法的引用
     * </li>
     * </ul>
     *
     * @return {String}
     */
    String key() default "";

    /**
     * 基类，默认使用Cache的类。
     */
    Class<?> baseClass() default Void.class;


    /**
     * 缓存名称，默认方法名
     *
     * @return
     */
    String cacheName() default "";


    /**
     * 使用的缓存Bean名称  使用{@link cn.ffgaff.common.tools.cache.GaffCache}类型bean
     *
     * @return
     */
    String cacheBeanName() default "";


    /**
     * 操作类型 {@link CacheOPType}
     *
     * @return
     */
    CacheOPType type() default CacheOPType.get;

    /**
     * 过期时间 -1表示永不过期
     *
     * @return
     */
    long expire() default 1200;


}
