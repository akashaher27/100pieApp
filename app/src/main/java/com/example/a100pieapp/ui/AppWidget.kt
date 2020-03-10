package com.example.a100pieapp.ui

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.a100pieapp.R
import com.example.a100pieapp.data.Repo
import com.example.a100pieapp.data.model.Currencies
import com.example.a100pieapp.ui.widget.AppWidgetView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val EXTRA_KEY_WIDGET_ID = "EXTRA_KEY_WIDGET_ID"
const val ACTION_WIDGET_NEXT_CLICK = "com.example.pi.ACTION_WIDGET_NEXT_CLICK"
const val ACTION_WIDGET_PREVIOUS_CLICK = "com.example.pi.ACTION_WIDGET_PREVIOUS_CLICK"

class AppWidget : AppWidgetProvider() {

    private val repo by lazy { Repo(context) }
    private lateinit var context: Context
    lateinit var widgetManager: AppWidgetManager
    lateinit var wComponentName: ComponentName

    companion object {
        var counter = 0
        fun getIntentForAction(context: Context, action: String, widgetId: Int): Intent {
            val intent = Intent(context, AppWidget::class.java).apply {
                this.action = action
                putExtra(EXTRA_KEY_WIDGET_ID, widgetId)
            }
            return intent
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AppWidget", "onReceive")
        this.context = context
        widgetManager = AppWidgetManager.getInstance(context)
        wComponentName = ComponentName(context, AppWidget::class.java)

        var isWidgetAction = handleWidgetAction(intent)
        if (isWidgetAction) {
            intent.action?.let { processWidgetAction(it) }
        } else {
            super.onReceive(context, intent)
        }
    }

    override fun onEnabled(context: Context) {
        Log.d("AppWidget", "onEnabled")
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray?
    ) {
        Log.d("AppWidget", "onUpdate")
        appWidgetIds?.forEach {
            appWidgetManager.updateAppWidget(it, AppWidgetView.getWidgetView(context, it))
        }
    }


    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        Log.d("AppWidget", "onAppWidgetOptionsChanged")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d("AppWidget", "onDeleted")
    }

    override fun onDisabled(context: Context?) {
        Log.d("AppWidget", "onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        Log.d("AppWidget", "onRestored")
    }

    private fun handleWidgetAction(intent: Intent): Boolean {
        var isWidgetAction = false

        when (intent.action) {
            ACTION_WIDGET_NEXT_CLICK -> {
                isWidgetAction = true
            }
            ACTION_WIDGET_PREVIOUS_CLICK -> {
                isWidgetAction = true
            }
        }
        return isWidgetAction
    }

    private suspend fun getDataFromLocal(index: Int): Currencies? {
        var data: Currencies? = null
        withContext(Dispatchers.IO) {
            repo.getItemFromLocalDatabase(index)
                .let {
                    data = Currencies(it.currency, it.currencyLong, it.txFee)
                }
        }
        return data
    }

    private fun update(context: Context, data: Currencies) {
        Log.d("AppWidget", "update")
        val widgetIds = widgetManager.getAppWidgetIds(wComponentName)
        for (widgetId in widgetIds) {
            val wRemoteView = AppWidgetView.getWidgetView(context, widgetId)
            wRemoteView.setTextViewText(R.id.currency, data.currency)
            wRemoteView.setTextViewText(R.id.currency_long, data.currencyLong)
            wRemoteView.setTextViewText(R.id.tx_fee, data.txFee)
            widgetManager.updateAppWidget(widgetId, wRemoteView)
        }
    }

    private fun processWidgetAction(action: String) {
        when (action) {
            ACTION_WIDGET_NEXT_CLICK -> {
                processNextClickEvent()
            }
            ACTION_WIDGET_PREVIOUS_CLICK -> {
                processPreviousClickEvent()
            }
        }
    }

    private fun processNextClickEvent() {
        CoroutineScope(Dispatchers.Main).launch {
            getDataFromLocal(++counter).let {
                if (it != null) {
                    update(context, it)
                }
            }
        }
    }

    private fun processPreviousClickEvent() {
        CoroutineScope(Dispatchers.IO).launch {
            if (counter > 1) {
                getDataFromLocal(--counter).let {
                    if (it != null) {
                        update(context, it)
                    }
                }
            }
        }
    }
}


