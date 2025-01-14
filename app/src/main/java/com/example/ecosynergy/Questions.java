    package com.example.ecosynergy;

    import java.util.List;

    public class Questions {
        private String title;  // The question title
        private String description;  // Description or additional context for the question
        private List<String> options;  // List of possible answers
        private int correctAnswerIndex;  // Index of the correct answer in the options list

        public Questions(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public Questions(String title, String description, List<String> options, int correctAnswerIndex) {
            this.title = title;
            this.description = description;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        // Getter methods
        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return options.get(correctAnswerIndex);
        }

        public boolean isCorrectAnswer(int index) {
            return index == correctAnswerIndex;
        }
    }
