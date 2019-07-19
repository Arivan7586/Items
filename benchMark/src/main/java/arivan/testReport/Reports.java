package arivan.testReport;

import arivan.caseClassRunner.CaseRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成测试报告
 */
public class Reports {

    /**
     * 生成给定测试的测试报告
     * @param runner 给定测试
     */
    public static void report(CaseRunner runner) {
        //首先取得记录所有测试用例详细信息的集合
        List<CaseRunner.Report> reports = runner.reports;

        //将同一个待测类的所有测试用例放在同一个Map中，便于生成测试报告
        Map<String,List<CaseRunner.Report>> map = new HashMap<String, List<CaseRunner.Report>>();
        for (CaseRunner.Report report : reports) {
            //key代表每一个测试用例所在的类的类名
            String key = report.getCaseName();
            List<CaseRunner.Report> value = null;
            if (!map.containsKey(key)) {
                value = new ArrayList<CaseRunner.Report>();
                value.add(report);
            } else {
                value = map.get(key);
                value.add(report);
            }
            map.put(key,value);
        }

        //对不同实例类的待测方法进行分开生成测试报告
        for (String key : map.keySet()) {
            List<CaseRunner.Report> list = map.get(key);
            //同一个实例类中所有待测方法的测试报告一起生成
            System.out.println("-----------------测试报告----------------");
            for (CaseRunner.Report report : list) {
                System.out.println("测试方法："+ report.getMethodName());
                System.out.println("    测试分为：" + report.getGroups() + " 组");
                System.out.println("    每组测试：" + report.getIterations() + " 次");
                System.out.println("    使用的总时间：" + report.getSumTime() + " 纳秒");
                System.out.println("    使用最多时间：" + report.getMaxTime() + " 纳秒");
                System.out.println("    使用最少时间：" + report.getMinTime() + " 纳秒");
                System.out.println("    平均使用用时：" + report.getAverageTime() + " 纳秒");
                System.out.println();
            }
            System.out.println("----------------------------------------");
        }
    }
}

