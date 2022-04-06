package org.goiteens;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Currency {
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

}
