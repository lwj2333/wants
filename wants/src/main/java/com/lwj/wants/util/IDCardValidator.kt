package com.lwj.wants.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class IDCardValidator {


    /** 
         * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古", 
         * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏", 
         * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南", 
         * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆", 
         * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃", 
         * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"} 
         */
    //省(直辖市)码表
    private val provinceCode: Array<String> = arrayOf(
        "11", "12", "13", "14", "15", "21", "22",
        "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
        "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
        "64", "65", "71", "81", "82", "91"
    )
    //身份证前17位每位加权因子
    private val power = arrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    //身份证第18位校检码
    private val refNumber = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")

    /**
     * 二代身份证正则表达式
     *
     * @param idNo
     * @return
     */
    private fun isIdNoPattern(idNo: String): Boolean {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X])$", idNo)
    }


            /** 
         * <p> 
         * 判断18位身份证的合法性 
         * </p> 
         * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。 
         * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 
         * <p> 
         * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。 
         * </p> 
         * <p> 
         * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码； 
         * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码； 
         * 6.第17位数字表示性别：奇数表示男性，偶数表示女性； 
         * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。 
         * </p> 
         * <p> 
         * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 
         * 2 1 6 3 7 9 10 5 8 4 2 
         * </p> 
         * <p> 
         * 2.将这17位数字和系数相乘的结果相加。 
         * </p> 
         * <p> 
         * 3.用加出来和除以11，看余数是多少？ 
         * </p> 
         * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 
         * 2。 
         * <p> 
         * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。 
         * </p> 
         *  
         * @param idcard 
         * @return 
         */
    /**
     * 二代身份证号码有效性校验
     *
     * @param idNo
     * @return
     */
    fun isValidIdNo(idNo: String): Boolean {
        return isIdNoPattern(idNo) && isValidProvinceId(idNo.substring(0, 2))
                && isValidDate(idNo.substring(6, 14)) && checkIdNoLastNum(idNo)
    }

    /**
     * 检查身份证的省份信息是否正确
     * @param provinceId
     * @return
     */
    private fun isValidProvinceId(provinceId: String): Boolean {
        for (id in provinceCode) {
            if (id == provinceId) {
                return true
            }
        }
        return false
    }

    /**
     * 判断日期是否有效
     * @param inDate
     * @return
     */
    private fun isValidDate(inDate: String?): Boolean {
        if (inDate == null) {
            return false
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        if (inDate.trim().length != dateFormat.toPattern().length) {
            return false
        }
        dateFormat.isLenient = false//执行严格的日期匹配
        try {
            dateFormat.parse(inDate.trim())
        } catch (e: ParseException) {
            return false
        }
        return true
    }

    /**
     * 计算身份证的第十八位校验码
     * @param cardIdArray
     * @return
     */
    private fun sumPower(cardIdArray: IntArray): String {
        var result = 0
        for (i in 0 until power.size) {
            result += power[i] * cardIdArray[i]
        }
        return refNumber[(result % 11)]
    }

    /**
     * 校验身份证第18位是否正确(只适合18位身份证)
     * @param idNo
     * @return
     */
    private fun checkIdNoLastNum(idNo: String): Boolean {
        if (idNo.length != 18) {
            return false
        }
        val tmp = idNo.toCharArray()
        val cardidArray = IntArray(tmp.size - 1)

        for (i in 0 until tmp.size - 1) {
            cardidArray[i] = Integer.parseInt(tmp[i] + "")
        }
        val checkCode = sumPower(cardidArray)
        var lastNum = tmp[tmp.size - 1] + ""
        if (lastNum == "x") {
            lastNum = lastNum.toUpperCase()
        }
        if (checkCode != lastNum) {
            return false
        }
        return true
    }
}