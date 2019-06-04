package com.tencet.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Project thymeleaf-archetype.
 * Package: com.tencet.util
 * Description: 二维码/条形码生成器
 * Author: ZeroOneSummer
 * Date: 2019年06月01日 15:53
 */
public class FixCodeUtil {
    private static final Logger log = LoggerFactory.getLogger(FixCodeUtil.class);

    private enum CodeType{QR_CODE, BAR_CODE}
    private static String CONTENT;              //内容信息
    private static String FILE_NAME;            //图片名称
    private static String DES_PATH;             //存储路径
    private static String LOGO_PATH;            //LOGO图片地址
    private static Integer QR_SIZE;             //二维码规格
    private static Integer LOGO_SIZE;           //二维码中心LOGO规格
    private static Integer BAR_HEIGHT;          //条形码高度
    private static Integer BAR_WIDTH;           //条形码宽度
    private static Integer WORD_WIDTH;          //条形码文字宽度
    private static BarcodeFormat BAR_FORMAT;    //条形码类型
    private static String FORMAT;               //图片格式
    private static String CHARSET;              //编码格式
    private static Integer MARGIN;              //白边大小，取值范围0~4
    private static ErrorCorrectionLevel LEVEL;  //二维码容错率 L = ~7% /M = ~15% /Q = ~25% /H

    static {
        FORMAT = "jpg";
        CHARSET = "UTF-8";
        BAR_FORMAT = BarcodeFormat.CODE_128;
        BAR_WIDTH = 200;
        BAR_HEIGHT = 45;
        WORD_WIDTH = 70;
        LOGO_SIZE = 60;
        QR_SIZE = 300;
        MARGIN = 1;
        LEVEL = ErrorCorrectionLevel.H;
        CONTENT = "既然你诚心诚意的打开了，那我就用今天研究的成果说一句：\"老伙计，节日快乐鸭！\"，hahaha~~~";
        FILE_NAME = "zos";
        LOGO_PATH = "E:\\ideawork_git\\thymeleaf-archetype\\src\\main\\resource\\static\\img\\me.jpg";
        DES_PATH = "C:\\Users\\v_pijiang\\Desktop";
    }

    //测试
    public static void main(String[] args) throws Exception {
        String content = "H201906041905";
        //公共参数配置
         FixCodeUtil.config(content, null, null, null);
        //二维码
//        FixCodeUtil.encode(CodeType.QR_CODE);
        //条形码
        FixCodeUtil.encode(CodeType.BAR_CODE);
    }

    //---------------------------------- 公共 ----------------------------------

    /**
     * 生成码
     */
    public static void encode(CodeType type) throws Exception {
        String fileName = "";
        if(CodeType.QR_CODE.equals(type)){
            //二维码
            fileName = createQrCode();
            log.info("二维码【{}】生成完成", fileName);
        } else if(CodeType.BAR_CODE.equals(type)){
            //条形码
            fileName = createBarCode();
            log.info("条形码【{}】生成完成", fileName);
        }
    }

    /**
     * 参数设置
     */
    public static void config(String content, String fileName, String logoPath, String desPath){
        CONTENT = StringUtils.isBlank(content) ? FORMAT : content;
        FILE_NAME = StringUtils.isBlank(fileName) ? FILE_NAME : fileName;
        LOGO_PATH = StringUtils.isBlank(logoPath) ? LOGO_PATH : logoPath;
        DES_PATH = StringUtils.isBlank(desPath) ? DES_PATH : desPath;
    }

    //---------------------------------- 条形码 ----------------------------------

    /**
     * 创建条形码
     */
    public static String createBarCode(){
        String fileName = "";
        BufferedImage image = null;
        try {
            image = insertWords(getBarCode(CONTENT), CONTENT);
            fileName = FILE_NAME.substring(0, FILE_NAME.indexOf(".") > 0 ? FILE_NAME.indexOf(".") : FILE_NAME.length())
                    + "." + FORMAT.toLowerCase();
            ImageIO.write(image, FORMAT, new File(DES_PATH + "/" + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 生成条形码图片
     */
    public static BufferedImage getBarCode(String content){
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, LEVEL);
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            hints.put(EncodeHintType.MARGIN, MARGIN);
            Code128Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.CODE_128, BAR_WIDTH, BAR_HEIGHT, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加文字
     */
    public static BufferedImage insertWords(BufferedImage image, String words){
        // 新的图片，把带logo的二维码下面加上文字
        if (StringUtils.isNotEmpty(words)) {
            BufferedImage outImage = new BufferedImage(BAR_WIDTH, WORD_WIDTH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outImage.createGraphics();
            //抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
            g2d.setStroke(s);
            //设置背景为白色
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,600,600);
            g2d.setColor(Color.BLACK);
            //文字添加到原条形码中
            g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            Color color=new Color(0, 0, 0);
            g2d.setColor(color);
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            int strWidth = g2d.getFontMetrics().stringWidth(words);
            //文字居中
            int wordStartX = (BAR_WIDTH - strWidth) / 2;
            int wordStartY = BAR_HEIGHT + 20;
            g2d.drawString(words, wordStartX, wordStartY);
            g2d.dispose();
            outImage.flush();
            return outImage;
        }
        return null;
    }

    //---------------------------------- 二维码 ----------------------------------

    /**
     * 生成二维码
     */
    public static String createQrCode() throws Exception {
        BufferedImage image = createImage(CONTENT, LOGO_PATH);
        File file = new File(DES_PATH);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        String fileName = FILE_NAME.substring(0, FILE_NAME.indexOf(".") > 0 ? FILE_NAME.indexOf(".") : FILE_NAME.length())
                + "." + FORMAT.toLowerCase();
        ImageIO.write(image, FORMAT, new File(DES_PATH + "/" + fileName));
        return fileName;
    }

    /**
     * 生成图片
     * @param content	内容
     * @param logoPath	logo路径
     */
    public static BufferedImage createImage(String content, String logoPath) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, LEVEL);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, MARGIN);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }
        //压缩插入logo图片
        insertLogo(image, logoPath);
        return image;
    }

    /**
     * 插入二维码中心LOGO
     */
    public static void insertLogo(BufferedImage source, String logoPath) throws Exception {
        //获取logo
        File file = new File(logoPath);
        if (!file.exists()){
            throw new Exception("logo file not found.");
        }

        //读取二维码图片，并构建绘图对象
        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null) > LOGO_SIZE ? LOGO_SIZE : src.getWidth(null);
        int height = src.getHeight(null) > LOGO_SIZE ? LOGO_SIZE : src.getHeight(null);
        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //绘制缩小后的图
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        src = image;

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QR_SIZE - width) / 2;
        int y = (QR_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 解析二维码
     * @param qrCodePath 二维码图片地址
     * @throws Exception
     */
    public static String qrDeCode(String qrCodePath) throws Exception {
        BufferedImage image;
        image = ImageIO.read(new File(qrCodePath));
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        //复杂模式，开启PURE_BARCODE模式,带图片LOGO的解码方案,否则会出现NotFoundException
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        log.info("解析内容：{}", resultStr);
        return resultStr;
    }
}
