1. Draw a diagram that explains the login flow of westdale techhub
2. Draw a diagram that explains the logout flow of westdale techhub
3. Explain how to set up ROLES in Spring Boot
4. How do you set environmental variables in Angular? 
5. Explain the flow of 
    
    login(){
    this.http.get<any>(`${environmental-variable}/api/user}).subscribe({
        next: (httpResponse) => {
            this.user = httpResponse.user;
            this.scopes = httpResponse.scopes;
        }
        error: (err) => {
            console.log("Failed to fetch user", err)
        }
    });

Specifically, outline who the observable and observer are 

6. Explain what a sessionCookie is and how HttpOnly can keep a session cookie safe from XSS attacks
7. Explain the default features that Spring Session provides for you
8. Explain the default features that Spring Security provides for you
9. Explain the default features that Spring MVC provides for you
10. set up the bare minimum configuration for dataSource, Jpa, Session, security.oauth2 for application.yaml + required dependencies in pom.xml
