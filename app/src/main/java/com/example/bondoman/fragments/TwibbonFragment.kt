package com.example.bondoman.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bondoman.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit


class TwibbonFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireActivity(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera(previewView)
            }
        }

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView:  androidx.camera.view.PreviewView
    private lateinit var imageView:  ImageView
    private var isCaptured: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_twibbon, container, false)

        previewView = view.findViewById(R.id.viewFinder)
        imageView = view.findViewById(R.id.imageView)
        if (allPermissionsGranted()) {
            startCamera(previewView)
        } else {
            requestPermissions()
        }

        val captureButton: Button = view.findViewById(R.id.image_capture_button)
        captureButton.setOnClickListener { changeButtonState() }
        cameraExecutor = Executors.newSingleThreadExecutor()

        val overlayImage: ImageView = view.findViewById(R.id.overlay_image)
        val preview: FrameLayout = view.findViewById(R.id.preview)
        overlayImage.bringToFront()
        overlayImage.post {
            val overlayWidth = overlayImage.width
            val overlayHeight = overlayImage.height
            val layoutParams: ViewGroup.LayoutParams = preview.layoutParams
            layoutParams.width = overlayWidth
            layoutParams.height = overlayHeight
            imageView.layoutParams = layoutParams
            preview.layoutParams = layoutParams
        }

        return view
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        Log.i("CAMERA", "MAU CAPTURE")

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class) override fun onCaptureSuccess(image: ImageProxy) {
                    // Get the captured image as a Bitmap
                    val bitmap = image.image?.toBitmap()

                    // Display the captured image in the ImageView
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = View.VISIBLE
                    previewView.visibility = View.GONE
                    isCaptured = true

                    // Close the ImageProxy to release resources
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            })
    }

    private fun changeButtonState() {
        if (isCaptured) {
            imageView.visibility = View.GONE
            previewView.visibility = View.VISIBLE
            isCaptured = false
        } else {
            takePhoto()
        }
    }

    private fun Image?.toBitmap(): Bitmap? {
        if (this == null) return null
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    private fun startCamera(previewView: androidx.camera.view.PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            requireActivity(), permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}