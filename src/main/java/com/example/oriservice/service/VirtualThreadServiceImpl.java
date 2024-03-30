package com.example.oriservice.service;

import com.example.oriservice.dto.FileDownloadResponse;
import com.example.oriservice.utils.FileUtil;
import com.example.oriservice.utils.FileUtilV2;
import com.google.common.collect.Lists;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class VirtualThreadServiceImpl implements VirtualThreadService {


    private final RestTemplate baseRestTemplate;

    private static final String FINALIZED_INVOICE_PDF_PATTER_STRING = "Invoice - \\d{8}-.*\\.pdf";
    private static final String INVOICE_PDF_PATTERN_STRING = "Invoice - I-\\w{3}\\d{4,5}-\\d*.pdf";
    private static final Pattern FINALIZED_INVOICE_PDF_PATTERN = Pattern.compile(FINALIZED_INVOICE_PDF_PATTER_STRING);
    private static final Pattern INVOICE_PDF_PATTERN = Pattern.compile(INVOICE_PDF_PATTERN_STRING);

    private final Predicate<File> FILE_NAME_NOT_NULL = file -> StringUtils.hasText(file.getName());
    private final Predicate<File> CONTAINS_INVOICE_STRING = file ->
        FINALIZED_INVOICE_PDF_PATTERN.matcher(file.getName()).matches() || INVOICE_PDF_PATTERN.matcher(file.getName()).matches();
    private final Predicate<File> ENDS_WITH_PDF = file -> file.getName().endsWith("pdf");

    @Override
    public void mockConcurrentRequest() {

        Map<String,Object> map = new HashMap<>();
        ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap<>();
//        try {
            log.info("mockConcurrentRequest test:{}",Thread.currentThread());
////            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        Thread.startVirtualThread(()->{log.info("new virtual tread:{}",Thread.currentThread());});
    }

    @Override
    public int mockMergePdfOOM(List<String> fileUrls, String fileName, Long shipperId) {
        try {
            if ("old".equals(fileName)) {
                List<File> downloadedFiles = downloadImageAndPdfFiles(fileUrls, shipperId);
                List<File> pdfFiles = prioritizeFiles(downloadedFiles);
                ByteArrayOutputStream combinedByteArrayOutputStream = FileUtil.mergePdf(pdfFiles);
                FileUtil.byteToFile(combinedByteArrayOutputStream.toByteArray(),"old",".pdf");
                return combinedByteArrayOutputStream.size();
            } else {
                List<FileDownloadResponse> fileDownloadResponseList = downloadFile(fileUrls);
                setInvoicePdfAsFirst(fileDownloadResponseList);
                ByteArrayOutputStream combinedByteArrayOutputStream = mergeFilesToPDF(fileDownloadResponseList);
                FileUtil.byteToFile(combinedByteArrayOutputStream.toByteArray(), "new", ".pdf");
                return combinedByteArrayOutputStream.size();
            }
        } catch (Exception e) {
            log.error("[combineAndUploadDocuments] error :{}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setInvoicePdfAsFirst(List<FileDownloadResponse> fileDownloadResponseList) {
        fileDownloadResponseList.sort(Comparator.comparing(fileDownloadResponse ->
            !(FINALIZED_INVOICE_PDF_PATTERN.matcher(fileDownloadResponse.getFileName()).matches() || INVOICE_PDF_PATTERN.matcher(
                fileDownloadResponse.getFileName()).matches())));
        log.debug("[setInvoicePdfAsFirst] result is {}",
            fileDownloadResponseList.stream().map(FileDownloadResponse::getFileName).collect(
                Collectors.joining(",")));
    }

    private ByteArrayOutputStream mergeFilesToPDF(List<FileDownloadResponse> fileDownloadResponseList) throws Exception {
        return FileUtilV2.mergeFilesToPdf(fileDownloadResponseList);
    }

    private List<FileDownloadResponse> downloadFile(List<String> urls) {
        log.debug("[downloadFile] url size : {}, urls : {}", urls.size(), urls);

        List<FileDownloadResponse> fileDownloadResponseList = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture[] futures = urls.stream()
            .map(this::downloadFileToByteAsync)
            .map(future -> future.thenAccept(fileResponse -> {
                if (null == fileResponse) {
                    log.warn("Async download File from url is failed");
                } else {
                    fileDownloadResponseList.add(fileResponse);
                }
            }))
            .toArray(CompletableFuture[]::new);
        // Wait until thread are all done
        CompletableFuture.allOf(futures).join();
        return fileDownloadResponseList;
    }

    public List<File> downloadImageAndPdfFiles(List<String> urls, Long shipperId) throws Exception {
        log.debug("downloadImageAndPdfFiles url size : {}, urls : {}", urls.size(), urls);
        List<File> imageFiles = Lists.newArrayList();
        List<File> pdfFiles = Lists.newArrayList();
        CompletableFuture[] futures = urls.stream()
            .map(this::downloadFile)
            .map(future -> future.thenAccept(file -> {
                if (null == file) {
                    log.warn("Async download File from url is failed");
                } else if (FileUtil.isImage(file)) {
                    imageFiles.add(file);
                } else if (FileUtil.isPDf(file)) {
                    if (!CONTAINS_INVOICE_STRING.test(file)) {
                        pdfFiles.add(FileUtil.convertPDFColorToGrayPDF(file));
                        log.warn("[downloadImageAndPdfFiles] end convertPDFColorToGrayPDF ");
                    } else {
                        pdfFiles.add(file);
                    }
                } else {
                    log.warn("Invalid file type for combineImageAndPdfFiles, file name :{}", file.getName());
                }
            }))
            .toArray(CompletableFuture[]::new);
        // Wait until thread are all done
        CompletableFuture.allOf(futures).join();
        if (!CollectionUtils.isEmpty(imageFiles)) {
            ByteArrayOutputStream pdfByteArrayOutputStream = FileUtil.imagesToPdf(imageFiles, 841, 595);
            File fileFromImageConversion = FileUtil.byteToFile(pdfByteArrayOutputStream.toByteArray(),
                "fileFromImageConversion",
                ".pdf");
            imageFiles.forEach(file -> log.debug("[mergeImageToPDF] files: {}", file.getName()));
            pdfFiles.add(fileFromImageConversion);
        }
        pdfFiles.forEach(file -> log.debug("downloadImageAndPdfFiles files: {}", file.getName()));
        return pdfFiles;
    }

    @Async
    public CompletableFuture<File> downloadFile(String url) {
        File file = null;
        try {
            URI uri = new URI(url);
            ResponseEntity<byte[]> response = baseRestTemplate.getForEntity(uri, byte[].class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("fetch file from url is failed, url :{} ReasonPhrase:{}",
                    url,
                    response.getStatusCode());
                throw new RuntimeException("Fetch File Error");
            }

            String originalFileName = FileUtil.getOriginalFileName(uri);
            String baseName = FilenameUtils.getBaseName(originalFileName);
            String extension = FilenameUtils.getExtension(originalFileName);
            file = FileUtil.byteToFile(response.getBody(), baseName, "." + extension);
        } catch (Exception e) {
            log.warn("[downloadFile] from url is failed, url :{}", url, e);
//            throw e;
        }
        return CompletableFuture.completedFuture(file);
    }

    private List<File> prioritizeFiles(List<File> files) {
        log.debug("prioritizeFiles size : {}", files.size());
        files.forEach(file -> log.debug("prioritizeFiles file : {}", file.getName()));
        long count = files.stream()
            .filter(FILE_NAME_NOT_NULL)
            .filter(CONTAINS_INVOICE_STRING)
            .filter(ENDS_WITH_PDF)
            .count();
        if (count > 1) {
            log.error("Over 2 files file name contains Invoice, ignore it.");
            return files;
        }

        if (count == 0) {
            log.error("Invoice PDF file not found.");
            return files;
        }
        Optional<File> invoicePdfFile = files.stream()
            .filter(FILE_NAME_NOT_NULL)
            .filter(CONTAINS_INVOICE_STRING)
            .filter(ENDS_WITH_PDF)
            .findFirst();

        if (invoicePdfFile.isPresent()) {
            log.debug("prioritizeFiles invoicePdfFile : {} exits.", invoicePdfFile.get().getName());
            List<File> prioritizedFiles = files.stream()
                .filter(FILE_NAME_NOT_NULL)
                .filter(file -> !CONTAINS_INVOICE_STRING.test(file))
                .collect(Collectors.toList());

            prioritizedFiles.add(0, invoicePdfFile.get());

            log.debug("prioritizeFiles final prioritized files size : {}", prioritizedFiles.size());
            prioritizedFiles.forEach(file -> log.debug("prioritizeFiles fileName : {}", file.getName()));

            return prioritizedFiles;
        }
        log.debug("prioritizeFiles files are not being prioritized.......");

        return files;
    }

    @Async
    public CompletableFuture<FileDownloadResponse> downloadFileToByteAsync(String url) {
        FileDownloadResponse fileDownloadResponse = null;
        try {
            URI uri = new URI(url);
            ResponseEntity<byte[]> response = baseRestTemplate.getForEntity(uri, byte[].class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("fetch file from url is failed, url :{} ReasonPhrase:{}",
                    url,
                    response.getStatusCode());
                throw new RuntimeException("Fetch File Error");
            }
            String originalFileName = FileUtil.getOriginalFileName(uri);
            String baseName = FilenameUtils.getBaseName(originalFileName);
            String extension = FilenameUtils.getExtension(originalFileName);
            fileDownloadResponse = new FileDownloadResponse(baseName + "." + extension, response.getBody());
        } catch (Exception e) {
            log.warn("[asyncDownloadFileToByte] from url is failed, url :{}", url, e);
//            throw e;
        }
        return CompletableFuture.completedFuture(fileDownloadResponse);
    }

}
