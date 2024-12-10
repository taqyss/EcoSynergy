# EcoSynergy

## **Collaboration Guide**

Welcome to the **EcoSynergy** project! 

---

### **1. Repository Setup**
1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/taqyss/EcoSynergy.git
   ```
2. Navigate to the project directory:
   ```bash
   cd EcoSynergy
   ```

3. Pull the latest changes from the `main` branch before starting any work:
   ```bash
   git pull origin main
   ```

---

### **2. Branching and Workflow**
We follow a **feature branch workflow** to keep development organized and prevent conflicts. Each team member works on their assigned branch.

#### **Steps:**
1. Create a new branch for your assigned activity:
   ```bash
   git checkout -b <branch-name>
   ```
   - Replace `<branch-name>` with the name of your feature, e.g., `main-activity`, `dashboard-activity`.

2. Work on your branch and commit changes regularly:
   ```bash
   git add .
   git commit -m "Describe your changes"
   ```

3. Push your branch to the remote repository:
   ```bash
   git push -u origin <branch-name>
   ```

4. When your feature is complete, submit a **Pull Request (PR)** to merge it into the `main` branch.

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
