package arivan.cases;

import arivan.Case;
import arivan.annotation.Benchmark;
import arivan.annotation.Measurement;
import arivan.annotation.WarmUp;
import arivan.testReport.Reports;

import java.util.Arrays;
import java.util.Random;


/**
 * 测试快速排序和归并排序
 */
@Measurement(iteratations = 10,groups = 5)
public class SortCase implements Case {

    /**
     * 生成一个随机数组
     * @param n 随机数组个数
     * @param max 数组元素的最大值
     * @return 返回生成的数组
     */
    private static int[] createArray(int n, int max) {
        int[] arr = new int[n];
        Random random = new Random(max);
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(max);
        }
        return arr;
    }

    /**
     * 预热方法
     */
    @WarmUp(iterations = 1) //此处设置预热1次
    public static void warmUp() {
        int[] arr = createArray(10000,100000);
    }

    /**
     * 测试系统排序用时
     * @return
     */
    @Benchmark
    public static long SystemSort() {
        int[] arr = createArray(100000,100000);
        long start = System.nanoTime();
        Arrays.sort(arr);
        long end = System.nanoTime();
        System.out.println("   SystemSort排序用时：" + (end - start) + "纳秒");
        return end - start;
    }

    /**
     * 测试快速排序
     */
    @Benchmark
    public static long quickSort() {
        int[] arr = createArray(100000,100000);
        long start = System.nanoTime();
        int n = arr.length;
        if (n <= 1) {
            return 0;
        }
        sort(arr,0,arr.length-1);
        long end = System.nanoTime();
        System.out.println("   递归法快速排序用时：" + (end - start) + "纳秒");
        return end - start;
    }
    private static void sort(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = partation(arr,l,r);
        sort(arr,l,mid-1);
        sort(arr,mid+1,r);
    }
    private static int partation(int[] arr, int l, int r) {
        int randomIndex = (int) (Math.random() * (r - l + 1) + l);
        swap(arr,l,randomIndex);
        int value = arr[l];
        int i = l + 1;
        int j = l;
        int k = r + 1;
        while (i < k) {
            if (arr[i] < value) {
                swap(arr,i++,++j);
            } else if (arr[i] > value) {
                swap(arr,i,--k);
            } else {
                i++;
            }
        }
        swap(arr,l,j);
        return j;
    }
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     *归并排序
     */
    @Benchmark
    public static long mergeSort() {
        int[] arr = createArray(100000,100000);
        long start = System.nanoTime();
        int n = arr.length;
        if (n <= 1) {
            return 0;
        }
        partition(arr,0,arr.length-1);
        long end = System.nanoTime();
        System.out.println("   归并排序用时：" + (end - start) + "纳秒");
        return end - start;
    }
    private static void partition(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = l + (r - l) / 2;
        partition(arr,l,mid);
        partition(arr,mid+1,r);
        if (arr[mid] > arr[mid+1]) {
            merge(arr,l,mid,r);
        }
    }
    private static void merge(int[] arr, int l,int mid, int r) {
        int[] array = new int[r-l+1];
        int i = l;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= r) {
            if (arr[i] <= arr[j]) {
                array[k++] = arr[i++];
            } else {
                array[k++] = arr[j++];
            }
        }
        int start = i;
        int end = mid;
        if (j <= r) {
            start = j;
            end = r;
        }
        while (start <= end) {
            array[k++] = arr[start++];
        }
        for (int m = 0; m < array.length; m++) {
            arr[l+m] = array[m];
        }
    }

}

