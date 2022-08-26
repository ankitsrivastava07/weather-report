package weatherReport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import weatherReport.constant.WeatherConstant;
import weatherReport.dto.ApiResponse;
import weatherReport.dto.WeatherReportDto;
import weatherReport.service.WeatherReportService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.*;

@Service
public class WeatherReportServiceImpl implements WeatherReportService {
    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Override
    //@CircuitBreaker(name="weatherReportFallback",fallbackMethod = "weatherReportFallback")
    public WeatherReportDto weatherReport(String city, String reportIn) {

        RestTemplate webClient = new RestTemplate();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("weather");
        return circuitBreaker.run(() -> {
                    String uri = "https://api.openweathermap.org/data/2.5/weather?q={city}&APPID=";
                    Map<String, String> parameter = new HashMap<>();
                    parameter.put("{" + "city" + "}", city);
                    parameter.put("APPID=", "APPID=" + WeatherConstant.API_KEY);
                    uri = setParameterToUri(uri, parameter);
                    Map<?, ?> data = webClient.getForEntity(uri, Map.class).getBody();
                    WeatherReportDto dto = convertToDto(data, reportIn);
                    return dto;
                }, throwable -> weatherReportFallback(throwable)
        );
    }

    public String setParameterToUri(String uri, Map<String, String> parameter) {
        for (Map.Entry<String, String> entry : parameter.entrySet())
            uri = uri.replace(entry.getKey(), entry.getValue());
        return uri;
    }

    public WeatherReportDto weatherReportFallback(Throwable exception) {
        Map<Object, Object> errors = new HashMap<>();
        WeatherReportDto apiResponse = new WeatherReportDto();
        if (exception instanceof HttpClientErrorException.NotFound) {
            errors.put("error", "City Not Found");
            errors.put("httpStatusCode", NOT_FOUND.name());
            apiResponse.setError(errors);
            return apiResponse;
        } else if (exception instanceof TimeoutException) {
            errors.put("error", WeatherConstant.REQUEST_TIMEOUT);
            errors.put("httpStatusCode", HttpStatus.REQUEST_TIMEOUT.name());
            apiResponse.setError(errors);
            return apiResponse;
        } else if (exception instanceof ResourceAccessException) {
            errors.put("error", "Application is in maintenance mode, Sorry for the inconvenience");
            errors.put("httpStatusCode", SERVICE_UNAVAILABLE.name());
            apiResponse.setError(errors);
            return apiResponse;
        }
        errors.put("error", "Something went wrong. Please try again later");
        errors.put("httpStatusCode", INTERNAL_SERVER_ERROR.name());
        apiResponse.setError(errors);
        return apiResponse;
    }

    public WeatherReportDto convertToDto(Map<?, ?> data, String reportIn) {
        WeatherReportDto dto = new WeatherReportDto();
        DecimalFormat df = new DecimalFormat("0.0");
        Map<String, Double> main = (Map<String, Double>) data.get("main");
        if (reportIn.equalsIgnoreCase("Celsius")) {
            dto.setTemp(df.format(main.get("temp") - WeatherConstant.ABSOLUTE_TEMP));
            dto.setFeels_like(df.format(main.get("feels_like") - WeatherConstant.ABSOLUTE_TEMP));
            dto.setTemp_min(df.format(main.get("temp_min") - WeatherConstant.ABSOLUTE_TEMP));
            dto.setTemp_max(df.format(main.get("temp_max") - WeatherConstant.ABSOLUTE_TEMP));
        } else {
            dto.setTemp(df.format(1.8 * main.get("temp") - 459.4));
            dto.setFeels_like(df.format(1.8 * main.get("feels_like") - 459.4));
            dto.setTemp_min(df.format(1.8 * main.get("temp_min") - 459.4));
            dto.setTemp_max(df.format(1.8 * main.get("temp_max") - 459.4));
        }
        dto.setCity(data.get("name").toString());
        dto.setHumidity(((Map<String, Double>) data.get("main")).get("humidity"));
        dto.setCountry((((Map<String, String>) data.get("sys")).get("country")));
        dto.setWindSpeed(((Map<String, Object>) data.get("wind")).get("speed").toString());
        dto.setDescription(((ArrayList<Map<String, Object>>) data.get("weather")).get(0).get("description").toString());
        return dto;
    }
}
