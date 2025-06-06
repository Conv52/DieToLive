public class Resources {
    private int coins;
    
    public Resources(int initialCoins) {
        this.coins = initialCoins;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public void addCoins(int amount) {
        coins += amount;
    }
    
    public void spend(int amount) {
        if (canAfford(amount)) {
            coins -= amount;
        }
    }
    
    public boolean canAfford(int amount) {
        return coins >= amount;
    }
}
