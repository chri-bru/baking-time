package dev.chribru.android.activities.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import dev.chribru.android.R;
import dev.chribru.android.activities.overview.RecipeOverviewActivity;

public class BakingTimeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget);
            views.setEmptyView(R.id.list_view, R.id.empty_widget);

            // Create an Intent to launch the RecipeOverviewActivity
            Intent intent = new Intent(context, RecipeOverviewActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_headline, pendingIntent);

            // setup collection
            views.setRemoteAdapter(R.id.list_view, new Intent(context, ListViewWidgetService.class));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BakingTimeWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }
}
