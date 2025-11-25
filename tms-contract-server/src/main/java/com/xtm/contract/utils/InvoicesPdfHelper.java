package com.xtm.contract.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
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
public class InvoicesPdfHelper {

    private static final String PATH = "/templates/";
    // 结算单模板 （西拓）
    public static final String INVOICES_TEMPL = "invoicesTempl.ftl";
    // 结算单模板 （尊俊）
    public static final String ZJ_INVOICES_TEMPL = "zjInvoicesTemp.ftl";

    public static String changeFtlToHtml(Map<String, Object> map,String templateName) throws BusinessException {
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
            configuration.setTemplateLoader(new ClassTemplateLoader(InvoicesPdfHelper.class, PATH));
            //设置字符集
            configuration.setDefaultEncoding("utf-8");
            //将生成的内容写入html文件中
            Template template = configuration.getTemplate(templateName);
            htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);
            htmlStr = htmlStr.replace("<br>", "<br/>").replaceAll("&nbsp;"," ");
            log.info("##############转换html成功！##################");
        } catch (IOException ioException) {
            log.info("##############转换html错误！##################:",ioException);
            return null;
        } catch (TemplateException templateException) {
            log.info("##############转换html错误！##################",templateException);
            return null;
        } catch (Exception e) {
            log.info("##############转换html错误！##################",e);
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

}
