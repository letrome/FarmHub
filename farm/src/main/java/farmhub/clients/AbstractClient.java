package farmhub.clients;

import com.fasterxml.jackson.databind.JsonNode;
import farmhub.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


public abstract class AbstractClient {
    protected static final String BAD_REQUEST_MESSAGE = "%s with id %s does not exist";
    protected static final String NOT_FOUND_MESSAGE = "%s not found for id %s";

    protected static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal server error for type %s and id %s";

    protected Logger logger;

    protected final String baseUrl;
    protected final HttpHeaders headers;
    protected final RestTemplate restTemplate;

    private final String type;


    public AbstractClient(String baseUrl, String type) {
        this.baseUrl = baseUrl;

        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        this.restTemplate = new RestTemplate();
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        this.logger = LoggerFactory.getLogger(AbstractClient.class);

        this.type = type;
    }

    protected <T> T executeGetRequest(String urlSuffix, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    responseType
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    protected <T> Optional<T> executeGetRequest(String urlSuffix, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    responseType
            );
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                logger.warn(e.getMessage());
                return Optional.empty();
            }

            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    protected <T> T executePostRequest(String urlSuffix, Object requestBody, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    responseType
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    protected <T> Optional<T> executePutRequest(String urlSuffix, Object requestBody, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.PUT,
                    new HttpEntity<>(requestBody, headers),
                    responseType
            );
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                logger.warn(e.getMessage());
                return Optional.empty();
            }

            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    protected <T> Optional<T> executePatchRequest(String urlSuffix, JsonNode inputNode, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.PATCH,
                    new HttpEntity<>(inputNode, headers),
                    responseType
            );
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                logger.warn(e.getMessage());
                return Optional.empty();
            }

            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    protected <T> Optional<T> executeDeleteRequest(String urlSuffix, Class<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    baseUrl + urlSuffix,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    responseType
            );
            return Optional.ofNullable(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                logger.warn(e.getMessage());
                return Optional.empty();
            }

            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
    }

    public abstract void reset();

    protected String notFoundMessage(String id){
        return String.format(NOT_FOUND_MESSAGE, type, id);
    }

    protected String badRequestMessage(String id){
        return String.format(BAD_REQUEST_MESSAGE, type, id);
    }

    protected String internalServerErrorMessage(String id){
        return String.format(INTERNAL_SERVER_ERROR_MESSAGE, type, id);
    }
}
