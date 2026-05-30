# Knowledge Check Answers

---

## 1. Create an interceptor that will insert a bearer token into an http request

```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const bearerTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access_token');
  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }
  return next(req);
};
```

Register it in `app.config.ts`:
```typescript
provideHttpClient(withInterceptors([bearerTokenInterceptor]))
```

---

## 2. Create an interceptor that will insert the SessionCookie into an http request

```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const sessionCookieInterceptor: HttpInterceptorFn = (req, next) => {
  const cloned = req.clone({ withCredentials: true });
  return next(cloned);
};
```

Setting `withCredentials: true` tells the browser to include cookies (such as a `JSESSIONID` or any session cookie) on cross-origin requests. The browser handles attaching the actual cookie automatically — you don't need to read or set it manually.

---

## 3. Explain what window.location.href is

`window.location.href` is a browser API property that holds the full URL of the current page (e.g. `"https://example.com/dashboard?tab=1"`).

- **Reading it** gives you the current URL as a string.
- **Writing it** (e.g. `window.location.href = 'https://example.com/login'`) causes a full-page navigation to that URL, the same as if the user had typed it in the address bar.

In an OAuth flow, writing `window.location.href` to the OAuth provider's authorization URL is how you kick off the redirect-based login.

---

## 4. Explain what HttpClient is

`HttpClient` is Angular's built-in service for making HTTP requests. It lives in `@angular/common/http` and provides typed, observable-based methods (`get`, `post`, `put`, `delete`, etc.).

Key traits:
- Returns **RxJS Observables**, so you can use `subscribe`, `pipe`, `async` pipe, etc.
- Supports typed responses: `httpClient.get<User[]>('/api/users')` gives you `Observable<User[]>`.
- Integrates with **interceptors**, which can modify every request/response (add auth headers, log errors, etc.) in one central place without touching individual service calls.

---

## 5. Create a generic HttpClient request in an Angular component

```typescript
import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-example',
  template: `<pre>{{ data | json }}</pre>`
})
export class ExampleComponent implements OnInit {
  private http = inject(HttpClient);
  data: any;

  ngOnInit(): void {
    this.http.get<any>('https://api.example.com/items').subscribe({
      next: (response) => (this.data = response),
      error: (err) => console.error(err)
    });
  }
}
```

---

## 6. Wire together a WebSecurityConfiguration in Spring Boot, focusing on oauth2Login and logout

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login**", "/error**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://localhost:4200", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("http://localhost:4200")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}
```

- `oauth2Login` activates the OAuth2/OIDC login flow. Spring handles the redirect to the provider, the callback, and session creation automatically.
- `logout` invalidates the server-side session and deletes the session cookie so the user is fully signed out.

---

## 7. Wire together a CorsConfiguration in Spring Boot, and explain what setAllowCredentials(true) does and how it relates to interceptors

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

**What `setAllowCredentials(true)` does:**
By default, browsers block cookies and `Authorization` headers from being sent or received on cross-origin requests. Setting `allowCredentials(true)` on the server tells the browser (via the `Access-Control-Allow-Credentials: true` response header) that it is permitted to include credentials.

**How it relates to interceptors:**
The Angular interceptor from question 2 sets `withCredentials: true` on the request. For this to actually work, the Spring backend must also have `allowCredentials(true)` in its CORS config — the two settings must agree. If the backend doesn't set it, the browser will block the response even if the Angular side is configured correctly. They are the client-side and server-side halves of the same handshake.

Note: when `allowCredentials` is `true`, the `allowedOrigins` must list specific origins (not `*`).

---

## 8. Create a PostgreSQL Docker container and wire it up to your Spring Boot application

**`docker-compose.yml`:**
```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
```

Run with: `docker compose up -d`

**`pom.xml` dependencies:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

**`application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Spring Boot auto-configures the `DataSource` and JPA from these properties, so no extra `@Bean` wiring is needed.

---

## 9. Explain what Customizer.withDefaults() does

`Customizer.withDefaults()` is needed for other configuration classes to be picked up by Spring's DI system. For example:

```java
http.cors(Customizer.withDefaults())
```

This line tells Spring Security to look in the DI container for a `CorsConfigurationSource` bean — meaning your separate `CorsConfig` class will be found and applied automatically. Without this line, Spring Security ignores your `CorsConfig` entirely even if it is registered as a `@Bean`.

It is also used for features like CSRF where you have no separate config class but still want to enable the feature with its defaults:

```java
http.csrf(Customizer.withDefaults())
```

In short: it signals Spring Security to activate a feature and wire in any related configuration beans from the DI context.


## 10. Why is logout a POST request?

Logout must be a POST (not a GET) because GET requests can be triggered silently — an attacker can embed `<img src="https://yourapp.com/logout">` in any page, and the browser will fire that GET request automatically without the user doing anything. This would log the user out without their knowledge.

A POST request requires an intentional form submission or JavaScript fetch, so it cannot be triggered just by loading a page. This makes it significantly harder for a third party to force a logout on your behalf.

---

## 11. What do state-changing requests (POST/PUT/DELETE) have to do with CSRF?

**CSRF (Cross-Site Request Forgery)** is an attack where a malicious site tricks the user's browser into making a state-changing request (like a POST) to your app — using the user's existing session cookie.

Because browsers automatically attach cookies to every request regardless of origin, a form on `evil.com` can POST to `yourapp.com/transfer-money` and the browser will include the victim's session cookie. The server sees a valid session and executes the action.

**The CSRF token breaks this** by requiring a secret value that only the legitimate frontend knows:

1. The server generates a random CSRF token and sends it to the frontend (either in a cookie or embedded in the HTML).
2. The frontend includes that token in every state-changing request (POST, PUT, DELETE) as a header or form field.
3. The server validates the token on every such request. A request from `evil.com` won't have it, so it gets rejected.

**How this relates to logout:** logout is a POST precisely because CSRF protection applies to POST requests. If logout were a GET, CSRF tokens wouldn't help — browsers fire GETs freely. Making it a POST means the CSRF token mechanism can protect it.