package arivan.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 需要测试的次数和组数
 */
@Target({ElementType.METHOD,ElementType.TYPE})

//只存在运行时期
@Retention(RetentionPolicy.RUNTIME)
public @interface Measurement {
    //表示每组测试的次数
    int iteratations();

    //表示一共有多少组测试
    int groups();
}

