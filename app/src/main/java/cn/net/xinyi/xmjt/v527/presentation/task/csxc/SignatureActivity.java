package cn.net.xinyi.xmjt.v527.presentation.task.csxc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.xinyi_tech.comm.util.FileUtils2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.xinyi.xmjt.R;

public class SignatureActivity extends AppCompatActivity {

    @BindView(R.id.signature_pad)
    SignaturePad signaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.sbtn_clear, R.id.sbtn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sbtn_clear:
                signaturePad.clear();
                break;
            case R.id.sbtn_ok:
                final Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                final Uri photoUri = FileUtils2.getPhotoUri();
                //photoUri.
                ImageUtils.save(signatureBitmap, photoUri.getPath(), Bitmap.CompressFormat.JPEG);
                final Intent intent = new Intent();
                intent.putExtra("qmzp", photoUri.getPath());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
