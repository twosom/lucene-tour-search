package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class QueryParserExample {

    public static void main(String[] args) throws IOException, ParseException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        // Directory 를 사용한다.
        Directory directory = new ByteBuffersDirectory();

        // 색인을 한다.
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);

        // 분석 결과를 확인한다.
        System.out.println("====================================>> '해변'과 '시골'이 포함되고 '산'이 포함되지 않은 여행정보");
        getQueryParser(directory);
    }

    private static void getQueryParser(Directory directory) throws ParseException {
        QueryParser queryParser = new QueryParser("description", new KoreanAnalyzer());
        Query query = queryParser.parse("해변 AND 시골 NOT 산");
        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, query);
    }
}
