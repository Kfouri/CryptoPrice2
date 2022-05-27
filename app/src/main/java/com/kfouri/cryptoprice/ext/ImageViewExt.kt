package com.kfouri.cryptoprice.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

fun ImageView.loadFromUrl(
        url: String,
        onLoaded: (resource: Drawable?) -> Unit = {}
) =
        Glide.with(this.context.applicationContext)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object: CustomViewTarget<ImageView, Drawable>(this) {

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        view.setImageDrawable(errorDrawable)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                        view.setImageDrawable(placeholder)
                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        view.setImageDrawable(resource)
                        onLoaded(resource)
                    }
                })

fun ImageView.loadFromResources(
    @DrawableRes resource: Int,
    onLoaded: (resource: Drawable?) -> Unit = {}
) =
        Glide.with(this.context.applicationContext)
                .load(resource)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(object: CustomViewTarget<ImageView, Drawable>(this) {

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        view.setImageDrawable(errorDrawable)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                        view.setImageDrawable(placeholder)
                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        view.setImageDrawable(resource)
                        onLoaded(resource)
                    }
                })