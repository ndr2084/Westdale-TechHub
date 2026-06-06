1. Explain the concept of an observable and a subject. Elaborate on hot, cold, unicast, and multicast
2. Write a typical fetch response using using the pyramid of hell, promise chaining, and async/await
3. Explain the difference between an observable and a promise. Make a list highlightning the differences of each
4. Explain how typescript can give you parallel processing if it's only a single threaded language
5. A promise will resolve into a Response object when it's unwrapped with a thenable. Explain how async/await bypasses the need for a thenable
6. Modularity became a necessity when applications would become large enough that collisions in the global namespace were frequent debugging points (clobbering), as well as long dependency chains that would exist in the script tag. Below is an example of an application with a long dependency chain; explain how you would convert it to ECMAS16 to resolve dependency issues and clobbering.

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Early JS App</title>

    <!-- 1. Core Utilities & Base Libraries (No dependencies) -->
    <script src="js/vendor/jquery.min.js"></script>
    <script src="js/vendor/underscore.min.js"></script>

    <!-- 2. UI Plugins (Dependent on jQuery) -->
    <script src="js/vendor/jquery-ui.min.js"></script>
    <script src="js/vendor/jquery.validate.min.js"></script>

    <!-- 3. Application Configuration & Globals (Dependent on Libraries) -->
    <script src="js/app/config.js"></script>
    <script src="js/app/utils.js"></script>

    <!-- 4. Data Models (Dependent on Config & Utils) -->
    <script src="js/app/models/userModel.js"></script>
    <script src="js/app/models/productModel.js"></script>

    <!-- 5. Business Logic & Controllers (Dependent on Models) -->
    <script src="js/app/controllers/userController.js"></script>
    <script src="js/app/controllers/cartController.js"></script>

    <!-- 6. UI Components & Views (Dependent on Controllers & Plugins) -->
    <script src="js/app/views/headerView.js"></script>
    <script src="js/app/views/cartView.js"></script>

    <!-- 7. Application Entry Point (Dependent on everything above) -->
    <script src="js/app/main.js"></script>
</head>
<body>
    <div id="app"></div>
</body>
</html>

7. Explain the difference between package.json (npm dependencies and configurations) and tsconfig.json (typescript compliation). Use the CLI to create these files from scratch. Where should these files exist? 

8. Explain the purpose of a javascript bundler; which one does angular use by default? 

9. Explain the use case of constructor and ngoninit. If you're injecting a service, is the constructor adequeate enough for ensuring that the service will be available when the component needs it? 

10. Give an example of using the banana in the box syntax in angular


