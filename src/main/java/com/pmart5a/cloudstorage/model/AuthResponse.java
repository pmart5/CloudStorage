package com.pmart5a.cloudstorage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty("auth-token")
    String authToken;
}
