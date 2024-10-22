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

### 4.2.0

- Updated to Minecraft 1.19.4.
- Added High Contrast textures for SpruceUI widgets.

## 5.0.0

- Updated to Minecraft 1.20.
- Switched to new versioning scheme where each major Minecraft version results in a library major bump.
- Use the new `GuiGraphics` Minecraft class which will cause heavy breakage.

### 5.0.1

- Fixed some shader color leaking in the boolean checkbox option.

### 5.0.2

- Fixed wrong background rendering in SpruceScreen.

### 5.0.3

- Updated to Minecraft 1.20.2 ([#46](https://github.com/LambdAurora/SpruceUI/pull/46)).
- Added Tatar translations ([#44](https://github.com/LambdAurora/SpruceUI/pull/44)).

## 5.1.0

- Updated to Minecraft 1.20.6 ([#51](https://github.com/LambdAurora/SpruceUI/pull/51)).
- Added `MenuBackground` and `MenuBorder` to adapt to Minecraft's new GUI design.

## 6.0.0

- Updated to Minecraft 1.21.2.
- Added `TexturedBorder` for textured borders.
- Updated widget textures to match the new sprite system.
- Removed `ScissorManager` in favor of `GuiGraphics` scissor handling.

### 6.0.1

- Fixed bad Minecraft version range.

## 6.1.0

- Added a way to use `SpruceToggleBooleanOption` without text.
- Added placeholder to `SpruceTextFieldWidget` and `SpruceTextAreaWidget`.
- Improved `SpruceTabbedWidget` construction and management.
- Fixed change listener not triggering when deleting a selection in `SpruceTextFieldWidget` and `SpruceTextAreaWidget`.
