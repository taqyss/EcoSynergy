package com.example.ecosynergy;

import java.util.ArrayList;
import java.util.List;

public class DataResource {
    private String branch; // "Article", "Reports", "Toolkits"
    private String category; // "Solar", "Wind", "Ocean", etc.
    private List<Subcategory> subcategories; // Subcategories for 'upNext'

    // Constructor
    public DataResource(String branch, String category, List<Subcategory> subcategories) {
        this.branch = branch;
        this.category = category;
        this.subcategories = subcategories;

    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }


    // Nested class for subcategory
    public static class Subcategory {

        private int id;
        private String title;

        private String articleTitle;
        private String articleContent;

        // Constructor
        public Subcategory(int id, String articleTitle,String articleContent) {
            this.id = id;
            this.articleTitle = articleTitle;;
            this.articleContent = articleContent;
        }

        public int getId() {
            return id;
        }
        public String getArticleTitle() {
            return articleTitle;
        }

        public void setArticleTitle(String articleTitle) {
            this.articleTitle = articleTitle;
        }

        public String getArticleContent() {
            return articleContent;
        }

        public void setArticleContent(String articleContent) {
            this.articleContent = articleContent;
        }
    }

    public List<DataResource.Subcategory> getUpNextSubcategories(int currentSubcategoryId) {
        List<DataResource.Subcategory> upNext = new ArrayList<>();
        int currentIndex = -1;

        // Find the current subcategory index
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i).getId() == currentSubcategoryId) {
                currentIndex = i;
                break;
            }
        }

        // Add the current subcategory and the next two (if available)
        if (currentIndex != -1) {
            for (int i = currentIndex + 1; i <= currentIndex + 2 && i < subcategories.size(); i++) {
                upNext.add(subcategories.get(i));
            }
        }
        return upNext;
    }

    public static List<DataResource> getDataResourcesForCategory(String categoryName) {
        List<DataResource> dataResources = new ArrayList<>();

        // Solar Energy
        if ("Solar Energy".equals(categoryName)) {
            // Article Data
            List<Subcategory> articleSubcategories = new ArrayList<>();
            articleSubcategories.add(new Subcategory(0, "Introduction to Solar Energy", "Solar energy is a renewable resource. In this article, we will explore its basic principles and applications."));
            articleSubcategories.add(new Subcategory(1, "Solar Panel Technology", "A deep dive into the technology behind solar panels, their construction, and how they convert sunlight into electricity."));
            articleSubcategories.add(new Subcategory(2, "Solar Energy Storage", "Understanding the methods and technologies for storing solar energy for later use, including batteries and thermal storage."));
            dataResources.add(new DataResource("Article", "Solar", articleSubcategories));

            // Report Data
            List<Subcategory> reportSubcategories = new ArrayList<>();
            reportSubcategories.add(new Subcategory(3, "Solar Energy Market Trends", "This report provides an overview of the global solar energy market, examining growth trends, investments, and regional developments."));
            reportSubcategories.add(new Subcategory(4, "Solar Power and Climate Change", "A detailed report on how solar power can help mitigate the effects of climate change and contribute to sustainable energy solutions."));
            dataResources.add(new DataResource("Report", "Solar", reportSubcategories));

            // Toolkit Data
            List<Subcategory> toolkitSubcategories = new ArrayList<>();
            toolkitSubcategories.add(new Subcategory(5, "Solar Panel Installation Guide", "A comprehensive toolkit for installing solar panels at home or on a commercial scale, covering steps, costs, and best practices."));
            toolkitSubcategories.add(new Subcategory(6, "Solar Battery Storage Systems", "A toolkit providing insights into solar battery storage systems, their design, and installation for residential or business applications."));
            dataResources.add(new DataResource("Toolkits", "Solar", toolkitSubcategories));
        }

        // Wind Energy
        else if ("Wind Energy".equals(categoryName)) {
            // Article Data
            List<Subcategory> articleSubcategories = new ArrayList<>();
            articleSubcategories.add(new Subcategory(7, "Introduction to Wind Energy", "This article explains the basics of wind energy, how it's harnessed, and its potential as a renewable energy source."));
            articleSubcategories.add(new Subcategory(8, "Wind Turbine Mechanics", "Learn about the mechanics behind wind turbines, their design, and how they generate power from the wind."));
            dataResources.add(new DataResource("Article", "Wind", articleSubcategories));

            // Report Data
            List<Subcategory> reportSubcategories = new ArrayList<>();
            reportSubcategories.add(new Subcategory(9, "Wind Energy Industry Outlook", "A comprehensive report on the current state of the wind energy industry, including market size, growth potential, and emerging technologies."));
            dataResources.add(new DataResource("Report", "Wind", reportSubcategories));

            // Toolkit Data
            List<Subcategory> toolkitSubcategories = new ArrayList<>();
            toolkitSubcategories.add(new Subcategory(10, "Building Small Wind Turbines", "A step-by-step guide and toolkit for constructing small wind turbines for personal or community use."));
            dataResources.add(new DataResource("Toolkits", "Wind", toolkitSubcategories));
        }

        // Hydro Energy
        else if ("Hydro Energy".equals(categoryName)) {
            // Article Data
            List<Subcategory> articleSubcategories = new ArrayList<>();
            articleSubcategories.add(new Subcategory(11, "Hydropower Basics", "An introduction to hydropower, including the process of converting the energy in flowing water into electricity."));
            dataResources.add(new DataResource("Article", "Hydro", articleSubcategories));

            // Report Data
            List<Subcategory> reportSubcategories = new ArrayList<>();
            reportSubcategories.add(new Subcategory(12, "Hydroelectric Power Plants and Sustainability", "This report analyzes the role of hydroelectric power plants in meeting global energy demands and their environmental impacts."));
            dataResources.add(new DataResource("Report", "Hydro", reportSubcategories));

            // Toolkit Data
            List<Subcategory> toolkitSubcategories = new ArrayList<>();
            toolkitSubcategories.add(new Subcategory(13, "Pumped Storage Hydroelectric Systems", "A toolkit explaining the technology behind pumped storage systems, their design, and implementation for energy storage."));
            dataResources.add(new DataResource("Toolkits", "Hydro", toolkitSubcategories));
        }

        // Biomass Energy
        else if ("Biomass Energy".equals(categoryName)) {
            // Article Data
            List<Subcategory> articleSubcategories = new ArrayList<>();
            articleSubcategories.add(new Subcategory(14, "Introduction to Biomass Energy", "Biomass energy refers to energy produced from organic materials. Learn the basics of biomass energy and its potential."));
            articleSubcategories.add(new Subcategory(15, "Types of Biomass Feedstock", "Explore the different types of biomass feedstocks, such as agricultural residues, wood, and waste materials, used for energy production."));
            dataResources.add(new DataResource("Article", "Biomass", articleSubcategories));

            // Report Data
            List<Subcategory> reportSubcategories = new ArrayList<>();
            reportSubcategories.add(new Subcategory(16, "Biomass Energy: Global Trends and Future Prospects", "A report on global biomass energy trends, future growth forecasts, and key challenges facing the industry."));
            dataResources.add(new DataResource("Report", "Biomass", reportSubcategories));

            // Toolkit Data
            List<Subcategory> toolkitSubcategories = new ArrayList<>();
            toolkitSubcategories.add(new Subcategory(17, "Biomass Gasification Systems", "A toolkit that explains the process of biomass gasification, its applications, and how to build and operate gasification systems."));
            dataResources.add(new DataResource("Toolkits", "Biomass", toolkitSubcategories));
        }

        return dataResources;
    }

}
