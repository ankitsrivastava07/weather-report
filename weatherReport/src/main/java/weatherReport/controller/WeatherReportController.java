package weatherReport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weatherReport.constant.WeatherConstant;
import weatherReport.dto.WeatherReportDto;
import weatherReport.service.WeatherReportService;

import java.util.Map;

@RestController
@RequestMapping("/weather")
@CrossOrigin("http://localhost:4200/")
public class WeatherReportController {
    @Autowired
    WeatherReportService weatherReportService;

    @GetMapping(value = "/report/{reportIn}", produces = "application/json")
    public ResponseEntity<?> weatherReport(@RequestParam(name = "city") String city, @PathVariable String reportIn) {
        WeatherReportDto weatherReport = weatherReportService.weatherReport(city, reportIn);
        int statusCode = 201;
        if (weatherReport.getError() != null && ((Map<?, ?>) weatherReport.getError()).containsKey("httpStatusCode")) {
            statusCode = WeatherConstant.HTTP_STATUS_CODE.get(((Map<?, ?>) weatherReport.getError()).get("httpStatusCode"));
            ((Map<?, ?>) weatherReport.getError()).remove("httpStatusCode");
        }
        return new ResponseEntity<>(weatherReport, HttpStatus.valueOf(statusCode));
    }

    @RequestMapping(name = "/service-status")
    public ResponseEntity<?> serviceStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
