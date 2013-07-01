package org.uzero.android.lock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * ロック画面設定Activity.
 * 
 * <p>
 * ロック画面に起動するActivityの設定,解除を行うActivityのベースクラス。
 * 本クラスを継承してロック画面設定,解除を制御する。
 * 
 * @author takimura
 */
public abstract class ScreenSettingActivity extends Activity {

	/**
	 * Screen設定サービス.
	 */
	private ScreenService mScreenService = null;

	/**
	 * Screenサービスリスナー.
	 */
    private OnConnectedListener mListener = null;

    /**
     * Screenサービスコネクション.
     */
	private ServiceConnection  mScreenServiceConn = null;

	/**
	 * Activityインスタンス生成コールバック.
	 * 
	 * <p>
	 * Screenサービスとのハンドシェイクを開始する.
	 * バインドの完了は {@link #setOnScreenListener(OnConnectedListener)} で登録されたリスナーへ通知する.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mScreenServiceConn = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {
				mScreenService = ((ScreenService.ScreenServiceBinder)service).getService();
				if( mListener != null ) {
					mListener.onConnected();
				}
			}

			public void onServiceDisconnected(ComponentName name) {
				mScreenService = null;
			}
		};

		Intent intent = new Intent(this, ScreenService.class);
        bindService(intent, mScreenServiceConn, BIND_AUTO_CREATE);

	}

	/**
	 * Activity起動コールバック.
	 * 
	 * <p>
	 * サービスが殺されリスナーが解除されていたケースを想定し、リストア要求を発行する.
	 */
	protected void onStart() {
		super.onStart();

		Intent service = new Intent(this, ScreenService.class);
		service.setAction(ScreenService.ACTION_LOCK_RESTORE);
		startService(service);
	}

	/**
	 * Activity破棄コールバック.
	 * 
	 * <p>
	 * サービスとのハンドシェイクを解除する.
	 * サービス自身はスクリーンOFF/ONを受け取るため生かしたままとする.
	 */
	protected void onDestroy() {
		super.onDestroy();
		if(mScreenServiceConn != null) {
			unbindService(mScreenServiceConn);
		}
	}

	/**
	 * Screenハンドシェイクリスナーの登録.
	 * @param listener 通知リスナー
	 */
    public void setOnScreenListener(final OnConnectedListener listener) {
    	mListener = listener;
    }

    /**
     * ロック画面Activityを設定する.
     * @param slaClass
     * @return
     */
	public final boolean setScreenActivity(Class<? extends ScreenActivity> slaClass) {
		return mScreenService.setActivity(slaClass);
	}

	/**
	 * ロック画面Activityを解除する.
	 */
	public final void clearScreenActivity() {
		mScreenService.clearActivity();
	}

	/**
	 * ロック画面Activityのパッケージ名を取得する.
	 * @return
	 */
	public final String getScreenActivityName() {
		return mScreenService.getActivity();
	}

	/**
	 * @author takimura
	 */
    public interface OnConnectedListener {
        void onConnected();
    }

}
