import java.awt.*;

public abstract class Zombie {
    protected int x, y;
    protected int health;
    protected int speed;
    protected int damage;
    protected int reward;
    
    public Zombie(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move() {
        // Pathfinding logic to move toward base
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 30, 30);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
    }
    
    public boolean isDead() {
        return health <= 0;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }
    
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }
    
    public int getDamage() {
        return damage;
    }
    
    public int getReward() {
        return reward;
    }
}
