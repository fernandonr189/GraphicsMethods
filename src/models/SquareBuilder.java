package models;

import java.awt.*;

public class SquareBuilder implements BuildMethods{
    @Override
    public void build(CustomBuffer buffer) {
        buffer.BresenhamLine(20, 20, 180, 20, Color.red);
        buffer.BresenhamLine(180, 20, 180, 180, Color.red);
        buffer.BresenhamLine(180,180, 20, 180, Color.red);
        buffer.BresenhamLine(20,180, 20, 20, Color.red);
        buffer.floodFill(21, 21, Color.red);
    }

    @Override
    public CustomBuffer scale(CustomBuffer buffer, double factor) {
        return null;
    }
}
