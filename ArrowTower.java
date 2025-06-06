public class ArrowTower extends Tower {
    public ArrowTower(int x, int y) {
        super(x, y);
        damage = 2;
        cost = 30;
    }
    
    @Override
    public void upgrade() {
        if (level < 3) {
            level++;
            if (level == 2) damage = 4;
            else if (level == 3) damage = 8;
        }
    }
    
    @Override
    public int getUpgradeCost() {
        if (level == 1) return 20;
        if (level == 2) return 50;
        return 0; // Max level
    }
    
    @Override
    public int getSellValue() {
        return cost + (level > 1 ? 20 : 0) + (level > 2 ? 50 : 0);
    }
}
