package excel;

import java.io.IOException;
import java.util.*;

public class Run {
    public static void main(String[] args) throws IOException {
        AnswerParser answerParser = new AnswerParser();
        System.out.println(answerParser.getAnswer("Целесообразно ли проводить рекламную акцию в варианте 1?"));


    }
}
