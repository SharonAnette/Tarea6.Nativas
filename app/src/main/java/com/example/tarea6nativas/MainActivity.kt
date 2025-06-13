package com.example.tarea6nativas
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.GestureDetector
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.node.ModelNode
import kotlin.math.atan2
import com.example.tarea6nativas.R
import dev.romainguy.kotlin.math.Quaternion
import io.github.sceneview.math.Position
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : AppCompatActivity() {

    private lateinit var arSceneView: ArSceneView
    private var modelNode: ModelNode? = null

    // Detectores de gestos
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var rotationGestureDetector: RotationGestureDetector? = null

    // Estado para la manipulación
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isDragging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arSceneView = findViewById(R.id.arSceneView)

        // 1. Tap para colocar modelo
        arSceneView.onTapAr = { hitResult, _ ->
            if (modelNode == null) {
                modelNode = ModelNode()
                lifecycleScope.launch {
                    modelNode!!.loadModelGlb(context = this@MainActivity, glbFileLocation = "Fly.glb")

                    // Posición y orientación inicial del modelo usando el pose del hitResult
                    val pose = hitResult.hitPose
                    modelNode!!.position = Position(pose.tx(), pose.ty(), pose.tz())
                    modelNode!!.quaternion = Quaternion(pose.qx(), pose.qy(), pose.qz(), pose.qw())

                    arSceneView.addChild(modelNode!!)
                    Toast.makeText(this@MainActivity, "Toca y arrastra, pellizca para escalar, rota con dos dedos", Toast.LENGTH_LONG).show()
                }
            }
        }

        // 2. Detectores de gestos
        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                modelNode?.let {
                    it.scale = it.scale.times(detector.scaleFactor)
                }
                return true
            }
        })

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // Mover sobre X/Z: ejemplo simple
                modelNode?.let {
                    val moveFactor = 0.001f
                    val dx = -distanceX * moveFactor
                    val dz = -distanceY * moveFactor
                    // mueve en plano horizontal relativo
                    it.position = it.position + Position(dx, 0f, dz)
                }
                return true
            }
        })

        rotationGestureDetector = RotationGestureDetector(object : RotationGestureDetector.OnRotationGestureListener {
            override fun OnRotation(rotationDetector: RotationGestureDetector) {
                modelNode?.let {
                    val rot = rotationDetector.angle
                    val rotation = rotateY(rot)
                    it.quaternion = it.quaternion * rotation
                }
            }
        })


        // 3. Asocia todos los detectores al touch del arSceneView
        arSceneView.setOnTouchListener { _, event ->
            if (modelNode != null) {
                gestureDetector.onTouchEvent(event)
                scaleGestureDetector.onTouchEvent(event)
                rotationGestureDetector?.onTouchEvent(event)
            }
            false
        }
    }

    // Clase detector de rotación (multi touch)
    class RotationGestureDetector(val mListener: OnRotationGestureListener) {
        private var fX: Float = 0.0f
        private var fY: Float = 0.0f
        private var sX: Float = 0.0f
        private var sY: Float = 0.0f
        var angle: Float = 0.0f

        private var isFirstTouch = false

        fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount == 2) {
                    fX = event.getX(0)
                    fY = event.getY(0)
                    sX = event.getX(1)
                    sY = event.getY(1)
                    isFirstTouch = true
                }
                MotionEvent.ACTION_MOVE -> if (event.pointerCount == 2) {
                    val nfX = event.getX(0)
                    val nfY = event.getY(0)
                    val nsX = event.getX(1)
                    val nsY = event.getY(1)

                    if (isFirstTouch) {
                        angle = 0.0f
                        isFirstTouch = false
                    } else {
                        val angle1 = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY)
                        angle = angle1
                        mListener.OnRotation(this)
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> if (event.pointerCount == 2) {
                    isFirstTouch = true
                }
            }
            return true
        }

        private fun angleBetweenLines(
            fx1: Float, fy1: Float, fx2: Float, fy2: Float,
            sx1: Float, sy1: Float, sx2: Float, sy2: Float
        ): Float {
            val angle1 = atan2((fy1 - fy2), (fx1 - fx2))
            val angle2 = atan2((sy1 - sy2), (sx1 - sx2))
            return Math.toDegrees((angle1 - angle2).toDouble()).toFloat()
        }

        interface OnRotationGestureListener {
            fun OnRotation(rotationDetector: RotationGestureDetector)
        }
    }
    fun rotateY(degrees: Float): Quaternion {
        val radians = Math.toRadians(degrees.toDouble())
        val half = (radians / 2.0).toFloat()
        val sinHalf = sin(half)
        val cosHalf = cos(half)
        return Quaternion(0f, sinHalf, 0f, cosHalf)
    }

}
