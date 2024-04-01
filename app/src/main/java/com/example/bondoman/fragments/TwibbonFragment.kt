package com.example.bondoman.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.media.ExifInterface
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
import java.io.ByteArrayInputStream
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
    private lateinit var captureButton: Button
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

        captureButton = view.findViewById(R.id.image_capture_button)
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

        val button1 : Button = view.findViewById(R.id.button1)
        val button2 : Button = view.findViewById(R.id.button2)
        val button3 : Button = view.findViewById(R.id.button3)
        val button4 : Button = view.findViewById(R.id.button4)
        val button5 : Button = view.findViewById(R.id.button5)
        button1.setOnClickListener {changeOverlay(button1, overlayImage)}
        button2.setOnClickListener {changeOverlay(button2, overlayImage)}
        button3.setOnClickListener {changeOverlay(button3, overlayImage)}
        button4.setOnClickListener {changeOverlay(button4, overlayImage)}
        button5.setOnClickListener {changeOverlay(button5, overlayImage)}

        return view
    }

    private fun changeOverlay(button: Button, overlayImage: ImageView) {
        val backgroundImage = button.background

        if (backgroundImage != null) {
            overlayImage.setImageDrawable(backgroundImage)
        }
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
                    captureButton.text = "RETAKE"
                    captureButton.setBackgroundResource(R.drawable.button)
                    captureButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

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
            captureButton.text = "CAPTURE"
            captureButton.setBackgroundResource(R.drawable.button_green)
            captureButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            takePhoto()
        }
    }

    private fun Image?.toBitmap(cameraFacing: Int = CameraCharacteristics.LENS_FACING_FRONT): Bitmap? {
        if (this == null) return null
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)

        // Get image orientation from metadata
        val inputStream = ByteArrayInputStream(bytes)
        val exifInterface = ExifInterface(inputStream)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // Decode the byte array into a bitmap
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        // Rotate the bitmap if necessary
        return rotateBitmap(bitmap, 90f, true)
    }


    private fun rotateBitmap(bitmap: Bitmap, degrees: Float, mirroredHorizontal: Boolean = false): Bitmap {
        val matrix = Matrix()
        if (mirroredHorizontal) {
            matrix.preScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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