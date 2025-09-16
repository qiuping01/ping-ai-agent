package com.ping.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.ping.aiagent.contant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PDF生成工具
 * <p>
 * 支持Markdown语法解析，生成精美的PDF文档
 */
@Slf4j
public class PDFGenerationTool {

    // Markdown语法正则表达式
    private static final Pattern HEADER_PATTERN = Pattern.compile("^(#{1,6})\\s+(.+)$", Pattern.MULTILINE);
    private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static final Pattern ITALIC_PATTERN = Pattern.compile("\\*(.+?)\\*");
    private static final Pattern LIST_PATTERN = Pattern.compile("^[-*+]\\s+(.+)$", Pattern.MULTILINE);
    private static final Pattern NUMBERED_LIST_PATTERN = Pattern.compile("^\\d+\\.\\s+(.+)$", Pattern.MULTILINE);
    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("```[\\s\\S]*?```");
    private static final Pattern INLINE_CODE_PATTERN = Pattern.compile("`(.+?)`");

    @Tool(description = "Generate a beautifully formatted PDF file from content that may contain Markdown syntax")
    public String generatePDF(
            @ToolParam(description = "Name of the file to save the generated PDF (without extension)") String fileName,
            @ToolParam(description = "Content to be included in the PDF, may contain Markdown syntax") String content) {

        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName + ".pdf";

        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            log.info("开始生成PDF文件: {}", filePath);

            // 创建PDF文档
            try (PdfWriter writer = new PdfWriter(filePath);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // 设置中文字体
                PdfFont font = createChineseFont();
                PdfFont boldFont = createChineseBoldFont();
                document.setFont(font);

                // 设置页边距
                document.setMargins(50, 50, 50, 50);

                // 解析并添加内容
                parseAndAddContent(document, content, font, boldFont);
            }

            log.info("PDF文件生成成功: {}", filePath);
            return "PDF generated successfully to: " + filePath;

        } catch (Exception e) {
            log.error("生成PDF文件失败: {}", e.getMessage(), e);
            return "Error generating PDF: " + e.getMessage();
        }
    }

    /**
     * 创建中文字体
     */
    private PdfFont createChineseFont() throws IOException {
        try {
            // 尝试使用系统字体
            return PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        } catch (Exception e) {
            log.warn("无法加载中文字体，使用默认字体");
            return PdfFontFactory.createFont();
        }
    }

    /**
     * 创建中文粗体字体
     */
    private PdfFont createChineseBoldFont() throws IOException {
        try {
            // 尝试使用粗体字体
            return PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        } catch (Exception e) {
            // 如果无法加载中文字体，使用默认字体
            log.warn("无法加载中文粗体字体，使用默认字体");
            return PdfFontFactory.createFont();
        }
    }

    /**
     * 解析Markdown内容并添加到PDF文档
     */
    private void parseAndAddContent(Document document, String content, PdfFont font, PdfFont boldFont) {
        // 预处理：移除代码块（避免干扰其他解析）
        content = removeCodeBlocks(content);

        // 按行分割内容
        String[] lines = content.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                // 空行，添加间距
                document.add(new Paragraph(" ").setMarginBottom(5));
                continue;
            }

            // 处理标题
            if (line.matches("^#{1,6}\\s+.+")) {
                addHeader(document, line, boldFont);
            }
            // 处理无序列表
            else if (line.matches("^[-*+]\\s+.+")) {
                List list = new List();
                list.setFont(font);

                // 收集连续的列表项
                while (i < lines.length && lines[i].trim().matches("^[-*+]\\s+.+")) {
                    String listItem = lines[i].trim().replaceFirst("^[-*+]\\s+", "");
                    listItem = cleanMarkdownSyntax(listItem);
                    list.add(new ListItem(listItem));
                    i++;
                }
                i--; // 回退一行，因为外层循环会自增

                document.add(list.setMarginBottom(10));
            }
            // 处理有序列表
            else if (line.matches("^\\d+\\.\\s+.+")) {
                List list = new List();
                list.setFont(font);
                list.setListSymbol("");

                int listNumber = 1;
                while (i < lines.length && lines[i].trim().matches("^\\d+\\.\\s+.+")) {
                    String listItem = lines[i].trim().replaceFirst("^\\d+\\.\\s+", "");
                    listItem = cleanMarkdownSyntax(listItem);
                    list.add(new ListItem(listNumber + ". " + listItem));
                    listNumber++;
                    i++;
                }
                i--; // 回退一行

                document.add(list.setMarginBottom(10));
            }
            // 处理普通段落
            else {
                String cleanLine = cleanMarkdownSyntax(line);
                if (!cleanLine.isEmpty()) {
                    Paragraph paragraph = new Paragraph(cleanLine)
                            .setFont(font)
                            .setMarginBottom(8)
                            .setTextAlignment(TextAlignment.JUSTIFIED);
                    document.add(paragraph);
                }
            }
        }
    }

    /**
     * 添加标题
     */
    private void addHeader(Document document, String line, PdfFont boldFont) {
        Matcher matcher = HEADER_PATTERN.matcher(line);
        if (matcher.find()) {
            String hashes = matcher.group(1);
            String title = matcher.group(2);

            int level = hashes.length();
            float fontSize = Math.max(20 - (level - 1) * 2, 12); // 根据级别调整字体大小

            Paragraph header = new Paragraph(title)
                    .setFont(boldFont)
                    .setFontSize(fontSize)
                    .setMarginTop(level == 1 ? 20 : 15)
                    .setMarginBottom(10);

            // 一级标题居中
            if (level == 1) {
                header.setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.DARK_GRAY);
            }

            document.add(header);
        }
    }

    /**
     * 清理Markdown语法标记
     */
    private String cleanMarkdownSyntax(String text) {
        // 移除粗体标记
        text = text.replaceAll("\\*\\*(.+?)\\*\\*", "$1");
        // 移除斜体标记
        text = text.replaceAll("(?<!\\*)\\*([^*]+?)\\*(?!\\*)", "$1");
        // 移除行内代码标记
        text = text.replaceAll("`(.+?)`", "$1");
        // 移除链接标记
        text = text.replaceAll("\\[(.+?)\\]\\(.+?\\)", "$1");

        return text;
    }

    /**
     * 移除代码块
     */
    private String removeCodeBlocks(String content) {
        return content.replaceAll("```[\\s\\S]*?```", "");
    }
}