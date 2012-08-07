package hotpot.game.blockban;

import hotpot.game.framework.Screen;
import hotpot.game.framework.impl.AndroidGame;

public class BlockBreakGame extends AndroidGame{

	@Override
	public Screen getStartScreen() {
		return new BlockBreakScreen(this);
	}

}
