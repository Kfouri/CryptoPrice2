package com.kfouri.cryptoprice2.ui.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("loadimage")
fun bindingImage(imageView: ImageView, url: String?) {
    Glide.with(imageView)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
}
