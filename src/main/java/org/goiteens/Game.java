package org.goiteens;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static String game(String message){
        String playerChoice="";

        switch (message) {
            case "✌️":playerChoice = "scissors"; break;
            case "🤚":playerChoice = "paper"; break;
            case "✊":playerChoice = "stone"; break;
        }


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
            if (dealerChoice.equals("scissors")) {
                return "Блін, Ви вийграли, я вибрав " + "✌️";
            }
            if (dealerChoice.equals("paper")) {
                return "Блін, Ви вийграли, я вибрав " + "🖐";
            }
            if (dealerChoice.equals("stone")) {
                return "Блін, Ви вийграли, я вибрав " + "\uD83D\uDC4A";
            }
        }
        if((playerChoice.equals("stone") && dealerChoice.equals("paper"))||(playerChoice.equals("paper") && dealerChoice.equals("scissors"))
                || (playerChoice.equals("scissors") && dealerChoice.equals("stone"))) {
            if (dealerChoice.equals("scissors")) {
                return "Ура, я вийграв, я вибрав " + "✌️";
            }
            if (dealerChoice.equals("paper")) {
                return "Ура, я вийграв, я вибрав " + "🖐";
            }
            if (dealerChoice.equals("stone")) {
                return "Ура, я вийграв, я вибрав " + "\uD83D\uDC4A";
            }

        }
        return "";
    }

    public static SendMessage sendInlineKeyBoardMessage(String chatId) {
        SendMessage text = new SendMessage() // Create a message object object
                .setChatId(chatId)
                .setText("Твій хід");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("✌️");
        row.add("✊");
        row.add("\uD83E\uDD1A");
        row.add("Назад");

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        text.setReplyMarkup(keyboardMarkup);
        return text;

    }
}
