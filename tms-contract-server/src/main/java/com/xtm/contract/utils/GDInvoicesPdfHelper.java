package com.xtm.contract.utils;

import com.xtm.common.exception.BusinessException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 22:06
 * @desc
 */
@Slf4j
public class GDInvoicesPdfHelper {

    private static final String PATH = "/templates/";
    // 结算单模板
    public static final String INVOICES_TEMPL = "gdInvoicesTempl.ftl";

    public static String changeFtlToHtml(Map<String, Object> map) throws BusinessException {
        log.info("开始生成本地模板HTML");
        if (map == null || map.isEmpty()) {
            log.info("写入结算单模版的数据不能为空！");
            return null;
        }
        String htmlStr = "";
        try {
            //声明配置类
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
            //设置模板路径
            //configuration.setDirectoryForTemplateLoading(new File(ResourceUtils.getURL("classpath:").getPath()+PATH));
            configuration.setTemplateLoader(new ClassTemplateLoader(GDInvoicesPdfHelper.class, PATH));
            //设置字符集
            configuration.setDefaultEncoding("utf-8");
            //将生成的内容写入html文件中
            Template template = configuration.getTemplate(INVOICES_TEMPL);
            htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);
            htmlStr = htmlStr.replace("<br>", "<br/>").replaceAll("&nbsp;"," ");
            log.info("##############转换html成功！##################");
        } catch (IOException ioException) {
            log.info("##############转换html错误！##################"+ioException);
            return null;
        } catch (TemplateException templateException) {
            log.info("##############转换html错误！##################"+templateException);
            return null;
        } catch (Exception e) {
            log.info("##############转换html错误！##################"+e);
            return null;
        }
        return htmlStr;
    }

    public final static class ENABLE_STATUS {
        /**
         * 启用
         */
        public final static Integer ENABLED = 1;
        /**
         * 禁用
         */
        public final static Integer DISABLED = 0;
    }

    public static class DOCUMENT_TYPE {
        //订单
        public final static Integer ORDER = 1133000;
        //货物托运单
        public final static Integer BOOKING_NOTE = 1133010;
        //调度单
        public final static Integer DISPATCH = 1133020;
        //运单
        public final static Integer DISPATCH_BATCH = 1133030;
        //出库单
        public final static Integer OUTSTOCK = 1133040;
        //入库单
        public final static Integer INSTOCK = 1133050;
        //记账凭证
        public final static Integer ACCOUNTING_VOUCHER = 1133060;
        //资金流水单
        public final static Integer CAPITAL_SEQUENCE = 1133240;
        //货物
        public final static Integer GOODS = 1133070;
        //地址
        public final static Integer ADDRESS = 1133071;
        //应收应付
        public final static Integer RECEIVE_PAY = 1133080;
        //货源
        public final static Integer GOODS_SUPPLY = 1133090;
        //网点
        public final static Integer OUTLETS = 1133290;
        //应收应付详情 结算单
        public final static Integer RECEIVE_PAY_SETTLE = 1133100;
        //订阅编号
        public final static Integer CUSTOMER_PRODUCT = 1133110;
        //采购配件
        public final static Integer PART_PURCHASE = 1133120;
        //维修
        public final static Integer VEHICLE_REPAIR = 1133130;
        //车辆保养
        public final static Integer VEHICLE_MAINTENANCE = 1133140;
        // 违章
        public final static Integer VEHICLE_PECCANCY = 1133150;
        // 事故
        public final static Integer VEHICLE_ACCIDENT = 1133160;
        // 规费
        public final static Integer VEHICLE_EXPENSE = 1133170;
        // 油费
        public final static Integer FUEL_COST = 1133180;
        // 货源余量
        public final static Integer SUPPLY_MARGIN = 1133190;
        // 载具-车辆
        public final static Integer VHICLE = 1133200;
        // 载具-船舶
        public final static Integer SHIP = 1133201;
        // 采购短途-载具
        public final static Integer SHORT_VEHICLE = 1133440;
        // 司机-司机
        public final static Integer DRIVER = 1133210;
        // 司机-船员
        public final static Integer MARINER = 1133211;
        //用户
        public final static Integer USER = 1133230;
        //集合货源号
        public final static Integer GOODS_GROUP_CODE = 1133270;
        // 路线预定单
        public final static Integer ROUTE_ORDER = 1133250;
        // 路线预运单
        public final static Integer ROUTE_BATCH = 1133260;
        //招标
        public final static Integer TENDERS = 1133220;
        // 结算
        public final static Integer TICKET = 1133280;
        //对账单
        public final static Integer ACCOUNT_STATEMENT = 1133330;
        //交账单
        public final static Integer EMPLOYEE_ACCOUNT = 1133300;
        // 路由
        public final static Integer PATHCODE = 1133310;
        // 车线
        public final static Integer VEHICLELINECODE = 1133320;
        //收发货单位
        public final static Integer TRADER = 2505020;
        // 发票申请编号
        public final static Integer INVOICE_APPLY_CODE = 1133340;
        // 发票，不对应字典值
        public final static Integer INVOICE = 1133341;
        // 路线
        public final static Integer ROUTE = 1133350;
        // 委运
        public final static Integer ENTRUST = 1133281;
        // 资金账户
        public final static Integer CAPITAL_ACCOUNT = 1133360;
        // 单据伙伴
        public final static Integer PARTNER = 1133370;
        // 合同
        public final static Integer CONTRACT = 1133380;

        // 竞价结算
        public final static Integer BID_SETTLEMENT = 1133390;
        // 能源单
        public final static Integer ENERGY_BILL = 1133410;
        // 找车费用
        public final static Integer FIND_CAR_CHARGE = 1133420;

        //采购合同
        public final static Integer LOGISTICS_CONTRACT=1133430;

        // 卸车费用
        public final static Integer UNLOAD_CAR_CHARGE = 1133440;
        //联合运单
        public final static Integer JOINT_DISPATCH_BATCH = 1133450;
        //批量运单
        public final static Integer BATCH_DISPATCH_BATCH = 1133460;
        // 服务费
        public final static Integer SERVICE_CHARGE = 1133470;

        public final static String PARTITION_GOODS_SUPPLY = "pGoods";

        public final static String PARTITION_ORDER = "pOrders";

        public final static String PARTITION_DISPATCH_BATCH = "pDispatch";

        public final static String PARTITION_ROUTE = "pRoute";

        public final static String PARTITION_RECEIVE_PAY = "pReceivepay";

        public final static String PARTITION_PARTNER = "pPartner";

        public final static String PARTITION_TICKET = "pTicket";
    }
}
