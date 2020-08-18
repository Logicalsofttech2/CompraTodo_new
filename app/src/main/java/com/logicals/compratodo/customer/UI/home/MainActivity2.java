package com.logicals.compratodo.customer.UI.home;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.logicals.compratodo.R;
import com.logicals.compratodo.utils.SimpleSearchSuggestionsProvider;


public class MainActivity2 extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity2.class.getSimpleName();

    private SearchSuggestionAdapter mSuggestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        setupSearchView(searchItem);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(LOG_TAG, "onNewIntent()");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v(LOG_TAG, "handleIntent(): query = " + query);


            if (!query.equals("") || !query.isEmpty())
            {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SimpleSearchSuggestionsProvider.AUTHORITY, SimpleSearchSuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
                startActivity(new Intent(MainActivity2.this,SearchableListActivity.class).
                        putExtra("product_name",query));
            }



         //   startActivity(new Intent(MainActivity.this,SearchableListActivity.class).putExtra("product_name","query"));

        }
    }





    private void setupSearchView(MenuItem searchItem) {
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        mSuggestionAdapter = new SearchSuggestionAdapter(this, null, 0);
        searchView.setSuggestionsAdapter(mSuggestionAdapter);





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = getRecentSuggestions(newText);
                mSuggestionAdapter.swapCursor(cursor);
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                searchView.setQuery(mSuggestionAdapter.getSuggestionText(position), true);
                return true;
            }
        });
    }


    public Cursor getRecentSuggestions(String query) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(SimpleSearchSuggestionsProvider.AUTHORITY);

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);

        String selection = " ?";
        String[] selArgs = new String[] { query };

        Uri uri = uriBuilder.build();

        return getContentResolver().query(uri, null, selection, selArgs, null);
    }
}
