package com.musalasoft.musalsoftDrone.handler;


import com.musalasoft.musalsoftDrone.exception.ResourceNotFoundException;
import com.musalasoft.musalsoftDrone.exception.ServiceException;
import com.musalasoft.musalsoftDrone.payload.response.ErrorResponse;
import com.musalasoft.musalsoftDrone.payload.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableWebMvc
@RestControllerAdvice
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestExceptionHandler implements ErrorController {

    @Value("${musala.app.debug}")
    private Boolean debugMode;
    private final ErrorAttributes errorAttributes;


    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param serverProperties   ServerProperties properties
     * @param errorViewResolvers error view resolvers
     */
    public RestExceptionHandler(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
//        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
        this.errorAttributes = errorAttributes;
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, ErrorAttributeOptions options) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, options);
    }



    /**
     * Overrides the base method to add our custom logic
     */
//    @SneakyThrows
//    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) throws Throwable {


        Throwable throwable = this.errorAttributes.getError(new ServletWebRequest(request));


        if (throwable == null) {
            Response response = new ErrorResponse();

            Map<String, Object> body;
            HttpStatus status;
            ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
            options = options.including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.BINDING_ERRORS);
            body = getErrorAttributes(request, options);
            response.addDataValue("body", body);
            status = getStatus(request);
            response.setMessage("your request could not be processed");

            addDebugFields(response, request, status, null);


            return new ResponseEntity<>(response, status);
        } else {
            throw throwable;
        }


    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Throwable ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();

        addDebugFields(response, req, HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();

        addDebugFields(response, req, HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleNullPointerException(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Request Could not be Processed");

        if (debugMode) {
            response.addDataValue("cause", ex.getCause());
        }
        addDebugFields(response, req, HttpStatus.UNPROCESSABLE_ENTITY, ex);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleHttpException(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getLocalizedMessage());

        response.addDataValue("supported_methods", ex.getSupportedMethods());

        addDebugFields(response, req, HttpStatus.METHOD_NOT_ALLOWED, ex);


        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleHttpException(ResourceNotFoundException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        addDebugFields(response, req, HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpException(ServiceException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(ex.getMessage());
        addDebugFields(response, req, HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleHttpException(NoResultException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Resource not found");
        addDebugFields(response, req, HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleHttpException(EntityNotFoundException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Resource not found");
        addDebugFields(response, req, HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Parameter is invalid");

        addDebugFields(response, req, HttpStatus.BAD_REQUEST, ex);


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpException(MissingServletRequestParameterException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Required Parameter is Missing From Request Payload");

        addDebugFields(response, req, HttpStatus.BAD_REQUEST, ex);


        Map<String, Object> param = new HashMap<>();

        param.put("name", ex.getParameterName());
        param.put("type", ex.getParameterType());

        response.addDataValue("parameter", param);


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleHttpException(BindException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Request properties could not be validated");

        addDebugFields(response, req, HttpStatus.UNPROCESSABLE_ENTITY, ex);

        response.addDataValue("errors", formatFieldErrors(ex.getFieldErrors()));


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleHttpException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Validation failed for a parameter in your request body.\n You might have the wrong parameters in you request body");


        addDebugFields(response, req, HttpStatus.UNPROCESSABLE_ENTITY, ex);
        response.addDataValue("errors", formatFieldErrors(ex.getFieldErrors()));


        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpException(ConstraintViolationException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Validation failed for a parameter in your request\n You might have the wrong parameters in you request");


        List<Map<String, Object>> mapList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String[] strings = constraintViolation.getMessageTemplate().split("\\.");
            String field = constraintViolation.getPropertyPath().toString().replaceAll("^.*?\\.","");
            hashMap.put("message", constraintViolation.getMessage());
            hashMap.put("rejectedValue", constraintViolation.getInvalidValue());
            hashMap.put("field", !field.isBlank() ? field : "Custom");
            hashMap.put("code", strings.length > 1 ? strings[strings.length - 2] : "Custom");
            mapList.add(hashMap);
        }

        addDebugFields(response, req, HttpStatus.BAD_REQUEST, ex);
        response.addDataValue("errors", mapList);


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    protected List<Map<String, Object>> formatFieldErrors(List<FieldError> fieldErrors) {

        List<Map<String, Object>> mapList = new ArrayList<>();


        for (FieldError fieldError : fieldErrors) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", fieldError.getDefaultMessage());
            hashMap.put("rejectedValue", fieldError.getRejectedValue());
            hashMap.put("field", fieldError.getField());
            hashMap.put("code", fieldError.getCode());
            mapList.add(hashMap);
        }
        return mapList;
    }

    protected void addDebugFields(Response response, HttpServletRequest req, HttpStatus status, Throwable ex) {

        if (status == HttpStatus.INTERNAL_SERVER_ERROR){
            if (debugMode) {
                response.setMessage(ex.getLocalizedMessage());
                response.addDataValue("cause", ex.getCause());
            } else {
                response.setMessage("A server error occurred");
            }
        }


        if (!debugMode) return;

        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        options = options.including(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.BINDING_ERRORS);
        Map<String, Object> body = getErrorAttributes(req, options);


        if (ex != null) {
            response.addDataValue("message", ex.getLocalizedMessage());
            response.addDataValue("exception", ex.getClass().getSimpleName());
        } else {
            response.addDataValue("message", "[null]");
            response.addDataValue("exception", null);
            response.addDataValue("body", body);
        }

        Object requestURI = req.getRequestURI();

        if (requestURI.equals("/error")) {
            requestURI = body.get("path");
        }
        response.addDataValue("path", requestURI);
        response.addDataValue("method", req.getMethod());
        response.addDataValue("code", status.value());
        response.addDataValue("error", status.getReasonPhrase());
    }




}
