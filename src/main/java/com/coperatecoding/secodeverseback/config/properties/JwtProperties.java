package com.coperatecoding.secodeverseback.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private long expirationHours;
    private String issuer;
}
<<<<<<< HEAD

=======
>>>>>>> 260839b2348078f54d8a065ff65888a02f67426e
