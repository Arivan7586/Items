package arivan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//只存在运行时期
@Retention(RetentionPolicy.RUNTIME)

//方法的注解，被注解的方法就是测试用例
@Target(ElementType.METHOD)
public @interface Benchmark {
}
