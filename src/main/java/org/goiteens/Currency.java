package org.goiteens;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Currency {
    public static String exchangeRating(String message) throws IOException {
        if(message.equals("₽(rub)")){
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
                case "$(usa)":
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
                case "€(euro)":
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
                case "zł(pln)":
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
                case "£(gbp)":
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
                case "¥(jpy)":
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
                case "fr(chf)":
                    index = list.indexOf("hrefuacurrencybankschf");

                    result += "Swiss Frank: \nОфіційний: ";
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
        SendMessage text = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("Виберіть валюту");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row1.add("$(USA)");
        row1.add("€(EURO)");
        row1.add("zł(PLN)");
        row1.add("£(GBP)");
        row2.add("¥(JPY)");
        row2.add("Fr(CHF)");
        row2.add("₽(RUB)");
        row2.add("Назад");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        text.setReplyMarkup(keyboardMarkup);
        return text;
    }

}
