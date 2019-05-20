# About
A small library allowing you to analyze images and track objects on interpolated paths.

See this Medium article for more details: https://medium.com/@cwurthner/object-detection-and-tracking-with-firebase-ml-kit-and-camerax-ml-product-search-part-3-8bd138257101  
See this sample app: https://github.com/crysxd/Object-Tracking-Demo/  
See this youtube video of the sample app: https://youtu.be/ME9iE0CYHeY  

# How to use

Add this in module's `build.gradle`
    
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }

Add this is app's `build.gralde`

    implementation 'com.github.crysxd:CameraX-Object-Tracking:latest-version'

Add a instance of `de.crysxd.cameraXTracker.CameraFragment` to your layout:

    <fragment
            android:id="@+id/cameraFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:name="de.crysxd.cameraXTracker.CameraFragment"/>
            
`CameraFragment` will handle the entire camera for you and display a preview image. In your `Activity` or `Fragment` use this code to set eveything up:

    private lateinit var imageAnalyzer: MyImageAnalyzer

    private val camera
        get() = supportFragmentManager.findFragmentById(R.id.cameraFragment) as CameraFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val boundingBoxArOverlay = BoundingBoxArOverlay(this, BuildConfig.DEBUG)
        imageAnalyzer = ViewModelProviders.of(this).get(MyImageAnalyzer::class.java)

        camera.imageAnalyzer = imageAnalyzer
        camera.arOverlayView.observe(camera, Observer {
            it.doOnLayout { view ->
                imageAnalyzer.arObjectTracker
                    .pipe(PositionTranslator(view.width, view.height))
                    .pipe(PathInterpolator())
                    .addTrackingListener(boundingBoxArOverlay)
            }

            it.add(boundingBoxArOverlay)
        })
    }
    
And implement `ThreadedImageAnalyzer` like this:

    class MyImageAnalyzer : ViewModel(), ThreadedImageAnalyzer {

        val arObjectTracker = ArObjectTracker()
        val uiHandler = Handler(Looper.mainLooper())

        override fun getHandler() = Handler(handlerThread.looper)

        override fun analyze(image: ImageProxy, rotationDegrees: Int) {
            val imageSize = Size(image.width, image.height)

            // Fancy computation with image here

            uiHandler.post {
                arObjectTracker.processObject(
                    if (fancyComputationWasSuccessfull) {
                        ArObject(
                            trackingId = o.trackingId ?: -1 /* An ID of the tracked object, e.g. from Firebase ML Kit*/,
                            boundingBox = o.boundingBox.toRectF() /* The bounding box */,
                            sourceSize = imageSize /* See above, the size of the input image */,
                            sourceRotationDegrees = rotationDegrees /* See above, the roation of the input image */
                        )
                    } else {
                        null
                    }
                )
            }
         }   
      }
