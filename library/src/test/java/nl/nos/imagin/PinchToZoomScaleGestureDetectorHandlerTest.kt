package nl.nos.imagin

import android.widget.ImageView
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * @author mennovogel (22/02/2019).
 */
class PinchToZoomScaleGestureDetectorHandlerTest {
    val pinchHandler =
            PinchToZoomScaleGestureDetectorHandler(mock(ImageView::class.java), 1f, 3f)

    @Test
    fun calculateNewTranslation_zoomOutFromCenteredImage_returnsExpectedTranslation() {
        val newTranslation = pinchHandler.calculateNewTranslation(
                1000,
                0.9f,
                500f,
                2.0f,
                1.8f, // from 2.0f
                1000,
                0f
        )
        assertEquals(0f, newTranslation)
    }

    @Test
    fun calculateNewTranslation_zoomOutFromLeftEdgedImage_returnsExpectedTranslation() {
        val newTranslation = pinchHandler.calculateNewTranslation(
                1000,
                0.9f,
                500f,
                2.0f,
                1.8f, // from 2.0f
                1000,
                -500f
        )
        assertEquals(-400f, newTranslation)
    }

    @Test
    fun calculateNewTranslation_zoomOutFromRightEdgedImage_returnsExpectedTranslation() {
        val newTranslation = pinchHandler.calculateNewTranslation(
                1000,
                0.9f,
                500f,
                2.0f,
                1.8f,
                1000,
                500f
        )
        assertEquals(400f, newTranslation)
    }
}