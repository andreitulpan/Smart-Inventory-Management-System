# Smart Inventory Management System
Tech Setup:
- Spring boot for the core framework.
- Hibernate for entity mapping.
- MySQL for the database.

API:
  Users:
    - GET /api/users → Fetch all users and display them in a list/table.
    - POST /api/users → Add a new user via a form.
    - GET /api/users/{id} → Display user details in a detailed view.
    - PUT /api/users/{id} → Update user details via an edit form.
    - DELETE /api/users/{id} → Remove a user.

  Foods:
    - GET /api/users/{userId}/foods → Fetch and display all foods for a user.
    - POST /api/users/{userId}/foods → Add new food items via a form.
    - PUT /api/users/{userId}/foods/{foodId} → Update food details.
    - DELETE /api/users/{userId}/foods/{foodId} → Delete food items.

  Medications:
    - GET /api/users/{userId}/medications → Fetch and display all medications for a user.
    - POST /api/users/{userId}/medications → Add new medication items via a form.
    - PUT /api/users/{userId}/medications/{foodId} → Update medication details.
    - DELETE /api/users/{userId}/medications/{foodId} → Delete medication items.
