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
module cn.ffgaff {
    requires spring.context;
    requires spring.core;
    requires cn.hutool;
    requires lombok;
    requires org.aspectj.weaver;
    requires spring.expression;
    requires spring.boot.autoconfigure;
    requires spring.data.redis;
    requires com.github.benmanes.caffeine;
    requires org.checkerframework.checker.qual;
    requires org.slf4j;

    exports cn.ffgaff.common.lambda.function;
    exports cn.ffgaff.common.lambda;
    exports cn.ffgaff.common.tools.cache;
    exports cn.ffgaff.common.exception;
    exports cn.ffgaff.spring.boot.cache;
    exports cn.ffgaff.spring.boot.cache.annotation;
}