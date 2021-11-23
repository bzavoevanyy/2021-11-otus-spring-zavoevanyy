package ru.otus.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import ru.otus.domain.Question;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionDaoImpl implements QuestionDao {

    private final String quizFile;
    private final String[] headers = {"id", "question", "option1", "option2", "option3", "option4", "rightAnswer"};

    public QuestionDaoImpl(String quizFile) {
        this.quizFile = quizFile;
    }

    @Override
    public List<Question> findAll() {
        try {
            Reader in = new InputStreamReader(new ClassPathResource(quizFile).getInputStream());
            // Create csv parser
            final var parse = CSVParser.parse(in, CSVFormat.Builder
                    .create()
                    .setHeader(headers)
                    .setDelimiter(";")
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .build());
            List<CSVRecord> records = parse.getRecords();
            // Marshal csv records to Question objects, collect to List and return
            return records.stream().map((record) -> {
                int id = Integer.parseInt(record.get("id"));
                String question = record.get("question");
                List<String> options = Stream.of(record.get("option1"), record.get("option2"), record.get("option3"), record.get("option4"))
                        .filter(option -> Objects.nonNull(option) && !option.isEmpty()).collect(Collectors.toList());
                int rightAnswerIndex = Integer.parseInt(record.get("rightAnswer"));
                return new Question(id, question, options, rightAnswerIndex);
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
