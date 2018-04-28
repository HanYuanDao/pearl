package tool;

import java.math.BigDecimal;

/**
 * @author Jasonhan hanzhe.jason@gmail.com:
 * @version 创建时间：2016年9月18日 下午5:08:00
 * 类说明 为避免浮点型运算过程中精度丢失的运算类。现只实现了double类型的方法且指定了舍弃小数位的方式为四舍五入。
 */
public class MathHelper {
    public static final int DEF_SCALE = 4;
    public static final int DEF_ROUND = BigDecimal.ROUND_HALF_UP;

    private MathHelper() {}

    /**
     * double型相加，避免精度丢失。默认保留小数点后四位小数
     * @param sourceList
     * @return
     */
    public static double add(double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.add(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value).doubleValue();
    }
    /**
     * double型相加，避免精度丢失。默认保留小数点后scale位小数
     * @param scale
     * @param sourceList
     * @return
     */
    public static double add(int scale, double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.add(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value, scale).doubleValue();
    }

    /**
     * double型相减，避免精度丢失。默认保留小数点后四位小数
     * @param sourceList
     * @return
     */
    public static double subtract(double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.subtract(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value).doubleValue();
    }
    /**
     * double型相减，避免精度丢失。默认保留小数点后scale位小数
     * @param scale
     * @param sourceList
     * @return
     */
    public static double subtract(int scale, double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.subtract(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value, scale).doubleValue();
    }

    /**
     * double型相乘，避免精度丢失。默认保留小数点后四位小数
     * @param sourceList
     * @return
     */
    public static double multiply(double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.multiply(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value).doubleValue();
    }
    /**
     * double型相乘，避免精度丢失。默认保留小数点后scale位小数
     * @param scale
     * @param sourceList
     * @return
     */
    public static double multiply(int scale, double ... sourceList) {
        BigDecimal value = null;
        for(double sourceValue : sourceList) {
            if(value != null) {
                value = value.multiply(new BigDecimal(Double.toString(sourceValue)));
            }else {
                value = new BigDecimal(Double.toString(sourceValue));
            }
        }
        return setScaleBigDecimal(value, scale).doubleValue();
    }

    /**
     * double型相除，避免精度丢失。默认保留小数点后四位小数
     * @param source1
     * @param source2
     * @return
     */
    public static double divide(double source1, double source2) {
        BigDecimal v1 = new BigDecimal(Double.toString(source1));
        BigDecimal v2 = new BigDecimal(Double.toString(source2));
        return v1.divide(v2, DEF_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * double型相除，避免精度丢失。默认保留小数点后scale位小数
     * @param source1
     * @param source2
     * @param scale
     * @return
     */
    public static double divide(double source1, double source2, int scale) {
        BigDecimal v1 = new BigDecimal(Double.toString(source1));
        BigDecimal v2 = new BigDecimal(Double.toString(source2));
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 调成数据精度，默认保留小数点后四位小数
     * @param source
     * @return
     */
    public static BigDecimal setScaleBigDecimal(BigDecimal source) {
        return source.setScale(DEF_SCALE, DEF_ROUND);
    }
    /**
     * 调成数据精度，默认保留小数点后scale位小数
     * @param source
     * @param scale
     * @return
     */
    public static BigDecimal setScaleBigDecimal(BigDecimal source, int scale) {
        return source.setScale(scale, DEF_ROUND);
    }
    /**
     * 调成数据精度，默认保留小数点后四位小数
     * @param source
     * @return
     */
    public static BigDecimal setScaleBigDecimal(double source) {
        return setScaleBigDecimal(new BigDecimal(Double.toString(source)));
    }
    /**
     * 调成数据精度，默认保留小数点后scale位小数
     * @param source
     * @param scale
     * @return
     */
    public static BigDecimal setScaleBigDecimal(double source, int scale) {
        return setScaleBigDecimal(new BigDecimal(Double.toString(source)), scale);
    }
    /**
     * 调成数据精度，默认保留小数点后四位小数
     * @param source
     * @return
     */
    public static double setScaleDouble(BigDecimal source) {
        return setScaleBigDecimal(source).doubleValue();
    }
    /**
     * 调成数据精度，默认保留小数点后scale位小数
     * @param source
     * @param scale
     * @return
     */
    public static double setScaleDouble(BigDecimal source, int scale) {
        return setScaleBigDecimal(source, scale).doubleValue();
    }
    /**
     * 调成数据精度，默认保留小数点后四位小数
     * @param source
     * @return
     */
    public static double setScaleDouble(double source) {
        return setScaleDouble(new BigDecimal(Double.toString(source)));
    }
    /**
     * 调成数据精度，默认保留小数点后scale位小数
     * @param source
     * @param scale
     * @return
     */
    public static double setScaleDouble(double source, int scale) {
        return setScaleDouble(new BigDecimal(Double.toString(source)), scale);
    }
}