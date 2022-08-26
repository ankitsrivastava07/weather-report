package weatherReport.constant;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
public class WeatherConstant {
    public static final Map<String,Integer> HTTP_STATUS_CODE = new HashMap<>();
    static{
        HTTP_STATUS_CODE.put(HttpStatus.INTERNAL_SERVER_ERROR.name(), 500);
        HTTP_STATUS_CODE.put(HttpStatus.REQUEST_TIMEOUT.name(), 408);
        HTTP_STATUS_CODE.put(HttpStatus.NOT_FOUND.name(), 404);
        HTTP_STATUS_CODE.put(HttpStatus.SERVICE_UNAVAILABLE.name(), 503);
    }
    public static final String API_KEY = "fe0c77b72eebee15405c0c516b431021";
    public static final String REQUEST_TIMEOUT = "Your request has been timeout";
    public static final Double ABSOLUTE_TEMP = 273.15;
}
