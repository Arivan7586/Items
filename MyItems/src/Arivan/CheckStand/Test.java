package Arivan.CheckStand;


import java.text.SimpleDateFormat;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Client.run();

//        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(format.format(new Date()));

//        String s = "0.5";
//        String m = "0.5.6";
//        System.out.println(GoodsManage.isAllLongDigit(s));
//        System.out.println(GoodsManage.isAllLongDigit(m));
//        System.out.println(GoodsManage.isAllDigit(s));

//        StringBuilder stringBuilder = new StringBuilder("abfdfsf");
//        while (stringBuilder.length() < 10) {
//            stringBuilder.append(1);
//        }
//        System.out.println(stringBuilder);

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

/**
 * 客户端
 */
class Client {
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            Surface.mainMenu();
            switch (scanner.nextLine()) {
                case "U":
                    OrderManage.buyMenu();
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

/**
 * 商品管理
 */
class GoodsManage {
    public static final TreeMap<Integer, String[]> goods =
            new TreeMap<>();

    public static void set() {
        Scanner scanner = new Scanner(System.in);
        Surface.setMenu();
        switch (scanner.nextLine()) {
            case "S":
                while (true) {
                    view();
                    String input = scanner.nextLine();
                    if (input.equals("R")) {
                        set();
                        break;
                    }
                    System.out.println("输入指令错误，请重新输入！");
                }
                break;
            case "A":
                while (true) {
                    System.out.println("请输入上架商品信息（如下格式：1 餐巾纸 1.4）:");
                    put();
                    view();
                    System.out.println("继续上架，请输入任意指令，返回上一层，请输入(R)");
                    String input = scanner.nextLine();
                    if (input.equals("R")) {
                        set();
                        break;
                    }
                }
                break;
            case "D":
                while (true) {
                    downGoods();
                    view();
                    System.out.println("继续下架，请输入任意指令，返回上一层，请输入(R)");
                    String input = scanner.nextLine();
                    if (input.equals("R")) {
                        set();
                        break;
                    }
                }
                break;
            case "U":
                while (true) {
                    System.out.println("请输入修改商品信息（如下格式：1 餐巾纸 1.4 ）:");
                    modifyGoods();
                    view();
                    System.out.println("继续修改，请输入任意指令，返回上一层，请输入(R)");
                    String input = scanner.nextLine();
                    if (input.equals("R")) {
                        set();
                        break;
                    }
                }
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
            Iterator<Integer> iterator = goods.keySet().iterator();
            System.out.println("       编号       产品名称          单价(元)");
            while (iterator.hasNext()) {
                System.out.print("       ");
                int keyValue = iterator.next();
                System.out.print(keyValue);
                System.out.print("         ");
                String[] arr = goods.get(keyValue);
                StringBuilder str = new StringBuilder(arr[0]);
                while (str.length() < 10) {
                    str.append(" ");
                }
                System.out.print(str);
                System.out.print("       ");
                System.out.println(arr[1]);
            }
        }
        System.out.println();
        System.out.println("                                       [R] 返回  ");
        System.out.println("*************************************************");
    }

    /**
     * 货物上架
     */
    public static void put() {
        System.out.println("放弃操作，请输入R");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String enter = scanner.nextLine();
            if (enter.equals("R")) {
                break;
            }
            String[] arr = enter.trim().split(" ");
            if (arr.length != 3) {
                System.out.println("输入有误，参数个数不等于3！");
            } else if (!isAllDigit(arr[0])) {
                System.out.println("输入有误，商品编号不是纯数字！");
            } else if (!isAllLongDigit(arr[2])) {
                System.out.println("输入有误，商品价格不是含小数点的数字！");
            } else {
                int id = Integer.parseInt(arr[0]);
                String[] value = new String[]{arr[1], arr[2]};
                goods.put(id, value);
                break;
            }
        }
    }

    /**
     * 下架商品
     */
    public static void downGoods() {
        System.out.println("请输入下架商品信息编号（如下格式：1 ）:");
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while (true) {
            input = scanner.nextLine();
            if (isAllDigit(input)) {
                break;
            }
            System.out.println("输入的参数不为纯数字，请重新输入！");
        }
        goods.remove(Integer.parseInt(input));
    }

    /**
     * 修改商品信息
     */
    public static void modifyGoods() {
        put();
    }

    /**
     * 判断一个字符串是否全由数字组成
     *
     * @param str 要判断的字符串
     * @return
     */
    public static boolean isAllDigit(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 57 || str.charAt(i) < 48) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否是带有小数点的数字
     *
     * @param str 需要判断的字符串
     * @return 是，返回true，否则返回false
     */
    public static boolean isAllLongDigit(String str) {
        int count = 1;
        int i = 0;
        while (count >= 0 && i < str.length()) {
            if (str.charAt(i) == '.' && i == 0) {
                return false;
            }else if (str.charAt(i) == '.') {
                count--;
            }else if (str.charAt(i) > 57 || str.charAt(i) < 48) {
                return false;
            }
            i++;
        }
        if (count == 0) {
            return true;
        }
        return false;
    }
}

/**
 * 订单管理
 */
class OrderManage {
    private static Integer T = 1;
    public static final TreeMap<Integer,String[]> orders =
            new TreeMap<>();
    public static void buyMenu() {
//        GoodsManage.view();
        System.out.println("******************* 买单功能 ********************");
        System.out.println("    [S] 查看 [A] 下单 [D] 取消 [L] 浏览 [R] 返回");
        System.out.println("       输入:  S A D L R 进入操作");
        System.out.println("************************************************");
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equals("R")) {
                break;
            }else if (input.equals("S") || input.equals("A") || input.equals("D")
            || input.equals("L")) {
                switch (input) {
                    case "S":
                        orderView();
                        while (true) {
                            System.out.println("返回请输入R");
                            if (scanner.nextLine().equals("R")) {
                                break;
                            }
                        }
                        buyMenu();
                        break;
                    case "A":
                        if (GoodsManage.goods.isEmpty()) {
                            System.out.println("当前货物为空！！！");
                        }else {
                            buy();
                        }
                        buyMenu();
                        break;
                    case "D":
                        System.out.println("取消订单");
                        break;
                    case "L":
                        browse();
                        while (true) {
                            System.out.println("返回请输入R");
                            if (scanner.nextLine().equals("R")) {
                                break;
                            }
                        }
                        buyMenu();
                        break;
                    default:
                            break;
                }
            }else {
                System.out.println("指令输入错误，请重新输入");
            }

        }
    }

    /**
     * 下单
     */
    public static void buy() {
        System.out.println("请输入下单信息[编号 数量]（如下格式：1  2 ）:");
        System.out.println("放弃操作，请输入R");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String intput = scanner.nextLine();
            if (intput.equals("R")) {
                break;
            }
            String[] arr = intput.trim().split(" ");
            if (arr.length != 2) {
                System.out.println("输入有误，参数个数不等于3！");
            } else if (!GoodsManage.isAllDigit(arr[0])) {
                System.out.println("输入有误，商品编号不是纯数字！");
            } else if (!GoodsManage.isAllDigit(arr[1])) {
                System.out.println("输入有误，商品编号不是纯数字！");
            } else {
                int id = Integer.parseInt(arr[0]);
                String count = arr[1];
                String[] good = GoodsManage.goods.get(id);
                String[] orser = new String[]{good[0],count,good[1]};
                orders.put(id,orser);
                orderView();
            }
        }
    }

    /**
     * 查看订单信息
     */
    public static void orderView() {
        Date date = new Date();
        System.out.println("===============================");
        System.out.println("编号："+T++);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("打印时间："+format.format(new Date()));
        System.out.println("===============================");
        System.out.println("编号     名称      数量     单价");
        System.out.println("===============================");
        System.out.println("总价："+ ordersSum());
        System.out.println("===============================");
    }

    private static double ordersSum() {
        Iterator<Integer> iterator = orders.keySet().iterator();
        double sum = 0;
        while (iterator.hasNext()) {
            String[] value = orders.get(iterator.next());
            sum += (Integer.parseInt(value[1])*Integer.parseInt(value[2]));
        }
        return sum;
    }

    /**
     * 浏览商品
     */
    public static void browse() {
        GoodsManage.view();
    }

    /**
     * 取消
     */
    public static void cancel() {

    }
}