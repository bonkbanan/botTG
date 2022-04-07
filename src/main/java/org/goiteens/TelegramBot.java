package org.goiteens;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
    List<String> trash = new ArrayList<>(Arrays.asList("слава україні","help","команди","допоможи","домога","слава нації","путін","україна","🇺🇦","💙💛","привіт","здравствуй","здравсте","бонжур","салам молейкум","боназива","hi","hello","bounjour","слава ісусу хресту"));
    List<String> trashForWeather = new ArrayList<>(Arrays.asList("хвилинка релаксу","/start","зіграти камінь-ножниці-папір","актуальний курс валют","патріотична хвилинка","нехай проблеми та незгоди не роблять вам в житті погоди(погода)"));
    List<String> storage = new ArrayList<>();
    int counter = 0;


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                String message = update.getMessage().getText().toLowerCase();
                storage.add(message);
                if (message.equals("/start")) {
                    SendMessage text = new SendMessage() // Create a message object object
                            .setChatId(update.getMessage().getChatId())
                            .setText("Привіт, я Banan's Bot.\n"+
                                    "Я вмію:\n" +
                                    "Відповідати на стандартні привітання привіт,hello,hi і інші Українські привітання.\n" +
                                    "Вмію грати у камінь-ножниці-папір. Для цього нажміть на 'Зіграти камінь-ножниці-папір'.\n"+
                                    "Можу дати свійжий курс валют. Для цього нажміть на 'Актуальний курс валют'.\n" +
                                    "Можу сказати свіжий прогноз погоди. Для цього нажмінть на 'Нехай проблеми та незгоди не роблять Вам в житті погоди(погода)'.\n"+
                                    "Якщо у вас був важкий день, нажміть на 'Хвилинка релаксу з Кімом'\n"+
                                    "Якщо хочете відчути у собі патріота, нажміть 'Патріотична хвилинка'");
                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                    keyboardMarkup.setResizeKeyboard(true);
                    List<KeyboardRow> keyboard = new ArrayList<>();
                    KeyboardRow row = new KeyboardRow();
                    KeyboardRow row1 = new KeyboardRow();
                    KeyboardRow row2 = new KeyboardRow();
                    row.add("Зіграти камінь-ножниці-папір");
                    row.add("Актуальний курс валют");
                    row1.add("Нехай проблеми та незгоди не роблять Вам в житті погоди(погода)");
                    row1.add("Help");
                    row2.add("Хвилинка релаксу з Кімом");
                    row2.add("Патріотична хвилинка");
                    keyboard.add(row);
                    keyboard.add(row1);
                    keyboard.add(row2);

                    keyboardMarkup.setKeyboard(keyboard);
                    text.setReplyMarkup(keyboardMarkup);
                    try {
                        execute(text);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("зіграти камінь-ножниці-папір")){
                    try {
                        execute(Game.sendInlineKeyBoardMessage(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    counter=0;
                }
                if(message.equals("актуальний курс валют")){
                    try {
                        execute(Currency.sendInlineKeyBoardForeignMoney(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    counter = 0;
                }
                if(trash.contains(message) && storage.get(storage.size()-counter-1).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")){
                    counter =0;
                }
                if(!trash.contains(message) && !trashForWeather.contains(message) && !storage.get(storage.size()-counter-1).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")){
                    try {
                        execute(new SendMessage().setText("Вибачте, але я не найшов у вашому повідомленні команду, яку я можу виконати(").setChatId(update.getMessage().getChatId().toString()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    counter =0;
                }
                if((storage.size()>1 && storage.get(storage.size()-2).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")) || storage.get(storage.size()-counter-1).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")){
                    if(!trash.contains(message)) {
                        if (counter != 0) {
                            try {
                                execute(new SendMessage().setText(Weather.weather(message)).setChatId(update.getMessage().getChatId().toString()));
                            } catch (TelegramApiException | IOException | ParseException e) {
                                e.printStackTrace();
                            }
                            counter++;
                        }
                    }else{
                        counter = 0;
                    }
                }
                if(message.equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")) {
                        forWeatherElse(update);
                        counter++;
                }
                if(message.equals("хвилинка релаксу з кімом")) {
                    try {
                        sendVideoKim(update);
                    } catch (TelegramApiException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter=0;
                }
                if(message.equals("патріотична хвилинка")) {
                    try {
                        sendVideoPatriotic(update);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    counter=0;
                }
                if(trash.contains(message) && !storage.get(storage.size()-counter-1).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")){
                    if (storage.size() == 1 && !storage.get(0).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")) {
                        forCharBot(update,message);
                        counter=0;
                    }else if (storage.size() > 1 && !storage.get(storage.size() - 2).equals("нехай проблеми та незгоди не роблять вам в житті погоди(погода)")) {
                        forCharBot(update,message);
                        counter=0;
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

    public void forCharBot(Update update,String message){
        String response = null;
        try {
            response = ChatBot.process(message);
            sendText((update.getMessage().getChatId().toString()), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forWeatherElse(Update update){
        try {
            execute(new SendMessage().setText("Введіть будь ласка '{Ваше місто} {(за бажанням) Місяць-день через '-'}'").setChatId(update.getMessage().getChatId().toString()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    public void sendVideoKim(Update message) throws TelegramApiException, InterruptedException {
        File Videofile = new File("/Users/bonkbanan/Desktop/botTG/src/main/java/org/videos/HD Epic Sax Gandalf.mp4");
        execute(sendPhoto(message));
        Thread.sleep(1000);
        execute(new SendMessage().setText("Розслабтесь, та прийміть зручне для Вас положення. (Зачекайте)").setChatId(message.getMessage().getChatId()));
        execute(sendVideo(message,Videofile));
    }

    public void sendVideoPatriotic(Update message) throws TelegramApiException {
        File Videofile = new File("/Users/bonkbanan/Desktop/botTG/src/main/java/org/videos/Patriotic.mp4");
        execute(sendVideo(message,Videofile));
    }

    public SendVideo sendVideo(Update message,File Videofile){
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(message.getMessage().getChatId().toString());
        sendVideo.setVideo(Videofile);
        sendVideo.setWidth(1280);
        sendVideo.setHeight(720);
        return sendVideo;
    }

    public SendPhoto sendPhoto(Update message){
        File photoFile = new File("/Users/bonkbanan/Desktop/botTG/src/main/java/org/videos/KimWearsSocks.jpeg");
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getMessage().getChatId().toString());
        sendPhoto.setPhoto(photoFile);
        return sendPhoto;
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
