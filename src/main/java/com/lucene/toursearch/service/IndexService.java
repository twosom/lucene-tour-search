package com.lucene.toursearch.service;

import com.lucene.toursearch.model.TourInfo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

public class IndexService {

    public void indexTourInfo(Directory directory, List<TourInfo> tourInfoList) {

        Analyzer analyzer = new KoreanAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (IndexWriter writer = new IndexWriter(directory, config)) {
            tourInfoList.forEach(i -> addDocument(writer, i));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addDocument(IndexWriter writer, TourInfo tourInfo) {
        Document document = new Document();
        document.add(new TextField("pathName", tourInfo.getPathName(), Field.Store.YES));

        document.add(new TextField("courseName", tourInfo.getCourseName(), Field.Store.YES));
        // 정렬하려면 다음 값을 설정해야 한다.
        document.add(new SortedDocValuesField("courseName", new BytesRef(tourInfo.getCourseName())));

        document.add(new TextField("area", tourInfo.getArea(), Field.Store.YES));
        document.add(new StringField("level", tourInfo.getLevel(), Field.Store.YES));
        document.add(new StoredField("distance", tourInfo.getDistance()));
        double hour = 0;
        if (StringUtils.hasText(tourInfo.getHour())) {
            hour = Double.parseDouble(tourInfo.getHour());
        }
        document.add(new DoublePoint("hour", hour));

        // 깂을 저장하려면 동일한 이름의 StoredField 에 저장해야 한다.
        document.add(new StoredField("hour", hour));

        // 정렬하려면 다음 값을 설정해야 한다.
        document.add(new DoubleDocValuesField("hour", hour));

        document.add(new TextField("description", tourInfo.getDescription(), Field.Store.YES));


        try {
            writer.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
