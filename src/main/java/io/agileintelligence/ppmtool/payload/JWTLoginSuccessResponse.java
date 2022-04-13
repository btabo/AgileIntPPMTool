package io.agileintelligence.ppmtool.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @ToString
public class JWTLoginSuccessResponse {

    private boolean success;
    private String token;

}
