package org.goiteens;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;


public class TelegramBot extends TelegramLongPollingBot{
    public TelegramBot() {

    }
    List<String> games = new ArrayList<>(Arrays.asList("scissors","paper","stone"));
    List<String> exchange = new ArrayList<>(Arrays.asList("dollar","euro","złoty","rubly","pound","yen"));
    List<String> trash = new ArrayList<>(Arrays.asList("давай зіграєм","курс валют","курс","хвилинка релаксу"));


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                String message = update.getMessage().getText().toLowerCase();
                if(message.equals("давай зіграєм")){
                    try {
                        execute(Game.sendInlineKeyBoardMessage(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("курс валют") || message.equals("курс") ){
                    try {
                        execute(Currency.sendInlineKeyBoardForeignMoney(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message.contains("погода")) {
                    try {
                        execute(new SendMessage().setText(Weather.weather(message)).setChatId(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException | IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(message.contains("хвилинка релаксу")) {
                    try {
                        sendVideo(update);
                    } catch (TelegramApiException | InterruptedException e) {
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
                    execute(new SendMessage().setText(Game.game(update.getCallbackQuery().getData()))
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if(exchange.contains(update.getCallbackQuery().getData())){
                try {
                    execute(new SendMessage().setText(Currency.exchangeRating(update.getCallbackQuery().getData()))
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public void sendVideo(Update message) throws TelegramApiException, InterruptedException {
        File photoFile = new File("/Users/bonkbanan/Desktop/0cdd7a590c590d1978e13f84bb3cb325.i900x675x595.jpeg");
        File Videofile = new File("/Users/bonkbanan/Desktop/HD Epic Sax Gandalf.mp4");
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getMessage().getChatId().toString());
        sendPhoto.setPhoto(photoFile);
        execute(sendPhoto);
        Thread.sleep(1000);
        execute(new SendMessage().setText("Розслабтесь, та прийміть зручне для Вас положення. (Зачекайте)").setChatId(message.getMessage().getChatId()));
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(message.getMessage().getChatId().toString());
        sendVideo.setVideo(Videofile);
        sendVideo.setWidth(1280);
        sendVideo.setHeight(720);
        execute(sendVideo);


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
