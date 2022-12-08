# SpruceUI Changelog

### 3.2.0

 - Removed some usage of lambdajcommon.

#### 3.2.1

 - Switched calls to OpenGL scissor to use RenderSystem instead in ScissorManager.
 - Improved slider release detection.
 - Removed all usage of lambdajcommon, its inclusion is now deprecated.

### 3.3.0

 - Added ability to dynamically remove entries for a `SpruceTabbedWidget`.
 - Added a variant of `SpruceScreen` for `HandledScreen`s.
 - Adjusted the consumers of `Tooltip#queueFor` to avoid boxing.
 - Removed bunch of useless `@NotNull` annotations.

#### 3.3.1

 - Added ability for `SpruceEntryListWidget` children to override scroll behaviour ([#23](https://github.com/LambdAurora/SpruceUI/pull/23)).
 - Removed inclusion of lambdajcommon.

#### 3.3.2

 - Removed exposed ModMenu in POM publication.

#### 3.3.3

 - Updated to 1.18.2, fix transitiveness issues.

## 4.0.0

 - Updated to Minecraft 1.19.

### 4.1.0

 - Updated libraries.
 - Couple minor bug fixes to improve UX ([#31](https://github.com/LambdAurora/SpruceUI/pull/31)).
 - Updated russian translations ([#30](https://github.com/LambdAurora/SpruceUI/pull/30)).
 - Added Traditional Chinese translations ([#34](https://github.com/LambdAurora/SpruceUI/pull/34)).
