package com.lucene.toursearch.helper;

import com.lucene.toursearch.model.TourInfo;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * @author twosom
 * @since 2022/02/20
 */
public class CsvLoader {

    public List<TourInfo> readTourInfo() throws IOException {
        var path = getFilePath();
        List<TourInfo> tourInfoList = null;
        try (Reader reader = Files.newBufferedReader(path)) {
            var csvToBean = new CsvToBeanBuilder<TourInfo>(reader)
                    .withType(TourInfo.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSkipLines(1)
                    .build();
            tourInfoList = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tourInfoList;
    }

    private Path getFilePath() throws IOException {
        var filePath = new ClassPathResource("한국관광공사_걷는길_인코딩_정제본.csv").getFile().getAbsolutePath();
        return Paths.get(filePath);
    }
}
