import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ToolKits {
    public static void StringToPng(String text, String outputPath) {
        // 设置图片的宽度和高度
        int width = 200;
        int height = 100;

        // 创建一个带有白色背景的图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色为白色
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, width, height);

        // 设置字体颜色为黑色
        g2d.setColor(Color.BLACK);

        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);

        // 计算文本的边界，用于居中显示
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        // 绘制文本
        g2d.setColor(Color.darkGray);
        g2d.drawString("sdlksj",x,y);
        g2d.setColor(Color.getColor(""));
        g2d.drawString(text, x, y);

        // 释放资源
        g2d.dispose();

        // 将图片写入文件
        try {
            ImageIO.write(image, "png", new File(outputPath));
            System.out.println("Image created successfully.");
        } catch (IOException ignored) {
        }
    }

}
