package com.boot4j.autolock.util;

import com.boot4j.autolock.exception.LockException;
import com.boot4j.autolock.starter.LockAutoConfiguration;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 锁工具
 *
 * @author Great
 * @since 2021/02/09
 */
public class LockUtils {

    /**
     * 获取Redisson客户端实例
     * @return 返回redisson对象
     */
    public static RedissonClient getRedisson() {
        return LockAutoConfiguration.getBean(RedissonClient.class);
    }

    /**
     * 加锁（阻塞）
     * @param key redis key
     * @param leaseSecond 获取锁后，自动释放时间，单位秒
     */
    public static void blockLock(String key, long leaseSecond) {
        getRedisson().getFairLock(key).lock(leaseSecond, TimeUnit.SECONDS);
    }

    /**
     * 加锁
     * @param key redis key
     * @param waitSecond  取不到锁后，等待锁的时间，单位秒
     * @param leaseSecond 获取锁后，自动释放时间，单位秒
     * @return true成功 false失败
     */
    public static boolean tryLock(String key, long waitSecond, long leaseSecond) {
        try {
            RLock rLock = getRedisson().getFairLock(key);
            return rLock.tryLock(waitSecond, leaseSecond, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new LockException("Try lock exception", e);
        }
    }

    /**
     * 解锁
     * @param key redis key
     */
    public static void unlock(String key) {
        RLock rLock = getRedisson().getFairLock(key);
        if (rLock != null) {
            rLock.unlock();
        }
    }

}
