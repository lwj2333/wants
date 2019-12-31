package com.lwj.wants.util


import java.math.BigDecimal
import java.math.RoundingMode


/**
 * @author by  LWJ
 * @date on 2018/10/2
 * @describe 添加描述
 */
object NumberUtils {
    /**
     * 目前java支持7中舍入法：
     * 1、 ROUND_UP：远离零方向舍入。向绝对值最大的方向舍入，只要舍弃位非0即进位。
     * 2、 ROUND_DOWN：趋向零方向舍入。向绝对值最小的方向输入，所有的位都要舍弃，不存在进位情况。
     * 3、 ROUND_CEILING：向正无穷方向舍入。向正最大方向靠拢。若是正数，舍入行为类似于ROUND_UP，若为负数，
     * 舍入行为类似于ROUND_DOWN。Math.round()方法就是使用的此模式。
     * 4、 ROUND_FLOOR：向负无穷方向舍入。向负无穷方向靠拢。若是正数，舍入行为类似于ROUND_DOWN；若为负数，
     *舍入行为类似于ROUND_UP。
     * 5、 HALF_UP：最近数字舍入(5进)。这是我们最经典的四舍五入。
     * 6、 HALF_DOWN：最近数字舍入(5舍)。在这里5是要舍弃的。
     * 7、 HAIL_EVEN：银行家舍入法。
     */
    fun doubleFormat(value: Double?): String? {
        return if (value == null) {
            BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
        } else {
            BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()
        }
    }

    /**
     *  提供指定数值的（精确）小数位四舍五入处理。
     *  @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun round(value: Double?, newScale: Int = 0, roundingMode: Int = BigDecimal.ROUND_HALF_UP): BigDecimal {
        return if (value == null) {
            BigDecimal(0).setScale(newScale, roundingMode)
        } else {
            BigDecimal(value).setScale(newScale, roundingMode)
        }
    }

    /**
     * 提供精确的加法运算
     * @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun add(d1: Double, d2: Double): BigDecimal {
        val b1 = BigDecimal(d1.toString())
        val b2 = BigDecimal(d2.toString())
        return b1.add(b2)
    }

    /**
     * 提供精确的减法运算
     * @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun subtract(d1: Double, d2: Double): BigDecimal {
        val b1 = BigDecimal(d1.toString())
        val b2 = BigDecimal(d2.toString())
        return b1.subtract(b2)
    }

    /**
     * 提供精确的乘法运算
     * @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun multiply(d1: Double, d2: Double): BigDecimal {
        val b1 = BigDecimal(d1.toString())
        val b2 = BigDecimal(d2.toString())
        return b1.multiply(b2)
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点后两位，以后的数字四舍五入
     * @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun divide(d1: Double, d2: Double,scale:Int=2): BigDecimal {
        val b1 = BigDecimal(d1.toString())
        val b2 = BigDecimal(d2.toString())
        return divide(b1,b2,scale)
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     * @return  返回BigDecimal  根据需要可转成各种数据类型
     */
    fun divide(b1: BigDecimal, b2: BigDecimal,scale:Int): BigDecimal {
           require(scale >= 0) { "The scale must be a positive integer or zero" }
        return b1.divide(b2,scale, RoundingMode.HALF_UP)
    }


}