package Arivan.CheckStand;

import java.util.Scanner;
import java.util.TreeMap;

public class Test {
    public static void main(String[] args) {
        Client.run();
//        GoodsManage.goods.clear();

//        Scanner scanner = new Scanner(System.in);
//        String s = scanner.nextLine();
//        String[] arr = s.trim().split(" ");
//        for (String s1 : arr) {
//            System.out.println(s1);
//        }
//
//        System.out.println(arr.length);
//        String a = "abc.dsf";
//        System.out.println(a);
//        a.replaceAll("\\.","");
//        System.out.println(a);
    }
}

class Surface {
    public static void mainMenu() {
        System.out.println();
        System.out.println("*************** 欢迎使简易收银台 *****************");
        System.out.println("       [U] 使用 [S] 设置 [A] 关于 [Q] 退出");
        System.out.println("          输入:  S A D U R 进行操作");
        System.out.println("************************************************");
        System.out.println();
    }

    public static void setMenu() {
        System.out.println("******************* 设置功能 ********************");
        System.out.println("    [S] 查看 [A] 上架 [D] 下架 [U] 修改 [R] 返回");
        System.out.println("       输入:  S A D U R 进行操作");
        System.out.println("************************************************");
        System.out.println();
    }

    public static void aboutMune() {
        System.out.println("******************** 关于 ***********************");
        System.out.println("        名称：简易收银台");
        System.out.println("        功能：基于字符界面的收银台操作系统");
        System.out.println("        作者: Arivan");
        System.out.println("        版本: v0.0.1");
        System.out.println("        意见反馈：1194028288@qq.com");
        System.out.println();
        System.out.println("                                       [R] 返回  ");
        System.out.println("*************************************************");
        System.out.println();
    }

    public static void exitMenu() {
        System.out.println("*************************************************");
        System.out.println("                欢迎使用，下次再见");
        System.out.println("*************************************************");
    }
}

class Client {
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            Surface.mainMenu();
            switch (scanner.nextLine()) {
                case "U":
                    System.out.println("111111111111111");
                    break;
                case "S":
                    GoodsManage.set();
                    break;
                case "A":
                    Surface.aboutMune();
                    while (!scanner.nextLine().equals("R")) {
                        System.out.println("指令输入错误！请重新输入！");
                    }
                    break;
                case "Q":
                    Surface.exitMenu();
                    flag = false;
                    break;
                    default:
                        System.out.println("指令输入错误！请重新输入！");
                        break;
            }
        }
    }

}

class GoodsManage {
    public static final TreeMap<Integer,String[]> goods =
            new TreeMap<>();
    public static void set() {
        Scanner scanner = new Scanner(System.in);
        Surface.setMenu();
        switch (scanner.nextLine()) {
            case "S":
                view();
                while (!scanner.nextLine().equals("R")) {
                    System.out.println("指令输入错误！请重新输入！");
                }
                break;
            case "A":
                put();
                view();
                while (!scanner.nextLine().equals("R")) {
                    System.out.println("指令输入错误！请重新输入！");
                }
                break;
            case "D":
                break;
            case "U":
                break;
            case "R":
                break;
            default:
                System.out.println("指令输入错误！请重新输入！");
                break;
        }
    }

    /**
     * 产看产品清单
     */
    public static void view() {
        System.out.println("******************  商品清单  ********************");
        if (goods.isEmpty()) {
            System.out.println("             尚未上架商品，当前商品清单为空！");
        } else {
            System.out.println("       编号       产品名称       单价");
            for (int i = 0; i < goods.size(); i++) {
                System.out.print("       ");

                System.out.print("       ");
                System.out.print("       ");

            }
            //System.out.println("商品清单！！！！！");
        }
        System.out.println();
        System.out.println("                                       [R] 返回  ");
        System.out.println("*************************************************");
    }

    /**
     * 货物上架
     */
    public static void put() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入上架商品信息（如下格式：1 餐巾纸 1.4）:");
            String enter = scanner.nextLine();
            String[] arr = enter.trim().split(" ");
            if (arr.length != 3) {
                System.out.println("输入有误，参数个数不等于3！");
            }else if (!isAllDigit(arr[0])) {
                System.out.println("输入有误，商品编号不是纯数字！");
            }else if (isAllLongDigit(arr[2])) {
                System.out.println("输入有误，商品价格不是含小数点的数字！");
            }else {
                int id = Integer.parseInt(arr[0]);
                String[] value = new String[]{arr[1],arr[2]};
                goods.put(id,value);
                break;
            }
        }
//        System.out.println("输出完成");
    }

    /**
     * 判断一个字符串是否全由数字组成
     * @param str 要判断的字符串
     * @return
     */
    private static boolean isAllDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 57 || str.charAt(i) < 48) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllLongDigit(String str) {
        if (!str.contains(".")) {
            return false;
        }

        return isAllDigit(str.replace("","\\."));
    }
}