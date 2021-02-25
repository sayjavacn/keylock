package cn.sayjava.keylock.aspect;

import cn.sayjava.keylock.annotation.AutoLock;
import cn.sayjava.keylock.model.LockContext;
import cn.sayjava.keylock.util.LockStack;
import cn.sayjava.keylock.util.LockUtils;
import cn.sayjava.keylock.exception.LockException;
import cn.sayjava.keylock.util.LockAspectUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 并发请求校验 用于处理并发请求时幂等失效问题
 * @author Great
 * @since 2021/02/03
 */
@Aspect
@Component
@Slf4j
public class LockAspect {

    /**
     * 线程参数传递使用
     * key:LockContext
     */
    private static final ThreadLocal<LockStack> LOCK_THREAD = new ThreadLocal<LockStack>() {
        @Override
        protected LockStack initialValue() {
            return new LockStack();
        }
    };

    @Pointcut("@annotation(cn.sayjava.keylock.annotation.AutoLock)")
    public void pointCut() {
    }

    /**
     * 切面前置处理，使用redis锁处理并发请求
     *
     * @param joinPoint 封装了代理方法信息的对象
     * @param autoLock 锁注解
     */
    @Before("pointCut()&&@annotation(autoLock)")
    public void preHandle(JoinPoint joinPoint, AutoLock autoLock) {
        autoLock = AnnotationUtils.getAnnotation(autoLock, AutoLock.class);
        LockContext context = LockAspectUtils.buildContext(joinPoint, autoLock);
        LockStack contextStack = LOCK_THREAD.get();

        //尝试加锁
        boolean lockFlag = LockUtils.tryLock(context.getKey(), context.getWaitSecond(), context.getLeaseSecond());
        if(lockFlag) {
            contextStack.push(context);
        }else{
            log.warn("请求未结束，请勿重复请求!");
            throw new LockException(context.getMessage());
        }
    }

    @After("pointCut()")
    public void afterHandle() {
        LockStack contextStack = LOCK_THREAD.get();
        if(contextStack.empty()) {
            return;
        }
        LockContext context = contextStack.pop();
        //redis释放锁
        LockUtils.unlock(context.getKey());
        if(contextStack.empty()) {
            LOCK_THREAD.remove();
        }
    }

}
