package com.coperatecoding.secodeverseback.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.credentials.bucket-name}")
    public String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    //Pre-Signed URL 받아옴
    public String getPreSignedUrl(String prefix, String fileName) {
        String onlyOneFileName = onlyOneFileName(fileName);

        log.info("get presinged url");

        if (!prefix.equals("")) {
            fileName = prefix + "/" + fileName;
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }

    private String onlyOneFileName(String filename){
        return UUID.randomUUID().toString()+filename;
    }

    // 이미지 수정으로 인해 기존 이미지 삭제 메소드
    public void deleteImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public void fileDelete(String fileUrl) {

        String fileKey = fileUrl.substring(58);
        String key = fileKey; // 폴더/파일.확장자
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();

        s3.deleteObject(bucketName, key);

        System.out.println(String.format("[%s] deletion complete", key));

    }


}
