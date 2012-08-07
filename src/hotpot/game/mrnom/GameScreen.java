package hotpot.game.mrnom;

import hotpot.game.framework.Game;
import hotpot.game.framework.Graphics;
import hotpot.game.framework.Input.TouchEvent;
import hotpot.game.framework.Pixmap;
import hotpot.game.framework.Screen;

import java.util.List;

import android.graphics.Color;
import android.util.Log;

/**
 * ゲームが行われる画面
 * 
 * @author takaesuyuuki
 * 
 */
public class GameScreen extends Screen {

    /**
     * ゲームの状態を表す
     * 
     * @author takaesuyuuki
     * 
     */
    enum GameState {
        Ready, Running, Paused, GameOver
    }

    GameState state = GameState.Ready;
    World world;
    int oldScore = 0;
    String score = "0";

    /**
     * コンストラクタ
     * 
     * @param game
     */
    public GameScreen(Game game) {
        super(game);
        world = new World();

    }

    @Override
    public void update(float deltaTime) {
        // タッチイベントとキーイベントを取得
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        // ゲームの状態によって描画内容を変更
        if (state == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }

    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();

        // 背景とゲーム全体の初期描画
        g.drawPixmap(Assets.background, 0, 0);
        drawWorld(world);

        // ゲームの状態によって描画内容を変更する
        if (state == GameState.Ready) {
            drawReadyUI();
        }
        if (state == GameState.Running) {
            drawRunningUI();
        }
        if (state == GameState.Paused) {
            drawPausedUI();
        }
        if (state == GameState.GameOver) {
            drawGameOverUI();
        }
        // スコアを表示する
        drawText(g, score, g.getHeight() / 2 - score.length() / 2, g.getHeight() - 42);
    }

    @Override
    public void pause() {
        if (state == GameState.Running) {
            state = GameState.Paused;
        }
        if (world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /**
     * ゲーム開始前の描画
     * 
     * @param touchEvents
     */
    private void updateReady(List<TouchEvent> touchEvents) {
        // 描画自体は何もしない。タッチイベントがあれば、ゲーム進行状態に移行する
        if (touchEvents.size() > 0) {
            state = GameState.Running;
        }
    }

    /**
     * ゲーム進行中の描画の更新処理
     * 
     * @param touchEvents
     * @param deltaTime
     */
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y < 64) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    state = GameState.Paused;
                    return;
                }
            }
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (event.x < 64 && event.y > 416) {
                    Log.e("DEBUG", "terun left go");
                    world.snake.turnLeft();
                }
                if (event.x > 256 && event.y > 416) {
                    world.snake.turnRight();
                }
            }
        }

        world.update(deltaTime);
        if (world.gameOver) {
            if (Settings.soundEnabled) {
                Assets.bitten.play(1);
            }
            state = GameState.GameOver;
        }
        if (oldScore != world.score) {
            oldScore = world.score;
            score = "" + oldScore;
            if (Settings.soundEnabled) {
                Assets.eat.play(1);
            }
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        if (Settings.soundEnabled) {
                            Assets.click.play(1);
                        }
                        state = GameState.Running;
                    }
                }

                if (event.y > 148 && event.y < 196) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 && event.y >= 200 && event.y <= 264) {
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        }
    }

    private void drawWorld(World world) {
        Graphics g = game.getGraphics();
        Snake snake = world.snake;
        SnakePart head = snake.parts.get(0);
        Stain stain = world.stain;

        Pixmap stainPixmap = null;
        if (stain.type == Stain.TYPE_1) {
            stainPixmap = Assets.stain1;
        }
        if (stain.type == Stain.TYPE_2) {
            stainPixmap = Assets.stain2;
        }
        if (stain.type == Stain.TYPE_3) {
            stainPixmap = Assets.stain3;
        }
        int x = stain.x * 32;
        int y = stain.y * 32;
        g.drawPixmap(stainPixmap, x, y);

        int len = snake.parts.size();
        for (int i = 0; i < len; i++) {
            SnakePart part = snake.parts.get(i);
            x = part.x * 32;
            y = part.y * 32;
            g.drawPixmap(Assets.tail, x, y);
        }
        Pixmap headPixmap = null;
        if (snake.direction == Snake.UP) {
            headPixmap = Assets.headUp;
        }
        if (snake.direction == Snake.LEFT) {
            headPixmap = Assets.headLeft;
        }
        if (snake.direction == Snake.DOWN) {
            headPixmap = Assets.headDown;
        }
        if (snake.direction == Snake.RIGHT) {
            headPixmap = Assets.headRight;
        }
        x = head.x * 32 + 16;
        y = head.y * 32 + 16;
        g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.ready, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.pause, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
    }

    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);
            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

}
