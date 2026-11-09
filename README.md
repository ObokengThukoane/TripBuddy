# TripBuddy

An Android app for planning trips and saving travel memories. Built in Java with Android Studio as a Mobile Applications module project at Eduvos.

## What it does

- **Travel memories**: create a memory with notes, a mood, a location, several photos and a song, then browse them in a photo gallery and swipe through the images.
- **Trip planning**: plan a trip with a budget and add expense line items, which the app totals for you.
- **Settings and session**: a login screen and user preferences kept with SharedPreferences.

## Tech

- Java, Android SDK (min SDK 24)
- SQLite through `SQLiteOpenHelper`, with four tables: memories, memory images, trips and trip line items
- RecyclerView with GridLayoutManager for the gallery, ViewPager2 for the full-screen image viewer
- Glide for image loading, MediaPlayer for music playback
- Storage Access Framework (`ACTION_OPEN_DOCUMENT`) for picking photos and audio

## Project layout

```
app/src/main/java/com/example/mytripbuddymb/
  activities/   screens: create memory, gallery, trip planner, login, settings
  database/     SQLite helper and data access
  model/        Memory, User and the gallery adapters
  utils/        SharedPreferences wrapper
```

## Running it

1. Clone the repo and open it in Android Studio.
2. Let Gradle sync.
3. Run on an emulator or a device with Android 7.0 or newer.

## Author

Obokeng Khumo Thukoane, [github.com/ObokengThukoane](https://github.com/ObokengThukoane)
