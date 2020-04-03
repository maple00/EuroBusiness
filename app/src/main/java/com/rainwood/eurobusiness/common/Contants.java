package com.rainwood.eurobusiness.common;

/**
 * @Author: a797s
 * @Date: 2019/12/6 13:42
 * @Desc: 常量定义
 */
public final class Contants {

    /**
     * Intent 请求码
     */
    // 扫一扫
    public static final int SCANCHECKCODE = 0x0001;

    /**
     * 记录最新的手机号
     */
    // 记录之前输入的手机号
    public static String PhoneCheckVerify = null;

    /**
     * 用户的类型
     * type：
     *  供应商：0
     *  门店： 1
     */
    public static int userType = -1;

    /**
     * 门店端
     * 首页选择模块记录
     * 初始化：1-12 为首页项目模块
     * 其次：
     *      13： 点击客户管理中新增客户中的收货地址填写
     *      14：点击客户管理中新增客户中的发票信息填写
     *      15: 点击客户管理详情中的编辑页面
     *      16: 客户管理中新增客户分类
     *      17: 客户管理中编辑客户分类
     *      18: 开票地址编辑
     *      19：收货地址编辑
     * 批发商端：
     *  初始化： 101 - 116 为首页项目模块
     *
     * 点击的位置记录：
     *  0x1001:      从供应商列表查看详情
     *  0x1002:       供应商列表新建供应商
     *  0x1003:       编辑门店
     *
     */
    public static int CHOOSE_MODEL_SIZE = -1;


    /**
     * 选择颜色或者尺寸
     */
    public static int choose_size = -1;

    /**
     * 请求失败
     */
    public static final String HTTP_MSG_RESPONSE_FAILED = "The request data failed and the response code is not 200,code = ";

    /**
     * ROOT_URL
     */
    public static final String ROOT_URI = "https://www.yumukeji.cn/project/oushang/";

    /**
     * 搜索条件
     */
    public static String SEARCH_CONDITIONS;
}
