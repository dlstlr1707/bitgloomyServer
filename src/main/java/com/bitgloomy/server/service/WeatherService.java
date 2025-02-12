package com.bitgloomy.server.service;

import com.bitgloomy.server.domain.ConvertWeatherData;
import com.bitgloomy.server.domain.Weather;
import com.bitgloomy.server.dto.RequestWeatherDTO;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    private static LocalDate localDate = LocalDate.now();
    private static LocalTime localHour = LocalTime.now();

    @Value("${KMA.api.accesskey}")
    private String accessKey;

    public ConvertWeatherData requestAPI(RequestWeatherDTO requestWeatherDTO) throws Exception {
        RequestWeatherDTO transferDTO=transferGps(requestWeatherDTO);
        // 기상청api에 변환한 격좌 좌표로 요청함
        String tempStringDate = localDate.format( DateTimeFormatter.BASIC_ISO_DATE);
        String tempStringHour = "0000";
        if(localHour.getHour()<2){
            tempStringHour = "2300";
            tempStringDate = localDate.minusDays(1).format( DateTimeFormatter.BASIC_ISO_DATE);
        }else if(localHour.getHour()>=2 && localHour.getHour()<5){
            tempStringHour = "0200";
        }else if(localHour.getHour()>=5 && localHour.getHour()<8){
            tempStringHour = "0500";
        }else if(localHour.getHour()>=8 && localHour.getHour()<11){
            tempStringHour = "0800";
        }else if(localHour.getHour()>=11 && localHour.getHour()<14){
            tempStringHour = "1100";
        }else if(localHour.getHour()>=14 && localHour.getHour()<17){
            tempStringHour = "1400";
        }else if(localHour.getHour()>=17 && localHour.getHour()<20){
            tempStringHour = "1700";
        }else if(localHour.getHour()>=20 && localHour.getHour()<23){
            tempStringHour = "2000";
        }else if(localHour.getHour()>=23){
            tempStringHour = "2300";
        }

        String stringUrl = UriComponentsBuilder.fromHttpUrl("https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 12)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", tempStringDate)
                .queryParam("base_time", tempStringHour)
                .queryParam("nx", (int)transferDTO.getXlatitude())
                .queryParam("ny", (int)transferDTO.getYlongitude())
                .queryParam("authKey", accessKey)
                .toUriString();
        //System.out.println(stringUrl);

        URL url = new URL(stringUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        // BufferedReader를 닫습니다.
        in.close();

        // 응답을 출력합니다.
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray dataCategory = jsonResponse.getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item");

        Weather weather = new Weather();

        for (int i = 0; i < dataCategory.length(); i++) {
            JSONObject item = dataCategory.getJSONObject(i);

            // 각 항목에서 id와 name 추출
            String category = item.getString("category");
            String fcstValue = item.getString("fcstValue");

            // 출력
            System.out.println(category);
            System.out.println(fcstValue);
            switch (category){
                case "POP":
                    weather.setPOP(fcstValue);
                    break;
                case "PTY":
                    weather.setPTY(fcstValue);
                    break;
                case "PCP":
                    weather.setPCP(fcstValue);
                    break;
                case "REH":
                    weather.setREH(fcstValue);
                    break;
                case "SNO":
                    weather.setSNO(fcstValue);
                    break;
                case "SKY":
                    weather.setSKY(fcstValue);
                    break;
                case "TMP":
                    weather.setTMP(fcstValue);
                    break;
                case "UUU":
                    weather.setUUU(fcstValue);
                    break;
                case "VVV":
                    weather.setVVV(fcstValue);
                    break;
                case "WAV":
                    weather.setWAV(fcstValue);
                    break;
                case "VEC":
                    weather.setVEC(fcstValue);
                    break;
                case "WSD":
                    weather.setWSD(fcstValue);
                    break;
            }
        }
        ConvertWeatherData convertWeatherData = convertWeatherData(weather);
        return convertWeatherData;
    }
    private RequestWeatherDTO transferGps(RequestWeatherDTO requestWeatherDTO) throws Exception{
        String transGpsUrl = UriComponentsBuilder.fromHttpUrl("https://apihub.kma.go.kr/api/typ01/cgi-bin/url/nph-dfs_xy_lonlat")
                .queryParam("lon", requestWeatherDTO.getLongitude())
                .queryParam("lat", requestWeatherDTO.getLatitude())
                .queryParam("help", 0)
                .queryParam("authKey", accessKey)
                .toUriString();
        System.out.println(transGpsUrl);

        URL url = new URL(transGpsUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        String[] result=new String[10];
        int idx = 0;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            result[idx] = inputLine;
            idx++;
        }
        // BufferedReader를 닫습니다.
        in.close();

        // 응답을 출력합니다.
        result=result[2].trim().split(",");
        System.out.println(result[2]);
        System.out.println(result[3]);
        requestWeatherDTO.setXlatitude(Integer.parseInt(result[2].trim()));
        requestWeatherDTO.setYlongitude(Integer.parseInt(result[3].trim()));
        return requestWeatherDTO;
    }
    private ConvertWeatherData convertWeatherData(Weather weather){
        ConvertWeatherData convertWeatherData = new ConvertWeatherData();
        convertWeatherData.setPCP(weather.getPCP());
        convertWeatherData.setPOP(weather.getPOP()+"%");
        if(weather.getPTY().equals("0")){
            convertWeatherData.setPTY("없음");
        }else if(weather.getPTY().equals("1")){
            convertWeatherData.setPTY("비");
        }else if(weather.getPTY().equals("2")){
            convertWeatherData.setPTY("비/눈");
        }else if(weather.getPTY().equals("3")){
            convertWeatherData.setPTY("눈");
        }else if(weather.getPTY().equals("4")){
            convertWeatherData.setPTY("소나기");
        }
        convertWeatherData.setREH(weather.getREH()+"%");
        convertWeatherData.setSNO(weather.getSNO());
        if(weather.getSKY().equals("1")){
            convertWeatherData.setSKY("맑음");
        }else if(weather.getSKY().equals("2")){
            convertWeatherData.setSKY("구름조금");
        }else if(weather.getSKY().equals("3")){
            convertWeatherData.setSKY("구름많음");
        }else if(weather.getSKY().equals("4")){
            convertWeatherData.setSKY("흐림");
        }
        convertWeatherData.setTMP(weather.getTMP()+" ℃");
        if(Double.parseDouble(weather.getUUU())>=0){
            convertWeatherData.setUUU("동 "+weather.getUUU()+"m/s");
        }else if(Double.parseDouble(weather.getUUU())<0){
            convertWeatherData.setUUU("서 "+weather.getUUU()+"m/s");
        }
        if(Double.parseDouble(weather.getVVV())>=0){
            convertWeatherData.setVVV("북 "+weather.getVVV()+"m/s");
        }else if(Double.parseDouble(weather.getVVV())<0){
            convertWeatherData.setVVV("남 "+weather.getVVV()+"m/s");
        }
        if(Double.parseDouble(weather.getVEC())>=0&&Double.parseDouble(weather.getVEC())<45){
            convertWeatherData.setVEC("N-NE"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=45&&Double.parseDouble(weather.getVEC())<90){
            convertWeatherData.setVEC("NE-E"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=90&&Double.parseDouble(weather.getVEC())<135){
            convertWeatherData.setVEC("E-SE"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=135&&Double.parseDouble(weather.getVEC())<180){
            convertWeatherData.setVEC("SE-S"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=180&&Double.parseDouble(weather.getVEC())<225){
            convertWeatherData.setVEC("S-SW"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=225&&Double.parseDouble(weather.getVEC())<270){
            convertWeatherData.setVEC("SW-W"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=270&&Double.parseDouble(weather.getVEC())<315){
            convertWeatherData.setVEC("W-NW"+"("+weather.getVEC()+"deg)");
        }else if(Double.parseDouble(weather.getVEC())>=315&&Double.parseDouble(weather.getVEC())<=360){
            convertWeatherData.setVEC("NW-N"+"("+weather.getVEC()+"deg)");
        }
        convertWeatherData.setWSD(weather.getWSD()+"m/s");
        convertWeatherData.setWAV(weather.getWAV()+"M");
        return convertWeatherData;
    }
}
