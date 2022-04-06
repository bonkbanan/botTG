package org.goiteens;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Game {
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
}
