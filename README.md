## AndroidSmartRatingBar
Android Smart RatingBar, with simple api and high performance.

Cause Android's RatingBar is had to use, you must use background image very **CAREFULLY**, otherwise you will get a broken star... 

And then I create this repo, with simple api. The most important thing is that It wiil auto scale stars according to your layout size. Enjoy it!

### Configurations

``` 
    <declare-styleable name="SmartRatingBar">
        <attr name="maxRating" format="integer" />
        <attr name="gap" format="dimension" />
         <attr name="rating" format="float" />
        <attr name="orientation" format="integer">
            <enum name="vertical" value="1" />
            <enum name="horizontal" value="0" />
        </attr>
        <attr name="ratingDrawable" format="reference" />
        <attr name="backgroundDrawable" format="reference" />
        <attr name="indicator" format="boolean" />
    </declare-styleable>
```

### Sample

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    tools:context="com.alibaba.smartratingbar.MainActivity">

    <com.alibaba.library.SmartRatingBar
        android:id="@+id/smart_rating_bar"
        android:layout_width="15dp"
        android:layout_height="wrap_content"
        app:gap="@dimen/common_dimen"
        app:indicator="false"
        app:maxRating="5"
        app:orientation="vertical"
        app:rating="3.5"
    />

</RelativeLayout>

```

### Screen Captrues

<div style="inline">

<img src="https://raw.githubusercontent.com/xckevin/AndroidSmartRatingBar/master/assets/small.png" width="40%" />

<img src="https://raw.githubusercontent.com/xckevin/AndroidSmartRatingBar/master/assets/normal.png" width="40%" />

</div>
