package com.reikyz.jandan.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.reikyz.jandan.R;
import com.reikyz.jandan.data.Config;
import com.reikyz.jandan.widget.ZoomImageView;

/**
 * Created by Reiky on 2016/3/14.
 */
public class ShowPicActivity extends BaseActivity {

    RelativeLayout rl;
    private ZoomImageView zoomImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_show_pic);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        zoomImageView.setOnClickListener(new ZoomImageView.OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        String picUrl = getIntent().getStringExtra(Config.PIC_URL);
        if (picUrl == null)
            finish();

        if (picUrl.indexOf("http") < 0) { // 不是网络图片
            bitmap = BitmapFactory.decodeFile(picUrl);
        } else {
            bitmap = ImageLoader.getInstance().loadImageSync(picUrl);
        }

        if (bitmap != null)
            zoomImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
//            bitmap.recycle();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_half_horizonal_entry, R.anim.activity_horizonal_exit);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
