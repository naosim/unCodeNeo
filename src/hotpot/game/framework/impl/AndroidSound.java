package hotpot.game.framework.impl;

import android.media.SoundPool;
import hotpot.game.framework.Sound;

public class AndroidSound implements Sound{

	int soundId;
	SoundPool soundPool;
	
	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundPool = soundPool;
		this.soundId = soundId;
	}
	

	@Override
	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}


	@Override
	public void dispose() {
		soundPool.unload(soundId);
	}

}
