package excel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AnswerParser {

    private final List<Map<String, String>> table;
    private final static String FILE_NAME = "Marketing.json";

    public AnswerParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(FILE_NAME);
        table = this.filter(mapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {
        }));
    }

    private List<Map<String, String>> filter(List<Map<String, String>> table) {
        return table.stream().filter(Objects::nonNull).filter(e -> e.get("ВОПРОС") != null).toList();
    }

    private String applyRegex(String str) {
        return str.replaceAll("[,:.? ]", "").toLowerCase(Locale.ROOT).replaceAll("c", "с");
    }

    public String getRightAnswerFromList(String question, List<String> choiceAnswers) {
        for (String ans : choiceAnswers){
            if (isAnswer(question, ans)){
                return ans;
            };
        }
        return null;
    }

    public boolean isAnswer(String question, String choiceAnswer) {
        String finalQuestion = question.substring(1, question.length() - 1);
        List<Map<String, String>> questions = table.stream().filter(e -> applyRegex(e.get("ВОПРОС")).contains(applyRegex(finalQuestion))).toList();
        String copyChoiceAnswer = applyRegex(choiceAnswer);
        for (Map<String, String> tableQuestion : questions) {
            if (applyRegex(tableQuestion.get("ОТВЕТ")).contains(copyChoiceAnswer)) {
                return true;
            }
        }
        return false;
    }

}
