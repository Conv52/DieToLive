import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    // Game states
    private static final int BUILD_PHASE = 0;
    private static final int COMBAT_PHASE = 1;
    private static final int PHASE_2 = 2;
    private static final int GAME_OVER = 3;
    private static final int WIN = 4;
    
    private int gameState = BUILD_PHASE;
    private int currentWave = 1;
    private int baseHealth = 100;
    
    // Grid settings
    private static final int GRID_SIZE = 40;
    private static final int ROWS = 15;
    private static final int COLS = 20;
    private int[][] grid = new int[ROWS][COLS];
    
    // Resources
    private Resources resources;
    
    // Game objects
    private ArrayList<Tower> towers = new ArrayList<>();
    private ArrayList<Zombie> zombies = new ArrayList<>();
    private Tower selectedTower;
    private Tower draggedTower;
    private Point dragOffset = new Point();
    
    // UI
    private Font gameFont = new Font("Arial", Font.BOLD, 16);
    
    public GamePanel() {
        setPreferredSize(new Dimension(COLS * GRID_SIZE, ROWS * GRID_SIZE));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
        resources = new Resources(100);
        
        // Set up game timer
        Timer timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw grid
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                g.setColor(grid[row][col] == 1 ? Color.GREEN : Color.BLACK);
                g.fillRect(col * GRID_SIZE, row * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(col * GRID_SIZE, row * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }
        
        // Draw towers
        for (Tower tower : towers) {
            tower.draw(g);
        }
        
        // Draw zombies
        for (Zombie zombie : zombies) {
            zombie.draw(g);
        }
        
        // Draw base
        g.setColor(Color.RED);
        g.fillRect((COLS/2) * GRID_SIZE, (ROWS/2) * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        g.setColor(Color.WHITE);
        g.drawString("BASE", (COLS/2) * GRID_SIZE + 5, (ROWS/2) * GRID_SIZE + 20);
        
        // Draw UI
        drawUI(g);
        
        // Draw dragged tower
        if (draggedTower != null) {
            Point mousePos = getMousePosition();
            if (mousePos != null) {
                draggedTower.drawAtPosition(g, mousePos.x - dragOffset.x, mousePos.y - dragOffset.y);
            }
        }
    }
    
    private void drawUI(Graphics g) {
        g.setFont(gameFont);
        g.setColor(Color.YELLOW);
        g.drawString("Coins: " + resources.getCoins(), 10, 20);
        g.drawString("Wave: " + currentWave + "/40", getWidth() - 100, 20);
        g.drawString("Base HP: " + baseHealth, getWidth() / 2 - 40, 20);
        
        if (gameState == PHASE_2) {
            g.setColor(Color.RED);
            g.drawString("PHASE 2: LAST STAND", getWidth() / 2 - 80, 40);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Game logic updates
        if (gameState == COMBAT_PHASE) {
            updateCombat();
        } else if (gameState == PHASE_2) {
            updatePhase2();
        }
        
        repaint();
    }
    
    private void updateCombat() {
        // Move zombies
        for (Zombie zombie : zombies) {
            zombie.move();
            
            // Check if zombie reached base
            if (zombie.getBounds().intersects(new Rectangle((COLS/2) * GRID_SIZE, (ROWS/2) * GRID_SIZE, GRID_SIZE, GRID_SIZE))) {
                baseHealth -= zombie.getDamage();
                zombies.remove(zombie);
                break;
            }
        }
        
        // Check game over conditions
        if (baseHealth <= 0) {
            if (currentWave > 20) {
                gameState = PHASE_2;
                // Trigger phase 2 effects
            } else {
                gameState = GAME_OVER;
            }
        }
        
        // Check wave completion
        if (zombies.isEmpty()) {
            resources.addCoins(30 + 15 * currentWave);
            currentWave++;
            gameState = BUILD_PHASE;
        }
    }
    
    private void updatePhase2() {
        // Phase 2 specific logic
        if (baseHealth <= 0) {
            gameState = GAME_OVER;
        }
        
        // Check win condition
        if (currentWave >= 40) {
            gameState = WIN;
        }
    }
    
    // Input handling methods
    @Override
    public void mousePressed(MouseEvent e) {
        if (gameState == BUILD_PHASE) {
            int gridX = e.getX() / GRID_SIZE;
            int gridY = e.getY() / GRID_SIZE;
            
            // Check if clicked on tower
            for (Tower tower : towers) {
                if (tower.contains(e.getPoint())) {
                    selectedTower = tower;
                    return;
                }
            }
            
            // Create new tower if hotkey pressed
            if (draggedTower == null) {
                // Tower creation logic based on key presses
                // (Implementation depends on key listener)
            }
        } else if (gameState == PHASE_2) {
            // Handle click damage in phase 2
            for (Zombie zombie : zombies) {
                if (zombie.contains(e.getPoint())) {
                    zombie.takeDamage(1); // Base click damage
                    if (zombie.isDead()) {
                        resources.addCoins(zombie.getReward());
                        zombies.remove(zombie);
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Tower hotkeys
        if (gameState == BUILD_PHASE) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    draggedTower = new ArrowTower(0, 0);
                    break;
                case KeyEvent.VK_2:
                    draggedTower = new BombTower(0, 0);
                    break;
                case KeyEvent.VK_3:
                    draggedTower = new IceTower(0, 0);
                    break;
                case KeyEvent.VK_4:
                    draggedTower = new MiniGunner(0, 0);
                    break;
                case KeyEvent.VK_SPACE:
                    if (gameState == BUILD_PHASE) {
                        gameState = COMBAT_PHASE;
                        spawnWave();
                    }
                    break;
                case KeyEvent.VK_T:
                    if (selectedTower != null && resources.canAfford(selectedTower.getUpgradeCost())) {
                        resources.spend(selectedTower.getUpgradeCost());
                        selectedTower.upgrade();
                    }
                    break;
                case KeyEvent.VK_Z:
                    if (selectedTower != null) {
                        resources.addCoins((int)(selectedTower.getSellValue() * 0.6));
                        towers.remove(selectedTower);
                        selectedTower = null;
                    }
                    break;
            }
        }
    }
    
    private void spawnWave() {
        // Create zombies based on current wave
        // Implementation depends on zombie types and wave progression
    }

    // Other necessary interface methods
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
