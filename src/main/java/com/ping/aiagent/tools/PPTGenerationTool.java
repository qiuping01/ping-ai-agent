package com.ping.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.ping.aiagent.contant.FileConstant;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PPTGenerationTool {
    
    private static final Logger logger = LoggerFactory.getLogger(PPTGenerationTool.class);
    
    // 预定义的主题颜色
    private static final Color[] THEME_COLORS = {
        new Color(68, 114, 196),   // 蓝色主题
        new Color(112, 173, 71),   // 绿色主题
        new Color(255, 192, 0),    // 橙色主题
        new Color(91, 155, 213),   // 浅蓝色主题
        new Color(237, 125, 49)    // 橙红色主题
    };
    
    // 字体回退列表
    private static final String[] FONT_FALLBACKS = {
        "Microsoft YaHei",
        "SimSun",
        "SimHei",
        "Arial Unicode MS",
        "Calibri",
        "Arial"
    };

    @Tool(description = "Generate a PowerPoint presentation with given content, supports multiple slides and Chinese characters")
    public String generatePPT(
            @ToolParam(description = "Name of the file to save the generated PPT (should end with .pptx)") String fileName,
            @ToolParam(description = "Content for the presentation. Use '---SLIDE---' to separate slides, '###TITLE###' for slide titles") String content,
            @ToolParam(description = "Theme color index (0-4), optional, default is 0") Integer themeIndex) {
        
        // 确保文件名以.pptx结尾
        if (!fileName.toLowerCase().endsWith(".pptx")) {
            fileName += ".pptx";
        }
        
        String fileDir = FileConstant.FILE_SAVE_DIR + "/ppt";
        String filePath = fileDir + "/" + fileName;
        
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            
            // 创建PowerPoint演示文稿
            XMLSlideShow ppt = new XMLSlideShow();
            
            // 设置主题颜色
            Color themeColor = getThemeColor(themeIndex);
            
            // 解析内容并创建幻灯片
            createSlidesFromContent(ppt, content, themeColor);
            
            // 保存文件
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                ppt.write(out);
            }
            
            ppt.close();
            
            logger.info("PPT generated successfully: {}", filePath);
            return "PPT文件生成成功，保存路径: " + filePath;
            
        } catch (Exception e) {
            logger.error("Error generating PPT: {}", e.getMessage(), e);
            return "PPT生成失败: " + e.getMessage();
        }
    }
    
    @Tool(description = "Generate a PowerPoint presentation from structured data with title, slides content")
    public String generateStructuredPPT(
            @ToolParam(description = "Name of the file to save the generated PPT") String fileName,
            @ToolParam(description = "Title of the presentation") String title,
            @ToolParam(description = "Slides content, each slide separated by '---', format: 'SlideTitle|SlideContent'") String slidesContent,
            @ToolParam(description = "Theme color index (0-4), optional") Integer themeIndex) {
        
        if (!fileName.toLowerCase().endsWith(".pptx")) {
            fileName += ".pptx";
        }
        
        String fileDir = FileConstant.FILE_SAVE_DIR + "/ppt";
        String filePath = fileDir + "/" + fileName;
        
        try {
            FileUtil.mkdir(fileDir);
            
            XMLSlideShow ppt = new XMLSlideShow();
            Color themeColor = getThemeColor(themeIndex);
            
            // 创建标题页
            createTitleSlide(ppt, title, themeColor);
            
            // 创建内容页
            String[] slides = slidesContent.split("---");
            for (String slideData : slides) {
                if (slideData.trim().isEmpty()) continue;
                
                String[] parts = slideData.split("\\|", 2);
                String slideTitle = parts.length > 0 ? parts[0].trim() : "无标题";
                String slideContent = parts.length > 1 ? parts[1].trim() : "";
                
                createContentSlide(ppt, slideTitle, slideContent, themeColor);
            }
            
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                ppt.write(out);
            }
            
            ppt.close();
            
            logger.info("Structured PPT generated successfully: {}", filePath);
            return "结构化PPT文件生成成功，保存路径: " + filePath;
            
        } catch (Exception e) {
            logger.error("Error generating structured PPT: {}", e.getMessage(), e);
            return "结构化PPT生成失败: " + e.getMessage();
        }
    }
    
    /**
     * 从内容字符串创建幻灯片
     */
    private void createSlidesFromContent(XMLSlideShow ppt, String content, Color themeColor) {
        String[] slides = content.split("---SLIDE---");
        
        for (int i = 0; i < slides.length; i++) {
            String slideContent = slides[i].trim();
            if (slideContent.isEmpty()) continue;
            
            if (i == 0) {
                // 第一张幻灯片作为标题页
                createTitleSlideFromContent(ppt, slideContent, themeColor);
            } else {
                // 其他幻灯片作为内容页
                createContentSlideFromContent(ppt, slideContent, themeColor);
            }
        }
        
        // 如果没有内容，创建一个默认幻灯片
        if (slides.length == 0 || (slides.length == 1 && slides[0].trim().isEmpty())) {
            createDefaultSlide(ppt, content, themeColor);
        }
    }
    
    /**
     * 创建标题幻灯片 - 使用简单的文本框方式
     */
    private void createTitleSlide(XMLSlideShow ppt, String title, Color themeColor) {
        XSLFSlide slide = ppt.createSlide();
        
        // 创建标题文本框
        XSLFTextBox titleBox = slide.createTextBox();
        titleBox.setAnchor(new Rectangle(50, 150, 600, 100));
        titleBox.clearText();
        
        XSLFTextParagraph titlePara = titleBox.addNewTextParagraph();
        XSLFTextRun titleRun = titlePara.addNewTextRun();
        titleRun.setText(title);
        titleRun.setFontSize(44.0);
        titleRun.setFontColor(themeColor);
        titleRun.setFontFamily(getAvailableFont());
        titleRun.setBold(true);
        titlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        
        // 创建副标题文本框
        XSLFTextBox subtitleBox = slide.createTextBox();
        subtitleBox.setAnchor(new Rectangle(50, 280, 600, 50));
        subtitleBox.clearText();
        
        XSLFTextParagraph subtitlePara = subtitleBox.addNewTextParagraph();
        XSLFTextRun subtitleRun = subtitlePara.addNewTextRun();
        subtitleRun.setText("由AI智能生成");
        subtitleRun.setFontSize(18.0);
        subtitleRun.setFontColor(Color.GRAY);
        subtitleRun.setFontFamily(getAvailableFont());
        subtitlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
    }
    
    /**
     * 从内容创建标题幻灯片
     */
    private void createTitleSlideFromContent(XMLSlideShow ppt, String content, Color themeColor) {
        String[] lines = content.split("\\r?\\n");
        String title = "演示文稿";
        
        // 查找标题标记
        for (String line : lines) {
            if (line.startsWith("###TITLE###")) {
                title = line.replace("###TITLE###", "").trim();
                break;
            }
        }
        
        // 如果没有找到标题标记，使用第一行作为标题
        if (title.equals("演示文稿") && lines.length > 0 && !lines[0].trim().isEmpty()) {
            title = lines[0].trim();
        }
        
        createTitleSlide(ppt, title, themeColor);
    }
    
    /**
     * 创建内容幻灯片 - 使用简单的文本框方式
     */
    private void createContentSlide(XMLSlideShow ppt, String title, String content, Color themeColor) {
        XSLFSlide slide = ppt.createSlide();
        
        // 创建标题文本框
        XSLFTextBox titleBox = slide.createTextBox();
        titleBox.setAnchor(new Rectangle(50, 50, 600, 80));
        titleBox.clearText();
        
        XSLFTextParagraph titlePara = titleBox.addNewTextParagraph();
        XSLFTextRun titleRun = titlePara.addNewTextRun();
        titleRun.setText(title);
        titleRun.setFontSize(32.0);
        titleRun.setFontColor(themeColor);
        titleRun.setFontFamily(getAvailableFont());
        titleRun.setBold(true);
        
        // 创建内容文本框
        XSLFTextBox contentBox = slide.createTextBox();
        contentBox.setAnchor(new Rectangle(50, 150, 600, 350));
        contentBox.clearText();
        
        String[] lines = content.split("\\r?\\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                XSLFTextParagraph para = contentBox.addNewTextParagraph();
                XSLFTextRun run = para.addNewTextRun();
                
                // 如果是列表项，添加项目符号
                if (line.trim().startsWith("•") || line.trim().startsWith("-") || line.trim().startsWith("*")) {
                    para.setBullet(true);
                    run.setText(line.trim().substring(1).trim());
                } else {
                    run.setText(line.trim());
                }
                
                run.setFontSize(18.0);
                run.setFontColor(Color.BLACK);
                run.setFontFamily(getAvailableFont());
            }
        }
    }
    
    /**
     * 从内容创建内容幻灯片
     */
    private void createContentSlideFromContent(XMLSlideShow ppt, String content, Color themeColor) {
        String[] lines = content.split("\\r?\\n");
        String title = "内容";
        StringBuilder contentBuilder = new StringBuilder();
        
        // 查找标题
        for (String line : lines) {
            if (line.startsWith("###TITLE###")) {
                title = line.replace("###TITLE###", "").trim();
            } else if (!line.trim().isEmpty()) {
                contentBuilder.append(line).append("\n");
            }
        }
        
        createContentSlide(ppt, title, contentBuilder.toString().trim(), themeColor);
    }
    
    /**
     * 创建默认幻灯片
     */
    private void createDefaultSlide(XMLSlideShow ppt, String content, Color themeColor) {
        createContentSlide(ppt, "内容", content, themeColor);
    }
    
    /**
     * 获取主题颜色
     */
    private Color getThemeColor(Integer themeIndex) {
        if (themeIndex == null || themeIndex < 0 || themeIndex >= THEME_COLORS.length) {
            return THEME_COLORS[0];
        }
        return THEME_COLORS[themeIndex];
    }
    
    /**
     * 获取可用的字体
     */
    private String getAvailableFont() {
        // 返回常用的中文字体
        return FONT_FALLBACKS[0]; // Microsoft YaHei
    }
    
    /**
     * 添加图片到幻灯片（可选功能）
     */
    public String addImageToSlide(String pptPath, String imagePath, int slideIndex) {
        try {
            if (!Files.exists(Paths.get(pptPath)) || !Files.exists(Paths.get(imagePath))) {
                return "文件不存在";
            }
            
            try (FileInputStream fis = new FileInputStream(pptPath);
                 XMLSlideShow ppt = new XMLSlideShow(fis)) {
                
                List<XSLFSlide> slides = ppt.getSlides();
                if (slideIndex >= slides.size()) {
                    return "幻灯片索引超出范围";
                }
                
                XSLFSlide slide = slides.get(slideIndex);
                
                // 读取图片
                byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
                XSLFPictureData pictureData = ppt.addPicture(imageBytes, PictureData.PictureType.PNG);
                
                // 添加图片到幻灯片
                XSLFPictureShape picture = slide.createPicture(pictureData);
                picture.setAnchor(new Rectangle(100, 100, 400, 300));
                
                // 保存修改后的PPT
                try (FileOutputStream out = new FileOutputStream(pptPath)) {
                    ppt.write(out);
                }
                
                return "图片添加成功";
            }
            
        } catch (Exception e) {
            logger.error("Error adding image to PPT: {}", e.getMessage(), e);
            return "添加图片失败: " + e.getMessage();
        }
    }
}