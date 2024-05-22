[![](https://jitpack.io/v/AlexeyErofeev/canvasdsl.svg)](https://jitpack.io/#AlexeyErofeev/canvasdsl)

# CanvasDSL 2

Simple lightweight kotlin-dsl for canvas drawings in old-style xml-layout apps (if you draw on canvas with compose you don't need it).

Inspired by [VectorMaster](https://github.com/harjot-oberai/VectorMaster) and various kotlin DSL-s. Used in production to visualize fitness tracker measures.

## Key features

* Inherited from standard drawable
* Easy to use for value animation
* SVG-like primitives
* Supports references to predefined groups 
* Viewport mechanics with relative "viewport points"
* No compatibility limits - works with minimum android sdk 1
* One dependency: kotlin stdlib, you should use it anyway for kotlin
* Using vector drawables as resource or convert in primitives
* Using path from string (vector drawable pathData or svg pathData)

## Usecases

* Charts
* Progress bars, charge indicators
* Visualizations of constantly updated data
* Bitmap decoration
* Animate on canvas

## Usage

Add it in your root build.gradle at the end of repositories:

```groovy
	allprojects {
		repositories {
			//...
			maven { url 'https://jitpack.io' }
		}
	} 
```

Add the dependency:
```groovy
	dependencies {
	        implementation 'com.github.AlexeyErofeev:canvasdsl:2.0'
	}
```
`groupFromDrawing` needs kotlin context receivers to use `Context`, so use in your `kotlinOptions`:
```groovy
    freeCompilerArgs = ["-Xcontext-receivers"]
```

See 
[example](https://github.com/AlexeyErofeev/canvasdsl/blob/master/example/src/main/java/com/mytoolbox/canvasdsl/example/MainActivity.kt) 
to learn how to draw using this library,
and also see full [primitives list](https://github.com/AlexeyErofeev/canvasdsl/blob/master/library/src/main/java/com/mytoolbox/canvasdsl/primitives) and 
[useful utils](https://github.com/AlexeyErofeev/canvasdsl/blob/master/library/src/main/java/com/mytoolbox/canvasdsl/utils) and
[extensions](https://github.com/AlexeyErofeev/canvasdsl/blob/master/library/src/main/java/com/mytoolbox/canvasdsl/common/Extensions.kt).