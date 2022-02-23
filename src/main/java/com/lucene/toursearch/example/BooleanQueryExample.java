package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.search.BooleanClause.Occur.SHOULD;

public class BooleanQueryExample {

    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();
        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);

        System.out.println("====================================>> 해변, 설악산 텀이 포함된 여행정보");

        getBooleanQuery(directory);
    }

    private static void getBooleanQuery(Directory directory) {
        Query condition1 = new TermQuery(new Term("description", "해변"));
        Query condition2 = new TermQuery(new Term("description", "설악산"));

        Query booleanQuery = new BooleanQuery.Builder()
                .add(new BooleanClause(condition1, SHOULD))
                .add(new BooleanClause(condition2, SHOULD))
                .build();

        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, booleanQuery);
    }
}
