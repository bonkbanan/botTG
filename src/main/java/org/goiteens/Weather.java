package org.goiteens;

import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Weather {
    public static String weather(String message) throws IOException, ParseException {
        Date date1 = new Date();
        SimpleDateFormat formatForYearNow = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> array = new ArrayList<>();
        array = Arrays.asList(message.split(" "));
        String city = array.get(0);
        String date;
        String kek = "";
//        date = format.format(date1);
//        kek = date;
        if(array.size() == 1){
            date = formatForDateNow.format(date1);
        }else {
            date = array.get(1);
        }

        if (city.equals("місто")) {
            return "Введіть будь ласка 'погода місто'.\nЯкщо ви хочете станом на сьогодні - нічого не добавляйте. Якщо на якусь певну дату, напишіть 'погода Місто місяць-день'";
        } else {
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
                url += "Chernivtsi";
                url += "?unitGroup=metric&key=F9JEPVVBPAU6MQBEFEA7DXNXD&contentType=json";
                String doc = Jsoup.connect(url).ignoreContentType(true).execute().body();
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
                    if (list.get(i).equals("conditions")) {
                        i++;
                        continue;
                    } else {
                        status += list.get(i);
                        if (list.get(i).equals("snow") || list.get(i).equals("rain") || list.get(i).equals("cloudy")) {
                            status += ", ";
                        } else {
                            status += " ";
                        }
                        i++;
                    }

                }
                if(days == 0) {
                    return "Станом на зараз, спостерігаєтсья така погода: \n" +
                            "Стан погоди: " + status +
                            "\nМаксимальна температура: " + trash(list, index, "tempmax") +
                            ",\nМінімальна температура: " + trash(list, index, "tempmin") +
                            ",\nТемпература в данний момент: " + trash(list, indexHour.getAsInt(), "temp") +
                            ",\nВідчувається як: " + trash(list, indexHour.getAsInt(), "feelslike") +
                            ",\nВологість повітря: " + trash(list, indexHour.getAsInt(), "humidity") +
                            "%,\nАтмосферний тиск: " + trash(list, indexHour.getAsInt(), "pressure") +
                            " мм ртутного стовпчика,\nШвидкість вітру: " + trash(list, indexHour.getAsInt(), "windspeed") +
                            " км/год,\nНебо в хмарах: " + trash(list, indexHour.getAsInt(), "cloudcover") +
                            "%,\nСхід Сонця: " + trashTime(list, indexHour.getAsInt(), "sunrise") +
                            ",\nЗахід Сонця: " + trashTime(list, indexHour.getAsInt(), "sunset");
                }else{
                    return "Станом на " + format.format(dateOne) + ", спостерігатиметься така погода: \n" +
                            "Стан погоди: " + status +
                            "\nМаксимальна температура: " + trash(list, index, "tempmax") +
                            ",\nМінімальна температура: " + trash(list, index, "tempmin") +
                            ",\nТемпература в данний момент: " + trash(list, indexHour.getAsInt(), "temp") +
                            ",\nВідчувається як: " + trash(list, indexHour.getAsInt(), "feelslike") +
                            ",\nВологість повітря: " + trash(list, indexHour.getAsInt(), "humidity") +
                            "%,\nАтмосферний тиск: " + trash(list, indexHour.getAsInt(), "pressure") +
                            " мм ртутного стовпчика,\nШвидкість вітру: " + trash(list, indexHour.getAsInt(), "windspeed") +
                            " км/год,\nНебо в хмарах: " + trash(list, indexHour.getAsInt(), "cloudcover") +
                            "%,\nСхід Сонця: " + trashTime(list, indexHour.getAsInt(), "sunrise") +
                            ",\nЗахід Сонця: " + trashTime(list, indexHour.getAsInt(), "sunset");
                }
            } else {
                return "Вибачте, але я не можу показати погоду, яка була раніше";
            }
        }
    }

    public static String trashTime(List<String> list, int index,String searchedWord) {
        List<String> finalList = list;
        OptionalInt kek;
        kek = IntStream.range(index, list.size()).filter(el -> isGoodWord(finalList, el, searchedWord)).findFirst();
        return list.get(kek.getAsInt()+1) + ":" + list.get(kek.getAsInt()+2) + ":" + list.get(kek.getAsInt()+3);
    }
    public static String trash(List<String> list,int index,String searchedWord){
        List<String> finalList = list;
        return list.get((IntStream.range(index, list.size()).filter(el -> isGoodWord(finalList, el,searchedWord)).findFirst()).getAsInt()+1)    ;
    }

    public static boolean isGoodTime(List<String> list,int index){
        Date date1 = new Date();
        SimpleDateFormat formatForHour = new SimpleDateFormat("HH");
        return list.get(index).equals(formatForHour.format(date1)) && list.get(index + 1).equals("00");
    }

    public static boolean isGoodWord(List<String> list,int index,String searchedWord){
        return list.get(index).equals(searchedWord);
    }


}
