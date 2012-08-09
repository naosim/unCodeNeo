package hotpot.game.blockban;

import hotpot.game.framework.Graphics;
import android.graphics.Color;

public class Dai implements GraphicDrawable, Rect {
	private int left = 110;
	private int top = 400;
	private final int width = 100;
	private final int height = 16;
	private final int color = Color.RED;

	@Override
	public void draw(Graphics g) {
		g.drawRect(left, top, width, height, color);// 操作台

	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return left;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return top;
	}

	public void setX(int x) {
		left = x;
	}

	public void setY(int y) {
		top = y;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

}