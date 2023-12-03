//package com.coperatecoding.secodeverseback.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.core.env.Environment;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("api/v1/profiles")
//public class ProfileController {
//    private final Environment environment;
//    @GetMapping
//    public String profile() {
//        final List<String> profiles = Arrays.asList(environment.getActiveProfiles());
//        final List<String> prodProfiles = Arrays.asList("prod1", "prod2");
//        final String defaultProfile = profiles.get(0);
//
//        return Arrays.stream(environment.getActiveProfiles())
//                .filter(prodProfiles::contains)
//                .findAny()
//                .orElse(defaultProfile);
//    }
//}
