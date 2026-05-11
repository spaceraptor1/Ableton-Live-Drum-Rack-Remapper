package com.example.rackcopier_be.controllers;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.example.rackcopier_be.services.RackService;

@RestController
@RequestMapping("/api/rack")
public class RackController {
    private final RackService rackService;

    RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity postMethodName(@RequestParam String file_paths, @RequestParam MultipartFile file) {
        InputStreamResource resource;

        try {
            rackService.copyRacks(file_paths, file);
        } catch (IOException ex) {
            System.getLogger(RackController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SAXException ex) {
            System.getLogger(RackController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return new ResponseEntity<>(file_paths, HttpStatus.OK);
    }
}
