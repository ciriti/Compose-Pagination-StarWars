# Star Wars App

## Features
- Fetch and display a paginated list of Star Wars characters.
- View detailed profiles of individual characters.
- Offline caching of previously fetched data.

## Use Cases Covered
- **Fetching a paginated list of people**: Retrieves and caches a list of Star Wars characters.
- **Loading additional pages**: Fetches more pages as the user scrolls.
- **Viewing a person's details**: Displays detailed information about a character.
- **Handling errors and displaying messages**: Catches network failures and server errors.
- **Handling empty responses**: Manages cases where the API returns no data.
- **Caching mechanism**: Expires cached data after 2 minutes to balance freshness and efficiency.

### Edge Cases Considered
- **Rapid Pagination Requests**: The app prevents multiple simultaneous requests when fetching new pages.
- **Empty API Responses**: If the API returns an empty list, the app does not attempt further requests.
- **Cache Handling**: Cached data older than 2 minutes is discarded and refreshed from the API.
- **Handling Last Page Scenarios**: If no additional pages exist, the app does not trigger unnecessary API calls.
  Absolutely! Here's a clear and concise summary for both:

---

### ✅ Why use `ImmutableList` in Compose state?

In Jetpack Compose, **state-driven UI** relies heavily on detecting changes via `equals()`.

#### 🔁 Problem with `List` (mutable or standard `List`):
- Even if contents are the same, `List`s can be **different references**.
- This causes **unnecessary recompositions**, even if visually nothing has changed.

#### ✅ Solution: Use `ImmutableList` (from `kotlinx.collections.immutable`)
- Provides **structural equality**: changes only when actual content changes.
- Efficient for Compose: reduces recompositions and improves performance.
- Works great with `@Stable` and other state optimizations.

```kotlin
val tasks: ImmutableList<Task> = persistentListOf()
```

---

### 🆚 `@Stable` vs `@Immutable`

| Annotation     | Meaning                                                                                   | Usage Tip                        |
|----------------|--------------------------------------------------------------------------------------------|----------------------------------|
| `@Immutable`   | **All properties are immutable**, and class is deeply immutable                           | For data classes with only `val`s |
| `@Stable`      | Class is **guaranteed to not change identity when values are equal** (but can have `var`s) | Use when you control equality (`equals()`) or have custom logic |

#### ✅ Compose behavior with them:
- Both help Compose **optimize recompositions**.
- `@Immutable` is **stricter**: Compose assumes it's safe to skip recompositions entirely if inputs don’t change.
- `@Stable` is **more flexible**: allows controlled mutability but still helps Compose skip redundant recompositions.

---

### 💡 Summary

| Goal                                     | Use                                     |
|------------------------------------------|------------------------------------------|
| Prevent recompositions for unchanged lists | Use `ImmutableList`                     |
| Optimize data class recompositions        | Annotate with `@Immutable` if fully immutable, else use `@Stable` |
| Avoid nulls and add clarity               | Use empty objects like `EmptyTask`      |

Let me know if you want a visual diagram or practical benchmark example too!
