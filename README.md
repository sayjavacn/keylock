# KeyLock

#### 简介

redis分布式锁，基于springboot2使用

#### 功能介绍
1. 以注解的形式快速实现分布式锁的功能
2. 支持锁续命功能
3. 支持锁某一条数据，可解决重复提交问题
4. 支持controller、service层的注解

#### 使用技术
1. springboot2
2. redis
3. redisson

#### 使用说明

1. 引用依赖包
```
<dependency>
    <groupId>com.sayjavacn</groupId>
    <artifactId>keylock</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>    
```   
2. 注解示例
```
@AutoLock("#s.id+#t.id+#name")
public void demo(Student s，Teacher t, String name){
    //TODO
}

-更多属性-
waitSecond：等待锁的时间，单位(秒)
leaseSecond: 默认-1会进行锁续期，释放时间是30S，其他不续期，使用默认即可
message: 获取不到锁的提示信息
```
springel 表达式的占位符为 “#” 使用时 可以传入整个对象，也可以传入对象中的属性，根据实际情况而定   

3. 配置redis连接
```
https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter
```

#### 其他
1. GIT仓库
``` 
   https://github.com/sayjavacn/keylock.git
   https://gitee.com/sayjavacn/keylock.git
```    
2. QQ交流群
```
145612313
```   
3. 发布中央仓库
```
mvn clean deploy -P release -Dmaven.test.skip=true

https://central.sonatype.org/pages/apache-maven.html

