package hotpot.game.blockban;

import android.graphics.Color;
import hotpot.game.framework.Graphics;

public class Block implements GraphicDrawable {
	int left;
	int top;
	final int width = 32;
	final int height = 16;
	final int color = Color.BLUE;
	boolean isVisivle;

	public Block(int left, int top) {
		this.left = left;
		this.top = top;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisivle == false) {
			return;
		}
		g.drawRect(left, top, width, height,color);
		
	}
}