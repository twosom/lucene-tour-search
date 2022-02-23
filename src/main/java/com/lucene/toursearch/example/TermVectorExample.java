package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.*;

import java.io.IOException;
import java.util.List;

public class TermVectorExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Analyzer analyzer = new KoreanAnalyzer();

        tourInfoList
                .stream()
                .filter(e -> e.getDescription().contains("357m"))
                .forEach(tourInfo -> {
                    analyzeTermVector(analyzer, tourInfo);
                });


    }

    private static void analyzeTermVector(Analyzer analyzer, TourInfo tourInfo) {
        System.out.println("원본문장 : " + tourInfo.getDescription());
        try (TokenStream tokenStream = analyzer.tokenStream("description", tourInfo.getDescription())) {
            // 토큰의 어휘 유형이다. 기본값은 word 다.
            TypeAttribute typeAttribute = tokenStream.addAttribute(TypeAttribute.class);
            // 구문 분석에 사용된다. 토큰스트림에서 이전 토큰과의 관계를 판별한다.
            PositionIncrementAttribute positionIncrementAttribute = tokenStream.addAttribute(PositionIncrementAttribute.class);
            PositionLengthAttribute positionLengthAttribute = tokenStream.addAttribute(PositionLengthAttribute.class);
            // 토큰의 시작 및 끝 문자 오프셋(시작값)이다.
            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            // 텀의 빈도
            TermFrequencyAttribute termFrequencyAttribute = tokenStream.addAttribute(TermFrequencyAttribute.class);
            // 토큰의 텍스트(텀)이다.
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            // 텀의 마지막 Bytes[] 인코딩을 사용자 정의한다.
            TermToBytesRefAttribute termToBytesRefAttribute = tokenStream.addAttribute(TermToBytesRefAttribute.class);
            tokenStream.reset();
            System.out.println();
            while (tokenStream.incrementToken()) {
                // 토큰들을 표시한다.
                System.out.println("typeAttribute : " + typeAttribute.type());
                System.out.println("positionIncrementAttribute : " + positionIncrementAttribute.getPositionIncrement());
                System.out.println("positionLengthAttribute : " + positionLengthAttribute.getPositionLength());
                System.out.println("offsetAttribute : " + offsetAttribute.startOffset());
                System.out.println("termFrequencyAttribute : " + termFrequencyAttribute.getTermFrequency());
                System.out.println("charTermAttribute :" + charTermAttribute);
                System.out.println("termToBytesRefAttribute :" + termToBytesRefAttribute.getBytesRef());
                System.out.println("===========================================");
            }
            tokenStream.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
