package com.kiko.uwidget_wear

import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import com.google.common.util.concurrent.Futures

private const val RESOURCES_VERSION = "1"
class MainTileService : TileService() {
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(
            TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                TimelineBuilders.Timeline.Builder().addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder().setLayout(
                    LayoutElementBuilders.Layout.Builder().setRoot(
                        LayoutElementBuilders.Text.Builder().setText("Hello world!").setFontStyle(
                            LayoutElementBuilders.FontStyle.Builder().setColor(argb(0xFF000000.toInt())).build()
                        ).build()
                    ).build()
                ).build()
            ).build()
            ).build())

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .build()
        )
}