import java.util.ArrayList;

public class WaveManager {
    private int currentWave = 1;
    
    public void startNextWave() {
        currentWave++;
    }
    
    public ArrayList<Zombie> generateWave() {
        ArrayList<Zombie> zombies = new ArrayList<>();
        // Logic to create zombies based on current wave
        return zombies;
    }
    
    public int getCurrentWave() {
        return currentWave;
    }
}
