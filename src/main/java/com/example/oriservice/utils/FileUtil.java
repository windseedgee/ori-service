package com.example.oriservice.utils;

import static com.itextpdf.text.Element.ALIGN_CENTER;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.PDFMergerUtility.DocumentMergeMode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
public class FileUtil {

    private static final Long MERGE_PDF_MEMORY_LIMIT = 200 * 1024 * 1024L;
    private static final Long USE_MEMORY_LIMIT = 10 * 1024 * 1024L;
    public static final String VOID_MARK_PATH = "templates/Void.png";


    public static File byteToFile(byte[] content, String fileName, String suffix) throws IOException {
        Path tempFile = Files.createTempFile(fileName, suffix);
        Files.write(tempFile, content);
        return new FileSystemResource(tempFile.toFile()).getFile();
    }

    public static ByteArrayOutputStream imagesToPdf(List<File> imageFiles, float height, float width)
        throws Exception {
        List<BufferedImage> bufferedImages = new ArrayList<>();
        imageFiles.stream().filter(FileUtil::isImage).forEach(imageFile -> {
                try {
                    bufferedImages.add(ImageIO.read(imageFile));
                } catch (IOException e) {
                    log.error("covert image to PDF error, file name :{}", imageFile.getName(), e);
                    throw new RuntimeException("Combine File Error");
                }
            }
        );
        return bufferedImagesToPdf(bufferedImages, height, width);

    }

    public static ByteArrayOutputStream mergePdf(List<File> pdfFiles) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        long totalFileLength = pdfFiles.stream().mapToLong(File::length).summaryStatistics().getSum();
        log.debug("[mergePdf] pdfFiles count :{}, total file size :{} ", pdfFiles.size(), totalFileLength);
        if (MERGE_PDF_MEMORY_LIMIT.compareTo(totalFileLength) < 0) {
            throw new RuntimeException("Combine File Error");
        }
        pdfFiles.stream()
            .filter(File::exists).filter(File::isFile).filter(FileUtil::isPDf).filter(FileUtil::isReadable)
            .forEach(pdfFile -> {
                try {
                    pdfMerger.addSource(pdfFile);
                } catch (Exception e) {
                    log.error("merge PDF file error, file name :{}", pdfFile.getName(), e);
                }
            });
        pdfMerger.setDocumentMergeMode(DocumentMergeMode.OPTIMIZE_RESOURCES_MODE);
        pdfMerger.setDestinationStream(bos);
        if (USE_MEMORY_LIMIT.compareTo(totalFileLength) > 0) {
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } else {
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
        }
        return bos;
    }


    public static boolean isImage(File file) {
        return file.getName().toLowerCase().endsWith(".bmp")
            || file.getName().toLowerCase().endsWith(".jpg")
            || file.getName().toLowerCase().endsWith(".jpeg")
            || file.getName().toLowerCase().endsWith(".gif")
            || file.getName().toLowerCase().endsWith(".png");

    }

    public static boolean isPDf(File file) {
        return file.getName().toLowerCase().endsWith(".pdf");

    }

    public static String getOriginalFileName(URI uri) {
        List<NameValuePair> valuePairs = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);

        return valuePairs.stream()
            .filter(nameValuePair -> "response-content-disposition".equals(nameValuePair.getName()))
            .findFirst()
            .map(NameValuePair::getValue)
            .filter(s -> s.contains("="))
            .map(s -> s.split("=")[1].replaceAll("\"", ""))
            .orElse(FilenameUtils.getName(uri.getPath()));
    }

    public static byte[] readFileToByteArray(File file) {
        byte[] bytes = null;
        try (FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bytes = bos.toByteArray();
        } catch (Exception e) {
            log.error("[readFileToByteArray] fileName :{}, error:{}", file.getName(), e.getMessage(), e);
        }
        return bytes;
    }

    public static ByteArrayOutputStream addVoidWaterMarkToPDF(File pdfFile) throws IOException, DocumentException {
        ClassPathResource classPathResource = new ClassPathResource(VOID_MARK_PATH);
        String markImageFileUrl = classPathResource.getURL().toString();
        log.debug("markImageFile path : {}", markImageFileUrl);
        return addWaterMarkToPDF(pdfFile, markImageFileUrl, 300, 250, 30.0f, 20.0f, 0.7f);
    }

    public static ByteArrayOutputStream addWaterMarkToPDF(File pdfFile,
        String markImageFileUrl,
        float absoluteX,
        float absoluteY,
        float rotationDegrees,
        float scalePercent,
        float fillOpacity) throws IOException, DocumentException {

        String pdfFileTemp = pdfFile.getAbsolutePath();
        log.debug("pdfFileTemp : {}", pdfFileTemp);
        PdfReader reader = new PdfReader(pdfFileTemp);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfStamper stamp = new PdfStamper(reader, os);
        Image img = Image.getInstance(markImageFileUrl);
        img.setAbsolutePosition(absoluteX, absoluteY);
        img.setAlignment(ALIGN_CENTER);
        img.setRotationDegrees(rotationDegrees);
        img.scalePercent(scalePercent);

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte cb = stamp.getOverContent(i);
            cb.saveState();
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(fillOpacity);
            cb.setGState(gs);
            cb.addImage(img);
            cb.restoreState();
        }
        stamp.close();
        log.debug("ByteArrayOutputStream size : {}", os.size());
        return os;
    }


    public static BufferedImage convertBufferedImageColorToBWImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y));
                int grayValue = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                int grayRGB = new Color(grayValue, grayValue, grayValue).getRGB();
                outputImage.setRGB(x, y, grayRGB);
            }
        }
        return outputImage;
    }

    public static List<BufferedImage> pdfToImage(byte[] input, float dpi) {
        List<BufferedImage> imgList = new ArrayList<>();
        PDDocument pdDocument = null;
        PDFRenderer renderer;
        PdfReader reader = null;
        try {
            pdDocument = PDDocument.load(input);
            renderer = new PDFRenderer(pdDocument);
            reader = new PdfReader(input);
            List<BufferedImage> images = IntStream.range(0, pdDocument.getNumberOfPages())
                .parallel()
                .mapToObj(pageIndex -> {
                    try {
                        return renderer.renderImageWithDPI(pageIndex, dpi);
                    } catch (IOException e) {
                        log.error("renderImageWithDPI error, pageIndex :{}", pageIndex, e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
            imgList.addAll(images);
            reader.close();
            pdDocument.close();
        } catch (IOException e) {
            log.error("covert PDF to image error", e);
            throw new RuntimeException("Covert PDF to image error");
        }
        return imgList;
    }

    public static File convertPDFColorToGrayPDF(File file) {
        try {
            String fileName = FilenameUtils.getName(file.getName());
            ByteArrayOutputStream byteArrayOutputStream = convertPDFColorToGrayPDF(readFileToByteArray(file));
            return byteToFile(byteArrayOutputStream.toByteArray(),
                "Gray" + fileName,
                ".pdf");
        } catch (Exception e) {
            log.error("convertPDFColorToGrayPDF error,", e);
        }
        return null;
    }

    public static ByteArrayOutputStream convertPDFColorToGrayPDF(byte[] input) throws DocumentException {
        List<BufferedImage> bufferedImages = pdfToImage(input, 150);
        return bufferedImagesToPdf(bufferedImages, 841, 595);
    }

    public static ByteArrayOutputStream bufferedImagesToPdf(List<BufferedImage> bufferedImages, float height, float width)
        throws DocumentException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document();
        document.setMargins(0, 0, 0, 0);
        PdfWriter.getInstance(document, bos);
        document.open();
        bufferedImages.stream().forEach(bufferedImage -> {
                try {
                    BufferedImage bwImage = convertBufferedImageColorToBWImage(bufferedImage);
                    Image img = Image.getInstance(bwImage, Color.BLACK);
                    img.setAlignment(ALIGN_CENTER);
                    if (img.getHeight() > height || img.getWidth() > width) {
                        float heightPercent = height / img.getHeight() * 100;
                        float widthPercent = width / img.getWidth() * 100;
                        img.scalePercent(Math.min(widthPercent, heightPercent));
                    }
                    document.setPageSize(new Rectangle(width, height));
                    document.newPage();
                    document.add(img);
                } catch (Exception e) {
                    log.error("covert image to PDF error", e);
                }
            }
        );
        document.close();
        return bos;
    }

    public static boolean isReadable(File pdfFile) {
        boolean flag = false;

        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(pdfFile));
            Document document = new Document(pdfReader.getPageSize(1));
            document.open();
            if (pdfReader.getNumberOfPages() != 0) {
                log.debug("PdfFile :{} is Readable", pdfFile.getName());
                flag = true;
            }
            document.close();
        } catch (Exception e) {
            log.warn("PdfFile :{} is corrupted", pdfFile.getName());
        }
        return flag;
    }


}
