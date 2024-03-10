package com.smsoft.playgroundbe.global.error;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodName, Response response) {
        log.error("Failed request {} with status: {}, reason: {}", methodName, response.status(), response.reason());

        if(HttpStatus.valueOf(response.status()).is5xxServerError()) {
            FeignException feignException = FeignException.errorStatus(methodName, response);

            return new RetryableException(
                    response.status(),
                    "Server error on " + methodName + " - " + feignException.getMessage(),
                    response.request().httpMethod(),
                    (Throwable) feignException,
                    (Long) null,
                    response.request()
            );
        }

        return defaultDecoder.decode(methodName, response);
    }
}
