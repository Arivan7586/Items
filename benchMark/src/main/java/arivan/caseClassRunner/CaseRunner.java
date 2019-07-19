package arivan.caseClassRunner;

import arivan.Case;
import arivan.annotation.Benchmark;
import arivan.annotation.Measurement;
import arivan.annotation.WarmUp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 此类用于接收加载到的所有测试用例，并运行
 */
public class CaseRunner {
    //默认每组运行次数
    private static final int DEFAULT_ITERATIONS = 10;
    //默认运行组数
    private static final int DEFAULT_GROUPS = 5;
    //用于记录所有的待测类实例
    private final List<Case> caseList;
    //用于存放每一个测试用例的详细信息（测试用时最大值、最小值、总和、组数、每组次数、测试的方法名）
    public List<Report> reports = new ArrayList<Report>();

    public CaseRunner(List<Case> list) {
        this.caseList = list;
    }

    /**
     * 开始运行测试方法
     */
    public void run() throws InvocationTargetException, IllegalAccessException {
        for (Case bCase : caseList) {
            int iterations = DEFAULT_ITERATIONS;
            int groups = DEFAULT_GROUPS;

            //先获取类级别的配置注解
            Measurement classMeasurement = bCase.getClass().getAnnotation(Measurement.class);
            if (classMeasurement != null) {
                iterations = classMeasurement.iteratations();
                groups = classMeasurement.groups();
            }

            //找到类对象中需要测试的方法
            //获取对象的所有方法
            Method[] methods = bCase.getClass().getMethods();
            for (Method method : methods) {
                Benchmark benchmark = method.getAnnotation(Benchmark.class);
                //判断当前方法是否有Benchmark注解
                if (benchmark == null) {
                    continue;
                }

                Measurement methodMeasureMent = method.getAnnotation(Measurement.class);
                if (methodMeasureMent != null) {
                    iterations = methodMeasureMent.iteratations();
                    groups = methodMeasureMent.groups();
                }

                runCase(bCase, method, iterations, groups);
            }
        }
    }

    /**
     * 此类用于存放预热方法及其方法所在类的实例对象
     */
    private class WarmMethod {
        Method method;  //预热方法
        Case aCase;     //预热方法所在类的实例对象
        int iteratons;  //需要预热的次数
        public WarmMethod(Method method, Case aCase , int iteratons) {
            this.method = method;
            this.aCase = aCase;
            this.iteratons = iteratons;
        }
    }

    /**
     * 取得所有的预热方法
     * @return 返回预热方法
     */
    private WarmMethod addWarmUpMethods() {
        for (Case bcase : caseList) {
            Method[] methods = bcase.getClass().getMethods();
            for (Method method : methods) {

                WarmUp warmUp = method.getAnnotation(WarmUp.class);
                if (warmUp != null) {
                    return new WarmMethod(method,bcase,warmUp.iterations());
                }
            }
        }
        return null;
    }

    /**
     * 实际运行方法，用于运行测试用例
     * @param bCase 测试方法所在类的实例对象
     * @param method 需要测试的方法
     * @param iterations 每组测试的次数
     * @param groups 一共需要测试的组数
     */
    private void runCase(Case bCase, Method method, int iterations, int groups) throws InvocationTargetException, IllegalAccessException {
        WarmMethod warmMethod = addWarmUpMethods();

        //记录时间耗费的最大值
        long maxTime = Long.MIN_VALUE;
        //记录时间耗费的最小值
        long minTime = Long.MAX_VALUE;
        //记录时间总和
        long sum = 0;
        //记录平均时间
        long averageTime = 0;

        System.out.println("------------------"+ method.getName() + "测试开始------------------");
        for (int i = 1; i <= groups; i++) {
            System.out.println("第" + i + "组测试开始：");
            //每组测试用例运行之前，运行预热方法
            if (warmMethod != null) {
                for (int j = 0; j < warmMethod.iteratons; j++) {
                    warmMethod.method.invoke(warmMethod.aCase);
                }
                System.out.println(" 预热完成，开始测试！！！");
            }
            //预热完成后，执行正式测试
            for (int j = 1; j <= iterations; j++) {
                Object obj = method.invoke(bCase);
                long time = (Long)obj;
                maxTime = time > maxTime ? time : maxTime;
                minTime = time < minTime ? time : minTime;
                sum += time;
            }
        }
        sum = sum - maxTime - minTime;
        averageTime = sum / (groups * iterations - 2);
        Report report = new Report(maxTime,minTime,sum,averageTime,method.getName(),bCase.getClass().getName(),groups,iterations);
        this.reports.add(report);
        System.out.println("------------------"+ method.getName() + "测试结束------------------");
        System.out.println();
    }

    /**
     * 用于记录当前方法测试使用的最长时间，最短时间，总时间，去掉最大最小时间后的平均时间,测试的组数，每组测试的次数
     */
    public class Report {
        private long maxTime;
        private long minTime;
        private long sumTime;
        private long averageTime;
        private String methodName;
        private String caseName;
        private int groups;
        private int iterations;

        public Report(long maxTime, long minTime, long sumTime, long averageTime, String methodName, String caseName,
                      int groups, int iterations) {
            this.maxTime = maxTime;
            this.minTime = minTime;
            this.sumTime = sumTime;
            this.averageTime = averageTime;
            this.methodName = methodName;
            this.caseName = caseName;
            this.groups = groups;
            this.iterations = iterations;
        }

        public long getMaxTime() {
            return maxTime;
        }

        public long getMinTime() {
            return minTime;
        }

        public long getSumTime() {
            return sumTime;
        }

        public long getAverageTime() {
            return averageTime;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getCaseName() {
            return caseName;
        }

        public int getGroups() {
            return groups;
        }

        public int getIterations() {
            return iterations;
        }
    }
}
