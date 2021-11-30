package ru.otus.dao;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.domain.Question;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final String quizFile;
    private final String[] headers = {"id", "question", "option1", "option2", "option3", "option4", "rightAnswer"};

    public QuestionDaoImpl(@Value("${quiz.source}") String quizFile) {
        this.quizFile = quizFile;
    }

    @Override
    public List<Question> findAll() throws QuestionsLoadingExceptions {
        try (final Reader in = new InputStreamReader(new ClassPathResource(quizFile).getInputStream());
             // Create csv parser
             final CSVParser parse = CSVParser.parse(in, CSVFormat.Builder
                     .create()
                     .setHeader(headers)
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
            List<String> options = Stream.of(record.get("option1"), record.get("option2"), record.get("option3"), record.get("option4"))
                    .filter(option -> Objects.nonNull(option) && !option.isEmpty()).collect(Collectors.toList());
            int rightAnswerIndex = Integer.parseInt(record.get("rightAnswer"));
            return new Question(id, question, options, rightAnswerIndex);
        }).collect(Collectors.toList());
    }
}
