package arivan;

import arivan.caseClassLoader.CaseLoader;
import arivan.caseClassRunner.CaseRunner;
import arivan.testReport.Reports;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * 此类为运行类，用于运行加载流程和测试运行流程
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        //加载测试类
        CaseLoader loader = new CaseLoader();
        CaseRunner runner = loader.load();
        //运行测试类
        runner.run();

        //生成测试报告
        Reports.report(runner);
    }

}
