package excel;

import java.io.IOException;
import java.util.*;

public class Run {
    public static void main(String[] args) throws IOException {
        AnswerParser answerParser = new AnswerParser();
        String question =  "Модель Р. Лоттерборна – это модель";
//        [6С, 6Р, 4С, 4Р]
        String ans1 = "6С";
        String ans2 = "6Р";
        String ans3 = "4С";
        String ans4 = "4Р";
        List<String> ansers = new ArrayList<String>();
        ansers.add(ans1);
        ansers.add(ans2);
        ansers.add(ans3);
        ansers.add(ans4);
        System.out.println(answerParser.getAnswer(question, ansers));
//        System.out.println(answerParser.getAnswer("Целесообразно ли проводить рекламную акцию в варианте 1?"));


    }
}
