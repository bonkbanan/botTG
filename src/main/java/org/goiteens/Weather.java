package org.goiteens;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Weather {
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
        if(date.equals(city) && !city.equals("місто")) {
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
            } else {
                return "Вибачте, але я не можу показати погоду, яка була раніше";
            }
        }else return "Введіть будь ласка 'погода, місто'.\nЯкщо ви хочете станом на сьогодні - нічого не добавляйте. Якщо на якусь певну дату, напишіть 'погода Місто місяць-день'";
    }

    public static boolean isGoodTime(List<String> list,int index){
        Date date1 = new Date();
        SimpleDateFormat formatForHour = new SimpleDateFormat("HH");
        return list.get(index).equals(formatForHour.format(date1)) && list.get(index + 1).equals("00");
    }

}
