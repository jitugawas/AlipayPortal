package com.payitnz.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.payitnz.service.AlipayAPIService;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

    private AlipayAPIService alipayAPIService;

    @Autowired
    public void setAlipayAPIService(AlipayAPIService alipayAPIService) {
        this.alipayAPIService = alipayAPIService;
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody String uploadFileHandler(@RequestParam("mcTransactionId") String mcTransactionId, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        System.out.println("---uploadFileHandler()---");
        String ipAddress = request.getRemoteAddr();
        String sender = request.getRemoteHost();
        if (!file.isEmpty()) {
            try {
                alipayAPIService.saveFile(mcTransactionId, file, ipAddress, sender);
                return "You successfully uploaded file=" + file.getOriginalFilename();
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getFile", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getFile(@RequestParam("mcTransactionId") String mcTransactionId) throws IOException {
        byte[] blobBytes = alipayAPIService.getFile(mcTransactionId);
        return blobBytes;
    }

}
