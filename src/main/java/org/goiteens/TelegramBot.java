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
import java.util.*;
import java.util.stream.Collectors;


public class TelegramBot extends TelegramLongPollingBot{
    public TelegramBot() {
        ChatBot.initDreams();
        ChatBot.initProfessions();
    }
    List<String> games = new ArrayList<>(Arrays.asList("scissors","paper","stone"));
    List<String> exchange = new ArrayList<>(Arrays.asList("dollar","euro","złoty","rubly","pound","yen"));

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                if(update.getMessage().getText().toLowerCase().equals("давай зіграєм")){
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(update.getMessage().getText().toLowerCase().equals("курс валют") ||update.getMessage().getText().toLowerCase().equals("курс") ){
                        try {
                            execute(sendInlineKeyBoardForeignMoney(update.getMessage().getChatId().toString()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        String message = update.getMessage().getText();
                        String response = ChatBot.process(message);

                        sendText((update.getMessage().getChatId().toString()), response);
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
                    result += "\nНа Чорному Ринку: ";
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
                    result += "\nНа Чорному Ринку: ";
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
                    result += "\nНа Чорному Ринку: ";
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
                    result += "\nНа Чорному Ринку: ";
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
                    result += "\nНа Чорному Ринку: ";
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
