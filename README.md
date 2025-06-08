# EcoSynergy

## **Collaboration Guide**

Welcome to the **EcoSynergy** project! hai :DDDDDDD

**IMPORTANT NOTES**

-The main branch is called ```main```

-No need to make a new project. You can CLONE the project from github to your local machine, as shown in step 1. 

-(**EVERYTIME BEFORE WORKING**)

Pull the latest changes from the `main` branch before starting any work (**VERY IMPORTANT!**):

   Pull from main branch from github, regardless of whichever branch you're on:
   ```bash
   git pull origin main
   ```

---
**Setting up the Toolbar in your activities**

In the activity's Java file, you need to use the setupToolbar method provided by the BaseActivity. Follow these steps:

1. **Extend the** BaseActivity: Your activity must extend ```BaseActivity```. 

```java
public class YourActivityName extends BaseActivity {
```

2. **Set Up the Toolbar**: Call ```setupToolbar``` in the ```onCreate``` method of your activity after setting the content view. Pass true to enable the back button or false if you don't need it.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_your_layout);

    // Set up the toolbar with back button enabled
    setupToolbar(true);

   //Set a custom title, or leave it blank if there's no need for a title
        getSupportActionBar().setTitle("Activity Title");

  // Set up bottom navigation
  setupBottomNavigation();
}
```

3. **Add the toolbar to your layout XML file**: To use the universal toolbar in your activity, include the following ```Toolbar``` element in your layout XML file:
```xml
<!-- Toolbar -->
    <include
        android:id="@+id/toolbar"
        layout="@layout/toolbar"
        app:layout_constraintTop_toTopOf="parent" />
```
---

### **1. Repository Setup**
1. Open windows command prompt and Clone the repository to your local machine (**Only for the first time**) (FIRST move to your desired folder for the project (eg. AndroidStudioProject) using ```cd path/to/folder```):
   ```bash
   git clone https://github.com/taqyss/EcoSynergy.git
   ```

---

### **2. Branching and Workflow**
We follow a **feature branch workflow** to keep development organized and prevent conflicts. Each team member works on their assigned branch, according to their activities.

#### **Steps:**
1. Create a new branch for your assigned activity:
   ```bash
   git checkout -b <branch-name>
   ```
   - Replace `<branch-name>` with the name of your feature, e.g., `main-activity`, `dashboard-activity`.

2. Work on your branch and commit changes regularly:

   For adding changes specific to a folder/directory, use ```git add path/to/directory```
   For example, only adding changes to drawable folder:
   ```bash
   git add app/src/main/res/drawable
   git commit -m "Describe your changes"
   ```

   For adding ALL changes:
   ```bash
   git add .
   git commit -m "Describe your changes"
   ```

4. Push your branch to the remote repository:

    **For first time**
   ```bash
   git push -u origin <branch-name>
   ```
    
    **Afterwards**
    ```bash
   git push 
   ```
5. When your feature is complete, submit a **Pull Request (PR)** to merge it into the `main` branch.

---

### **3. Submitting a Pull Request**
1. Push your latest changes to your feature branch.
2. Go to the **Pull Requests** tab on GitHub and click **New Pull Request**.
3. Select your branch as the source and `main` as the target.
4. Add a detailed description of your changes, including:
   - What you implemented.
   - Any issues resolved.
   - Screenshots or recordings (if applicable).
5. Request a review from at least one team member.

---

### **4. Code Reviews**
Before merging a branch into `main`:
1. Team members review the PR for:
   - Correct functionality.
   - Adherence to the project’s coding standards.
   - Potential conflicts with other branches.
2. Discuss feedback and make necessary changes.
3. Once approved, merge the branch into `main`.

---

### **5. Resolving Merge Conflicts**
1. If you encounter a conflict when merging:
   - Pull the latest changes from `main`:
     ```bash
     git pull origin main
     ```
   - Open the conflicting file(s) and manually resolve the conflicts.
   - Stage the resolved files:
     ```bash
     git add <file>
     ```
   - Commit the resolution:
     ```bash
     git commit -m "Resolve merge conflicts"
     ```

2. Push the updated branch and re-submit your PR.

---

### **6. Navigation and Integration**
1. Ensure that your activity integrates seamlessly with other parts of the app.
   - Use the shared navigation system (e.g., intents or fragments).
   - Test how your activity interacts with others before submitting a PR.
2. Coordinate with the team member handling navigation setup to avoid conflicts.

---

### **7. Testing**
1. Test your feature thoroughly on your local machine:
   - Ensure UI elements are responsive.
   - Check for functionality issues and bugs.
2. After merging, test the app as a team to verify integration:
   - Navigation between activities.
   - Data consistency across shared resources.
   - Performance on different devices.

---

### **8. Shared Resources**
1. **Common Styles and Themes:**
   - Use the centralized `res/values/styles.xml` and `colors.xml` for styling.
2. **Strings:**
   - Add text content to `res/values/strings.xml` to avoid hardcoding.
3. **Data Models:**
   - Share reusable models (e.g., `User`, `Project`) in `com.ecosynergy.models`.

---

### **9. Communication**
1. Use GitHub Issues to track tasks and bugs.
2. Regularly update the team on progress through a shared platform (e.g., Slack, Discord).
3. Schedule weekly check-ins to discuss progress and resolve blockers.

---

### **10. Final Steps**
1. Ensure all features are merged into the `main` branch.
2. Conduct thorough end-to-end testing as a team.
3. Export the APK for submission or demonstration.

---

### **Helpful Git Commands**
- **Check current branch:**
  ```bash
  git branch
  ```
- **Switch branches:**
  ```bash
  git checkout <branch-name>
  ```
- **Pull the latest changes:**
  ```bash
  git pull origin main
  ```
- **View repository status:**
  ```bash
  git status
  ```
- **Resolve conflicts manually:** Edit the conflicting files and commit the resolution.

---
