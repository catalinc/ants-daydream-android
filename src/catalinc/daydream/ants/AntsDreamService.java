package catalinc.daydream.ants;


import android.service.dreams.DreamService;

public class AntsDreamService extends DreamService {

    @Override
    public void onDreamingStarted() {
        setInteractive(false);
        setFullscreen(true);
        setContentView(R.layout.main);
    }

}
