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

	/**
	 * 背景
	 * 
	 * @author naosim
	 * 
	 */
	public static class Background implements GraphicDrawable {

		@Override
		public void draw(Graphics g) {
			g.drawRect(0, 0, 320, 480, Color.WHITE);// 背景

		}
	}

	Background backGround = new Background();
	public Dai dai = new Dai();

	List<Block> blocks;

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
				Block newBlock = new Block(30 + i * 33, 53 + j * 17);
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
					dai.setX(event.x - dai.getWidth() / 2);
					if (dai.getX() < 0) {
						dai.setX(0);
					}
					if (dai.getX() > 320) {
						dai.setX(320 - dai.getWidth());
					}
				}
			}
		}
		// 玉が壁にあったったとき、方向を更新
		if (bollDirection == BollDirection.LEFT_UP && bollLeft <= (0 + bollCy)) {// 左上に上昇中に壁に当たったら右上に移動
			bollDirection = BollDirection.RIGHT_UP;
		} else if (bollDirection == BollDirection.RIGHT_UP
				&& bollLeft >= (320 - bollCy)) {// 右上に移動中に右の壁に当たったら左上に移動
			bollDirection = BollDirection.LEFT_UP;
		} else if (bollDirection == BollDirection.LEFT_DOWN
				&& bollLeft <= (0 + bollCy)) {// 左下に移動中に左の壁に当たったら右下に移動
			bollDirection = BollDirection.RIGHT_DOWN;
		} else if (bollDirection == BollDirection.RIGHT_DOWN
				&& bollLeft >= (320 - bollCy)) {// 右下に移動中に右の壁に当たったら左下に移動
			bollDirection = BollDirection.LEFT_DOWN;
		} else if (bollDirection == BollDirection.LEFT_UP
				&& bollTop <= (0 + bollCy)) {// 左上に移動中に上の壁に当たると左下に移動
			bollDirection = BollDirection.LEFT_DOWN;
		} else if (bollDirection == BollDirection.RIGHT_UP
				&& bollTop <= (0 + bollCy)) {// 右上に移動中に上の壁に当たると右下に移動
			bollDirection = BollDirection.RIGHT_DOWN;
		}

		// 表示されているブロックに触れたとき、玉を反転。ついでにブロックを非表示にする
		for (Block block : blocks) {
			if (block.isVisivle == false) {
				continue;
			}
			// ブロックの上部分に触れたときの処理
			if (bollDirection == BollDirection.LEFT_DOWN
					&& (bollTop + bollCy < block.top + block.height && bollTop
							+ bollCy > block.top)
					&& (bollLeft > block.left && bollLeft < block.left
							+ block.width)) {// 左下に移動中にブロックの上部に触れたら左上に移動
				bollDirection = BollDirection.LEFT_UP;
				block.isVisivle = false;
			} else if (bollDirection == BollDirection.RIGHT_DOWN
					&& (bollTop + bollCy < block.top + block.height && bollTop
							+ bollCy > block.top)
					&& (bollLeft > block.left && bollLeft < block.left
							+ block.width)) {// 右下に移動中にブロックの上部に触れたら右上に移動
				bollDirection = BollDirection.RIGHT_UP;
				block.isVisivle = false;

				// ブロックのした部分に触れたときの処理
			} else if (bollDirection == BollDirection.RIGHT_UP
					&& (bollTop - syoritien < block.top + block.height)
					&& (bollLeft > block.left && bollLeft < block.left
							+ block.width)) {// 右上に移動中にブロックの下部に触れたら右下に移動
				bollDirection = BollDirection.RIGHT_DOWN;
				block.isVisivle = false;
			} else if (bollDirection == BollDirection.LEFT_UP
					&& (bollTop - syoritien < block.top + block.height && bollTop < block.top)
					&& (bollLeft > block.left && bollLeft < block.left
							+ block.width)) {// 左上に移動中にブロックの下部に触れたら右下に移動
				bollDirection = BollDirection.LEFT_DOWN;
				block.isVisivle = false;
			}

			// ブロックの左部分に触れたときの処理
			if (bollDirection == BollDirection.RIGHT_UP
					&& (bollLeft + bollCy < block.left + block.width && bollLeft
							+ bollCy > block.left)
					&& ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop
							+ bollCy < block.top + block.height && bollTop
							+ bollCy > block.top))) {// 右上に移動中だったら左上に移動
				bollDirection = BollDirection.LEFT_UP;
				block.isVisivle = false;
			} else if (bollDirection == BollDirection.RIGHT_DOWN
					&& (bollLeft + bollCy < block.left + block.width && bollLeft
							+ bollCy > block.left)
					&& ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop
							+ bollCy < block.top + block.height && bollTop
							+ bollCy > block.top))) {// 右下に移動中なら左下に移動
				bollDirection = BollDirection.LEFT_DOWN;
				block.isVisivle = false;
			}

			// ブロックの右部分に触れた時の処理
			if (bollDirection == BollDirection.LEFT_UP
					&& (bollLeft < block.left + block.width && bollLeft > block.left)
					&& ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop
							+ bollCy < block.top + block.height && bollTop
							+ bollCy > block.top))) {// 左上に移動中だったら右上に移動
				bollDirection = BollDirection.RIGHT_UP;
				block.isVisivle = false;
			} else if (bollDirection == BollDirection.LEFT_DOWN
					&& (bollLeft < block.left + block.width && bollLeft > block.left)
					&& ((bollTop < block.top + block.height && bollTop > block.top) || (bollTop
							+ bollCy < block.top + block.height && bollTop
							+ bollCy > block.top))) {// 左下に移動中なら右下に移動
				bollDirection = BollDirection.RIGHT_DOWN;
				block.isVisivle = false;
			}
		}

		// 操作台に触れたとき、玉の方向を変更
		if (bollDirection == BollDirection.LEFT_DOWN
				&& (bollTop + bollCy > dai.getY() && bollTop < dai.getY()
						+ dai.getHeight())
				&& (bollLeft > dai.getX() && bollLeft < dai.getX()
						+ dai.getWidth())) {// 左下に移動中に台に触れたら左上に移動
			bollDirection = BollDirection.LEFT_UP;
		} else if (bollDirection == BollDirection.RIGHT_DOWN
				&& (bollTop + bollCy > dai.getY() && bollTop < dai.getY()
						+ dai.getHeight())
				&& (bollLeft > dai.getX() && bollLeft < dai.getX()
						+ dai.getWidth())) {// 左下に移動中に台に触れたら左上に移動
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

		// 背景
		backGround.draw(g);

		// 台
		dai.draw(g);
		((AndroidGraphics) g).drawCircle(bollLeft, bollTop, bollCy, bollColor);// 玉

		// ブロックの描画
		for (Block block : blocks) {
			block.draw(g);
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
	
	public boolean hitTest(Rect obj1, Rect obj2) {
		int pX[] = {obj1.getX(), obj1.getX() + obj1.getWidth(),  obj1.getX() + obj1.getWidth(), obj1.getX()};
		int pY[] = {obj1.getY(), obj1.getY(), obj1.getY() + obj1.getHeight(), obj1.getY() + obj1.getHeight()};
		
		for(int i = 0; i < pX.length; i++) {
			if(pX[i] >= obj2.getX() && pX[i] <= obj2.getX() + obj2.getWidth() && pY[i] >= obj2.getY() && pY[i] <= obj2.getY() + obj2.getHeight()) {
				return true;
			}
		}
		return false;
	}

}
