## Goodreads: Book Recommender

### Условие

Ще създадем приложение, предоставящо разнообразна информация и препоръки на книги, основано на базови статистически подходи от Машинното обучение.

Ще използваме [dataset от kaggle](https://www.kaggle.com/datasets/ishikajohari/best-books-10k-multi-genre-data) с 10-те хиляди най-препоръчвани книги за всички времена. Приложили сме минимални корекции срямо него и ще работим с версията, приложена във файла [goodreads_data.csv](./resources/goodreads_data.csv).

Всеки ред от файла съдържа информация за една книга, като полетата са разделени със запетая (CSV формат):

`N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL`

За реализация на алгоритмите в тази задача, ще трябва да се запознаем с няколко концепции:

- [**TF-IDF**](https://en.wikipedia.org/wiki/Tf–idf) (Term Frequency-Inverse Document Frequency): това е статистическа метрика, използвана за оценка на важността на дадена дума в рамките на колекция от документи. *Term Frequency* (TF) изразява честотата на появяване на дадена дума в конкретен документ. *Inverse Document Frequency* (IDF) измерва колко рядка е думата в цялата колекция от документи, като намалява тежестта на често срещаните думи. Умножавайки двете, TF-IDF определя колко важна е дадена дума в документ спрямо останалите документи в колекцията. Използва се често в системите за обработка на естествен език (Natural Language Processing, NLP), за да определи релевантността на думa за даден текст.

- [**Overlap coefficient**](https://en.wikipedia.org/wiki/Overlap_coefficient): прост алгоритъм за изчисляване на подобността на две множества от думи. Ако означим двете множества с A и B, коефициентът се пресмята по следната формула:

    ![Overlap coefficient](https://wikimedia.org/api/rest_v1/media/math/render/svg/27b7794575d6bdf344e47a165544cb37a9fe5e47)

    Или по-просто казано: броят на общите елементи на A и B, разделен на размера на по-малкото от двете множества.

- [**Stopwords**](https://en.wikipedia.org/wiki/Stop_word): Има едно множество от често срещани в свободен текст думи, които носят твърде малко семантика: определителни членове, местоимения, предлози, съюзи и т.н. Такива думи се наричат *stopwords* и много алгоритми, свързани с обработка на естествен език, ги игнорират - т.е. премахват ги от съответните входни текстове, защото внасят "шум", т.е. намаляват качеството на резултата. 
Няма еднозначна дефиниция (или речник), коя дума е stopword в даден език. В нашия алгоритъм, ще ползваме списъка от 174 stopwords в английския език, записани по една на ред в текстовия файл [stopwords.txt](./resources/stopwords.txt), който сме заимствали от сайта [ranks.nl](https://www.ranks.nl/stopwords). 
Обърнете внимание, че в думите, които съдържат апостроф сме го премахнали и те са една дума - `o'clock = oclock`.

1. **Book Recommender API**

В пакета `bg.sofia.uni.fmi.mjt.goodreads.recommender` създайте интерфейс `BookRecommenderAPI`, който дефинира алгоритъм за препоръчване на книги

**Обърнете внимание**, че низът, репрезентиращ жанровете, е във формат `"[genreA, genreB, genreC]"`.

Зареждането на книгите се осъществява от класа `BookLoader`. Той има един статичен метод, който приема `Reader` и връща `Set<Book>`. Използвайте този клас за зареждане на dataset-а и **не го променяйте**

2. **SimilarityCalculator**

   За изчисляване на подобността между две книги ще използваме интерфейса `SimilarityCalculator` в пакета `bg.sofia.uni.fmi.mjt.goodreads.recommender.similarityCalculator`

   За да върнем възможно най-точни и реалистични стойности, ще изчисляваме подобността на базата на жанрове и описание на книгите.
За тази цел, ще използваме няколко пакета, в които ще трябва да създадем калкулатори:
- в пакета `bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres` ще намерите частична имплементация на класа `GenresOverlapSimilarityCalculator`. Използвайте *Overlap coefficient* (виж точката с теорията), за да изчислите коефициента на съвместимост на жанровете на две книги.
- в пакета `bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions` - ще намерите частична имплементация на класа `TFIDFSimilarityCalculator`, който генерира оценки на думите от описанията на две книги (на база на `TF-IDF` алгоритъма). За повече информация, разгледайте [допълнителното описание на алгоритъма](./tfidf.md).
- в пакета `bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite` завършете имплементацията на класа `CompositeSimilarityCalculator`. Идеята на този клас е да комбинира различни калкулатори със съответните им тежести. Сумарният резултат е сбор от резултатите на дефинираните композитни калкулатори, умножени по тяхната тежест.

3. **Tokenizer**

В пакета `bg.sofia.uni.fmi.mjt.goodreads.tokenizer` ще намерите частична имплементация на класа `Tokenizer`. Неговата идея е да извлича думи от подаден низ като прилага "изчистване" на низа и премахване на stopwords. `Tokenizer` класът има метод `List<String> tokenize(String input)`, чиято идея е да извлече всички думи от подадения `String`.

За целта, трябва първо да премахнем всички препинателни знаци и последователности от повече от един `whitespace` и финално да превърнем резултата в `lowercase`. След това трябва да извлечем думитe и да премахнем тези от тях, които са stopwords.

4. **Book Finder API**

Интерфейс за търсене и филтриране на книги

**Опции за търсене**

Методите на интерфейса, които предоставят търсене по жанрове/ключови думи, приемат като аргумент и `MatchOption`, който определя дали се търсят книги, съдържащи всички или поне един от подадените жанрове/думи.

