package org.uzero.android.crope.theme;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ThemeResources {

	public static final String TYPE_DRAWABLE = "drawable";

	public static final String TYPE_STRING = "string";

	public static final String THEME_WALLPAPER = "theme_wallpaper";

	public static final String DOCKBAR_BG = "dockbar_bg";

	public static final String VEIL = "veil";

	public static final ThemeResources EMPTY = new ThemeResources();

	public static ThemeResources mDefaultThemeResources = EMPTY;

	private PackageManager mPackageManager = null;

	private ApplicationInfo mAppInfo = null;

	private String mPackage = null;

	private Resources mRes = null;

	private ThemeResources() {
	}

	public ThemeResources(Context context, String themePackage) throws NameNotFoundException {
		if(context == null) {
			throw new NullPointerException();
		}

		if(themePackage == null) {
			throw new NameNotFoundException();
		}

		mPackage = themePackage;
		mPackageManager = context.getPackageManager();
		mAppInfo = mPackageManager.getApplicationInfo(getPackageName(), 0);
		mRes = mPackageManager.getResourcesForApplication(themePackage);
	}

	public static ThemeResources getDefaultThemeResources() {
		return mDefaultThemeResources;
	}

	public static void setDefaultThemeResources(Context context, String themePackage) throws NameNotFoundException {
		mDefaultThemeResources = new ThemeResources(context, themePackage);
		mDefaultThemeResources.preLoadResource();
	}

	public String getPackageName() {
		return mPackage;
	}

	public CharSequence getApplicationLabel() {
		return mPackageManager.getApplicationLabel(mAppInfo);
	}

	public Drawable getApplicationIcon() {
		try {
			return mPackageManager.getApplicationIcon(getPackageName());
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public Drawable getResourceDrawable(String name) {
		if(mPackage == null) {
			return null;
		}

		int id = mRes.getIdentifier(name, TYPE_DRAWABLE, mPackage);
		if(id <= 0) {
			return null;
		}
		
		return mRes.getDrawable(id);
	}

	public String getResourceString(String name) {
		if(mPackage == null) {
			return null;
		}

		int id = mRes.getIdentifier(name, TYPE_STRING, mPackage);
		if(id <= 0) {
			return null;
		}
		
		return mRes.getString(id);
	}

	protected void preLoadResource() {
		getResourceDrawable(ThemeResources.THEME_WALLPAPER);
		getResourceDrawable(ThemeResources.DOCKBAR_BG);
	}

}
