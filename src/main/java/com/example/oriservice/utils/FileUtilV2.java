package com.example.oriservice.utils;

import com.example.oriservice.dto.FileDownloadResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilV2 {

    private static final Long MERGE_PDF_MEMORY_LIMIT = 200 * 1024 * 1024L;

    public static ByteArrayOutputStream mergeFilesToPdf(List<FileDownloadResponse> fileDownloadResponseList)
        throws DocumentException, IOException {
        long totalFileLength = fileDownloadResponseList.stream()
            .map(FileDownloadResponse::getBody)
            .mapToLong(b -> b.length)
            .summaryStatistics()
            .getSum();
        log.debug("[mergeFilesToPdf] pdfFiles count :{}, total file size :{} ", fileDownloadResponseList.size(), totalFileLength);
        if (MERGE_PDF_MEMORY_LIMIT.compareTo(totalFileLength) < 0) {
            throw new RuntimeException("Combine File Error");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, bos);

        document.open();
        for (FileDownloadResponse fileInfo : fileDownloadResponseList) {

            if (isImage(fileInfo.getFileName())) {
                Image image = Image.getInstance(fileInfo.getBody());

                float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
                float documentHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
                float imageWidth = image.getWidth();
                float imageHeight = image.getHeight();
                float scaleWidth = documentWidth / imageWidth;
                float scaleHeight = documentHeight / imageHeight;
                float scale = Math.min(scaleWidth, scaleHeight);
                image.scalePercent(scale * 100);

                float x = (documentWidth - image.getScaledWidth()) / 2 + document.leftMargin();
                float y = document.getPageSize().getHeight() - image.getScaledHeight() - document.topMargin();
                image.setAbsolutePosition(x, y);
                document.newPage();
                document.add(image);

            } else if (isPDf(fileInfo.getFileName())) {
                PdfReader reader = new PdfReader(fileInfo.getBody());
                int numPages = reader.getNumberOfPages();
                PdfContentByte contentByte = writer.getDirectContent();
                for (int pageNum = 1; pageNum <= numPages; pageNum++) {
                    document.newPage();
                    PdfImportedPage importPage = writer.getImportedPage(reader, pageNum);
                    contentByte.addTemplate(importPage, 0, 0);
                }
                reader.close();

            } else {
                log.warn("[mergeFilesToPdf] File is not an image or PDF, ignore file, fileName is {}", fileInfo.getFileName());
            }
        }
        document.close();
        return bos;
    }

    public static boolean isImage(String fileName) {
        String fileNameLowerCase = fileName.toLowerCase();
        return fileNameLowerCase.endsWith(".bmp")
            || fileNameLowerCase.endsWith(".jpg")
            || fileNameLowerCase.endsWith(".jpeg")
            || fileNameLowerCase.endsWith(".gif")
            || fileNameLowerCase.endsWith(".png");

    }

    public static boolean isPDf(String fileName) {
        return fileName.toLowerCase().endsWith(".pdf");

    }
}
