package com.ping.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "九三大阅兵.pdf";
        String content = """
                9月10日，国务院台办举行例行新闻发布会，发言人陈斌华答记者问。
                有记者问，九三阅兵引发岛内高度关注，台湾同胞纷纷赞叹祖国大陆辉煌的发展成就、强大的军事实力，表示增强了民族自豪感。同时对民进党当局及“台独”势力不自量力谋“独”挑衅，将把台湾带向险境而深感不安。对此有何评论？
                陈斌华表示，9月3日是中国人民抗日战争胜利纪念日，是海内外中华儿女扬眉吐气、永志难忘的“胜利日”，中国人民同热爱和平的世界各国人民一道，以一场盛大阅兵仪式，共同铭记历史、缅怀先烈、珍爱和平、开创未来。习近平总书记的重要讲话和九三阅兵展现了中华民族迈向伟大复兴的坚定信心，也是中国军队作为世界和平守护者的坚定宣示，更是一份中国人民推动迈向人类命运共同体的时代宣言。
                陈斌华说，台湾同胞有的现场观礼，更多的以各种方式观看阅兵直播，纷纷表示“九三阅兵是全民族的骄傲”，岛内媒体报道称“台湾民众无论是去现场，还是通过电视或网络观看或关注，都与有荣焉”。阅兵中一个个受阅方队威武雄壮，一件件新式武器装备震撼亮相，展示了解放军体系作战能力、新域新质战力，彰显了我们维护和平、捍卫正义的决心和能力，也彰显了我们维护统一、反对“台独”的决心和能力。包括台湾同胞在内全体中华儿女，无不为之欢欣鼓舞，无不为身为中国人而骄傲自豪。
                陈斌华强调，要和平、要发展、要交流、要合作是台湾社会的主流民意。赖清德当局不断增加防务预算，妄图“以武谋独”，只会让台湾更加兵凶战危，只会让台湾民众更加寝食难安。越来越多台湾同胞对此表示反对，是自然而然的事。希望广大台湾同胞同我们一道铭记历史、缅怀先烈，赓续爱国主义传统，弘扬伟大民族精神、伟大抗战精神，坚决反对“台独”分裂行径和外来干涉，坚定维护台海和平稳定，共襄祖国统一的千秋伟业，共创民族复兴的美好未来。
                (来源:人民日报客户端)
                On September 10, the Taiwan Affairs Office of the State Council held a regular press conference, where spokesperson Chen Binhua responded to questions from journalists.
                
                A reporter asked: The September 3 military parade has drawn significant attention in Taiwan. Compatriots in Taiwan have expressed admiration for the mainland’s remarkable developmental achievements and powerful military strength, noting that it has strengthened their sense of national pride. At the same time, they have expressed deep concern over the Tsai Ing-wen administration and "Taiwan independence" forces’ overestimation of their capabilities in pursuing independence provocations, which would push Taiwan into peril. What is your comment on this?
                
                Chen Binhua stated: September 3 is the Victory Day of the Chinese People’s War of Resistance Against Japanese Aggression, a day of triumph and an unforgettable "Victory Day" for Chinese people at home and abroad. Together with peace-loving people.
                """;
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}