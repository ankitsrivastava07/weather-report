package weatherReport.dto;
import lombok.Data;
import java.util.Map;
@Data
public class ApiResponse {
    private WeatherReportDto data;
    private Object error;
    private Boolean status = Boolean.FALSE;
}
