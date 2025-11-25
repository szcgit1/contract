package com.xtm.contract.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
public class PdfHelper {

    private static final String PATH = "/templates/";
    // 运单合同模板
    public static final String WAYBILL_DTL_CONTRACT = "waybillContractTempl.ftl";
    // 联合运输合同模板
    public static final String JOINT_WAYBILL_DTL_CONTRACT = "jointWaybillContractTempl.ftl";
    // 批量运输合同模板
    public static final String BATCH_WAYBILL_DTL_CONTRACT = "batchWaybillContractTempl.ftl";

    public static String changeFtlToHtml(Map<String, Object> map, Integer contractType, Integer businessType) throws BusinessException {
        log.info("开始生成本地模板HTML");
        if (map == null || map.isEmpty()) {
            log.info("写入合同模版的数据不能为空！");
            return null;
        }
        String htmlStr = "";
        try {
            ContractInfoQryVO contract = null;
            if(!contractType.equals(DicConstant.DOCUMENT_TYPE.FIND_CAR_CHARGE)
                    && !contractType.equals(DicConstant.DOCUMENT_TYPE.UNLOAD_CAR_CHARGE)
                    && !contractType.equals(DicConstant.DOCUMENT_TYPE.SERVICE_CHARGE)){
                contract = (ContractInfoQryVO)map.get("contract");
            }
            //声明配置类
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
            //设置模板路径
            //configuration.setDirectoryForTemplateLoading(new File(ResourceUtils.getURL("classpath:").getPath()+PATH));
            configuration.setTemplateLoader(new ClassTemplateLoader(PdfHelper.class, PATH));
            //设置字符集
            configuration.setDefaultEncoding("utf-8");
            //将生成的内容写入html文件中
            String ftlName = "";
            if (DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(businessType)) {
                ftlName = DicConstant.ZQSIGN_DEFINITION.TRIPARTITE_DTL_CONTRACT;
            } else {
                if (contractType.equals(DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT)) {
                    ftlName = DicConstant.ZQSIGN_DEFINITION.FRA_CONTRACT;
                } else if (contractType.equals(DicConstant.DOCUMENT_TYPE.FIND_CAR_CHARGE)){ // 找车服务费汇总模板
                    ftlName = DicConstant.FIND_CAR_CHARGE_SUMMARY_TEMPL;
                } else if (contractType.equals(DicConstant.DOCUMENT_TYPE.UNLOAD_CAR_CHARGE)){ // 卸车服务费汇总模板
                    ftlName = DicConstant.UNLOAD_CAR_CHARGE_SUMMARY_TEMPL;
                } else if (contractType.equals(DicConstant.DOCUMENT_TYPE.SERVICE_CHARGE)) { // 技术服务费汇总模板
                    ftlName = DicConstant.SERVICE_CHARGE_SUMMARY_TEMP;
                } else if (contractType.equals(DicConstant.CONTRACT_TYPE.JOINT_CONTRACT)){ // 联合运单
                    ftlName = JOINT_WAYBILL_DTL_CONTRACT;
                }else if (contractType.equals(DicConstant.CONTRACT_TYPE.BATCH_CONTRACT)){ // 批量运单
                    ftlName = BATCH_WAYBILL_DTL_CONTRACT;
                } else {
                    if(DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(contract.getDocumentType())){ // 运单合同，使用新运单合同模板  1133030
                        ftlName = WAYBILL_DTL_CONTRACT;
                    }else {
                        ftlName = DicConstant.ZQSIGN_DEFINITION.DTL_CONTRACT;
                    }
                }
            }
            Template template = configuration.getTemplate(ftlName);
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

    /**
     * @Description 用于供外部类调用获取关键字所在PDF文件坐标
     * @param filepath
     * @param keyWords
     * @return float[]
     */
    public static float[] getKeyWordsByPath(String filepath, String keyWords) throws BusinessException {
        log.info("获取坐标的pdf是{}，关键字是:{}",filepath,keyWords);
        float[] coordinate = null;
        try{
            PdfReader pdfReader = new PdfReader(filepath);
            coordinate = getKeyWords(pdfReader, keyWords);
        } catch (IOException e) {
            log.error("获取坐标失败:", e);
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.LOCAL_PDF_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
        return coordinate;
    }

    /**
     * @Description 获取关键字所在PDF坐标
     * @param pdfReader
     * @param keyWords
     * @return float[]
     */
    private static float[] getKeyWords(PdfReader pdfReader, String keyWords) throws BusinessException {
        float[] coordinate = null;
        int page = 0;
        try{
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            CustomRenderListener renderListener = new CustomRenderListener();
            renderListener.setKeyWord(keyWords);
            for (page = 1; page <= pageNum; page++) {
                renderListener.setPage(page);
                pdfReaderContentParser.processContent(page, renderListener);
                coordinate = renderListener.getPcoordinate();
                if (coordinate != null) break;
            }
        } catch (IOException e) {
            log.error("获取关键字所在PDF坐标失败:", e);
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.LOCAL_PDF_ERROR.getCode());
            throw new BusinessException(code,"签署失败");
        }
        return coordinate;
    }


    public static void main(String[] args) throws BusinessException {
        /*String html = changeFtlToHtml(null,DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT);
       System.out.println(html);*//*
        List<ContractGoodsJsonReq> contractGoodsJsonReqs = new ArrayList<>();
        ContractGoodsJsonReq contractGoodsJsonReq = new ContractGoodsJsonReq();
        contractGoodsJsonReq.setGoodsId("--MiP4mrQaKLaMECqiX-1w");
        contractGoodsJsonReq.setGoodsCode("100022");
        contractGoodsJsonReq.setGoodsName("煤炭");
        contractGoodsJsonReq.setModel("111");
        contractGoodsJsonReq.setGoodsDesc("111111");
        contractGoodsJsonReq.setGoodMeasuring("35件 / 26吨 / 45m³");
        contractGoodsJsonReqs.add(contractGoodsJsonReq);
        String goodJson = JSONObject.toJSONString(contractGoodsJsonReqs);
        System.out.println(goodJson);
    */

    String filePath = "http://47.98.176.116:28007///upload/contract/81341627614122168a.pdf";

    String keyWords1 = "2vHBDO678LIs1sxligkzM";

    float[] keyWordsCoordinate1 = PdfHelper.getKeyWordsByPath(filePath, keyWords1);
        System.out.println(keyWords1);

        if (keyWordsCoordinate1.length>0) {
        for (int i = 0; i < keyWordsCoordinate1.length; i++) {

            System.out.println("坐标值: " + keyWordsCoordinate1[i]);
        }

    }
    String keyWords2 = "gabBDOAhePLIs1sxl221z3";
    float[] keyWordsCoordinate2 = PdfHelper.getKeyWordsByPath(filePath, keyWords2);
        System.out.println(keyWords2);
        if (keyWordsCoordinate2.length>0) {
        for (int i = 0; i < keyWordsCoordinate2.length; i++) {
            System.out.println("坐标值:" + keyWordsCoordinate2[i]);
        }
    }
    }
}
