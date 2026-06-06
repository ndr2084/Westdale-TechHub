Once you get past the basics, you'll notice most RxJS code is built from a handful of recurring patterns ("recipes").

## 1. Search / Typeahead

Listen to user input, wait for them to stop typing, cancel stale requests.

```ts
searchControl.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  switchMap(term => api.search(term))
)
```

Use when:

* Search bars
* Autocomplete
* Live filtering

---

## 2. Component Cleanup

Automatically unsubscribe when a component is destroyed.

```ts
private destroy$ = new Subject<void>();

data$.pipe(
  takeUntil(this.destroy$)
).subscribe();
```

```ts
ngOnDestroy() {
  this.destroy$.next();
  this.destroy$.complete();
}
```

Use when:

* Angular components
* Long-lived streams

---

## 3. One-Time Read

Get a single value and stop listening.

```ts
store.user$.pipe(
  take(1)
)
```

or

```ts
firstValueFrom(store.user$)
```

Use when:

* Reading current state
* Initialization

---

## 4. Polling

Call an API repeatedly.

```ts
timer(0, 5000).pipe(
  switchMap(() => api.getStatus())
)
```

Meaning:

```text
0s   → request
5s   → request
10s  → request
...
```

Use when:

* Status updates
* Background refresh

---

## 5. Combine Multiple Sources

Wait for several streams.

```ts
combineLatest([
  user$,
  permissions$
]).pipe(
  map(([user, permissions]) => ({
    user,
    permissions
  }))
)
```

Use when:

* View models
* Derived state

Example:

```text
user$         ----U1------U2---
permissions$  --P1-------------
```

Produces:

```text
--------------[U1,P1]-[U2,P1]
```

---

## 6. Parallel API Requests

Run multiple requests together.

```ts
forkJoin({
  user: api.getUser(),
  orders: api.getOrders()
})
```

Waits until all complete.

Result:

```ts
{
  user: {...},
  orders: [...]
}
```

Use when:

* Loading page data
* Dashboard initialization

---

## 7. Dependent Requests

Request B depends on Request A.

```ts
api.getUser().pipe(
  switchMap(user =>
    api.getOrders(user.id)
  )
)
```

Use when:

* Chained API calls

---

## 8. Prevent Double Clicks

Ignore rapid submissions.

```ts
fromEvent(button, 'click').pipe(
  exhaustMap(() => api.submit())
)
```

Meaning:

```text
click
click
click
```

Only first request runs until it finishes.

Use when:

* Form submission
* Payment buttons

---

## 9. Retry Failed Requests

```ts
api.getData().pipe(
  retry(3)
)
```

Try up to 3 more times before failing.

Or:

```ts
api.getData().pipe(
  retry({
    count: 3,
    delay: 1000
  })
)
```

Use when:

* Unstable networks
* Temporary server issues

---

## 10. Loading Indicator

Track request state.

```ts
loading = true;

api.getData().pipe(
  finalize(() => {
    loading = false;
  })
)
```

Use when:

* Spinners
* Busy states

---

## 11. Cache Latest Value

Share a single request among subscribers.

```ts
user$ = api.getUser().pipe(
  shareReplay(1)
);
```

Without it:

```ts
user$.subscribe();
user$.subscribe();
```

might trigger:

```text
GET /user
GET /user
```

With `shareReplay(1)`:

```text
GET /user
```

and everyone gets the same result.

Use when:

* Cached HTTP data
* Shared state

---

## 12. Stream-Based State

Create a state store using subjects.

```ts
private counter$ = new BehaviorSubject(0);

increment() {
  this.counter$.next(
    this.counter$.value + 1
  );
}
```

Consumers:

```ts
counter$.subscribe(...)
```

Use when:

* Lightweight state management

---

## 13. React to Route Changes

```ts
this.route.paramMap.pipe(
  map(params => params.get('id')),
  switchMap(id => api.getUser(id))
)
```

If user navigates quickly:

```text
/users/1
/users/2
/users/3
```

old requests are cancelled automatically.

---

## 14. Debounced Save

Autosave after user stops typing.

```ts
form.valueChanges.pipe(
  debounceTime(1000),
  switchMap(value => api.save(value))
)
```

Use when:

* Draft saving
* Settings pages

---

## 15. Event Bus

Create application-wide events.

```ts
private events$ = new Subject<AppEvent>();
```

Publish:

```ts
events$.next({
  type: 'USER_LOGGED_IN'
});
```

Subscribe:

```ts
events$.subscribe(...)
```

Use sparingly, but useful for:

* Cross-cutting notifications
* Analytics events

---

### The 80/20 operators

In most Angular apps, you'll repeatedly see:

```ts
pipe
map
filter
tap
switchMap
take
takeUntil
combineLatest
forkJoin
catchError
shareReplay
debounceTime
distinctUntilChanged
```

If you're comfortable with those, you can read and write the majority of production RxJS code. The more specialized operators (`exhaustMap`, `concatMap`, `groupBy`, `window`, etc.) tend to show up less frequently and are usually solving specific concurrency problems.

