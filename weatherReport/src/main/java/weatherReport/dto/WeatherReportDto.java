package weatherReport.dto;
import lombok.Data;

import java.util.Map;

@Data
public class WeatherReportDto {
    private String temp;
    private String feels_like;
    private String temp_min;
    private String temp_max;
    private String country;
    private String description;
    private String city;
    private String windSpeed;
    private Object humidity;
    private Map<Object, Object> error;
}
