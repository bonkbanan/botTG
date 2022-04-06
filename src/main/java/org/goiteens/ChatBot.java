package org.goiteens;

import java.io.IOException;
import java.util.*;

public class ChatBot {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();


        String botAnswer = process(message);
        System.out.println(botAnswer);
    }

    public static String process(String message) throws IOException {
        if ("слава україні".equals(message)) {
            return "Героям Слава";
        }

        if(message.contains("help") || message.contains("команди") || message.contains("допоможи") || message.contains("домога")){
            return "Я вмію:\n" +
                    "Відповідати на стандартні привітання: привіт,hello,hi і інші Українські привітання.\n" +
                    "Вмію грати у камінь-ножниці-папір. Для початку напишіть 'Давай Зіграєм'.\n"+
                    "Можу дати свійжий курс валют. Для цього напишіть 'Курс Валют'.\n" +
                    "Можу сказати свіжий прогноз погоди. Для цього напишіть 'погода (Місто)'(На жаль Київ тимчасово не працює(((). Якщо ви хочете станом на сьогодні - нічого не добавляйте. Якщо на якусь певну дату, напишіть 'погода Місто місяць-день'\n"+
                    "Якщо у вас був важкий день, напишіть 'хвилинка релаксу'";

        }
        if ("слава нації".equals(message)){
            return "Смерть Ворогам";
        }
        if ("путін".equals(message)){
            return "Х****";
        }
        if("україна".equals(message) || "🇺🇦".equals(message) || "💙💛".equals(message)){
            return "Понад Усе";
        }
        if (isHelloMessage(message)) {
            String botName = "Banan's Bot";
            return "Вітаю, я - " + botName;
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

        return list.contains(message);
    }

}
