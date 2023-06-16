package com.care.bedu.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.care.bedu.file.dao.FileUploadDao;
import com.care.bedu.file.service.FileUploadService;
import com.care.bedu.file.vo.FileUploadVO;
import com.care.bedu.lecture.vo.LectureVO;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    private String fileBaseDir = "C:/Desktop/";

    @Autowired
    private FileUploadDao dao;

    private LectureVO vo;

    @Override
    public boolean upload(MultipartHttpServletRequest request, Model model) {
        MultipartFile file = request.getFile("fieldName"); // fieldname 을 요청에 있는 파일필드의 실제이름으로 바꿈

        if (file == null || file.isEmpty()) {
            return false;
        }

        String title = (String) model.getAttribute("LectDtlTitle"); 

        String time = ""; // time 에 값 설정
        String url = ""; // url 에 값 걸정
        String uuid = UUID.randomUUID().toString();

        String saveFileName = time + "_" + uuid + "." + getFileExtension(file.getOriginalFilename());

        Path path = Paths.get(fileBaseDir + File.separator + saveFileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }

        FileUploadVO vo = new FileUploadVO();
        vo.setLectDtlTitle(title);
        vo.setLectDtlNum(uuid);
        vo.setLectDtlTime(time);
        vo.setLectVideoUrl(url);

        // Perform additional logic with the FileUploadVO

        return true;
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
    

    @Override
    public HashMap<String, Object> getLectureList(String keyword) {
        HashMap<String, Object> map = new HashMap<>();

        ArrayList<LectureVO> list = new ArrayList<>();

        list = dao.getLectureList(keyword);

        map.put("item", list);

        return map;
    }
}
