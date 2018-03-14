package com.Ease.Utils.File;

import com.Ease.Context.Variables;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {

    private static void createLogo(String uploadPath, String name) throws HttpServletException {
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdir();
            String filePath = uploadPath + File.separator + "logo.png";
            OutputStream outputStream = new FileOutputStream(new File(filePath));
            String icon;
            String[] name_splitted = name.split(" ");
            icon = name_splitted[0].substring(0, 1).replaceAll("\\W", "_");
            if (name_splitted.length > 1)
                icon += name_splitted[1].substring(0, 1).replaceAll("\\W", "_");
            byte[] file = download(new URL("http://placehold.it/175x175/373b60/FFFFFF/&text=" + icon.toUpperCase()));
            IOUtils.write(file, outputStream);
        } catch (Exception e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static void createWebsiteLogo(String folder, String name) throws HttpServletException {
        createLogo(Variables.WEBSITES_FOLDER_PATH + folder, name);
    }

    public static void createSoftwareLogo(String folder, String name) throws HttpServletException {
        createLogo(Variables.SOFTWARE_FOLDER_PATH + folder, name);
    }

    private static byte[] download(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        int len = uc.getContentLength();
        try (InputStream is = new BufferedInputStream(uc.getInputStream())) {
            byte[] data = new byte[len];
            int offset = 0;
            while (offset < len) {
                int read = is.read(data, offset, data.length - offset);
                if (read < 0) {
                    break;
                }
                offset += read;
            }
            if (offset < len) {
                throw new IOException(
                        String.format("Read %d bytes; expected %d", offset, len));
            }
            return data;
        }
    }
}
