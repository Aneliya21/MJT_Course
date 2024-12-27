# Photo Edge Detector :framed_picture:

Тази седмица задачата ни е да разработим модули на библиотека за обработка на изображения. Библиотеката ще може да работи с изображения в различни графични формати (JPEG, PNG, BMP) и ще прилага различни трансформации върху тях. В първата версия, клиентът иска функционалност за конвертиране на цветно изображение в черно-бяло и възможност за откриване на ръбовете в изображение.

![Maserati Edge Detected](../lecture/images/07.12-maserati-edge-detected.png)

Библиотеката има два основни компонента:

- Алгоритми за обработка на изображения
- Мениджър за файловата система за зареждане и съхраняване на изображения.

## Основни интерфейси и класове

Ето интерфейсите и класовете, които трябва да реализирате. Някои класове и интерфейси са частично дефинирани, за да ви насочат.

### ImageAlgorithm

Интерфейсът `ImageAlgorithm` представлява алгоритъм за обработка на изображения.

### GrayscaleAlgorithm

Интерфейсът `GrayscaleAlgorithm` е маркерен интерфейс за алгоритми за конвертиране в черно-бяло изображение.


### EdgeDetectionAlgorithm

Интерфейсът `EdgeDetectionAlgorithm` е друг маркерен интерфейс, този път за алгоритми за откриване на ръбове.

```java
package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

public interface EdgeDetectionAlgorithm extends ImageAlgorithm {
}
```

### FileSystemImageManager

Интерфейсът `FileSystemImageManager` управлява зареждането и съхраняването на изображения от файловата система.

### LocalFileSystemImageManager

Класът `LocalFileSystemImageManager` има публичен конструктор по подразбиране, имлпементира интерфейса `FileSystemImageManager` и предоставя методи за зареждане и съхраняване на изображения.

:point_right: Подсказка: в имплементацията ще ви влезе в употреба класът [javax.imageio.ImageIO](https://docs.oracle.com/en/java/javase/23/docs/api/java.desktop/javax/imageio/ImageIO.html) 

### LuminosityGrayscale

Класът `LuminosityGrayscale` също има публичен конструктор по подразбиране, имплементира интерфейса `GrayscaleAlgorithm` и прилага черно-бяло конвертиране, използвайки [*Mетода на осветеност (Luminocity method)*](https://www.johndcook.com/blog/2009/08/24/algorithms-convert-color-grayscale/).
В литературата се срещат и други стойности на коефициентите във формулата, но ние ще използваме класическите, които са посочени и в линкнатата по-горе статия: `0.21 R + 0.72 G + 0.07 B`.

### SobelEdgeDetection

Класът `SobelEdgeDetection` има публичен конструктор `SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm)`, имплементира интерфейса `EdgeDetectionAlgorithm` и прилага един от класическите алгоритми за откриване на ръбове в изображения, [*Оператор на Sobel*](https://en.wikipedia.org/wiki/Sobel_operator), известен също като *Оператор на Sobel–Feldman* и *Филтър на Sobel*.

За да се запознаете с алгоритъма, може да разгледате [тази статия](https://cse442-17f.github.io/Sobel-Laplacian-and-Canny-Edge-Detection-Algorithms/), която съдържа и интерактивна визуализация, която ще ви помогне да схванете как работи той.

