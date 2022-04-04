package org.goiteens;

import java.util.*;

public class ChatBot {
    private static Map<String, Integer> professions;
    private static Map<String, Integer> dreams;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        initProfessions();
        initDreams();

        String botAnswer = process(message);
        System.out.println(botAnswer);
    }
    
    public static void initProfessions() {
        professions = new LinkedHashMap<>();

        professions.put("Designer", 20000);
        professions.put("Java", 55000);
        professions.put("Frontend", 40000);
    }

    public static void initDreams() {
        dreams = new LinkedHashMap<>();

        dreams.put("Машина", 260000);
        dreams.put("iPhone", 27000);
        dreams.put("Macbook", 270000);
    }

    public static String process(String message) {
        if ("слава україні".equals(message.toLowerCase())) {
            return "Героям Слава";
        }
        if("/start".equals(message.toLowerCase())){
            return "Привіт, я Banan's Bot.\n"+
                    "Я вмію:\n" +
                    "Відповідати на стандартні привітання привіт,hello,hi і інші Українські привітання.\n" +
                    "Вмію грати у камінь-ножниці-папір. Для початку напишіть 'Давай Зіграєм'.\n"+
                    "Можу дати свійжий курс валют. Для цього напишіть 'Курс Валют'.\n";
        }
        if("help".equals(message.toLowerCase()) || "команди".equals(message.toLowerCase()) || "допоможи".equals(message.toLowerCase())){
            return "Я вмію:\n" +
            "Відповідати на стандартні привітання привіт,hello,hi і інші Українські привітання.\n" +
            "Вмію грати у камінь-ножниці-папір. Для початку напишіть 'Давай Зіграєм'.\n"+
            "Можу дати свійжий курс валют. Для цього напишіть 'Курс Валют'.\n";
        }
        if ("слава нації".equals(message.toLowerCase())){
            return "Смерть Ворогам";
        }
        if ("путін".equals(message.toLowerCase())){
            return "Х****";
        }
        if("україна".equals(message.toLowerCase()) || "🇺🇦".equals(message.toLowerCase()) || "💙💛".equals(message.toLowerCase())){
            return "Понад Усе";
        }

        if (isHelloMessage(message)) {
            String botName = "Banan's Bot";
            return "Вітаю, я - " + botName;
        }

        return "Вибачте, але я не найшов у вашому повідомлення команду, яку я можу виконати(";
    }

    public static int find(String message, Map<String, Integer> data) {
        message = message.toLowerCase();

        for(String word: data.keySet()) {
            String lowerCasedWord = word.toLowerCase();

            if (message.contains(lowerCasedWord)) {
                return data.get(word);
            }
        }

        return -1;
    }
    
    public static int calculateMonthCount(int dreamCost, int professionSalary) {
        int monthCount = dreamCost / professionSalary;
        monthCount = validateMonthCount(monthCount);
        return monthCount;
    }

    public static int validateMonthCount(int monthCount) {
        if (monthCount == 0) {
            return 1;
        }

        return monthCount;
    }



    private static boolean isHelloMessage(String message) {
        message = message.toLowerCase();

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
