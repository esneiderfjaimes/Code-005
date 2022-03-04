package com.red.code005.ui.camera

import android.app.Application
import androidx.annotation.Nullable
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.util.concurrent.ListenableFuture
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException


class CameraViewModel(application: Application) : AndroidViewModel(application) {

    /*@SuppressLint("StaticFieldLeak")
    var context: Context = getApplication<Application>().applicationContext

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var imageCapture: ImageCapture? = null


    lateinit var cameraExecutor: ExecutorService

    fun startCamera(surfaceProvider: Preview.SurfaceProvider?, owner: LifecycleOwner) {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(owner, cameraSelector, preview)

            } catch (exc: Exception) {
                Log.e(CameraFragment.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


        */
    /** Use external media if it is available, our app's file directory otherwise *//*
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        */
    /** Helper function used to create a timestamped file *//*
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)
    }*/
}

/** View model providing access to the camera  */
class CameraXViewModel(application: Application) : AndroidViewModel(application) {
    private var mProcessCameraProviderLiveData: MutableLiveData<CameraProviderResult>? =
        null // Failure during ProcessCameraProvider.getInstance()

    /**
     * Returns a [LiveData] containing CameraX's [ProcessCameraProvider] once it has
     * been initialized.
     */
    val cameraProvider: LiveData<CameraProviderResult>?
        get() {
            if (mProcessCameraProviderLiveData == null) {
                mProcessCameraProviderLiveData = MutableLiveData()
                try {
                    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                        ProcessCameraProvider.getInstance(getApplication())
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                            mProcessCameraProviderLiveData!!.setValue(
                                CameraProviderResult.fromProvider(cameraProvider))
                        } catch (e: ExecutionException) {
                            if (e.cause is CancellationException) {
                                mProcessCameraProviderLiveData!!.value =
                                    CameraProviderResult.fromError(
                                        Objects.requireNonNull(e.cause))
                            }
                        } catch (e: InterruptedException) {
                            throw AssertionError("Unexpected thread interrupt.", e)
                        }
                    }, ContextCompat.getMainExecutor(getApplication()))
                } catch (e: IllegalStateException) {
                    // Failure during ProcessCameraProvider.getInstance()
                    mProcessCameraProviderLiveData!!.setValue(CameraProviderResult.fromError(e))
                }
            }
            return mProcessCameraProviderLiveData
        }

    /**
     * Class for wrapping success/error of initializing the [ProcessCameraProvider].
     */
    class CameraProviderResult private constructor(
        /**
         * Returns a [ProcessCameraProvider] if the result does not contain an error,
         * otherwise returns `null`.
         *
         *
         * Use [.hasProvider] to check if this result contains a provider.
         */
        @get:Nullable
        @param:Nullable val provider: ProcessCameraProvider?,
        /**
         * Returns a [Throwable] containing the error that prevented the
         * [ProcessCameraProvider] from being available. Returns `null` if no error
         * occurred.
         *
         *
         * Use [.hasProvider] to check if this result contains a provider.
         */
        @get:Nullable
        @param:Nullable val error: Throwable?,
    ) {

        /**
         * Returns `true` if this result contains a [ProcessCameraProvider]. Returns
         * `false` if it contains an error.
         */
        fun hasProvider(): Boolean {
            return provider != null
        }

        companion object {
            fun fromProvider(provider: ProcessCameraProvider?): CameraProviderResult {
                return CameraProviderResult(provider,  /*error=*/null)
            }

            fun fromError(error: Throwable?): CameraProviderResult {
                return CameraProviderResult( /*provider=*/null, error)
            }
        }
    }

    companion object {
        private const val TAG = "CameraXViewModel"

        private var sConfiguredCameraXCameraImplementation: String? = null

        // Does not explicitly configure with an implementation and relies on default config provider
        // or previously configured implementation.
        const val IMPLICIT_IMPLEMENTATION_OPTION = "implicit"

        // Camera2 implementation.
        const val CAMERA2_IMPLEMENTATION_OPTION = "camera2"

        // Camera-pipe implementation.
        const val CAMERA_PIPE_IMPLEMENTATION_OPTION = "camera_pipe"
        private const val DEFAULT_CAMERA_IMPLEMENTATION = IMPLICIT_IMPLEMENTATION_OPTION
    }
}