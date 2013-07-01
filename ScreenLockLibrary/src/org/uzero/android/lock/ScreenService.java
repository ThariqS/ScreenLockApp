package org.uzero.android.lock;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

/**
 * Screenサービス.
 * 
 * @author takimura
 *
 */
public class ScreenService extends Service {

	/**
	 * Screen Lock SharedPreference Name.
	 */
	public static final String SCREEN_PREFERENCE_NAME = "screenlock";

	/**
	 * アクション:Screenリストア.
	 */
	public static final String ACTION_LOCK_RESTORE = "org.uzero.android.lock.action_restore";

	/**
	 * アクション:サービス開始.
	 */
	public static final String ACTION_LOCK_START = "org.uzero.android.lock.action_start";

	/**
	 * アクション:サービス停止.
	 */
	public static final String ACTION_LOCK_STOP = "org.uzero.android.lock.action_stop";

	/**
	 * Extra:ウィンドウモード.
	 */
	public static final String EXTRA_WINDOW_MODE = "org.uzero.android.lock.extra_window_mode";

	/**
	 * Extra:パッケージ名.
	 */
	public static final String EXTRA_PACKAGE_NAME = "org.uzero.android.lock.extra_package_name";

	/**
	 * Extra:クラス名.
	 */
	public static final String EXTRA_CLASS_NAME = "org.uzero.android.lock.extra_class_name";

	/**
	 * サービスバインダー.
	 */
	private final IBinder mBinder = new ScreenServiceBinder();

	/**
	 * バインド要求.
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		restoreLockReceiver();
		return mBinder;
	}

	/**
	 * サービス起動.
	 * 
	 * @param intent インテント
	 * @param flags フラグ
	 * @param startId スタートID
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int result = super.onStartCommand(intent, flags, startId);
		if(intent != null && intent.getAction() == null) {
			// FIXME: L-01Dがリカバリ出来ていない件の対策, 要原因調査
			restoreLockReceiver();
			return result;
		}

		if(intent == null || ACTION_LOCK_RESTORE.equals(intent.getAction())) {
			restoreLockReceiver();
		}

		else if(ACTION_LOCK_START.equals(intent.getAction())) {
			String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
			String className = intent.getStringExtra(EXTRA_CLASS_NAME);
			registerReceiver(packageName, className);
		}

		else if(ACTION_LOCK_STOP.equals(intent.getAction())) {
			setLockActivity(null);
		}

		return result;
	}

	/**
	 * サービス終了処理.
	 * 
	 * デーモンサービスとしてのサービスの再起動を図る。
	 */
	public void onDestroy() {
		super.onDestroy();
		Intent service = new Intent(this, ScreenService.class);
		service.setAction(ACTION_LOCK_RESTORE);
		startService(service);
	}

    /**
     * ロック画面Activityを設定する.
     * @param slaClass
     * @return
     */
	public boolean setActivity(Class<? extends Activity> slaClass) {
		registerReceiver(getPackageName(), slaClass.getName());
        return true;
	}

	/**
	 * ロック画面Activityを解除する.
	 */
	public void clearActivity() {
		unregisterReceiver();
	}

	/**
	 * ロック画面Activityのパッケージ名を取得する.
	 * @return
	 */
	public String getActivity() {
		SharedPreferences prefs = getSharedPreferences(SCREEN_PREFERENCE_NAME, MODE_PRIVATE);
		return prefs.getString("pkgname", null);
	}

	/**
	 * レシーバーの再登録(リストア).
	 */
	private void restoreLockReceiver() {
		SharedPreferences prefs = getSharedPreferences(SCREEN_PREFERENCE_NAME, MODE_PRIVATE);
		String className = prefs.getString("slaname", null);

		if(className == null) {
			return;
		}

		setLockActivity(className);
	}

	/**
	 * Screen Activityの設定.
	 * 
	 * <p>
	 * ロック画面で起動する Activity のクラス名を設定する。
	 * 本メソッドの呼び出し時に Receiver の登録も同時に実施する。
	 * クラス名が null の場合は Receiver の登録を解除する。
	 * 
	 * @param className クラス名
	 */
	private void setLockActivity(String className) {

		if(className == null) {
			try {
				getApplicationContext().unregisterReceiver(ScreenReceiver.getReceiver());
			} catch (IllegalArgumentException e) {
				// ignore unregistered state exception.
			}
		}
		
		else {
			try {
				Class<? extends ScreenActivity> clazz = Class.forName(className).asSubclass(ScreenActivity.class);
		        ScreenReceiver receiver = ScreenReceiver.getReceiver();
		        receiver.setClass(clazz);
		        IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		        getApplicationContext().registerReceiver(receiver, screenOffFilter);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ディスプレイONで起動するActivityを登録する.
	 * 
	 * <p>
	 * ディスプレイONで起動するActivityをパッケージ/クラス名を指定して登録する。
	 * 登録したActivityのパッケージ/クラス名はSharedPreferences({@link #SCREEN_PREFERENCE_NAME})に保存し、再起動時等によるリストア時に利用可能な状態とする。
	 * 
	 * @param packageName パッケージ名
	 * @param className クラス名
	 */
	private void registerReceiver(String packageName, String className) {

		// Activityの登録
		setLockActivity(className);

		// SharedPreferencesへの保存
		SharedPreferences prefs = getSharedPreferences(SCREEN_PREFERENCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("slaname", className);
		editor.putString("pkgname", packageName);
		editor.commit();

	}

	/**
	 * ディスプレイONで起動するActivityを解除する.
	 * 
	 * <p>
	 * ディスプレイONで起動するActivityの登録を解除する。
	 */
	private void unregisterReceiver() {

		// Activityの解除
		setLockActivity(null);

		// SharedPreferecnesの削除
		SharedPreferences prefs = getSharedPreferences(SCREEN_PREFERENCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();

	}

	/**
	 * Screenサービスバインダー.
	 * @author takimura
	 *
	 */
	public class ScreenServiceBinder extends Binder {
		/**
		 * サービスインスタンスの取得.
		 * @return Screenサービスインスタンス
		 */
		public ScreenService getService() {
			return ScreenService.this;
		}
	}

}
