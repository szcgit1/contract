package com.xtm.contract.utils;
import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: tong
 * @Date: 2021/7/2 10:44
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRenderListener implements RenderListener{

    /**
     * 坐标
     */
    private float[] pcoordinate = null;

    /**
     * 关键字
     */
    private String keyWord;

    /**
     * 页码
     */
    private int page;

    @Override
    public void beginTextBlock() {}

    @Override
    public void endTextBlock() {}

    @Override
    public void renderImage(ImageRenderInfo arg0) {}

    @Override
    public void renderText(TextRenderInfo textRenderInfo) {
        String text = textRenderInfo.getText();
        if (null != text && text.contains(keyWord)) {
            Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
            pcoordinate = new float[3];
            pcoordinate[0] = boundingRectange.x;
            pcoordinate[1] = boundingRectange.y;
            pcoordinate[2] = page;
        }
    }

}
