package com.afzaln.exovideoplayer;

/**
 * Copyright 2022 Yoshihito Ikeda (yqritc)
 * https://github.com/yqritc/Android-ScalableVideoView
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Matrix

class Scaler(private val viewSize: Size, private val videoSize: Size) {
    private val noScale: Matrix
        get() {
            val sx = videoSize.width / viewSize.width.toFloat()
            val sy = videoSize.height / viewSize.height.toFloat()
            return getMatrix(sx, sy, PivotPoint.LEFT_TOP)
        }

    fun getScaleMatrix(scalableType: ScalableType): Matrix = when (scalableType) {
        ScalableType.NONE -> noScale
        ScalableType.FIT_XY -> fitXY()
        ScalableType.FIT_CENTER -> fitCenter()
        ScalableType.FIT_START -> fitStart()
        ScalableType.FIT_END -> fitEnd()
        ScalableType.LEFT_TOP -> getOriginalScale(PivotPoint.LEFT_TOP)
        ScalableType.LEFT_CENTER -> getOriginalScale(PivotPoint.LEFT_CENTER)
        ScalableType.LEFT_BOTTOM -> getOriginalScale(PivotPoint.LEFT_BOTTOM)
        ScalableType.CENTER_TOP -> getOriginalScale(PivotPoint.CENTER_TOP)
        ScalableType.CENTER -> getOriginalScale(PivotPoint.CENTER)
        ScalableType.CENTER_BOTTOM -> getOriginalScale(PivotPoint.CENTER_BOTTOM)
        ScalableType.RIGHT_TOP -> getOriginalScale(PivotPoint.RIGHT_TOP)
        ScalableType.RIGHT_CENTER -> getOriginalScale(PivotPoint.RIGHT_CENTER)
        ScalableType.RIGHT_BOTTOM -> getOriginalScale(PivotPoint.RIGHT_BOTTOM)
        ScalableType.LEFT_TOP_CROP -> getCropScale(PivotPoint.LEFT_TOP)
        ScalableType.LEFT_CENTER_CROP -> getCropScale(PivotPoint.LEFT_CENTER)
        ScalableType.LEFT_BOTTOM_CROP -> getCropScale(PivotPoint.LEFT_BOTTOM)
        ScalableType.CENTER_TOP_CROP -> getCropScale(PivotPoint.CENTER_TOP)
        ScalableType.CENTER_CROP -> getCropScale(PivotPoint.CENTER)
        ScalableType.CENTER_BOTTOM_CROP -> getCropScale(PivotPoint.CENTER_BOTTOM)
        ScalableType.RIGHT_TOP_CROP -> getCropScale(PivotPoint.RIGHT_TOP)
        ScalableType.RIGHT_CENTER_CROP -> getCropScale(PivotPoint.RIGHT_CENTER)
        ScalableType.RIGHT_BOTTOM_CROP -> getCropScale(PivotPoint.RIGHT_BOTTOM)
        ScalableType.START_INSIDE -> startInside()
        ScalableType.CENTER_INSIDE -> centerInside()
        ScalableType.END_INSIDE -> endInside()
    }

    private fun getMatrix(sx: Float, sy: Float, px: Float, py: Float): Matrix {
        val matrix = Matrix()
        matrix.setScale(sx, sy, px, py)
        return matrix
    }

    private fun getMatrix(sx: Float, sy: Float, pivotPoint: PivotPoint): Matrix {
        return when (pivotPoint) {
            PivotPoint.LEFT_TOP -> getMatrix(sx, sy, 0f, 0f)
            PivotPoint.LEFT_CENTER -> getMatrix(sx, sy, 0f, viewSize.height / 2f)
            PivotPoint.LEFT_BOTTOM -> getMatrix(sx, sy, 0f, viewSize.height.toFloat())
            PivotPoint.CENTER_TOP -> getMatrix(sx, sy, viewSize.width / 2f, 0f)
            PivotPoint.CENTER -> getMatrix(sx, sy, viewSize.width / 2f, viewSize.height / 2f)
            PivotPoint.CENTER_BOTTOM -> getMatrix(sx, sy, viewSize.width / 2f, viewSize.height.toFloat())
            PivotPoint.RIGHT_TOP -> getMatrix(sx, sy, viewSize.width.toFloat(), 0f)
            PivotPoint.RIGHT_CENTER -> getMatrix(sx, sy, viewSize.width.toFloat(), viewSize.height / 2f)
            PivotPoint.RIGHT_BOTTOM -> getMatrix(sx, sy, viewSize.width.toFloat(), viewSize.height.toFloat())
        }
    }

    private fun getFitScale(pivotPoint: PivotPoint): Matrix {
        var sx = viewSize.width.toFloat() / videoSize.width
        var sy = viewSize.height.toFloat() / videoSize.height
        val minScale = sx.coerceAtMost(sy)
        sx = minScale / sx
        sy = minScale / sy
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun fitXY(): Matrix {
        return getMatrix(1f, 1f, PivotPoint.LEFT_TOP)
    }

    private fun fitStart(): Matrix {
        return getFitScale(PivotPoint.LEFT_TOP)
    }

    private fun fitCenter(): Matrix {
        return getFitScale(PivotPoint.CENTER)
    }

    private fun fitEnd(): Matrix {
        return getFitScale(PivotPoint.RIGHT_BOTTOM)
    }

    private fun getOriginalScale(pivotPoint: PivotPoint): Matrix {
        val sx = videoSize.width / viewSize.width.toFloat()
        val sy = videoSize.height / viewSize.height.toFloat()
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun getCropScale(pivotPoint: PivotPoint): Matrix {
        var sx = viewSize.width.toFloat() / videoSize.width
        var sy = viewSize.height.toFloat() / videoSize.height
        val maxScale = sx.coerceAtLeast(sy)
        sx = maxScale / sx
        sy = maxScale / sy
        return getMatrix(sx, sy, pivotPoint)
    }

    private fun startInside(): Matrix = if (
        videoSize.height <= viewSize.width && videoSize.height <= viewSize.height
    ) {
        // video is smaller than view size
        getOriginalScale(PivotPoint.LEFT_TOP)
    } else {
        // either of width or height of the video is larger than view size
        fitStart()
    }

    private fun centerInside(): Matrix = if (
        videoSize.height <= viewSize.width && videoSize.height <= viewSize.height
    ) {
        // video is smaller than view size
        getOriginalScale(PivotPoint.CENTER)
    } else {
        // either of width or height of the video is larger than view size
        fitCenter()
    }

    private fun endInside(): Matrix = if (
        videoSize.height <= viewSize.width && videoSize.height <= viewSize.height
    ) {
        // video is smaller than view size
        getOriginalScale(PivotPoint.RIGHT_BOTTOM)
    } else {
        // either of width or height of the video is larger than view size
        fitEnd()
    }
}

enum class ScalableType {
    NONE,
    FIT_XY,
    FIT_START,
    FIT_CENTER,
    FIT_END,
    LEFT_TOP,
    LEFT_CENTER,
    LEFT_BOTTOM,
    CENTER_TOP,
    CENTER,
    CENTER_BOTTOM,
    RIGHT_TOP,
    RIGHT_CENTER,
    RIGHT_BOTTOM,
    LEFT_TOP_CROP,
    LEFT_CENTER_CROP,
    LEFT_BOTTOM_CROP,
    CENTER_TOP_CROP,
    CENTER_CROP,
    CENTER_BOTTOM_CROP,
    RIGHT_TOP_CROP,
    RIGHT_CENTER_CROP,
    RIGHT_BOTTOM_CROP,
    START_INSIDE,
    CENTER_INSIDE,
    END_INSIDE
}

enum class PivotPoint {
    LEFT_TOP,
    LEFT_CENTER,
    LEFT_BOTTOM,
    CENTER_TOP,
    CENTER,
    CENTER_BOTTOM,
    RIGHT_TOP,
    RIGHT_CENTER,
    RIGHT_BOTTOM
}
