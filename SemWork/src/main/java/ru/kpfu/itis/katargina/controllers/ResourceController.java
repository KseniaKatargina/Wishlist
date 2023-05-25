package ru.kpfu.itis.katargina.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class ResourceController {
    @GetMapping({"/static/style/{code}.css", "/{where}/static/style/{code}.css"})
    @ResponseBody
    public ResponseEntity<String> styles(@PathVariable("code") String code,
                                         @PathVariable(value = "where", required = false) String where) throws IOException {
        return getResource("static/style", code + ".css", "text/css");
    }

    @GetMapping({"/static/js/{code}.js", "/{where}/static/js/{code}.js"})
    @ResponseBody
    public ResponseEntity<String> js(@PathVariable("code") String code,
                                     @PathVariable(value = "where", required = false) String where) throws IOException {
        return getResource("static/js", code + ".js", "application/javascript");
    }

    private ResponseEntity<String> getResource(String folder, String code, String contentType) throws IOException {
        String resourcePath = folder + "/" + code;
        ClassPathResource resource = new ClassPathResource(resourcePath);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BufferedReader bf = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bf.readLine()) != null) {
            sb.append(line).append("\n");
        }

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", contentType + "; charset=utf-8");

        return new ResponseEntity<>(sb.toString(), httpHeaders, HttpStatus.OK);
    }
}
