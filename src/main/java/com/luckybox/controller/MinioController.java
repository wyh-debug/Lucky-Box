package com.luckybox.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.luckybox.pojo.dto.BucketPolicyConfigDto;
import com.luckybox.pojo.dto.MinioDTO;
import com.luckybox.pojo.dto.Result;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
@Slf4j
public class MinioController {
    private final MinioClient minioClient;
    @Value("${spring.minio.bucketName}")
    private String BUCKET_NAME;

    @Value("${spring.minio.endpoint}")
    private String ENDPOINT;
    @PostMapping("/upload")
    public Result upload(@RequestBody MultipartFile file) throws Exception {
        //创建桶
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
        if(isExist) {
            log.info("存储桶已经存在");
        }else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            //创建桶并 只读权限
            BucketPolicyConfigDto bucketPolicyConfigDto = crateBucketPolicy();
            minioClient.setBucketPolicy(SetBucketPolicyArgs
                    .builder()
                    .bucket(BUCKET_NAME)
                    .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                    .build());
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if(originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/" + UUID.randomUUID().toString().replace("_", "") + suffix;
        //上传文件
         minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(BUCKET_NAME)
                .object(fileName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
        MinioDTO minioDTO = new MinioDTO();
        minioDTO.setName(fileName);
        minioDTO.setUrl(ENDPOINT + "/" + BUCKET_NAME + "/" + fileName);
        return Result.ok(minioDTO);
    }

    private BucketPolicyConfigDto crateBucketPolicy() {
        //设置只读策略
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement
                .builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::" + BUCKET_NAME + "/*").build();
        return BucketPolicyConfigDto
                .builder()
                .version("2012-10-17")
                .Statement(CollectionUtil.toList(statement))
                .build();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String objectName) throws Exception{
        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket(BUCKET_NAME)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.ok();
    }

}
