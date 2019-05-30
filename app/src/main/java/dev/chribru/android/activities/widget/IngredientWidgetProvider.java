package dev.chribru.android.activities.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import dev.chribru.android.R;
import dev.chribru.android.activities.recipe.RecipeActivity;
import dev.chribru.android.activities.recipe.RecipeDetailFragment;

public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_widget);
            views.setEmptyView(R.id.list_view, R.id.empty_widget);

            // Create an Intent to launch the RecipeActivity
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, 0); // TODO get recipe ID
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.list_view_layout, pendingIntent);

            // setup collection
            views.setRemoteAdapter(R.id.list_view, new Intent(context, ListViewWidgetService.class));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        }

    }
}
