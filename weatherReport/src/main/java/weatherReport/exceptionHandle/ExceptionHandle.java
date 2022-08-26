package weatherReport.exceptionHandle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import weatherReport.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleError(Throwable exception){
        Map<String, String> map = new HashMap<>();
        if(exception instanceof RuntimeException){
            map.put("error","Something went wrong ");
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus(Boolean.FALSE);
            apiResponse.setError(map);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
}
