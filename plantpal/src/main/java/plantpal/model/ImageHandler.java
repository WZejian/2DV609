package plantpal.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;

public class ImageHandler {

    public static Image getFileFromBlob(String id, Blob blobIn) {
        final Path placeholderPath = Paths.get("tmp/placeholder.PNG");
        try {
            String imgPathstr = "tmp/" + id + ".jpeg";
            Path outPath = Paths.get(imgPathstr).toAbsolutePath();

            // Conversion from blob to byte[]
            byte[] binary = null;
            int blobLength = (int) blobIn.length();
            binary = blobIn.getBytes(1, blobLength);

            // ImageIO reads from binary supplied by binaryIn, then this buffered image is
            // written
            ByteArrayInputStream binaryIn = new ByteArrayInputStream(binary);
            BufferedImage newImage = ImageIO.read(binaryIn);
            ImageIO.write(newImage, "jpeg", new File(outPath.toString()));
            binaryIn.close();

            Image fxImage = new Image(outPath.toUri().toString());
            return fxImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Image(placeholderPath.toUri().toString());

    }

    /**
     * Clears the tmp folder of files, spares any subdirectories.
     */
    public static void clearTmp() {
        for (File file : (new File("./tmp")).listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static Blob imageToBlob(Image img) {
        Blob imgBlob = null;
        try {
            URI imgURI = new URI(img.getUrl());
            FileInputStream fileinputStream = new FileInputStream(Paths.get(imgURI).toString()); // Original file reader
            BufferedImage bImage = ImageIO.read(fileinputStream);// Read original file
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            new ByteArrayInputStream(res);
            imgBlob = new javax.sql.rowset.serial.SerialBlob(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgBlob;
    }

    /**
     * Crop the image to square.
     */
    public static Image cropToSquare(Image img) {
        double d = Math.min(img.getWidth(), img.getHeight());
        double x = (d - img.getWidth()) / 2;
        double y = (d - img.getHeight()) / 2;
        
        Canvas canvas = new Canvas(d, d);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(img, x, y);
        
        return canvas.snapshot(null, null);
    }

    /**
     * Crop the image to 3:2.
     */
    public static Image cropTo32(Image img) {
        double ratio = 1.5;
        int width = (int) Math.min(img.getWidth(), img.getHeight() * ratio);
        int height = (int) Math.min(img.getWidth() / ratio, img.getHeight());
        int x = (int) (width - img.getWidth()) / 2;
        int y = (int) (height - img.getHeight()) / 2;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.drawImage(img, x, y);

        return canvas.snapshot(null, null);
    }

}
