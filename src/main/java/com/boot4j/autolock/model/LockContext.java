package com.boot4j.autolock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * 锁上下文
 *
 * @author xiongwei.lin
 * @since 2021/02/04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockContext implements Serializable {

    /**
     * 存放rediskey 值
     */
    private String key;

    /**
     * 等待锁的时间，单位(秒)
     */
    private long waitSecond;

    /**
     * 获取到锁，自动释放时间，单位(秒)，默认30S
     */
    private long leaseSecond;

    /**
     * 加锁失败的异常消息
     */
    private String message;

    /**
     * lock root，存放class.method
     */
    private String root;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LockContext context = (LockContext) o;
        return Objects.equals(key, context.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
