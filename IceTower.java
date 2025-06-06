public class IceTower extends Tower {
    public IceTower(int x, int y) {
        super(x, y);
        damage = 1;
        cost = 25;
    }
    
    @Override
    public void upgrade() {
        if (level < 5) {
            level++;
            // Damage increases based on level
            damage = level * 2 - 1;
        }
    }
    
    @Override
    public int getUpgradeCost() {
        switch(level) {
            case 1: return 60;
            case 2: return 45;
            case 3: return 50;
            case 4: return 130;
            default: return 0;
        }
    }
    
    @Override
    public int getSellValue() {
        return cost + getTotalUpgradeCost();
    }
    
    private int getTotalUpgradeCost() {
        int total = 0;
        for (int i = 1; i < level; i++) {
            total += getUpgradeCostForLevel(i);
        }
        return total;
    }
    
    private int getUpgradeCostForLevel(int lvl) {
        switch(lvl) {
            case 1: return 60;
            case 2: return 45;
            case 3: return 50;
            case 4: return 130;
            default: return 0;
        }
    }
}
