package com.poovam.giphygallery.common.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.fragment.app.DialogFragment
import com.poovam.giphygallery.R
import kotlinx.android.synthetic.main.gif_popup_view.*
import kotlinx.android.synthetic.main.gif_popup_view.view.*


/**
 * This fragment is used to show gif as a pop up
 * This should be created only through
 * Initially shows a preview but then loads full quality
 */
class GifPopupView : DialogFragment() {

    companion object {
        private const val PREVIEW_GIF_URL = "PREVIEW_GIF_URL"
        private const val HIGH_QUALITY_GIF_URL = "HIGH_QUALITY_GIF_URL"

        fun newInstance(previewGifUrl: String, highQualityGifUrl: String): GifPopupView {
            val instance = GifPopupView()
            val args = Bundle()
            args.putString(PREVIEW_GIF_URL, previewGifUrl)
            args.putString(HIGH_QUALITY_GIF_URL, highQualityGifUrl)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gif_popup_view, container, false)
        handleOrientation(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val previewGifUrl = arguments?.getString(PREVIEW_GIF_URL)
        val originalUrl = arguments?.getString(HIGH_QUALITY_GIF_URL)

        share.setOnClickListener { shareGifUrl(originalUrl) }

        val shimmerDrawable = ShimmerView(view.context).getRandomShimmerColor(
            ShimmerView.ShimmerColor.values().random()
        )

        ImageRenderer.render(
            view.context,
            previewGifUrl,
            originalUrl,
            gifPopupView,
            shimmerDrawable
        )
    }

    private fun shareGifUrl(url: String?){
        activity?.let {
            ShareCompat.IntentBuilder.from(it)
                .setType("text/plain")
                .setChooserTitle("Share Gif")
                .setText(url)
                .startChooser()
        }
    }

    private fun handleOrientation(view: View){
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            (view.gifPopupView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "2:1"
        } else {
            (view.gifPopupView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "1:1"
        }
    }
}