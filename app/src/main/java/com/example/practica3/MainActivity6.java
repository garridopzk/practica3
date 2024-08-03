package com.example.practica3;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity; // Asegúrate de importar correctamente AppCompatActivity según tu configuración

import java.io.IOException;

public class MainActivity6 extends AppCompatActivity {

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private Button btnCapture;
    private Button btnRecord;
    private MediaRecorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreview = findViewById(R.id.surfaceView);
        btnCapture = findViewById(R.id.btnCapture);
        btnRecord = findViewById(R.id.btnRecord);

        // Inicializar la cámara
        mCamera = Camera.open();
        mHolder = mPreview.getHolder();

        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mHolder.getSurface() == null) {
                    return;
                }

                try {
                    mCamera.stopPreview();
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });

        // Capturar imagen
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });

        // Grabar video
        btnRecord.setOnClickListener(new View.OnClickListener() {
            boolean isRecording = false;

            @Override
            public void onClick(View v) {
                if (isRecording) {
                    // Detener la grabación
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    mMediaRecorder.release();
                    mMediaRecorder = null;
                    mCamera.lock();
                    btnRecord.setText("Record");
                    isRecording = false;
                    Toast.makeText(MainActivity6.this, "Video guardado", Toast.LENGTH_SHORT).show(); // Corregido a MainActivity6.this
                } else {
                    // Iniciar la grabación
                    mCamera.unlock();
                    mMediaRecorder = new MediaRecorder();
                    mMediaRecorder.setCamera(mCamera);
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                    mMediaRecorder.setOutputFile("/sdcard/video.mp4"); // Cambiar la ubicación según sea necesario
                    try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                        btnRecord.setText("Stop");
                        isRecording = true;
                        Toast.makeText(MainActivity6.this, "Grabando video...", Toast.LENGTH_SHORT).show(); // Corregido a MainActivity6.this
                    } catch (IOException e) {
                        e.printStackTrace();
                        mMediaRecorder.release();
                    }
                }
            }
        });
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Procesar los datos de la imagen capturada aquí, por ejemplo, guardarla en un archivo
            Toast.makeText(MainActivity6.this, "Imagen capturada", Toast.LENGTH_SHORT).show(); // Corregido a MainActivity6.this
            mCamera.startPreview(); // Volver a iniciar la vista previa después de capturar la imagen
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
}
