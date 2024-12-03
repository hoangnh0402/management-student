package com.example.demo.controller;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class LoadImageController {

  @Value("${upload.file.path}")
  private String pathUploadImage;

  @GetMapping(value = "loadImage")
  @ResponseBody
  public byte[] index(@RequestParam(value = "image") String image, HttpServletResponse response)
      throws IOException {
    response.setContentType("image/jpeg");
    File file = new File(pathUploadImage + File.separatorChar + image);
    InputStream inputStream = null;
    if (file.exists()) {
      try {
        inputStream = new FileInputStream(file);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      if (inputStream != null) {
        return IOUtils.toByteArray(inputStream);
      }
    }
    return null;
  }

}
