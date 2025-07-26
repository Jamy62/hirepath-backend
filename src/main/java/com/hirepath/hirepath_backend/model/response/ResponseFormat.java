package com.hirepath.hirepath_backend.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@NoArgsConstructor
public class ResponseFormat {
    @Getter @Setter
    @JsonProperty("success") private Boolean success;
    @Getter @Setter
    @JsonProperty("data") private Optional<Object> data;
    @Getter @Setter @JsonProperty("uuid") private Optional<String> uuid;
    @Getter @Setter @JsonProperty("message") private Optional<String> message;
    @Getter @Setter @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Optional<ZonedDateTime> timestamp;
    @Getter @Setter @JsonProperty("error") private Optional<String> error;

    public static ResponseFormat createSuccessResponse(Object object, String message) {
        ResponseFormat response = new ResponseFormat();
        response.setSuccess(true);
        response.setTimestamp(Optional.of(ZonedDateTime.now()));
        response.setMessage(Optional.ofNullable(message));
        response.setData(Optional.ofNullable(object));
        return response;
    }
}