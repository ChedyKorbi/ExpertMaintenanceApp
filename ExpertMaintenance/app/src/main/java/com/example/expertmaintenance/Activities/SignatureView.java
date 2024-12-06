package com.example.expertmaintenance.Activities;

import android.content.Context;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.io.OutputStream;
import java.util.Objects;

public class SignatureView extends View {

    private Paint paint = new Paint();
    private Path path = new Path();

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void clear() {
        path.reset();
        invalidate();
        Toast.makeText(getContext(), "Signature cleared!", Toast.LENGTH_SHORT).show();
    }

    public void saveSignature() {
        Bitmap signatureBitmap = getSignatureBitmap();

        if (signatureBitmap != null) {
            try (OutputStream fos = getContext().getContentResolver().openOutputStream(
                    Objects.requireNonNull(getContext().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            getImageDetails()
                    )))) {

                if (fos != null) {
                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    Toast.makeText(getContext(), "Signature saved successfully", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), "Error saving signature: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ContentValues getImageDetails() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "signature_" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Signatures");
        return values;
    }

    private Bitmap getSignatureBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}
