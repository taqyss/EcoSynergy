package com.example.ecosynergy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DummyData {

    public static List<Article> getDummyArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("What is Solar Energy?", "Solar energy is derived from the Sun and is harnessed using solar panels..."));
//        articles.add(new Article("The Future of Wind Power", "Wind energy is rapidly becoming a major source of renewable power..."));
//        articles.add(new Article("How Hydropower Works", "Hydropower uses water flow to generate electricity..."));
        return articles;
    }

    public static List<Video> getDummyVideos() {
        List<Video> videos = new ArrayList<>();
        videos.add(new Video("Introduction to Solar Energy",
                "https://www.youtube.com/watch?v=solarEnergy123",
                "https://img.youtube.com/vi/solarEnergy123/0.jpg"));
//        videos.add(new Video("Wind Power Basics",
//                "https://www.youtube.com/watch?v=windPower567",
//                "https://img.youtube.com/vi/windPower567/0.jpg"));
//        videos.add(new Video("Hydropower 101",
//                "https://www.youtube.com/watch?v=hydropower789",
//                "https://img.youtube.com/vi/hydropower789/0.jpg"));
        return videos;
    }

    public static List<Questions> getDummyQuestions() {
        List<Questions> questions = new ArrayList<>();
        questions.add(new Questions("Solar Energy", "What is the main component used to harness solar energy?"));
//        questions.add(new Questions("Wind Power", "What is the primary technology used to capture wind energy?"));
//        questions.add(new Questions("Hydropower", "Which natural resource is used to generate hydropower?"));
        return questions;
    }

    public static List<String> getAllCategory() {
        List<String> modules = new ArrayList<>();

        modules.add("Wind Energy");
        modules.add("Solar Energy");
        modules.add("Hydro Energy");
        modules.add("Geothermal Energy");
        modules.add("Biomass and Bioenergy Energy");
        modules.add("Ocean Energy");

        return modules;
    }

    public static List<String> getSubCategoriesForCategory(String category) {
        switch (category) {
            case "Solar Energy":
                return Arrays.asList("What is Solar Energy?", "How Solar Panels Work", "Benefits of Solar Energy", "Recap");
            case "Wind Energy":
                return Arrays.asList("What is Wind Energy?", "How Turbines Work", "Benefits of Wind Energy");
            default:
                return Collections.emptyList();
        }
    }

    public static String getContentForSubCategoryBasic(String subCategory) {
        switch (subCategory) {
            case "What is Solar Energy?":
                return "This section explains solar energy basics and its importance.";
            case "How Solar Panels Work":
                return "This section provides an overview of photovoltaic cells.";
            default:
                return "Content not available.";
        }
    }

    public static List<Comment> getDummyComments() {
        List<Comment> comments = new ArrayList<>();

        // Adding first comment with replies
        Comment comment1 = new Comment(
                "https://example.com/avatar1.png", // userAvatar
                "Alice",                         // username
                "This article on solar energy is really insightful!", // commentText
                25,                              // upvote
                "2025-01-10 12:34",              // timestamp
                Arrays.asList(                   // replies
                        new Comment(
                                "https://example.com/avatar2.png",
                                "Bob",
                                "I agree, it's well-written.",
                                10,
                                "2025-01-10 12:45",
                                Collections.emptyList()
                        ),
                        new Comment(
                                "https://example.com/avatar3.png",
                                "Charlie",
                                "Indeed! It helped me understand the basics.",
                                8,
                                "2025-01-10 13:00",
                                Collections.emptyList()
                        )
                )
        );

        // Adding second comment with no replies
        Comment comment2 = new Comment(
                "https://example.com/avatar4.png",
                "David",
                "Can someone explain more about photovoltaic cells?",
                15,
                "2025-01-10 14:20",
                Collections.emptyList()
        );

        // Adding third comment with a single reply
        Comment comment3 = new Comment(
                "https://example.com/avatar5.png",
                "Eve",
                "How efficient are solar panels during cloudy days?",
                12,
                "2025-01-10 15:10",
                Collections.singletonList(
                        new Comment(
                                "https://example.com/avatar6.png",
                                "Frank",
                                "They're less efficient but still produce some energy.",
                                5,
                                "2025-01-10 15:30",
                                Collections.emptyList()
                        )
                )
        );

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        return comments;
    }
}
