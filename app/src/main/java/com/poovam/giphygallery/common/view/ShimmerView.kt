package com.poovam.giphygallery.common.view

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.poovam.giphygallery.R

/**
 * Shimmer drawables are generated using this class
 */
class ShimmerView(private val context: Context) {

    companion object {
        private const val DURATION = 1250L
        private const val BASE_ALPHA = 1f
        private const val HIGHLIGHT_ALPHA = 1f
    }

    enum class ShimmerColor(@ColorRes val baseColor: Int, @ColorRes val highlightColor: Int) {
        Blue(R.color.colorPlaceholderBlue, R.color.colorPlaceholderBlueShimmer),
        Green(R.color.colorPlaceholderGreen, R.color.colorPlaceholderGreenShimmer),
        Red(R.color.colorPlaceholderRed, R.color.colorPlaceholderRedShimmer),
        Yellow(R.color.colorPlaceholderYellow, R.color.colorPlaceholderYellowShimmer);
    }

    fun getRandomShimmerColor(shimmerColor: ShimmerColor): ShimmerDrawable {

        val shimmer: Shimmer =
            Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, shimmerColor.baseColor))
                .setHighlightColor(ContextCompat.getColor(context, shimmerColor.highlightColor))
                .setDuration(DURATION)
                .setBaseAlpha(BASE_ALPHA)
                .setHighlightAlpha(HIGHLIGHT_ALPHA)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()

        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }
}