package com.bitgloomy.server.service;

import com.bitgloomy.server.dto.RequestWeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {
    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    @Value("${KMA.api.accesskey}")
    private String accessKey;

    public void requestAPI(RequestWeatherDTO requestWeatherDTO) throws Exception {
        RequestWeatherDTO transferDTO=transferGps(requestWeatherDTO);
        // 기상청api에 변환한 격좌 좌표로 요청함

        String stringUrl = UriComponentsBuilder.fromHttpUrl("https://apihub.kma.go.kr/api/typ01/url/fct_grnd_grid.php")
                .queryParam("tmfc", 0)
                .queryParam("authKey", accessKey)
                .queryParam("nx", (int)transferDTO.getXlatitude())
                .queryParam("ny", (int)transferDTO.getYlongitude())
                .toUriString();
        System.out.println(stringUrl);

        URL url = new URL(stringUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        // BufferedReader를 닫습니다.
        in.close();

        // 응답을 출력합니다.
        System.out.println(response.toString());

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
}
