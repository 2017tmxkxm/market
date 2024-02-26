package com.free.market.common.file;

import com.free.market.file.domain.FileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB 저장 파일 정보
     * @throws IOException
     */
    public FileRequest uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        //saveName=2981929234d941598d725f1c351c5c03.PNG
        String saveName = createSaveFileName(multipartFile.getOriginalFilename());
        log.info("saveName={}", saveName);

        //today=240225
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        log.info("today={}", today);

        String uploadPath = getUploadPath(today) + File.separator + saveName;
        log.info("uploadPath={}", uploadPath);


        multipartFile.transferTo(new File(uploadPath));

        return FileRequest.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize())
                .build();

    }

    /**
     * 다중 파일 업로드
     * @param multipartFiles - 파일 객체 List
     * @return DB 저장 파일 정보
     * @throws IOException
     */
    public List<FileRequest> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<FileRequest> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                files.add(uploadFile(multipartFile));
            }
        }

        return files;
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로(today)
     * @return 업로드 경로
     */
    private String getUploadPath(String addPath) {
        return makeDirectories(fileDir + File.separator +addPath);
    }

    /**
     * 저장 파일명 생성
     * @param filename
     * @return 저장 파일명
     */
    private String createSaveFileName(String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid +"." + extension;
    }

    /**
     * 업로드 폴더 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(String path) {

        log.info("path={}", path);
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        return dir.getPath();
    }

}
