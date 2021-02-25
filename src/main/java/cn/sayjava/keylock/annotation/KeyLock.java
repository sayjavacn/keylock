package cn.sayjava.keylock.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 分布式锁，注解到方法上
 *
 * @author Great
 * @since 2021/02/03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KeyLock {

    @AliasFor("key")
    String value() default "";
    /**
     * 生成rediskey规则，使用SpringEL表达式，如无指定则默认锁定方法
     * 注解：@KeyLock(key="#s.name+#s.name")
     * 加前缀：@KeyLock(key="'前缀'+#s.name+#s.name")
     * @return 返回KEY值
     */
    @AliasFor("value")
    String key() default "";

    /**
     * @return 等待锁的时间，单位(秒)
     */
    long waitSecond() default 0L;

    /**
     * @return 锁自动释放时间，单位(秒)
     * 设置-1会进行锁续期，默认释放时间是30S
     */
    long leaseSecond() default -1L;

    /**
     * @return 获取不到锁提示
     */
    String message() default "系统繁忙，请稍后再试！";

}
