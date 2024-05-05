package models;

import java.awt.*;

public class CircleBuilder implements BuildMethods{
    @Override
    public CustomBuffer build(CustomBuffer buffer) {
        buffer.basicCircle(100, 100,80, Color.blue);
        buffer.floodFill(100, 100, Color.blue);
        buffer.DDALine(20, 20, 180, 180, Color.blue);
        return buffer;
    }
}
