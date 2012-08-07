package hotpot.game.mrnom;

import hotpot.game.framework.Game;
import hotpot.game.framework.Graphics;
import hotpot.game.framework.Input.TouchEvent;
import hotpot.game.framework.Screen;

import java.util.List;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();

        for (int i = 0; i < len; i++) {

            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                }

                if (inBounds(event, 64, 220, 192, 42)) {
                     game.setScreen(new GameScreen(game));
//                	game.setScreen(new TetorisGame(game));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
                
                if (inBounds(event, 64, 220 + 42, 192, 42)) {
                    game.setScreen(new HighScoreScreen(game));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }

                // TODO ヘルプページ実装
                if (inBounds(event, 64, 220 + 84, 192, 42)) {
                    game.setScreen(new HelpScreen(game));
                    if (Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
            }
        }
    }

    /**
     * タッチされた場所が、指定された領域内か判定する
     * @param event タッチイベント
     * @param x 判定対象の短形領域の左上のx座標
     * @param y 判定対象の短形領域の左上のy座標
     * @param width 判定対象の短形領域の幅
     * @param height 判定対象の短形領域の高さ
     * @return 短形領域内でタッチされていればtrue
     */
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.logo, 32, 20);
        g.drawPixmap(Assets.mainMenu, 64, 220);
        if (Settings.soundEnabled) {
            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        } else {
            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
        }
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
