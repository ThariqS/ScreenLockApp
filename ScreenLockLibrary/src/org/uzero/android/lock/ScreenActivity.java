package org.uzero.android.lock;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * ロック画面Activity.
 * @author takimura
 *
 */
public abstract class ScreenActivity extends Activity {

	/**
	 * 無効化キーコード一覧.
	 */
	public static final int[] IGNORE_KEY_CODES = {
		KeyEvent.KEYCODE_BACK,
		KeyEvent.KEYCODE_VOLUME_UP,
		KeyEvent.KEYCODE_VOLUME_DOWN,
		KeyEvent.KEYCODE_HOME,
	};
	
    /**
     * キーイベントのハンドリング.
     * 
     * @param keyCode キーコード
     * @param event イベント
     * @return 継続処理指定(true:継続処理あり, false:継続処理なし)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	for(int ignoreKeyCode : IGNORE_KEY_CODES) {
    		if(keyCode == ignoreKeyCode) {
    			return true;
    		}
    	}
        return super.onKeyDown(keyCode, event);
    }

    /**
     * キーイベントのハンドリング.
     * 
     * @param keyCode キーコード
     * @param event イベント
     * @return 継続処理指定(true:継続処理あり, false:継続処理なし)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	for(int ignoreKeyCode : IGNORE_KEY_CODES) {
    		if(keyCode == ignoreKeyCode) {
    			return true;
    		}
    	}
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ロック画面Activityを終了する.
     * 
     * <p>
     * Activityインスタンスを残すため、Activityの終了(onDestory)ではなくHOMEの起動をもって終了とする。
     */
    @Override
    public void finish() {
    	finish(false);
    }

    public void finish(boolean destroy) {
    	if(destroy) {
    		super.finish();
    	} else {
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        	startActivity(intent);
    	}
    }

    /**
     * キーガードを無視してActivity表示する.
     */
    @Override
    protected void onResume() {
        super.onResume();

        int flag = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        getWindow().setFlags(flag, flag);
    }

}
