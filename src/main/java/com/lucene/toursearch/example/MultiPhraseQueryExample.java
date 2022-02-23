package com.lucene.toursearch.example;

import com.lucene.toursearch.helper.CsvLoader;
import com.lucene.toursearch.model.TourInfo;
import com.lucene.toursearch.service.IndexService;
import com.lucene.toursearch.service.SearchService;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;

public class MultiPhraseQueryExample {
    public static void main(String[] args) throws IOException {
        CsvLoader csvLoader = new CsvLoader();
        List<TourInfo> tourInfoList = csvLoader.readTourInfo();

        Directory directory = new ByteBuffersDirectory();
        IndexService indexService = new IndexService();
        indexService.indexTourInfo(directory, tourInfoList);
        System.out.println("====================================>> '서해안의'과 '체험하는' 혹은 '서해안의'과 '해돋이가' 사이가 하나의 단어가 포함된 여행정보");
        getMultiPhraseQuery(directory);
    }

    private static void getMultiPhraseQuery(Directory directory) {
        var term2 = new Term[]{
                new Term("description", "체험하는"),
                new Term("description", "해돋이가"),
                new Term("description", "석양을")
        };
        Query query = new MultiPhraseQuery.Builder()
                .add(new Term("description", "서해안의"))
                .add(term2)
                .setSlop(1)
                .build();

        SearchService searchService = new SearchService();
        searchService.getQueryResult(directory, query);
    }
}
