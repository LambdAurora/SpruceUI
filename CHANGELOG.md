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

## 6.2.0

- Added upside-down English translations ([#57](https://github.com/LambdAurora/SpruceUI/pull/57)).
- Made button texts scrolling like in modern Minecraft buttons.
- Improved handling of long titles in `SpruceSeparatorWidget`.
- Improved scrollbar refocus when entries change in `SpruceEntryListWidget`.

### 6.2.1

- Added Hindi translations ([#59](https://github.com/LambdAurora/SpruceUI/pull/59)).

### 6.2.2

- Fixed a severe silent build error.

## 7.0.0

- Updated to Minecraft 1.21.5.
- Removed HUD-related APIs in favor of Fabric API's own HUD APIs.
- List widgets now use the proper scrollbar sprites.

### 7.0.1

- Added support for NeoForge.

### 7.0.2

- Fixed Minecraft version dependency constraint and other issues in FMJ.

## 8.0.0

- Added screen-related events.
  - Events are similar to Fabric API's screen events.
  - Events are now usable in a multi-loader context.
- Added `SpruceGuiGraphics` wrapper around `GuiGraphics` to add an extended feature set.
  - SpruceUI-managed renderables now use `SpruceGuiGraphics` instead.
- Removed `OpenScreenCallback` event.
- Updated to Minecraft 1.21.6.
- Reworked the tooltip system to be closer to Vanilla's and allow more flexibility.
  - Added custom client tooltip components including a sprite tooltip component, a thumbnail tooltip component, etc.
- Reworked the label widget to be more flexible.

### 8.0.1

- Switched to [Yumi Minecraft Libraries: Foundation] for event management.
  - The Yumi Commons libraries are no longer Jar-in-Jar in SpruceUI, instead consumer mods should Jar-in-Jar [Yumi Minecraft Libraries: Foundation] directly.

### 8.0.2

- Updated [Yumi Minecraft Libraries: Foundation].

### 8.0.3

- Improved mojmap publication.
- Updated [Yumi Minecraft Libraries: Foundation].

### 8.0.4

- Adjusted the Minecraft dependency constraints on NeoForge.

[Yumi Minecraft Libraries: Foundation]: https://github.com/YumiProject/yumi-minecraft-foundation-library "Yumi Minecraft Foundation Library page"
