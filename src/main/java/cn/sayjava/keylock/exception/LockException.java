package cn.sayjava.keylock.exception;

/**
 * 锁异常类
 * @author Great
 * @since 2021/02/05
 */
public class LockException extends RuntimeException {

    public static final LockException NULL_KEY_EXCEPTION = new LockException("Auto lock key is null");


    public LockException() {
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
