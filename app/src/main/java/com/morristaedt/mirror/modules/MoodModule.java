package com.morristaedt.mirror.modules;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.morristaedt.mirror.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by akodiakson on 9/13/15.
 */
public class MoodModule {
    private static final String TAG = "MoodModule";
    private WeakReference<Context> mContextWeakReference;
    private CameraSource mCameraSource = null;

    private MoodListener mCallBacks;

    public interface MoodListener {
        void onShouldGivePositiveAffirmation(String affirmation);
    }

    public MoodModule(WeakReference<Context> contextWeakReference) {
        mContextWeakReference = contextWeakReference;
    }

    public void getCurrentMood(MoodListener moodListener) {
        createCameraSource();
        mCallBacks = moodListener;
    }

    public void release() {
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource() {

        Context context = mContextWeakReference.get();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(new Detector.Processor<Face>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(final Detector.Detections<Face> detections) {
                final SparseArray<Face> detectedItems = detections.getDetectedItems();
                if (detectedItems.size() != 0) {
                    final int key = detectedItems.keyAt(0);
                    final Face face = detectedItems.get(key);
                    final float isSmilingProbability = face.getIsSmilingProbability();
                    String feedback = getFeedbackForSmileProbability(isSmilingProbability);
                    mCallBacks.onShouldGivePositiveAffirmation(feedback);
                }
            }
        });

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        try {
            mCameraSource = new CameraSource.Builder(context, detector)
                    .setRequestedPreviewSize(640, 480)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .setRequestedFps(30.0f)
                    .build();

            mCameraSource.start();
        } catch (IOException | RuntimeException e) {
            Log.e(TAG, "Something went horribly wrong, with your face.", e);
        }
    }

    private String getFeedbackForSmileProbability(float isSmilingProbability) {
        final boolean isSmiling = isSmilingProbability > 0.5f;
        final boolean aFaceIsntDetected = isSmilingProbability <= 0;

        String feedback;

        if (isSmiling || aFaceIsntDetected) {
            return null;
        }

        final Resources resources = mContextWeakReference.get().getResources();
        if (isSmilingProbability < 0.15) {
            feedback = resources.getString(R.string.it_gets_better);
        } else if (isSmilingProbability < 0.30) {
            feedback = resources.getString(R.string.looking_good);
        } else {
            feedback = resources.getString(R.string.something_special);
        }

        return feedback;
    }
}
