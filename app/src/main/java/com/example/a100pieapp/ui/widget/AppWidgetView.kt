package com.example.a100pieapp.ui.widget

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import com.example.a100pieapp.R
import com.example.a100pieapp.ui.ACTION_WIDGET_NEXT_CLICK
import com.example.a100pieapp.ui.ACTION_WIDGET_PREVIOUS_CLICK
import com.example.a100pieapp.ui.AppWidget


class AppWidgetView : RemoteViews {

    private val context: Context
    private val widgetId: Int

    companion object {
        fun getWidgetView(context: Context, widgetId: Int): AppWidgetView {
            return AppWidgetView(context, widgetId)
        }

    }

    private constructor(context: Context, widgetId: Int)
            : super(context.packageName, R.layout.app_widget) {
        this.context = context
        this.widgetId = widgetId

        init()
    }

    private fun init() {
        Log.d("AppWidgetView", "init")
        setOnClickPendingIntent(R.id.next, getPendingIntent(ACTION_WIDGET_NEXT_CLICK))
        setOnClickPendingIntent(R.id.previous, getPendingIntent(ACTION_WIDGET_PREVIOUS_CLICK))
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = AppWidget.getIntentForAction(
            context,
            action,
            widgetId
        )
        return PendingIntent.getBroadcast(
            context,
            widgetId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}