package com.bzavoevanyy.dao;

import com.bzavoevanyy.config.QuestionDaoProps;
import com.bzavoevanyy.domain.AnswerOption;
import com.bzavoevanyy.domain.Question;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {

    private final QuestionDaoProps props;

    @Override
    public List<Question> findAll() throws QuestionsLoadingExceptions {
        val quizFile = props.getFileName();
        try (final Reader in = new InputStreamReader(new ClassPathResource(quizFile).getInputStream());
             // Create csv parser
             final CSVParser parse = CSVParser.parse(in, CSVFormat.Builder
                     .create()
                     .setHeader(props.getHeaders())
                     .setDelimiter(";")
                     .setAllowMissingColumnNames(false)
                     .setSkipHeaderRecord(true)
                     .setIgnoreEmptyLines(true)
                     .build())) {
            List<CSVRecord> records = parse.getRecords();
            if (records.isEmpty()) {
                throw new UnsupportedCSVFileException("Couldn't read csv file");
            }
            return marshalRecordsToList(records);
        } catch (Exception e) {
            throw new QuestionsLoadingExceptions("Wrong CSV file:" + e.getMessage(), e);
        }
    }

    // Marshal csv records to Question objects, collect to List and return
    private List<Question> marshalRecordsToList(List<CSVRecord> records) {
        return records.stream().map((record) -> {
            int id = Integer.parseInt(record.get("id"));
            String question = record.get("question");
            List<AnswerOption> options = Stream.of(
                            record.get("option1"),
                            record.get("option2"),
                            record.get("option3"),
                            record.get("option4"))
                    .filter(option -> Objects.nonNull(option) && !option.isEmpty())
                    .map(AnswerOption::new)
                    .collect(Collectors.toList());
            int rightAnswerIndex = Integer.parseInt(record.get("rightAnswer"));
            options.get(rightAnswerIndex - 1).setRight(true);
            return new Question(id, question, options);
        }).collect(Collectors.toList());
    }
}
