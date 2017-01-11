# android-material-seekbar

Add a Material ProgressBar on pre-lollipop devices. Supports Android 2.3 API 9 (GINGERBREAD) and up.

Usage:

    Step 1. Add the JitPack repository to your build file
    
    Add it in your root build.gradle at the end of repositories:
    
    allprojects {
		  repositories {
			  ...
			  maven { url 'https://jitpack.io' }
		  }
	}
    
    Step 2. Add the dependency
    
    dependencies {
	        compile 'com.github.robertapengelly:android-material-seekbar:1.0.0'
	}

Implementation:

    Create a layout file with a MaterialSeekBar widget (layout/activity_main.xml)
    
        <?xml version="1.0" encoding="utf-8" ?>
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/activity_main"
            android:layout_height="match_parent"
            android:layout_width="match_parent"
            android:orientation="vertical"
            android:padding="10dp">
            
            <robertapengelly.support.widget.MaterialSeekBar
                android:layout_height="wrap_content"
                android:layout_width="match_parent"
                app:msb_value="50" />
        
        </LinearLayout>

Styling the MaterialSeekBar:

    Add colorAccent and colorControlNormal to your style (values/styles.xml)
    
        <style name="Theme" parent="@android:style/Theme.NoTitleBar">
            <item name="colorAccent">@color/colorAccent</item>
            <item name="colorControlNormal">@color/color_normal_material_dark</item>
        </style>
    
    Additional MaterialSeekBar properties
    
        app:msb_barStyle="continuous | discrete"
        app:msb_max="10"
        app:msb_min="1"
        app:msb_stepSize="2"
        app:msb_tick="true"
        app:msb_tickColor="@color/tickColor"
        app:msb_tickStep="2"
