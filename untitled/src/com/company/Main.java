package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

//тут не трогай
interface ImageOperation {
    int execute(int rgb) throws Exception;
}

//и тут не трогай
interface ImageIteratorCallback {
    void callback(int rgb);
}

//а этот класс особенно не трогай. Трогать можно экземпляр этого класса
class RgbMaster {
    public boolean hasAlphaChannel;
    private BufferedImage image;
    private int width;
    private int height;
    private int[] colors;

    RgbMaster(String path) throws IOException {
        //Делаем объект ImageIO для использования API ввода / вывода изображения.
        image = ImageIO.read(new File(path));
        //Получаем ширину.
        width = image.getWidth();
        //Получаем высоту.
        height = image.getHeight();
        //Получаем массив integer пикселей RGB.
        colors = image.getRGB(0, 0, width, height, null, 0, width * height);
        //Есть ли прозрачность.
        hasAlphaChannel = image.getAlphaRaster() != null;
    }

    static float[] rgbIntToArray(int rgbInt) {
        Color color = new Color(rgbInt);
        return color.getRGBComponents(null); // Получаем массив из цветового int. Вида: [red, green, blue, alpha].
    }

    static int rgbArrayToInt(float[] rgbArray) throws Exception {
        //Получаем цвет в виде int из массива [r,g,b] или [r,g,b,a].
        if (rgbArray.length == 3) {
            return new Color(rgbArray[0], rgbArray[1], rgbArray[2]).getRGB();
        } else if (rgbArray.length == 4) {
            return new Color(rgbArray[0], rgbArray[1], rgbArray[2], rgbArray[3]).getRGB();
        } else {
            throw new Exception("invalid color");
        }
    }

    void changeImage(ImageOperation operation) throws Exception {
        //Проходимся по всем пикселям в картинке и переопределяем значением которое вернула lambda.
        for (int i = 0; i < colors.length; i++) {
            colors[i] = operation.execute(colors[i]);
        }
    }

    void iterateInImage(ImageIteratorCallback operation) {
        //Проходимся по всем пикселям в картинке и вызываем lambd-у с аргументов в виде цвета(int).
        for (int i = 0; i < colors.length; i++) {
            operation.callback(colors[i]);
        }
    }

    void save(String fileName) throws IOException {
        //Проверяем тип с прозрачностью ли картинка, создаём объект который будет хранить картинку и в итоге сохранит её.
        int type = hasAlphaChannel ? BufferedImage.TYPE_INT_ARGB: BufferedImage.TYPE_INT_RGB;
        image = new BufferedImage(width, height, type);
        image.setRGB(0, 0, width, height, colors, 0, width * height);
        ImageIO.write(image, "png", new File(fileName));
    }
}

//TODO: Тебе нужно реализовать реализацию этого класса ImageFunctionsImpl.
//И уже эту реализацию использовать как лямбды
abstract class ImageFunctions {
    HashMap<Integer, Integer> frequency = new HashMap<>(); //<color, count>

    abstract int greyScale(int color) throws Exception;

    abstract int sepia(int color) throws Exception;

    abstract int inversion(int color) throws Exception;

    abstract int onlyRed(int color) throws Exception;

    abstract int onlyGreen(int color) throws Exception;

    abstract int onlyBlue(int color) throws Exception;

    abstract void fft(int color); //fill frequency map
}

class ImageFunctionsImpl extends ImageFunctions {
    @Override
    int greyScale(int color) throws Exception {
        //Press f to pay respect.
        float[] pixel = RgbMaster.rgbIntToArray(color);
        float avg = (pixel[0] + pixel[1] + pixel[2]) / 3;

        return RgbMaster.rgbArrayToInt(new float[] {avg, avg, avg, pixel[3]});
    }

    @Override
    int sepia(int color) throws Exception {
        //(Кастуем) магические заклинания с магическими цифрами.
        float[] pixel = RgbMaster.rgbIntToArray(color);
        float[] newPixel = new float[4];
        float tone = (float) (.299 * pixel[0] + .587 * pixel[1] + .114 * pixel[2]);

        newPixel[0] = (float) ((tone > .8078) ? 1 : tone + .1922);
        newPixel[1] = (float) ((tone < .0549) ? 0 : tone - .0549);
        newPixel[2] = (float) ((tone < .2196) ? 0 : tone - .2196);

        newPixel[3] = pixel[3];

        return RgbMaster.rgbArrayToInt(newPixel);
    }

    @Override
    int inversion(int color) throws Exception {
        //Что же мы тут делаем ...
        float[] pixel = RgbMaster.rgbIntToArray(color);

        pixel[0] = 1 - pixel[0];
        pixel[1] = 1 - pixel[1];
        pixel[2] = 1 - pixel[2];

        return RgbMaster.rgbArrayToInt(pixel);
    }

    @Override
    int onlyRed(int color) throws Exception {
        //Обнуляем зелёный и синий.
        float[] pixel = RgbMaster.rgbIntToArray(color);

        pixel[1] = 0;
        pixel[2] = 0;
        return RgbMaster.rgbArrayToInt(pixel);
    }

    @Override
    int onlyGreen(int color) throws Exception {
        //Обнуляем красный и синий.
        float[] pixel = RgbMaster.rgbIntToArray(color);

        pixel[0] = 0;
        pixel[2] = 0;
        return RgbMaster.rgbArrayToInt(pixel);
    }

    @Override
    int onlyBlue(int color) throws Exception {
        //Обнуляем красный и зелёный.
        float[] pixel = RgbMaster.rgbIntToArray(color);

        pixel[0] = 0;
        pixel[1] = 0;

        return RgbMaster.rgbArrayToInt(pixel);
    }

    @Override
    void fft(int color) {
        //Делаем инкремент в мапе.
        frequency.merge(color, 1, Integer::sum);
    }
}

/* one eternity later*/
/* 75 years later*/
public class Main {
    public static void main(String[] args) throws Exception {
        RgbMaster rbgMaster = new RgbMaster("in_image.png");
        ImageFunctionsImpl imageFunc = new ImageFunctionsImpl();

        rbgMaster.changeImage(imageFunc::onlyRed);
//        rbgMaster.changeImage(imageFunc::onlyBlue);
//        rbgMaster.changeImage(imageFunc::onlyGreen);
//        rbgMaster.changeImage(imageFunc::greyScale);
//        rbgMaster.changeImage(imageFunc::sepia);
//        rbgMaster.changeImage(imageFunc::inversion);
        rbgMaster.save("out_image.png");

//        rbgMaster.iterateInImage(imageFunc::fft);
//        System.out.println(imageFunc.frequency);
    }
}