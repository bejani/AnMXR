package ir.bejani;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by yurd on 4/22/2019.
 */

public class clsGlobFonts extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_reg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
