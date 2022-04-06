package org.goiteens;


import com.fasterxml.jackson.core.JsonParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TelegramBot extends TelegramLongPollingBot{
    public TelegramBot() {

    }
    List<String> games = new ArrayList<>(Arrays.asList("scissors","paper","stone"));
    List<String> exchange = new ArrayList<>(Arrays.asList("dollar","euro","złoty","rubly","pound","yen"));
    List<String> trash = new ArrayList<>(Arrays.asList("давай зіграєм","курс валют","курс"));


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                String message = update.getMessage().getText().toLowerCase();
                if(message.equals("давай зіграєм")){
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("курс валют") || message.equals("курс") ){
                    try {
                        execute(sendInlineKeyBoardForeignMoney(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message.contains("погода")) {
                    try {
                        execute(new SendMessage().setText(weather(message)).setChatId(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException | IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(!trash.contains(message) && !message.contains("погода")){
                    String response = null;
                    try {
                        response = ChatBot.process(message);
                        sendText((update.getMessage().getChatId().toString()), response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(update.hasCallbackQuery()) {
            if (games.contains(update.getCallbackQuery().getData())) {
                try {
                    execute(new SendMessage().setText(game(update.getCallbackQuery().getData()))
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if(exchange.contains(update.getCallbackQuery().getData())){
                try {
                    execute(new SendMessage().setText(exchangeRating(update.getCallbackQuery().getData()))
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String weather(String message) throws IOException, ParseException {
        Date date1 = new Date();
        List<String> array = new ArrayList<>(Arrays.asList(message.split(" ")));
        String city = array.get(1);
        String date;
        SimpleDateFormat formatForYearNow = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String kek = "";
        if (array.size() == 2) {
            date = formatForDateNow.format(date1);
        } else {
            date = array.get(2);
        }
        String[] dayMonth = date.split("-");
        if (dayMonth[0].equals("1") && date1.getMonth() == 12) {
            kek += Integer.parseInt(formatForYearNow.format(date1)) + 1;
            kek += "-";
            kek += date;
        } else {
            kek += formatForYearNow.format(date1);
            kek += "-";
            kek += date;
        }
        Date dateOne = format.parse(kek);
        long difference = dateOne.getTime() - date1.getTime();
        int days = (int) (difference / (24 * 60 * 60 * 1000));

        if (days >= 0) {
            String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
            url += city;
            url += "?unitGroup=metric&key=F9JEPVVBPAU6MQBEFEA7DXNXD&contentType=json";
            String doc = Jsoup.connect(url).ignoreContentType(true).execute().body();
            ;
            doc = doc.toLowerCase();
            doc = doc.replaceAll("[,:]", " ");
            doc = doc.replaceAll("[^a-z- 0-9.]", "");
            List<String> list = new ArrayList<>(Arrays.asList(doc.split(" ")));
            list = list.stream().filter(el -> el.length() > 1).collect(Collectors.toList());
            int index;


            OptionalInt indexHour;
            List<String> finalList = list;
            indexHour = IntStream.range(0, list.size()).filter(el -> isGoodTime(finalList, el)).findFirst();

            index = list.indexOf(kek);
            String status = "";
            int i = index + 66;
            while (!list.get(i).equals("description")) {
                status += list.get(i);
                status += " ";
                i++;
            }
            return "У цьому місті в цей момент часу така погода: \n" +
                    "Стан погоди: " + status +
                    "\nМаксимальна температура: " + list.get(index + 4) +
                    "\nМінімальна температура: " + list.get(index + 6) +
                    "\nТемпература в данний момент: " + list.get(indexHour.getAsInt() + 6) +
                    "\nВідчувається як: " + list.get(indexHour.getAsInt() + 8) +
                    "\nВологість повітря: " + list.get(indexHour.getAsInt() + 10) +
                    "%\nАтмосферний тиск: " + list.get(indexHour.getAsInt() + 30) +
                    " мм ртутного стовпчика\nНебо в хмарах: " + list.get(index + 40) +
                    "%\nСхід Сонця: " + list.get(index + 52) + ":" + list.get(index + 53) + ":" + list.get(index + 54) +
                    "\nЗахід Сонця: " + list.get(index + 58) + ":" + list.get(index + 59) + ":" + list.get(index + 60);
        }
        else{
            return "Вибачте, але я не можу показати погоду, яка була раніше";
        }
    }

    public static boolean isGoodTime(List<String> list,int index){
        Date date1 = new Date();
        SimpleDateFormat formatForHour = new SimpleDateFormat("HH");
        return list.get(index).equals(formatForHour.format(date1)) && list.get(index + 1).equals("00");
    }

    public static SendMessage sendInlineKeyBoardForeignMoney(String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("$(USA)");
        inlineKeyboardButton1.setCallbackData("dollar");
        inlineKeyboardButton2.setText("€(EURO)");
        inlineKeyboardButton2.setCallbackData("euro");
        inlineKeyboardButton3.setText("zł (PLN)");
        inlineKeyboardButton3.setCallbackData("złoty");
        inlineKeyboardButton6.setText("₽ (RUB)");
        inlineKeyboardButton6.setCallbackData("rubly");
        inlineKeyboardButton5.setText("£ (GBP)");
        inlineKeyboardButton5.setCallbackData("pound");
        inlineKeyboardButton4.setText("¥ (JPY)");
        inlineKeyboardButton4.setCallbackData("yen");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        keyboardButtonsRow2.add(inlineKeyboardButton4);
        keyboardButtonsRow2.add(inlineKeyboardButton5);
        keyboardButtonsRow2.add(inlineKeyboardButton6);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Вибирайте Валюту").setReplyMarkup(inlineKeyboardMarkup);
    }


    public static SendMessage sendInlineKeyBoardMessage(String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("✌️");
        inlineKeyboardButton1.setCallbackData("scissors");
        inlineKeyboardButton2.setText("\uD83E\uDD1A");
        inlineKeyboardButton2.setCallbackData("paper");
        inlineKeyboardButton3.setText("✊");
        inlineKeyboardButton3.setCallbackData("stone");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow1.add(inlineKeyboardButton3);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Твій хід").setReplyMarkup(inlineKeyboardMarkup);
    }


    private void sendText(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String exchangeRating(String message) throws IOException {
        if(message.equals("rubly")){
            return "Російський корабель, іди на***";
        }else {
            Document doc = Jsoup.connect("https://minfin.com.ua/ua/currency/banks/").get();
            Elements body = doc.select("body");
            String array = new String(String.valueOf(body));
            array = array.toLowerCase();
            array = array.replaceAll("[:<]", " ");
            array = array.replaceAll("[^a-z 0-9.]", "");
            List<String> list = new ArrayList<>(Arrays.asList(array.split(" ")));
            list = list.stream().filter(el -> el.length() > 1).collect(Collectors.toList());
            String result = "";
            int index;
            switch (message) {
                case "dollar":
                    index = list.indexOf("hrefuacurrencybanksusd");

                    result += "USD: \nОфіційний: ";
                    result += list.get(index + 6);
                    result += "/";
                    result += list.get(index + 23);
                    result += "\nВалютний курс: ";
                    result += list.get(index + 45);
                    result += "/";
                    result += list.get(index + 49);
                    break;
                case "euro":
                    index = list.indexOf("hrefuacurrencybankseur");

                    result += "EURO: \nОфіційний: ";
                    result += list.get(index + 6);
                    result += "/";
                    result += list.get(index + 23);
                    result += "\nВалютний курс: ";
                    result += list.get(index + 45);
                    result += "/";
                    result += list.get(index + 49);
                    break;
                case "złoty":
                    index = list.indexOf("hrefuacurrencybankspln");

                    result += "Złoty: \nОфіційний: ";
                    result += list.get(index + 6);
                    result += "/";
                    result += list.get(index + 23);
                    result += "\nВалютний курс: ";
                    result += list.get(index + 45);
                    result += "/";
                    result += list.get(index + 49);
                    break;
                case "pound":
                    index = list.indexOf("hrefuacurrencybanksgbp");

                    result += "Pound: \nОфіційний: ";
                    result += list.get(index + 6);
                    result += "/";
                    result += list.get(index + 23);
                    result += "\nВалютний курс: ";
                    result += list.get(index + 45);
                    result += "/";
                    result += list.get(index + 49);
                    break;
                case "yen":
                    index = list.indexOf("hrefuacurrencybanksjpy");

                    result += "Yen: \nОфіційний: ";
                    result += list.get(index + 6);
                    result += "/";
                    result += list.get(index + 23);
                    result += "\nВалютний курс: ";
                    result += list.get(index + 45);
                    result += "/";
                    result += list.get(index + 49);
                    break;
            }
            return result;
        }
    }

    public static String game(String message){
        String playerChoice = message;

        int dealer = (int) (Math.random() * 3);
        String dealerChoice = "";
        switch (dealer) {
            case 0:dealerChoice = "scissors"; break;
            case 1:dealerChoice = "paper"; break;
            case 2:dealerChoice = "stone"; break;
        }
        if(playerChoice.equals(dealerChoice)){
            return "Нічия";
        }
        if((playerChoice.equals("paper") && dealerChoice.equals("stone"))||(playerChoice.equals("scissors") && dealerChoice.equals("paper"))
                || (playerChoice.equals("stone") && dealerChoice.equals("scissors"))){
            if (dealerChoice == "scissors") {
                return "Блін, Ви вийграли, я вибрав " + "✌️";
            }
            if (dealerChoice == "paper") {
                return "Блін, Ви вийграли, я вибрав " + "🖐";
            }
            if (dealerChoice == "stone") {
                return "Блін, Ви вийграли, я вибрав " + "\uD83D\uDC4A";
            }
        }
        if((playerChoice.equals("stone") && dealerChoice.equals("paper"))||(playerChoice.equals("paper") && dealerChoice.equals("scissors"))
                || (playerChoice.equals("scissors") && dealerChoice.equals("stone"))) {
            if (dealerChoice == "scissors") {
                return "Ура, я вийграв, я вибрав " + "✌️";
            }
            if (dealerChoice == "paper") {
                return "Ура, я вийграв, я вибрав " + "🖐";
            }
            if (dealerChoice == "stone") {
                return "Ура, я вийграв, я вибрав " + "\uD83D\uDC4A";
            }

        }
        return "";
    }

    @Override
    public String getBotUsername() {
        return "Banan's Bot";
    }

    @Override
    public String getBotToken() {
        return "1786184098:AAFlKIERGm8vfnNK25L_glvAmcut5HZTSDs";
    }
}
