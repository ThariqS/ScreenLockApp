package org.uzero.android.lock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * スクリーンON/OFFレシーバー.
 * 
 * <p>
 * Broadcast Intentを受け取って以下に記載するアクションを実施する.
 * <ul>
 *   <li>スクリーンOFFイベント - Screen Activityを起動する
 *   <li>キーガード解除イベント - Screen Serviceを起動(Screen Activityのリストア)をする
 * </ul>
 * 
 * @author takimura
 */
public class ScreenReceiver extends BroadcastReceiver {

	/**
	 * レシーバーインスタンス.
	 */
	private static ScreenReceiver sReceiver = null;

	/**
	 * 起動Activity.
	 */
	private Class<? extends Activity> mClazz = null;

	/**
	 * コンストラクタ.
	 * @deprecated {@link #getReceiver()}を利用すること
	 */
	public ScreenReceiver() {
	}

	/**
	 * レシーバーインスタンスを取得する.
	 * @return レシーバーインスタンス
	 */
	public static ScreenReceiver getReceiver() {
		if(sReceiver == null) {
			sReceiver = new ScreenReceiver();
		}
		return sReceiver;
	}

	/**
	 * スクリーンONイベントコールバック.
	 * 
	 * <p>
	 * スクリーンONのイベントでロック画面Activityを起動する。
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
			onActionScreenOff(context, intent);
		}

		else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
			onActionUserPresent(context, intent);
		}

		else if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			onActionBootComplete(context, intent);
		}
	}

	/**
	 * ロック画面Activityを設定する.
	 * @param clazz ロック画面Activity
	 */
	public void setClass(Class<? extends Activity>  clazz) {
		mClazz = clazz;
	}

	/**
	 * ロック解除コールバック.
	 * 
	 * @param context コンテキスト.
	 * @param intent インテント.
	 */
	private void onActionUserPresent(Context context, Intent intent) {
		Intent service = new Intent(context, ScreenService.class);
		service.setAction(ScreenService.ACTION_LOCK_RESTORE);
		context.startService(service);
	}

	/**
	 * ブート完了コールバック.
	 * 
	 * @param context コンテキスト.
	 * @param intent インテント.
	 */
	private void onActionBootComplete(Context context, Intent intent) {
		Intent service = new Intent(context, ScreenService.class);
		service.setAction(ScreenService.ACTION_LOCK_RESTORE);
		context.startService(service);
	}

	/**
	 * スクリーンオフコールバック.
	 * 
	 * @param context コンテキスト.
	 * @param intent インテント.
	 */
	private void onActionScreenOff(Context context, Intent intent) {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int status = telephonyManager.getCallState();
		if (status == TelephonyManager.CALL_STATE_OFFHOOK || status == TelephonyManager.CALL_STATE_RINGING) {
			return;
		}

		Intent newIntent = new Intent();
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		newIntent.setClass(context, mClazz);
		context.startActivity(newIntent);
	}
}
