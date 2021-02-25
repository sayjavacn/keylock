package cn.sayjava.keylock.util;

import cn.sayjava.keylock.annotation.AutoLock;
import cn.sayjava.keylock.exception.LockException;
import cn.sayjava.keylock.model.LockContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 切面工具类
 * @author Great
 * @since 2021/02/04
 */
public class LockAspectUtils {

    private static final String KEY_PREFIX = "LOCK:KEY:";

    /**
     * 构建切面上下文
     *
     * @param joinPoint 封装了代理方法信息的对象
     * @param autoLock 锁注解
     * @return 返回上下文
     */
    public static LockContext buildContext(JoinPoint joinPoint, AutoLock autoLock) {
        //获取方法形参
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        //获取方法入参
        Object[] parameterValues = joinPoint.getArgs();

        String key = generateKey(method, parameterNames, parameterValues, autoLock.key());
        return LockContext.builder().key(key).waitSecond(autoLock.waitSecond()).leaseSecond(autoLock.leaseSecond()).message(autoLock.message()).root(method.getClass().getName() + "." + method.getName()).build();
    }

    /**
     * 生成MD5的key
     * @param method 方法对象
     * @param parameterNames 参数名
     * @param parameterValues 参数值
     * @param keyExpression KEY表达式
     * @return 返回KEY
     */
    private static String generateKey(Method method, String[] parameterNames, Object[] parameterValues, String keyExpression) {
        String key;
        if (StringUtils.hasText(keyExpression)) {
            key = parseKeyExpression(parameterNames, parameterValues, keyExpression);
        } else {
            key = method.getClass().getName() + "." + method.getName();
        }

        if (!StringUtils.hasText(key)) {
            throw LockException.NULL_KEY_EXCEPTION;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdBytes = md.digest(key.getBytes());
            return KEY_PREFIX + DatatypeConverter.printHexBinary(mdBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new LockException("Auto lock key encrypt fail by MD5", e);
        }
    }

    /**
     * 解析KEY表达式
     * @param parameterNames 参数名
     * @param parameterValues 参数值
     * @param keyExpression KEY表达式
     */
    private static String parseKeyExpression(String[] parameterNames, Object[] parameterValues, String keyExpression) {
        if (parameterNames == null || parameterNames.length == 0) {
            return keyExpression;
        }

        //使用SpringEL表达式解析注解上的key
        Expression expression = new SpelExpressionParser().parseExpression(keyExpression);

        //解析表达式
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], parameterValues[i]);
        }
        Object expressionValue = expression.getValue(evaluationContext);
        return expressionValue == null ? "" : expressionValue.toString();
    }

}
