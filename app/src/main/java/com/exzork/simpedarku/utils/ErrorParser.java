package com.exzork.simpedarku.utils;

import com.exzork.simpedarku.model.ErrorResponse;
import com.exzork.simpedarku.rest.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ErrorParser {
    public static ErrorResponse errorResponse(Response response){
        ErrorResponse errorResponse = new ErrorResponse();
        Converter<ResponseBody, ErrorResponse> converter = ApiClient.getClient().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        try {
            errorResponse = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errorResponse;
    }
}
