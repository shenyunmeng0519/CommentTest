package teacherkline.sikkha.com.textvip.comm;



import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

public class StringUtils {


    public static Object NullToObject(Object o) {
        if (null == o || JSONObject.NULL == o || "null" == o) {
            return null;
        }
        return o;
    }

    public static String NullToStr(Object o) {
        if (null == o || null == o.toString() || JSONObject.NULL == o) {
            return "";
        }
        return o.toString();
    }

    public static int NullToInt(Object o) {
        if (null == o || null == o.toString() || "".equals(o) || JSONObject.NULL == o) {
            return 0;
        }
        return Integer.parseInt(o.toString());
    }

    public static long NullToLong(Object o) {
        if (null == o || null == o.toString() || JSONObject.NULL == o) {
            return 0;
        }
        return Long.parseLong(o.toString());
    }

    public static double NullToDouble(Object o) {
        if (null == o || null == o.toString() || JSONObject.NULL == o || o.equals("")) {
            return 0;
        }
        return Double.parseDouble(o.toString());
    }

    public static boolean NullToBoolean(Object o) {
        if (null == o || JSONObject.NULL == o) {
            return false;
        }
        return (Boolean) o;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.equals("null");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @return
     */
    public static String subZeroAndDot(Double dV) {
        dV = NullToDouble(dV);

        BigDecimal bigDecimal = new BigDecimal(dV);
        String result = bigDecimal.toString();
        return subZeroAndDot(result);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @return
     */
    public static String subZeroAndDot(String result) {

        if (result.indexOf(".") > 0) {
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }


    /**
     * <p>Title: myPercent</p>
     *
     * @param y
     * @param z
     * @return
     * @author chenwei
     * @date 2016年4月15日 下午1:35:34
     */
    public static int myPercent(int y, int z) {
        String baifenbi = "";// 接受百分比的值
        double baiy = y * 1.0;
        double baiz = z * 1.0;
        double fen = baiy / baiz;
        // NumberFormat nf = NumberFormat.getPercentInstance();注释掉的也是一种方法
        // nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
        DecimalFormat df1 = new DecimalFormat("##%");
        // ##.00%
        // 百分比格式，后面不足2位的用0补齐
        baifenbi = df1.format(fen);
        return Integer.parseInt(baifenbi.replace("%", ""));
    }

    /**
     * 帖子标题显示处理
     * */
    public static String getTieziTitle(String title) {
        title = title.replace("\\n", " ");
        title = title.replace("&#39;", "'");
        title = title.replace("&lt;", "<");
        title = title.replace("&gt;", ">");
        title = title.replace("&quot;", "”");
        title = title.replace("&amp;", "&");

        return title;
    }

    /**
     * 帖子内容显示处理
     * */
    public static String getTieziContent(String content) {
        content = content.replace("\\n", "\r\n");
        content = content.replace("&#39;", "'");
        content = content.replace("&lt;", "<");
        content = content.replace("&gt;", ">");
        content = content.replace("&quot;", "”");
        content = content.replace("&amp;", "&");
        return content;
    }
    /**
     * 发帖内容处理
     * */
    public static String setTieziContent(String contents) {
        contents  = StringUtils.NullToStr(contents.replace("\n", "\\n"));
        contents  = StringUtils.NullToStr(contents.replace("%", "%25"));
        contents  = StringUtils.NullToStr(contents.replace("+", "%2B"));
        return contents;
    }
    /**
     * post传参百分号问题
     * */
    public static String setPostCanShu(String canshu) {
        canshu  = StringUtils.NullToStr(canshu.replace("%", "%25"));
        canshu  = StringUtils.NullToStr(canshu.replace("+", "%2B"));
        return canshu;
    }
    /**
     * 从一个数组中随机取出几个数组成数组
     */
    public static String[] getRandomFromArray(String[] array, int count) {
        String[] a = array;
        String[] result = new String[count];
        boolean r[] = new boolean[array.length];
        Random random = new Random();
        int m = count; // 要随机取的元素个数
        if (m > a.length || m < 0)
            return a;
        int n = 0;
        while (true) {
            int temp = random.nextInt(a.length);
            if (!r[temp]) {
                if (n == m) // 取到足量随机数后退出循环
                    break;
                n++;
                System.out.println("得到的第" + n + "个随机数为：" + a[temp]);
                result[n - 1] = a[temp];
                r[temp] = true;
            }
        }
        return result;
    }
/**
 * 从要加密的Map中去除不要加密的字段后，剩余用，拼接成字符串
 * */
    public static String getRandomString(Map<String, String> map, String array) {
        int iC;
        if (map.isEmpty()) return "";
        if (array != null)
            iC = map.size() - array.split(",").length;
        else
            iC = map.size();
        int iR = new Random().nextInt(iC) + 1;
        int i = 0;
        String strR = "";
        for (Map.Entry<String, String> st : map.entrySet()) {

            if (array != null && ("," + array + ",").contains("," + st.getKey() + ",")) continue;
            if (iR == i) break;
            if (!"".equals(strR)) strR += "&";
            strR += st.getKey() + "=" + st.getValue();
            i++;
        }
        return strR;
    }
}
