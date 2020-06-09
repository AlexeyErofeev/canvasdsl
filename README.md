[![](https://jitpack.io/v/AlexeyErofeev/canvasdsl.svg)](https://jitpack.io/#AlexeyErofeev/canvasdsl)

# CanvasDSL

Simple lightweight kotlin-dsl for canvas drawings.

## Key features

* SVG-like primitives
* Supports references to predefined groups 
* Viewport mechanics with relative "viewport points"
* Mutable model, ready for animation
* No compatibility limits - works with minimum android sdk 1
* One dependency: kotlin stdlib, you should use it anyway for kotlin

## Usecases

* Charts
* Determinate progress, charge indicators
* Another simple data visualization 
* Bitmap decoration

#Usage

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
	        implementation 'com.github.AlexeyErofeev:canvasdsl:1.0-alpha'
	}
```
