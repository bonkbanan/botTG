package org.goiteens;

import java.io.IOException;
import java.util.*;

public class ChatBot {
    public static String process(String message) throws IOException {
        if (message.contains("слава україні")) {
            return "Героям Слава";
        }

        if(message.contains("help") || message.contains("команди") || message.contains("допоможи") || message.contains("домога")){
            return "Я вмію:\n" +
                    "Відповідати на стандартні привітання привіт,hello,hi і інші Українські привітання.\n" +
                    "Вмію грати у камінь-ножниці-папір. Для цього нажміть на 'Зіграти камінь-ножниці-папір'.\n"+
                    "Можу дати свійжий курс валют. Для цього нажміть на 'Актуальний курс валют'.\n" +
                    "Можу сказати свіжий прогноз погоди. Для цього нажмінть на 'Нехай проблеми та незгоди не роблять Вам в житті погоди(погода)'.\n"+
                    "Якщо у вас був важкий день, нажміть на 'Хвилинка релаксу з Кімом'\n"+
                    "Якщо хочете відчути у собі патріота, нажміть 'Патріотична хвилинка'";

        }
        if ("слава нації".contains(message)){
            return "Смерть Ворогам";
        }

        if (message.contains("путін")){
            return "Х****";
        }
        if(message.contains("україні") || message.contains("🇺🇦") || message.contains("💙💛")){
            return "Понад Усе";
        }
        if (isHelloMessage(message)) {
            String botName = "Banan's Bot";
            return "Вітаю, я - " + botName;
        }
        if (message.contains("слава ісусу хресту")){
            return "Слава на віки!";
        }

        return "Вибачте, але я не найшов у вашому повідомленні команду, яку я можу виконати(";
    }

    private static boolean isHelloMessage(String message) {
        List<String> list = new ArrayList<>();
        list.add("привіт");
        list.add("здравствуй");
        list.add("здравсте");
        list.add("бонжур");
        list.add("салам молейкум");
        list.add("боназива");
        list.add("hi");
        list.add("hello");
        list.add("bounjour");
        list.add("слава ісусу хресту");

        return list.contains(message);
    }

}
