import java.awt.*;

public abstract class Tower {
    protected int x, y;
    protected int level = 1;
    protected int damage;
    protected int range;
    protected int cost;
    
    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public abstract void upgrade();
    public abstract int getUpgradeCost();
    public abstract int getSellValue();
    
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString("Lvl " + level, x + 5, y + 20);
    }
    
    public void drawAtPosition(Graphics g, int x, int y) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 30, 30);
    }
    
    public boolean contains(Point p) {
        return new Rectangle(x, y, 30, 30).contains(p);
    }
    
    public int getDamage() {
        return damage;
    }
}
