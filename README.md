# Imagin

Imagin is an Android library, written in Kotlin. It attaches to an ImageView and allows you to add 'pinch to zoom', 'swipe to close', 'double tap to zoom' and dragging functionality. Because it attaches to a regular ImageView, it allows you to use all existing functionality of the ImageView or extend the ImageView to add extra functionality. Keep in mind that Imagin sets an OnTouchListener on the ImageView.

This library is currently supported on Android 5.1 Lollipop (22) and higher. 

![](https://github.com/nos-digital/imagin/raw/master/preview.gif)

## Usage

1. Add the Imagin library to your `build.gradle` file:

```gradle
dependencies {
    implementation("nl.nos.imagin:${imagin.version}")
}
```

2. Usage in your project:

    Load an image into an ImageView like usual.

```kotlin
Imagin.with(imageWrapper, imageView)
    // enable double tap to zoom functionality
    .enableDoubleTapToZoom()
    // enable pinch to zoom functionality
    .enablePinchToZoom()
    // add an event listener when the user does a single tap
    .enableSingleTap(object : SingleTapHandler.OnSingleTapListener {
        override fun onSingleTap() {
            Toast.makeText(imageView.context, picture.name, Toast.LENGTH_SHORT).show()
        }
    })
    // this allows us to do an action when the user swipes the ImageView vertically and/or horizontally
    .enableScroll(
        allowScrollOutOfBoundsHorizontally = false,
        allowScrollOutOfBoundsVertically = true,
        scrollDistanceToCloseInPx = distanceToClose
    ) {
        onSwipedToCloseListener?.onSwipeToClose()
    }
```

## Licence

Imagin is available under the MIT license.
