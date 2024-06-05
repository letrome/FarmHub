package farmhub.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<JsonNode> handleNotFoundException(
            NotFoundException ex, HttpServletRequest httpServletRequest) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<JsonNode> handleBadRequestException(
            BadRequestException ex, HttpServletRequest httpServletRequest) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<JsonNode> handleInternalServerErrorException(
            InternalServerErrorException ex, HttpServletRequest httpServletRequest) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<JsonNode> buildResponseEntity(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.createObjectNode()
                        .put("error", message));
    }
}
