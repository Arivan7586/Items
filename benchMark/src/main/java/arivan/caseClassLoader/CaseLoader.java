package arivan.caseClassLoader;

import arivan.Case;
import arivan.caseClassRunner.CaseRunner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CaseLoader {
    /**
     * 用于自动加载所有的测试用例类
     * @return 返回测试用例类集合List
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public CaseRunner load() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //包名称
        String packName = "arivan.cases";

        List<String> classNameList = loadClassName();
        //用于存放所有实现了Case接口的类
        List<Case> caseList = new ArrayList<Case>();

        for (String className : classNameList) {
            Class<?> cls = Class.forName(packName + "." + className);
            //判断当前类cls是否为Case的实现类
            if (hasInterface(cls,Case.class)) {
                caseList.add((Case)cls.newInstance());
            }
        }
        return new CaseRunner(caseList);
    }

    /**
     * 用于判断当前类cls是否实现了intf接口
     * @param cls 当前类cls
     * @param intf 接口intf
     * @return 若cls实现了接口intf，返回true，否则返回false
     */
    private boolean hasInterface(Class<?> cls, Class<?> intf) {
        Class<?>[] intfs = cls.getInterfaces();

        for (Class<?> i : intfs) {
            if (i == intf) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过固定类来取得所有存放于测试用例包cases下的 *.class文件名集合
     * @return 返回List<String>
     * @throws IOException
     */
    private List<String> loadClassName() throws IOException {
        //用于存放所有类名的List集合
        List<String> classNameList = new ArrayList<String>();

        //1 根据一个固定类找到一个类加载器
        String packg = "arivan/cases";
        ClassLoader classLoader = this.getClass().getClassLoader();

        //2 根据类加载器找到类文件所在的路径
        Enumeration<URL> urls = classLoader.getResources(packg);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (!url.getProtocol().equals("file")) {
                //暂时不支持非 *.class文件
                continue;
            }
            //进行转码，防止出现乱码问题
            String dirName = URLDecoder.decode(url.getPath(),"UTF-8");
            File dir = new File(dirName);

            if (!dir.isDirectory()) {
                //如果当前dir不是目录，则跳过
                continue;
            }

            //3 扫描路径所在的所有 *.class文件，作为类文件
            File[] files = dir.listFiles();
            if (files == null) {
                continue;
            }

            for (File file : files) {
                String fileName = file.getName();
                //判断后缀是否为.class
                if (!fileName.endsWith(".class")) {
                    continue;
                }
                //取得类名
                String className = fileName.substring(0,fileName.length() - 6);

                classNameList.add(className);
            }
        }
        return classNameList;
    }
}
