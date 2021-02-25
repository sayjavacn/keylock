package com.boot4j.autolock.util;

import com.boot4j.autolock.model.LockContext;

import java.util.ArrayList;

/**
 * 非线性安全的栈
 *
 * @author Great
 * @since 2021/02/04
 */
public class LockStack extends ArrayList<LockContext> {

    /**
     * push函数：将元素存入栈顶
     * @param context 锁上下文
     */
    public void push(LockContext context) {
        if(context == null) {
            return;
        }
        add(context);
    }

    /**
     * @return pop函数：返回栈顶元素，并将其从栈中删除
     */
    public LockContext pop() {
        LockContext context = peek();
        // 删除栈顶元素
        remove(context);
        return context;
    }

    /**
     * @return peek函数：返回栈顶元素，不执行删除操作
     */
    public LockContext peek() {
        int len = size();
        if (len == 0) {
            return null;
        }
        // 返回栈顶元素
        return this.get(len - 1);
    }

    /**
     * @return 栈是否为空
     */
    public boolean empty() {
        return size() == 0;
    }

}
