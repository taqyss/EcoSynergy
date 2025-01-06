package com.example.ecosynergy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailsActivity extends BaseActivity {

    private List<Article> articles; // List to hold news articles
    private int currentIndex = 0;   // Tracks the currently displayed article

    private TextView newsTitle;
    private TextView newsDate;
    private TextView newsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        // Initialize the toolbar and bottom navigation
        setupToolbar(true);
        getSupportActionBar().setTitle("News & Announcements"); // Set the title of the activity
        setupBottomNavigation();

        // Initialize the article list
        initializeArticles();

        // Find views
        newsTitle = findViewById(R.id.newsTitle);
        newsDate = findViewById(R.id.newsDate);
        newsContent = findViewById(R.id.newsContent);

        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);

        // Display the first article initially
        displayArticle(currentIndex);

        // Set click listeners for navigation buttons
        btnPrev.setOnClickListener(view -> navigateToPreviousArticle());
        btnNext.setOnClickListener(view -> navigateToNextArticle());
    }

    @Override
    protected int getCurrentActivityId() {
        return R.id.nav_dashboard; // Adjust this ID if necessary
    }

    /**
     * Initializes the list of articles.
     */
    private void initializeArticles() {
        articles = new ArrayList<>();
        articles.add(new Article(
                "EcoSynergy 1.0 Beta is Now Live!",
                "January 1, 2025",
                "We are thrilled to announce the release of EcoSynergy 1.0 Beta, marking a significant milestone in our journey toward fostering a sustainable future. This version introduces a variety of features designed to educate, inspire, and engage users in clean energy practices.\n" +
                        "\n" +
                        "Key features of EcoSynergy 1.0 Beta include:\n" +
                        "\n" +
                        "Interactive Learning Modules: Learn about solar, wind, and hydropower energy through immersive modules tailored to your knowledge level.\n" +
                        "Collaboration Tools: Join forces with like-minded individuals on clean energy projects and initiatives.\n" +
                        "Personalized Dashboards: Track your progress, set goals, and receive recommendations based on your interests and activities.\n" +
                        "EcoSynergy 1.0 Beta represents just the beginning of our vision. Future updates will include more learning modules, gamification features, and integration with global sustainability initiatives. We welcome your feedback to make EcoSynergy even better!\n" +
                        "\n" +
                        "Together, let’s pave the way for a cleaner, greener tomorrow. Start exploring EcoSynergy 1.0 Beta today, and become an active participant in creating a sustainable world for generations to come."
        ));
        articles.add(new Article(
                "Want to Contribute as an Admin?",
                "December 26, 2024",
                "Are you passionate about clean energy and sustainability? Do you want to play a bigger role in the EcoSynergy community? We’re looking for dedicated individuals to join our team as administrators!\n" +
                        "\n" +
                        "As an admin, you’ll have the opportunity to:\n" +
                        "\n" +
                        "Curate Content: Help create and manage educational materials and resources for the platform.\n" +
                        "Moderate Discussions: Ensure that our collaboration spaces remain constructive, respectful, and inspiring.\n" +
                        "Organize Events: Plan and oversee community activities, such as webinars, workshops, and clean energy campaigns."
        ));
        // Add more articles as needed
    }

    /**
     * Displays the article at the specified index.
     *
     * @param index Index of the article to display
     */
    private void displayArticle(int index) {
        Article article = articles.get(index);
        newsTitle.setText(article.getTitle());
        newsDate.setText(article.getDate());
        newsContent.setText(article.getContent());
    }

    /**
     * Navigates to the previous article, looping to the last article if at the first one.
     */
    private void navigateToPreviousArticle() {
        if (currentIndex == 0) {
            currentIndex = articles.size() - 1; // Go to the last article
        } else {
            currentIndex--;
        }
        displayArticle(currentIndex);
    }

    /**
     * Navigates to the next article, looping to the first article if at the last one.
     */
    private void navigateToNextArticle() {
        if (currentIndex == articles.size() - 1) {
            currentIndex = 0; // Go to the first article
        } else {
            currentIndex++;
        }
        displayArticle(currentIndex);
    }

    /**
     * A simple class to represent a news article.
     */
    private static class Article {
        private final String title;
        private final String date;
        private final String content;

        public Article(String title, String date, String content) {
            this.title = title;
            this.date = date;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getContent() {
            return content;
        }
    }
}
