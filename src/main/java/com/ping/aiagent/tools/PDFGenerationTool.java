package com.ping.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.ping.aiagent.contant.FileConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PDFGenerationTool {

    private static final Logger logger = LoggerFactory.getLogger(PDFGenerationTool.class);

    // 字体回退列表
    private static final String[] FONT_FALLBACKS = {
            // 常见的中文字体
            "STSongStd-Light",
            "STSong-Light",
            "MSungStd-Light",
            "MHei-Medium",
            "SimSun",
            "SimHei",
            "Microsoft YaHei",
            "NotoSansCJK-Regular",
            // 最后回退到标准字体
            "Helvetica"
    };

    // 对应的编码
    private static final String[] ENCODINGS = {
            "UniGB-UCS2-H",
            "UniGB-UTF16-H",
            "Identity-H",
            PdfEncodings.IDENTITY_H,
            PdfEncodings.WINANSI
    };

    @Tool(description = "Generate a PDF file with given content, supports Chinese characters")
    public String generatePDF(
            @ToolParam(description = "Name of the file to save the generated PDF (should end with .pdf)") String fileName,
            @ToolParam(description = "Content to be included in the PDF") String content) {

        // 确保文件名以.pdf结尾
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            fileName += ".pdf";
        }

        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName;

        try {
            // 创建目录
            FileUtil.mkdir(fileDir);

            // 获取可用的字体
            PdfFont font = getAvailableFont();

            // 创建 PdfWriter 和 PdfDocument 对象
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // 设置字体
                document.setFont(font);

                // 创建段落，支持换行
                String[] lines = content.split("\\r?\\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Paragraph paragraph = new Paragraph(line)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMarginBottom(5f);
                        document.add(paragraph);
                    } else {
                        // 空行处理
                        document.add(new Paragraph(" ").setMarginBottom(5f));
                    }
                }
            }

            logger.info("PDF generated successfully: {}", filePath);
            return "PDF文件生成成功，保存路径: " + filePath;

        } catch (Exception e) {
            logger.error("Error generating PDF: {}", e.getMessage(), e);
            return "PDF生成失败: " + e.getMessage();
        }
    }

    /**
     * 获取可用的字体，按优先级尝试加载
     */
    private PdfFont getAvailableFont() throws IOException {
        // 首先尝试加载自定义字体文件
        PdfFont customFont = tryLoadCustomFont();
        if (customFont != null) {
            logger.info("Successfully loaded custom font");
            return customFont;
        }

        // 尝试系统内置字体
        for (String fontName : FONT_FALLBACKS) {
            for (String encoding : ENCODINGS) {
                try {
                    PdfFont font = PdfFontFactory.createFont(fontName, encoding);
                    logger.info("Successfully loaded font: {} with encoding: {}", fontName, encoding);
                    return font;
                } catch (Exception e) {
                    logger.debug("Failed to load font: {} with encoding: {}, trying next...", fontName, encoding);
                }
            }
        }

        // 最后回退到标准字体
        logger.warn("All Chinese fonts failed, falling back to standard font");
        return PdfFontFactory.createFont();
    }

    /**
     * 尝试加载自定义字体文件
     */
    private PdfFont tryLoadCustomFont() {
        String[] customFontPaths = {
                "src/main/resources/static/fonts/simsun.ttf",
                "src/main/resources/fonts/simsun.ttf",
                "fonts/simsun.ttf",
                "static/fonts/simsun.ttf",
                // Windows系统字体路径
                "C:/Windows/Fonts/simsun.ttc",
                "C:/Windows/Fonts/simhei.ttf",
                "C:/Windows/Fonts/msyh.ttc",
                // Linux系统字体路径
                "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
                "/System/Library/Fonts/PingFang.ttc" // macOS
        };

        for (String fontPath : customFontPaths) {
            try {
                if (Files.exists(Paths.get(fontPath))) {
                    PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
                    logger.info("Successfully loaded custom font from: {}", fontPath);
                    return font;
                }
            } catch (Exception e) {
                logger.debug("Failed to load custom font from: {}, error: {}", fontPath, e.getMessage());
            }
        }

        return null;
    }

    /**
     * 检查字体是否支持中文字符
     */
    private boolean supportsChinese(PdfFont font) {
        try {
            // 测试一个常见的中文字符
            return font.containsGlyph('中');
        } catch (Exception e) {
            return false;
        }
    }
}