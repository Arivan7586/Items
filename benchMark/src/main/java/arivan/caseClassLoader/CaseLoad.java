package arivan.caseClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class CaseLoad {

    public List<String> loadCase() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<String> classNameList = new LinkedList<String>();
        Enumeration<URL> urls = classLoader.getResources("arivan/cases");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (!url.getProtocol().equals("file")) {
                continue;
            }
            classNameList.addAll(classFiles(URLDecoder.decode(url.getPath(),
                    "UTF-8")));
        }
        return classNameList;
    }
    private List<String> classFiles(String path) {
        File file = new File(path);
        List<String> list = new ArrayList<String>();
        File[] files = file.listFiles();
        if (files == null) {
            return list;
        }
        for (File file1 : files) {
            if (file.isDirectory()) {
                continue;
            }
            String fileName = file.getName();
            //判断当前文件是否为class文件
            if (!fileName.endsWith(".class")) {
                continue;
            }
            if (fileName.lastIndexOf("$") != -1) {
                continue;
            }
            String name = fileName.substring(0,fileName.length()-6);
            list.add(name);
        }
        return list;
    }
}
