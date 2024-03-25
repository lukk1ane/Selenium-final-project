import java.awt.Color;

public class Main {
    public static void main(String[] args) {

    }




    public static String convertRGBToHex(String rgbColor) {
        String[] rgbValues = rgbColor.replace("rgba(", "").replace(")", "").split(",");
        int r = Integer.parseInt(rgbValues[0].trim());
        int g = Integer.parseInt(rgbValues[1].trim());
        int b = Integer.parseInt(rgbValues[2].trim());
        Color color = new Color(r, g, b);
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
