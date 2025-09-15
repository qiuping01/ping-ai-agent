package com.ping.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 恋爱文档工具
 * 生成恋爱文档PDF并发送给指定用户
 */
@Component
public class LoveDocumentTool {

    private final EmailTool emailTool;

    private final String defaultFrom;

    public LoveDocumentTool(EmailTool emailTool,@Value("${spring.mail.username}") String defaultFrom) {
        this.emailTool = emailTool;
        this.defaultFrom = defaultFrom;
    }

    @Tool(name = "generateAndSendLoveDocument",
            description = "Generate a love document PDF and send it to the specified email address")
    public String generateAndSendLoveDocument(
            @ToolParam(description = "Recipient email address") String recipientEmail,
            @ToolParam(description = "Content of the love document") String loveContent,
            @ToolParam(description = "Email subject (optional)") String subject) {

        try {
            // 1. 生成PDF文件名（使用时间戳确保唯一性）
            String fileName = "love-document-" + System.currentTimeMillis() + ".pdf";

            // 2. 直接创建 PDFGenerationTool 实例并生成PDF
            PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
            String pdfResult = pdfGenerationTool.generatePDF(fileName, loveContent);

            if (pdfResult.contains("Error")) {
                return pdfResult; // 返回错误信息
            }

            // 3. 提取PDF文件路径
            String filePath = pdfResult.replace("PDF generated successfully to: ", "");

            // 4. 创建HTML格式的邮件内容
            String htmlContent = createLoveDocumentEmailContent(loveContent);

            // 5. 设置邮件主题（如果未提供则使用默认主题）
            String emailSubject = (subject != null && !subject.trim().isEmpty()) ?
                    subject : "💌 您的个性化恋爱文档";

            // 6. 发送邮件
            String emailResult = emailTool.sendMimeEmail(emailSubject, htmlContent, recipientEmail, filePath);

            return emailResult;
        } catch (Exception e) {
            return "Error generating and sending love document: " + e.getMessage();
        }
    }

    /**
     * 创建恋爱文档邮件的HTML内容
     */
    private String createLoveDocumentEmailContent(String loveContent) {
        // 对内容进行HTML转义和换行处理
        String formattedContent = escapeHtml(loveContent)
                .replace("\n", "<br>")
                .replace("\r", "");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>AI恋爱大师：您的恋爱文档</title>" +
                "    <style>" +
                "        @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700&family=Source+Sans+Pro:wght@300;400;600&display=swap');" +
                "        body { " +
                "            font-family: 'Source Sans Pro', sans-serif; " +
                "            line-height: 1.6; " +
                "            color: #555; " +
                "            margin: 0; " +
                "            padding: 0; " +
                "            background-color: #f9f7f7;" +
                "        }" +
                "        .container { " +
                "            max-width: 650px; " +
                "            margin: 30px auto; " +
                "            background: white; " +
                "            border-radius: 12px; " +
                "            overflow: hidden; " +
                "            box-shadow: 0 5px 15px rgba(0,0,0,0.08);" +
                "        }" +
                "        .header { " +
                "            background: linear-gradient(135deg, #ff6b6b 0%, #ff8e8e 100%); " +
                "            color: white; " +
                "            padding: 35px 30px; " +
                "            text-align: center; " +
                "        }" +
                "        .header h1 { " +
                "            font-family: 'Playfair Display', serif; " +
                "            margin: 0; " +
                "            font-size: 32px; " +
                "            font-weight: 700;" +
                "            letter-spacing: 1px;" +
                "        }" +
                "        .content { " +
                "            padding: 35px 40px; " +
                "            background-color: #fff; " +
                "        }" +
                "        .love-content { " +
                "            background-color: #fff9f9; " +
                "            border-left: 4px solid #ff6b6b; " +
                "            padding: 25px; " +
                "            margin: 25px 0; " +
                "            border-radius: 0 8px 8px 0;" +
                "            font-size: 16px;" +
                "            line-height: 1.8;" +
                "        }" +
                "        .love-content p { " +
                "            margin: 0 0 15px 0;" +
                "        }" +
                "        .love-content p:last-child { " +
                "            margin-bottom: 0;" +
                "        }" +
                "        .footer { " +
                "            margin-top: 30px; " +
                "            text-align: center; " +
                "            font-size: 13px; " +
                "            color: #888; " +
                "            padding: 20px;" +
                "            background-color: #f5f5f5;" +
                "        }" +
                "        .signature { " +
                "            margin-top: 25px; " +
                "            color: #ff6b6b; " +
                "            font-style: italic; " +
                "        }" +
                "        .attachment-note {" +
                "            background-color: #f0f9ff;" +
                "            padding: 15px;" +
                "            border-radius: 8px;" +
                "            margin-top: 25px;" +
                "            font-size: 14px;" +
                "            border-left: 4px solid #4dabf7;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>" +
                "            <h1>💖 恋爱文档 💖</h1>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <p>亲爱的用户，</p>" +
                "            <p>我们已根据您的要求精心准备了这份恋爱文档，希望它能成为您爱情故事中的美好一页。</p>" +
                "            " +
                "            <div class='love-content'>" +
                formattedContent +
                "            </div>" +
                "            " +
                "            <div class='attachment-note'>" +
                "                <p>📎 <strong>附件说明</strong>：完整的精美排版PDF文档已附加到本邮件中，您可以下载保存或打印珍藏。</p>" +
                "            </div>" +
                "            " +
                "            <p class='signature'>祝您爱情甜蜜，幸福永驻！<br>您的AI恋爱助手</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>此邮件由AI恋爱助手自动生成</p>" +
                "            <p>如有任何疑问，请联系我们的客服支持</p>" +
                "            <p>© 2025 qp · 守护您的每一份感动</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    // HTML转义工具方法，防止XSS攻击并确保内容正确显示
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}