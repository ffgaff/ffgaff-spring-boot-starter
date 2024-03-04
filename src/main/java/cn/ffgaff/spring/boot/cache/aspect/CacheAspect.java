

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

package cn.ffgaff.spring.boot.cache.aspect;

import cn.ffgaff.common.lambda.Sef;
import cn.ffgaff.common.tools.cache.GaffCache;
import cn.ffgaff.common.tools.cache.GaffCacheUtil;
import cn.ffgaff.common.tools.cache.TimeOutValid;
import cn.ffgaff.spring.boot.cache.annotation.CacheOPType;
import cn.ffgaff.spring.boot.cache.annotation.ExpiredCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.Objects;

/**
 * 缓存aspect
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class CacheAspect {
    private static final GaffCache<CacheAspectKey, CacheParser> cache = GaffCacheUtil.newBuilder().builder();

    @SneakyThrows
    @Around("@annotation(easyCache)")
    public Object around(ProceedingJoinPoint point, ExpiredCache easyCache) {

        Class<?> baseClass = point.getTarget().getClass();
        if (!easyCache.baseClass().equals(Void.class)) {
            baseClass = easyCache.baseClass();
        }
        MethodSignature me = (MethodSignature) point.getSignature();
        CacheAspectKey key = new CacheAspectKey(baseClass, me, easyCache);
        CacheParser parser = cache.getCache(key, o -> o.setValue(new CacheParser(key)));

        log.debug("aspect::{}", parser.toString());

        return parser.exec(point, easyCache);
    }
}


@Data
class RootClass {
    MethodSignature methodSignature;
    Object target;
    Class<?> targetClass;
    Object[] args;

}

@Slf4j
class CacheParser {

    private final CacheAspectKey key;
    private GaffCache<Object, Object> cache;
    private SpelExpressionParser parser;
    private Expression keyExpression;

    public CacheParser(CacheAspectKey key) {
        this.key = key;
        if (StrUtil.isBlank(key.getBeanName())) {
            cache = SpringUtil.getBean(GaffCache.class);
        } else {
            cache = SpringUtil.getBean(key.getBeanName());
        }
//        if (cache == null) throw new RuntimeException();
        if (key.getIsSpel()) {
            parser = new SpelExpressionParser();
            keyExpression = parser.parseExpression(key.getKey());
        }
    }

    Object exec(ProceedingJoinPoint point, ExpiredCache annotation) throws Throwable {
        Object ckobj;
        if (key.getIsSpel()) {
            CacheEvaluationContext context = new CacheEvaluationContext(point, key);
            ckobj = keyExpression.getValue(context);
        } else {
            ckobj = point.getArgs();
        }
        CacheKey ck = new CacheKey(key, ckobj);
        log.debug("cacheKey {} hash {}", ck, ck.hashCode());
        if (annotation.type() == CacheOPType.get) {
            log.debug("get");
            return cache.getCache(ck, o -> {
                try {
                    o.setValue(point.proceed())
                            .setValid(new TimeOutValid().setExpiresIn(annotation.expire()));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (annotation.type() == CacheOPType.update) {
            log.debug("update");
            Object tem = point.proceed();
            cache.setCache(ck, tem, new TimeOutValid().setExpiresIn(annotation.expire()));
            return tem;
        } else if (annotation.type() == CacheOPType.delete) {
            log.debug("delete");
            cache.delete(ck);
            return point.proceed();
        } else {
            return point.proceed();
        }
    }


}

class CacheEvaluationContext extends StandardEvaluationContext {

    public CacheEvaluationContext(ProceedingJoinPoint point, CacheAspectKey key) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        RootClass rootClass = new RootClass();
        rootClass.setMethodSignature(methodSignature);
        rootClass.setArgs(point.getArgs());
        rootClass.setTarget(point.getTarget());
        rootClass.setTargetClass(point.getTarget().getClass());
        if (rootClass.getArgs().length > 0) {
            String[] names = methodSignature.getParameterNames();
            for (int i = 0; i < names.length; i++) {
                setVariable(names[i], rootClass.getArgs()[i]);
            }
        }
        setRootObject(rootClass);
    }

    @Override
    public Object lookupVariable(String name) {
        return super.lookupVariable(name);
    }
}

class CacheAspectKey implements Comparable<CacheAspectKey> {
    private final Class<?> tagerClass;
    private final MethodSignature methodSignature;
    private final ExpiredCache cacheAnnotation;
    private String className;
    private String cacheName;
    private String key;

    private Boolean isSpel;

    private String string;

    ExpiredCache getCacheAnnotation() {
        return cacheAnnotation;
    }

    String getClassName() {
        if (className == null) {
            className = tagerClass.getName();
        }
        return className;
    }

    String getBeanName() {
        return cacheAnnotation.cacheBeanName();
    }

    String getCacheName() {
        if (cacheName == null)
            if (StrUtil.isBlank(cacheAnnotation.cacheName())) {
                cacheName = methodSignature.getName();
            } else {
                cacheName = cacheAnnotation.cacheName();
            }

        return cacheName;
    }

    String getKey() {
        if (key == null)
            if (StrUtil.isBlank(cacheAnnotation.key())) {
                isSpel = false;
                String[] names = methodSignature.getParameterNames();
                key = Sef.ofArray(methodSignature.getParameterTypes())
                        .mapIndex((o, i) ->
                                StrUtil.format("{} {}", o.getName(), names[i])
                        ).joining(",", "(", ")")
                ;
            } else {
                isSpel = true;
                key = cacheAnnotation.key();
            }
        return key;
    }

    Boolean getIsSpel() {
        if (isSpel == null)
            getKey();
        return isSpel;
    }

    CacheAspectKey(Class<?> tagerClass, MethodSignature methodSignature, ExpiredCache key) {
        this.tagerClass = tagerClass;
        this.methodSignature = methodSignature;
        this.cacheAnnotation = key;

    }

    @Override
    public int hashCode() {
        return this.tagerClass.hashCode() * 11 + toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheAspectKey other) {
            return getBeanName().equals(other.getBeanName()) && getClassName().equals(other.getClassName())
                    && getCacheName().equals(other.getCacheName()) && getKey().equals(other.getKey());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(CacheAspectKey o) {
        int bj = getBeanName().compareTo(o.getBeanName());
        if (0 == bj) {
            bj = getClassName().compareTo(o.getClassName());
            if (0 == bj) {
                bj = getCacheName().compareTo(o.getCacheName());
                if (0 == bj) {
                    bj = getKey().compareTo(o.getKey());
                }
            }
        }
        return bj;
    }

    @Override
    public String toString() {
        if (string == null)
            string = Sef.of(getClassName(), getCacheName(), getKey()).joining(".");
        return string;
    }
}

class CacheKey {

    private final CacheAspectKey cacheAspectKey;

    private final Object key;

    CacheKey(CacheAspectKey cacheAspectKey, Object key) {
        this.cacheAspectKey = cacheAspectKey;
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheKey other) {
            return Objects.deepEquals(key, other.key) && cacheAspectKey.equals(other.cacheAspectKey);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code;
        if (this.key instanceof Object[] arr) {
            code = Arrays.hashCode(arr);
        } else {
            code = Objects.hashCode(this.key);
        }
        return cacheAspectKey.hashCode() * 20 + code;
    }

    @Override
    public String toString() {
        return StrUtil.format("{}:{}", cacheAspectKey, key);
    }
}