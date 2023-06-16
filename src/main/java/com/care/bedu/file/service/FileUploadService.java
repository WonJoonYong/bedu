package com.care.bedu.file.service;


import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import org.springframework.web.multipart.MultipartHttpServletRequest;


public interface FileUploadService {

    public boolean upload(MultipartHttpServletRequest request, Model model);
    public HashMap<String, Object> getLectureList(String keyword);
}
