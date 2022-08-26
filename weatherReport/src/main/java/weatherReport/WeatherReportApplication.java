package weatherReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
@SpringBootApplication
public class WeatherReportApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeatherReportApplication.class, args);
	}
}
