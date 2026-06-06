Here's a clean example using a todo domain as the subject. I'll show the modern signals-based approach first since that's the current Angular idiom, then the classic RxJS version in case you're on an older version or prefer observables.

## Signals-based (Angular 17+)

```typescript
import { Injectable, computed, signal } from '@angular/core';

export interface Todo {
  id: number;
  title: string;
  done: boolean;
}

@Injectable({ providedIn: 'root' })
export class TodoService {
  // Private writable state — only the service can mutate it
  private readonly _todos = signal<Todo[]>([]);

  // Public read-only views
  readonly todos = this._todos.asReadonly();
  readonly remaining = computed(() => this._todos().filter(t => !t.done).length);

  add(title: string): void {
    this._todos.update(list => [
      ...list,
      { id: Date.now(), title, done: false },
    ]);
  }

  toggle(id: number): void {
    this._todos.update(list =>
      list.map(t => (t.id === id ? { ...t, done: !t.done } : t))
    );
  }

  remove(id: number): void {
    this._todos.update(list => list.filter(t => t.id !== id));
  }
}
```

A component just reads the signals directly in its template:

```typescript
@Component({
  selector: 'app-todos',
  template: `
    <p>{{ todos().length }} todos, {{ remaining() }} left</p>
    <button (click)="svc.add('New task')">Add</button>
    @for (todo of todos(); track todo.id) {
      <div (click)="svc.toggle(todo.id)">{{ todo.title }}</div>
    }
  `,
})
export class TodosComponent {
  constructor(public svc: TodoService) {}
  todos = this.svc.todos;
  remaining = this.svc.remaining;
}
```

## RxJS-based (the classic pattern)

Same idea, but state lives in a `BehaviorSubject` and is exposed as an `Observable`:

```typescript
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TodoService {
  private readonly _todos = new BehaviorSubject<Todo[]>([]);

  readonly todos$: Observable<Todo[]> = this._todos.asObservable();
  readonly remaining$ = this.todos$.pipe(
    map(list => list.filter(t => !t.done).length)
  );

  add(title: string): void {
    const next = [...this._todos.value, { id: Date.now(), title, done: false }];
    this._todos.next(next);
  }

  toggle(id: number): void {
    this._todos.next(
      this._todos.value.map(t => (t.id === id ? { ...t, done: !t.done } : t))
    );
  }
}
```

Components consume it with the `async` pipe: `*ngFor="let todo of todos$ | async"`.

The key principles in both versions are the same: keep the mutable state private, expose only read-only views, provide the service at the root (or a feature level), and let all state changes flow through explicit methods. That gives you a single source of truth for the domain and keeps components free of business logic. Want me to extend either version with async loading from an API?
