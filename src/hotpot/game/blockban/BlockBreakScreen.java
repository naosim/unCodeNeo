package hotpot.game.blockban;

import hotpot.game.framework.Game;
import hotpot.game.framework.Graphics;
import hotpot.game.framework.Input.TouchEvent;
import hotpot.game.framework.Screen;
import hotpot.game.framework.impl.AndroidGraphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class BlockBreakScreen extends Screen {

    enum BollDirection {
        LEFT_UP, LEFT_DOWN, RIGHT_UP, RIGHT_DOWN
    }

    class Block {
        int left;
        int top;
        int width;
        int height;
        int color;
        boolean isVisivle;

        public Block(int left, int top, int w, int h, int color) {
            this.left = left;
            this.top = top;
            width = w;
            height = h;
            this.color = color;
        }
    }

    List<Block> blocks;

    // 操作台の設定
    public int daiLeft = 110;
    public int daiTop = 400;
    public int daiWidth = 100;
    public int daiHeight = 16;
    public int daiColor = Color.RED;

    // 玉の設定
    public int bollLeft = 110;
    public int bollTop = 280;
    public int bollCy = 5;// 玉の直径
    public int bollColor = Color.GRAY;
    public BollDirection bollDirection = BollDirection.LEFT_UP;
    public int moveKyori = 4; // 玉の移動速度
    public int syoritien = 9; // 処理遅延

    public BlockBreakScreen(Game game) {
        super(game);
        // ブロックの初期配置
        blocks = new ArrayList<Block>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                Block newBlock = new Block(30 + i * 33, 53 + j * 17, 32, 16, Color.BLUE);
                newBlock.isVisivle = true;
                blocks.add(newBlock);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        for (TouchEvent event : touchEvents) {
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.y > 416) {// 画面の下をクリックしたら台をその場所に移動
                    daiLeft = event.x - daiWidth / 2;
                    if (daiLeft < 0) {
                        daiLeft = 0;
                    }
                    if (daiLeft > 320) {
                        daiLeft = 320 - daiWidth;
                    }
                }
            }
        }
        // 玉が壁にあったったとき、方向を更新
        if (bollDirection == BollDirection.LEFT_UP && bollLeft <= (0 + bollCy)) {// 左上に上昇中に壁に当たったら右上に移動
            bollDirection = BollDirection.RIGHT_UP;
        } else if (bollDirection == BollDirection.RIGHT_UP && bollLeft >= (320 - bollCy)) {// 右上に移動中に右の壁に当たったら左上に移動
            bollDirection = BollDirection.LEFT_UP;
        } else if (bollDirection == BollDirection.LEFT_DOWN && bollLeft <= (0 + bollCy)) {// 左下に移動中に左の壁に当たったら右下に移動
            bollDirection = BollDirection.RIGHT_DOWN;
        } else if (bollDirection == BollDirection.RIGHT_DOWN && bollLeft >= (320 - bollCy)) {// 右下に移動中に右の壁に当たったら左下に移動
            bollDirection = BollDirection.LEFT_DOWN;
        } else if (bollDirection == BollDirection.LEFT_UP && bollTop <= (0 + bollCy)) {// 左上に移動中に上の壁に当たると左下に移動
            bollDirection = BollDirection.LEFT_DOWN;
        } else if (bollDirection == BollDirection.RIGHT_UP && bollTop <= (0 + bollCy)) {// 右上に移動中に上の壁に当たると右下に移動
            bollDirection = BollDirection.RIGHT_DOWN;
        }

        // 表示されているブロックに触れたとき、玉を反転。ついでにブロックを非表示にする
        for (Block block : blocks) {
            if (block.isVisivle == false) {
                continue;
            }
            // ブロックの上部分に触れたときの処理
            if (bollDirection == BollDirection.LEFT_DOWN
                    && (bollTop + bollCy < block.top + block.height && bollTop + bollCy > block.top)
                    && (bollLeft > block.left && bollLeft < block.left + block.width)) {// 左下に移動中にブロックの上部に触れたら左上に移動
                bollDirection = BollDirection.LEFT_UP;
                block.isVisivle = false;
            } else if (bollDirection == BollDirection.RIGHT_DOWN
                    && (bollTop + bollCy < block.top + block.height && bollTop + bollCy > block.top)
                    && (bollLeft > block.left && bollLeft < block.left + block.width)) {// 右下に移動中にブロックの上部に触れたら右上に移動
                bollDirection = BollDirection.RIGHT_UP;
                block.isVisivle = false;

                // ブロックのした部分に触れたときの処理
            } else if (bollDirection == BollDirection.RIGHT_UP && (bollTop - syoritien < block.top + block.height)
                    && (bollLeft > block.left && bollLeft < block.left + block.width)) {// 右上に移動中にブロックの下部に触れたら右下に移動
                bollDirection = BollDirection.RIGHT_DOWN;
                block.isVisivle = false;
            } else if (bollDirection == BollDirection.LEFT_UP
                    && (bollTop - syoritien < block.top + block.height && bollTop < block.top)
                    && (bollLeft > block.left && bollLeft < block.left + block.width)) {// 左上に移動中にブロックの下部に触れたら右下に移動
                bollDirection = BollDirection.LEFT_DOWN;
                block.isVisivle = false;
            }

            // ブロックの左部分に触れたときの処理
            if (bollDirection == BollDirection.RIGHT_UP
                    && (bollLeft + bollCy < block.left + block.width && bollLeft + bollCy > block.left)
                    && ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop + bollCy < block.top
                            + block.height && bollTop + bollCy > block.top))) {// 右上に移動中だったら左上に移動
                bollDirection = BollDirection.LEFT_UP;
                block.isVisivle = false;
            } else if (bollDirection == BollDirection.RIGHT_DOWN
                    && (bollLeft + bollCy < block.left + block.width && bollLeft + bollCy > block.left)
                    && ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop + bollCy < block.top
                            + block.height && bollTop + bollCy > block.top))) {// 右下に移動中なら左下に移動
                bollDirection = BollDirection.LEFT_DOWN;
                block.isVisivle = false;
            }

            // ブロックの右部分に触れた時の処理
            if (bollDirection == BollDirection.LEFT_UP
                    && (bollLeft < block.left + block.width && bollLeft > block.left)
                    && ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop + bollCy < block.top
                            + block.height && bollTop + bollCy > block.top))) {// 左上に移動中だったら右上に移動
                bollDirection = BollDirection.RIGHT_UP;
                block.isVisivle = false;
            } else if (bollDirection == BollDirection.LEFT_DOWN
                    && (bollLeft < block.left + block.width && bollLeft > block.left)
                    && ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop + bollCy < block.top
                            + block.height && bollTop + bollCy > block.top))) {// 左下に移動中なら右下に移動
                bollDirection = BollDirection.RIGHT_DOWN;
                block.isVisivle = false;
            }
        }

        // 操作台に触れたとき、玉の方向を変更
        if (bollDirection == BollDirection.LEFT_DOWN && (bollTop + bollCy > daiTop && bollTop < daiTop + daiHeight)
                && (bollLeft > daiLeft && bollLeft < daiLeft + daiWidth)) {// 左下に移動中に台に触れたら左上に移動
            bollDirection = BollDirection.LEFT_UP;
        } else if (bollDirection == BollDirection.RIGHT_DOWN && (bollTop + bollCy > daiTop && bollTop < daiTop + daiHeight)
                && (bollLeft > daiLeft && bollLeft < daiLeft + daiWidth)) {// 左下に移動中に台に触れたら左上に移動
            bollDirection = BollDirection.RIGHT_UP;
        }

        // 玉の場所を更新
        if (bollDirection == BollDirection.LEFT_UP) {
            bollLeft -= moveKyori;
            bollTop -= moveKyori;
        } else if (bollDirection == BollDirection.RIGHT_UP) {
            bollLeft += moveKyori;
            bollTop -= moveKyori;
        } else if (bollDirection == BollDirection.LEFT_DOWN) {
            bollLeft -= moveKyori;
            bollTop += moveKyori;
        } else if (bollDirection == BollDirection.RIGHT_DOWN) {
            bollLeft += moveKyori;
            bollTop += moveKyori;
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 320, 480, Color.WHITE);// 背景

        g.drawRect(daiLeft, daiTop, daiWidth, daiHeight, daiColor);// 操作台
        ((AndroidGraphics) g).drawCircle(bollLeft, bollTop, bollCy, bollColor);// 玉

        // ブロックの描画
        for (Block block : blocks) {
            if (block.isVisivle == false) {
                continue;
            }
            g.drawRect(block.left, block.top, block.width, block.height, block.color);
        }

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
