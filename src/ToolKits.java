import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ToolKits {
    public static void StringToPng(String text, String outputPath) {
        // 设置图片的宽度和高度
        int width = 200;
        int height = 100;

        // 创建一个带有白色背景的图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 设置背景颜色为白色
        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, width, height);

        // 设置字体颜色为黑色
        g2d.setColor(Color.BLACK);

        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 35);
        g2d.setFont(font);

        // 计算文本的边界，用于居中显示
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        // 绘制文本
        g2d.setColor(Color.darkGray);
        g2d.drawString("======",x,y);
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
    public static String getRandomApiKey() {
        Random random = new Random();
        String a = "qwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20 ;i++) {
            stringBuilder.append(a.charAt(random.nextInt(a.length())));
        }
        return stringBuilder.toString();
    }
    public static long getNextHourAsInt() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取下一个小时的时间
        LocalDateTime nextHour = now.plusHours(1);
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 将下一个小时的时间格式化为字符串
        String formattedNextHour = nextHour.format(formatter).trim();
        // 将格式化后的字符串转换为整数
        return Long.parseLong(formattedNextHour);
    }
    public static long getNowAsInt() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextHour = now.plusHours(0);
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 将下一个小时的时间格式化为字符串
        String formattedNextHour = nextHour.format(formatter).trim();
        // 将格式化后的字符串转换为整数
        return Long.parseLong(formattedNextHour);
    }
}
