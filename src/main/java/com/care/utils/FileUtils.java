package com.care.utils;

import com.care.Constants;
import com.care.domain.Picture;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by nujian on 16/2/22.
 */
public class FileUtils {


    private static final String PATH_SPARATOR = "/";


    public static String createFile(MultipartFile file) throws Exception {
        Assert.notNull(file);
        byte[] data = file.getBytes();
        return createFile(data);
    }


    public static String createFile(byte[] data) throws Exception {
        if (getImgType(data) == null)
            throw new Exception("can't determine image type");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String dateDir = format.format(new Date());

        String fileToken = UUID.randomUUID().toString();

        File dir = new File(Constants.upload_base_dir + PATH_SPARATOR + dateDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String storeDir = Constants.upload_base_dir + PATH_SPARATOR + dateDir;
        String fileName = fileToken + "." + getImgType(data);
        //todo 图片压缩
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(storeDir, fileName)));
        stream.write(data);
        stream.close();

        final BufferedImage img_280_280 = resize(data, 280, 280);
        final String fileName_280 = fileToken + Picture.PIC_280_280 + ".jpg";
        final String jpg = "jpg";
        ImageIO.write(img_280_280, jpg, new File(storeDir, fileName_280));

        return dateDir + PATH_SPARATOR + fileToken;
    }


    public static BufferedImage resize(byte[] data, final int desiredWidth, final int desiredHeight) throws IOException {
        BufferedImage rawImage = ImageIO.read(new ByteArrayInputStream(data));

        int orginWidth = rawImage.getWidth();
        int orginHeight = rawImage.getHeight();
        double orginRatio = orginWidth / (double) orginHeight;
        double desiredRatio = desiredWidth / (double) desiredHeight;

        int x = 0, y = 0;
        int scaleWidth = desiredWidth, scaleHeight = desiredHeight;

        switch (Double.compare(orginRatio, desiredRatio)) {
            case 1:
                scaleWidth = (int) Math.ceil(orginWidth * desiredHeight / (double) orginHeight);
                x = (scaleWidth - desiredWidth) / 2;
                break;
            case -1:
                scaleHeight = (int) Math.ceil(orginHeight * desiredWidth / (double) orginWidth);
                y = (scaleHeight - desiredHeight) / 2;
                break;
            default:
                break;
        }
        BufferedImage scaleImg = Scalr.resize(rawImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                scaleWidth, scaleHeight, Scalr.OP_ANTIALIAS);
        BufferedImage crop = scaleImg.getSubimage(x, y, desiredWidth, desiredHeight);

        //dealing with PNG to JPG conversion bug
        BufferedImage imageRGB = new BufferedImage(crop.getWidth(),
                crop.getHeight(), BufferedImage.TYPE_INT_RGB);
        imageRGB.getGraphics().fillRect(0, 0, crop.getWidth(), crop.getHeight());
        imageRGB.getGraphics().drawImage(crop, 0, 0, null);
        return imageRGB;
    }

    public static String getImgType(byte[] imgdata) throws Exception {
        String imageType = null;
        if (imgdata == null || imgdata.length < 9) {
            throw new Exception("not a image file");
        }

        if ((imgdata[0] == 71) && (imgdata[1] == 73) && (imgdata[2] == 70)
                && (imgdata[3] == 56) && ((imgdata[4] == 55) || (imgdata[4] == 57))
                && (imgdata[5] == 97)) {
            imageType = "GIF";
        }
        if ((imgdata[6] == 74) && (imgdata[7] == 70) && (imgdata[8] == 73)
                && (imgdata[9] == 70)) {
            imageType = "JPG";
        }
        if ((imgdata[0] == 66) && (imgdata[1] == 77)) {
            imageType = "BMP";
        }
        if ((imgdata[1] == 80) && (imgdata[2] == 78) && (imgdata[3] == 71)) {
            imageType = "PNG";
        }

        String formatName = determineImageFormat(imgdata);
        if (StringUtils.isNotBlank(formatName)) {
            if (formatName.equalsIgnoreCase("jpeg")) {
                formatName = "JPG";
            }
            imageType = formatName.toUpperCase();
        }
        if(StringUtils.isBlank(imageType)){
            throw new Exception("unsupported image file");
        }
        return imageType.toLowerCase();
    }

    public static String determineImageFormat(byte[] imageBytes) throws IOException {
        final ByteArrayInputStream bStream = new ByteArrayInputStream(imageBytes);
        final ImageInputStream imgStream = ImageIO.createImageInputStream(bStream);
        final Iterator<ImageReader> iter = ImageIO.getImageReaders(imgStream);
        final ImageReader imgReader = iter.next();
        return imgReader.getFormatName();
    }

    public static String determineImageFormat(File f) throws IOException {
        final ImageInputStream imgStream = ImageIO.createImageInputStream(f);
        final Iterator<ImageReader> iter = ImageIO.getImageReaders(imgStream);
        final ImageReader imgReader = iter.next();
        return imgReader.getFormatName();
    }


}
