package weatherReport.service;
import weatherReport.dto.WeatherReportDto;
public interface WeatherReportService {
    WeatherReportDto weatherReport(String city, String reportIn);
}
