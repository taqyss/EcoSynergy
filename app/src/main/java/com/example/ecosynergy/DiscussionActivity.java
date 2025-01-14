package com.example.ecosynergy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;

public class DiscussionActivity extends BaseActivity {

    private String currentSubcategoryTitle;
    private CommentType discussionType;
    private FirebaseDataFetcher dataFetcher;
    private FirebaseResourceFetcher resourceFetcher;
    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // Set up Toolbar
        setupToolbar(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Discussion");
        }

        setupBottomNavigation();

        // Initialize data fetchers
        dataFetcher = new FirebaseDataFetcher();
        resourceFetcher = new FirebaseResourceFetcher();

        // Fetch subcategory title and discussion type from intent
        currentSubcategoryTitle = getIntent().getStringExtra("SUBCATEGORY");
        String typeString = getIntent().getStringExtra("discussionType");
        if (typeString != null) {
            discussionType = CommentType.valueOf(typeString);
        }

        // Fetch data based on discussion type
        if (discussionType == CommentType.MODULE) {
            fetchModulesData();
        } else if (discussionType == CommentType.RESOURCE) {
            fetchResourcesData();
        }

        // Load the fragment
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle arguments = new Bundle();
        arguments.putString("SUBCATEGORY", currentSubcategoryTitle);
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void fetchModulesData() {
        dataFetcher.fetchDataModules(new FirebaseDataFetcher.FirebaseCallback() {
            @Override
            public void onDataFetchedModules(List<DataModule> dataModules) {
                Log.d("DiscussionActivity", "Modules Data Fetched");
                fetchCommentsForModules(currentSubcategoryTitle);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("DiscussionActivity", "Error fetching modules: " + errorMessage);
                Toast.makeText(DiscussionActivity.this, "Error fetching modules", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchResourcesData() {
        resourceFetcher.fetchDataResource(new FirebaseResourceFetcher.SubcategoryCallback() {
            @Override
            public void onDataFetched(List<DataResource> dataResources) {
                Log.d("DiscussionActivity", "Resource Data Fetched");
                fetchCommentsForResource(currentSubcategoryTitle);
            }

            @Override
            public void onSubcategoryFetched(DataResource.Subcategory subcategory) {
                // No implementation needed for this case
            }

            @Override
            public void onDataFetchedResource(List<DataResource> dataResources) {
                Log.d("DiscussionActivity", "Resource Data Fetched");
                fetchCommentsForResource(currentSubcategoryTitle);
            }

            @Override
            public void onSuccess(Object result) {
                // No implementation needed for this case
            }

            @Override
            public void onError(Exception error) {
                Log.e("DiscussionActivity", "Error fetching resource data", error);
                Toast.makeText(DiscussionActivity.this, "Error fetching resources", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCommentsForModules(String subcategory) {
        dataFetcher.fetchCommentsForSubcategory(subcategory, new FirebaseDataFetcher.CommentsModuleCallback() {
            @Override
            public void onCommentsFetched(List<Comment> fetchedComments) {
                comments.clear();
                comments.addAll(fetchedComments);
                updateFragmentComments(fetchedComments);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("DiscussionActivity", "Error fetching comments: " + errorMessage);
                Toast.makeText(DiscussionActivity.this, "Error fetching comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCommentsForResource(String subcategory) {
        resourceFetcher.fetchCommentsForSubcategory(subcategory, new FirebaseResourceFetcher.CommentsCallback() {
            @Override
            public void onCommentsFetched(List<Comment> fetchedComments) {
                comments.clear();
                comments.addAll(fetchedComments);
                updateFragmentComments(fetchedComments);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("DiscussionActivity", "Error fetching comments: " + errorMessage);
                Toast.makeText(DiscussionActivity.this, "Error fetching comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFragmentComments(List<Comment> comments) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DiscussionFragment fragment = (DiscussionFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.displayComments(comments);
        }
    }

    public void storeComment(String subcategory, Comment comment) {
        StoreCommentCallback callback = new StoreCommentCallback() {
            @Override
            public void onCommentStored() {
                Toast.makeText(DiscussionActivity.this, "Comment stored successfully!", Toast.LENGTH_SHORT).show();
                // Optionally, refresh the comments list
                if (discussionType == CommentType.MODULE) {
                    fetchCommentsForModules(subcategory);
                } else if (discussionType == CommentType.RESOURCE) {
                    fetchCommentsForResource(subcategory);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(DiscussionActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        if (discussionType == CommentType.MODULE) {
            dataFetcher.storeNewComment(subcategory, comment, callback);
        } else if (discussionType == CommentType.RESOURCE) {
            resourceFetcher.storeNewComment(subcategory, comment, callback);
        }
    }

    public static void openDiscussionActivity(Context context, String subcategory, CommentType type) {
        Intent intent = new Intent(context, DiscussionActivity.class);
        intent.putExtra("SUBCATEGORY", subcategory);
        intent.putExtra("discussionType", type.name());
        context.startActivity(intent);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
