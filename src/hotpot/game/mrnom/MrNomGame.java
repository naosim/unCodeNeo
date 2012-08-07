package hotpot.game.mrnom;

import hotpot.game.framework.Screen;
import hotpot.game.framework.impl.AndroidGame;

public class MrNomGame extends AndroidGame {
    /** Called when the activity is first created. */

    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

}