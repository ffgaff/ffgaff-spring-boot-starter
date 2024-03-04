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

package cn.ffgaff.common.tools.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;


@Accessors(chain = true)
@Slf4j
public class TimeOutValid implements GaffCacheValueValid {

    public TimeOutValid() {
        this.createTime = LocalDateTime.now();
    }

    private final LocalDateTime createTime;

    @Setter
    @Getter
    private Long expiresIn;

    @Override
    public boolean isValid() {
        log.debug("call valid:{}", expiresIn);
        return Optional.ofNullable(expiresIn).filter(o -> o >= 0).map(o ->
                createTime.plusSeconds(expiresIn).isAfter(LocalDateTime.now())
        ).orElse(true);
    }
}
