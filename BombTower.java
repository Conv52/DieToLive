public class BombTower extends Tower {
    public BombTower(int x, int y) {
        super(x, y);
        damage = 5;
        cost = 40;
    }
    
    @Override
    public void upgrade() {
        // No upgrades for bomb tower
    }
    
    @Override
    public int getUpgradeCost() {
        return 0;
    }
    
    @Override
    public int getSellValue() {
        return cost;
    }
}
