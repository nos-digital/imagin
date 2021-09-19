package nl.nos.imagin

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.abs

class CalculatorTest {

    @Test
    fun testCalculateFutureTranslation_inCenter_returnsCenterTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            2f,
            50f,
            0f,
            100,
            1f
        )
        // Use abs, because it doesn't matter if it's -0 or 0.
        assertEquals(0f, abs(result))
    }

    @Test
    fun testCalculateFutureTranslation_inAlmostCenter_returnsAlmostCenterTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            2f,
            40f,
            0f,
            100,
            1f
        )
        // Use abs, because it doesn't matter if it's -0 or 0.
        assertEquals(20f, abs(result))
    }

    @Test
    fun testCalculateFutureTranslation_onLeftEdge_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            2f,
            0f,
            0f,
            100,
            1f
        )
        assertEquals(50f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onLeftSide_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            2f,
            10f, // 10 should still be enough to zoom the the edge
            0f,
            100,
            1f
        )
        assertEquals(50f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onExactLeftSide_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            2f,
            25f,
            0f,
            100,
            1f
        )
        assertEquals(50f, result)
    }

    @Test
    fun testCalculateFutureTranslation_inCenterWithSmallImage_returnsCenterTranslation() {
        val result = Calculator.calculateFutureTranslation(
            40,
            2f,
            30f,
            0f,
            60,
            1f
        )
        // Use abs, because it doesn't matter if it's -0 or 0.
        assertEquals(0f, abs(result))
    }

    @Test
    fun testCalculateFutureTranslation_onLeftEdgeWithSmallImage_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            40,
            2f,
            0f,
            0f,
            60,
            1f
        )
        assertEquals(10f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onLeftSideWithSmallImage_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            40,
            2f,
            10f, // 10 should still be enough to zoom the the edge
            0f,
            60,
            1f
        )
        assertEquals(10f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onExactLeftSideWithSmallImage_returnsLeftTranslation() {
        val result = Calculator.calculateFutureTranslation(
            40,
            2f,
            22.5f, // 22.5 should be exactly enough to zoom the the edge
            0f,
            60,
            1f
        )
        assertEquals(10f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onExtremeScaledSmallImageEdge_returnsTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            10f,
            50f,
            0f,
            200,
            1f
        )
        assertEquals(400f, result)
    }

    @Test
    fun testCalculateFutureTranslation_onExtremeScaledSmallImageBottomEdge_returnsTranslation() {
        val result = Calculator.calculateFutureTranslation(
            100,
            10f,
            150f,
            0f,
            200,
            1f
        )
        assertEquals(-400f, result)
    }
}