package com.afzaln.exovideoplayer

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView

import kotlin.jvm.JvmOverloads;

/*
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

class ScalableTextureView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet? = null,
) : TextureView(context, attrs) {
    private var scalableType: ScalableType = ScalableType.NONE

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ScalableTextureView, 0, 0).use {
            val scaleType = it.getInt(
                R.styleable.ScalableTextureView_scalableType, ScalableType.NONE.ordinal,
            )
            scalableType = ScalableType.values()[scaleType]
        }
    }

    private fun scaleVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth == 0 || videoHeight == 0) {
            return
        }
        val viewSize = Size(width, height)
        val videoSize = Size(videoWidth, videoHeight)
        val scaler = Scaler(viewSize, videoSize)
        val matrix = scaler.getScaleMatrix(scalableType)
        setTransform(matrix)
    }

    fun onVideoSizeChanged(width: Int, height: Int) {
        scaleVideoSize(width, height)
    }
}

class Size(val width: Int, val height: Int)
