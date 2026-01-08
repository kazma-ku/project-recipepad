# Study Notes - RecipePad App Changes

## 1. Fixed Signup Functionality

**Files:** `SignupActivity.java`, `activity_signup.xml`

**Problem:** Signup button wasn't working.

**Causes & Fixes:**

| Issue | Location | Fix |
|-------|----------|-----|
| Root layout had `android:onClick="swapToLogin"` intercepting all taps | `activity_signup.xml:8` | Removed the onClick attribute |
| No input validation | `SignupActivity.java` | Added check for empty fields with Toast message |
| No error feedback on failure | `SignupActivity.java` | Added Toast when signup fails |

**Code added for validation:**
```java
String usernameText = username.getText().toString().trim();
String emailText = email.getText().toString().trim();
String passwordText = password.getText().toString().trim();

if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
    Toast.makeText(SignupActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
    return;
}
```

---

## 2. Upgraded Android Gradle Plugin for JDK 21 Support

**Files:** `build.gradle`, `app/build.gradle`, `gradle-wrapper.properties`, `AndroidManifest.xml`

**Problem:** Build failed with D8 dexer NullPointerException because AGP 7.1.2 doesn't support JDK 21.

**Changes:**

| Component | Before | After |
|-----------|--------|-------|
| Gradle | 7.2 | 8.6 |
| Android Gradle Plugin | 7.1.2 | 8.3.0 |
| Kotlin | 1.6.20 | 1.9.22 |
| compileSdk / targetSdk | 32 | 34 |
| Java compatibility | 1.8 | 17 |

**Key learnings:**

### a) Namespace migration
AGP 8.x requires `namespace` in `build.gradle` instead of `package` in `AndroidManifest.xml`:

```groovy
// app/build.gradle
android {
    namespace 'com.bcit.comp3717_recipe_pad'
    // ...
}
```

### b) Plugin syntax change
Changed from `apply plugin:` to `plugins { }` block:

```groovy
// Old way
apply plugin: 'kotlin-android'

// New way
plugins {
    id 'org.jetbrains.kotlin.android'
}
```

### c) Kotlin JVM target
Added `kotlinOptions` for Java 17 compatibility:

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = '17'
    }
}
```

### d) Project-level build.gradle (new format)
```groovy
plugins {
    id 'com.android.application' version '8.3.0' apply false
    id 'com.android.library' version '8.3.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
    id 'com.google.gms.google-services' version '4.4.0' apply false
}
```

---

## 3. Added Mock Data Feature

**Files:** `DataHandler.java`, `TrendingActivity.java`, `menu_action_bar.xml`, `strings.xml`

**What was added:**
- `addMockData(Context context)` method in `DataHandler.java`
- Menu option "Load Mock Data" in action bar
- 6 sample recipes with real food images

**How it works:**
1. Load images from `res/drawable/` as Bitmap
2. Compress to JPEG bytes
3. Upload to Firebase Storage with UUID filename
4. Create Recipe document in Firestore with the storage path
5. Refresh activity after 2-second delay

### Code pattern for Firebase Storage upload:

```java
// Get references
FirebaseStorage storage = FirebaseStorage.getInstance();
StorageReference storageRef = storage.getReference();
StorageReference imageRef = storageRef.child("foods/" + UUID.randomUUID() + ".jpeg");

// Load and compress image
Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_pasta);
ByteArrayOutputStream baos = new ByteArrayOutputStream();
bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
byte[] data = baos.toByteArray();

// Upload then create Firestore document
imageRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
    // Image uploaded successfully, now create recipe
    Recipe recipe = new Recipe(imagePath, title, desc, ingredients, steps, nutrFacts, userID);

    db.collection("recipes")
        .add(recipe)
        .addOnSuccessListener(documentReference -> {
            Log.d("debug", "Recipe added: " + documentReference.getId());
        });
}).addOnFailureListener(e -> {
    Log.w("debug", "Error uploading image", e);
});
```

### Adding menu item:

```xml
<!-- res/menu/menu_action_bar.xml -->
<item
    android:id="@+id/item_actionbar_loadmockdata"
    android:title="@string/load_mock_data" />
```

### Handling menu click with delayed refresh:

```java
public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.item_actionbar_loadmockdata) {
        DataHandler.addMockData(this);
        Toast.makeText(this, R.string.mock_data_loaded, Toast.LENGTH_SHORT).show();
        // Delay refresh to allow uploads to complete
        new android.os.Handler().postDelayed(this::recreate, 2000);
    }
    return super.onOptionsItemSelected(item);
}
```

---

## 4. Fixed AndroidManifest.xml Syntax Error

**Problem:** `android:windowSoftInputMode="adjustPan"` was outside the activity tag.

**Before (broken):**
```xml
<activity android:name=".RecipeActivity" android:exported="false" />
android:windowSoftInputMode="adjustPan"
```

**After (fixed):**
```xml
<activity
    android:name=".RecipeActivity"
    android:exported="false"
    android:windowSoftInputMode="adjustPan" />
```

---

## Git Commits Made

| Commit | Description |
|--------|-------------|
| `d581b4b` | Fix signup functionality and upgrade to AGP 8.3.0 |
| `9018fb4` | Add mock data feature for testing |
| `7e79778` | Add food images to mock data feature |

---

## Useful Commands

```bash
# Build the app
./gradlew assembleDebug

# Clean and rebuild
./gradlew clean assembleDebug

# Install APK to connected device/emulator
~/Library/Android/sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch the app
~/Library/Android/sdk/platform-tools/adb shell am start -n com.bcit.comp3717_recipe_pad/.MainActivity

# Check connected devices
~/Library/Android/sdk/platform-tools/adb devices
```
