package GameWindow;

import DataBase.DataBase;
import Entity.CollisionCheck;
import Entity.Entity;
import Entity.Player;
import Graphics.AssetsSetter;
import Graphics.UI;
import Input.KeyManager;
import Map.TileManager;
import Objects.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable {
    static final int default_TileSize = 48;
    //public int tileSize = default_TileSize;

    //Camera parameters
    final int maxScreenCol = 25;
    final int maxScreenRow = 15;
    final int screenWidth = default_TileSize * maxScreenCol; //1200
    final int screenHeight = default_TileSize * maxScreenRow; //920

    //World parameters
    int maxWorldCol = 0; //1500
    int maxWorldRow = 0; //2500
    int FPS = 60;
    boolean playState;
    boolean menuState;

    KeyManager KeyMan = new KeyManager(this);
    Thread gameThread;
    public UI ui = new UI(this);
    public TileManager tileManager = new TileManager(this);
    public Player player = Player.getInstance(this, KeyMan);
    public AssetsSetter assets_setter = new AssetsSetter(this);

    public Entity[] enemies = new Entity[5];

    public ArrayList<Entity> gun = new ArrayList<Entity>();
    public SuperObject[] obj = new SuperObject[3];
    public CollisionCheck colCheck = new CollisionCheck(this);

    public DataBase db = DataBase.getInstance();

    public Game() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyMan);
        this.setFocusable(true);

    }

    public void setupGame() {
        playState = false;
        menuState = true;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double draw_interval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + draw_interval;
        while (gameThread != null) {
            update();
            repaint();
            try {
                double remaningTime = nextDrawTime - System.nanoTime();
                remaningTime = remaningTime / 1000000;

                if (remaningTime < 0)
                    remaningTime = 0;

                Thread.sleep((long) remaningTime);

                nextDrawTime += draw_interval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void update() {
        if (playState) {
            player.update();
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null) {
                    enemies[i].update();
                }

                if (enemies[i] == null) {
                    assets_setter.newEnemy(i);
                }
            }
            int tempindx = -99;
            for (int i = 0; i < gun.size(); i++) {
                gun.get(i).update();

                if (gun.get(i).used) {
                    tempindx = i;
                }
            }
            if (tempindx != -99)
                gun.remove(tempindx);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        if (menuState) {
            ui.draw(g2D);
        } else {
            tileManager.draw(g2D);

            for (SuperObject superObject : obj) {
                if (superObject != null) {
                    superObject.draw(g2D, this);

                }
            }

            for (Entity enemy : enemies) {
                if (enemy != null) {
                    enemy.draw(g2D);
                }
            }

            for (Entity bullet : gun) {
                if (!bullet.used) {
                    bullet.draw(g2D, 32, 32);
                }
            }

            player.draw(g2D);

            ui.draw(g2D);
        }

        g2D.dispose();
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setMaxWorldRow(int x) {
        maxWorldRow = x;
    }

    public void setMaxWorldCol(int x) {
        maxWorldCol = x;
    }

    public boolean getPlayState() {
        return playState;
    }

    public void setPlayState(boolean x) {
        playState = x;
    }

    public boolean getMenuState() {
        return menuState;
    }

    public void setMenuState(boolean x) {
        menuState = x;
    }

    public static int getDefaultTileSize_s() {
        return default_TileSize;
    }

    public int getDefaultTileSize() {
        return default_TileSize;
    }


}
