package excel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AnswerParser {

    private final List<Map<String, String>> table;
    private final static String FILE_NAME = "Marketing.json";

    public AnswerParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(FILE_NAME);
        table = this.filter(mapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>(){}));
    }

    private List<Map<String, String>> filter(List<Map<String, String>> table){
        return table.stream().filter(Objects::nonNull).filter(e -> e.get("ВОПРОС") != null).toList();
    }

    public Optional<Map<String, String>> getAnswer(String question){
        String finalQuestion = question.substring(1, question.length() - 1);
        return table.stream().filter(e -> e.get("ВОПРОС").contains(finalQuestion)).findFirst();
    }

    public List<Map<String, String>> getTable() {
        return table;
    }
}
