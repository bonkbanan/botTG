package org.goiteens;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TelegramBot extends TelegramLongPollingBot{
    public TelegramBot() {
        ChatBot.initDreams();
        ChatBot.initProfessions();
    }
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
                }else {
                    String message = update.getMessage().getText();
                    String response = ChatBot.process(message);

                    sendText((update.getMessage().getChatId().toString()), response);
                }
            }
        }else if(update.hasCallbackQuery()){
            try {
                execute(new SendMessage().setText(game(update.getCallbackQuery().getData()))
                        .setChatId(update.getCallbackQuery().getMessage().getChatId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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
public static String game(String message){
    List<String> scissors = new ArrayList<>(
            Arrays.asList("✂️","✌️","🖖","✌️","✌","✂","scissors")
    );
    List<String> paper = new ArrayList<>(
            Arrays.asList("🖐","paper")
    );
    List<String> stone = new ArrayList<>(
            Arrays.asList("\uD83D\uDC4A","\uD83E\uDD1C","✊","🤛","stone")
    );

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
    if((paper.contains(playerChoice) && stone.contains(dealerChoice))||(scissors.contains(playerChoice) && paper.contains(dealerChoice))
            || (stone.contains(playerChoice) && scissors.contains(dealerChoice))){
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
    if((paper.contains(dealerChoice) && stone.contains(playerChoice))||(scissors.contains(dealerChoice) && paper.contains(playerChoice))
            || (stone.contains(dealerChoice) && scissors.contains(playerChoice))) {
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
        return "denysjava02_bot";
    }

    @Override
    public String getBotToken() {
        return "1786184098:AAFlKIERGm8vfnNK25L_glvAmcut5HZTSDs";
    }
}
